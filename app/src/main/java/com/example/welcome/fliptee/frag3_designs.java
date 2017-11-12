package com.example.welcome.fliptee;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jraska.falcon.Falcon;
import com.rtugeek.android.colorseekbar.ColorSeekBar;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.ugurtekbas.fadingindicatorlibrary.FadingIndicator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

public class frag3_designs extends android.support.v4.app.Fragment {
    private static final int RESULT_OK =-1 ;
    private static int request=1;
    String ImageDecode;
    ColorSeekBar colorSeekBar;
    SeekBar seekBar;
    TextView textView;
    Button btnHide,btnShow;
    SlidingUpPanelLayout slidingLayout;
    private static final int CAMERA_REQUEST = 1888;
    final int [] iv_int={R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.drawable.scripalt,R.drawable.walt2};
    String[] FILE;
    File file_screen;
    Integer increase;
    Runnable runnable;
    FadingIndicator indicator ;
    View v;
    RelativeLayout relativeLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        v= inflater.inflate(R.layout.fragment_frag3_designs,container,false);
        ImageView but_gall=(ImageView) v.findViewById(R.id.id_gallery_designs);
        ImageView butt_screen=(ImageView) v.findViewById(R.id.button_screenshot);
        but_gall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i,request);
            }

        });
        final Display display = getActivity().getWindowManager().getDefaultDisplay();
        final ImageView save=(ImageView) v.findViewById(R.id.id_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save.setImageResource(R.drawable.heartimages);
                new CountDownTimer(2000, 1000) {
                    public void onTick(long millisUntilFinished) {

                    }
                    public void onFinish() {
                        save.setImageResource(R.mipmap.favourite_heart);
                    }
                }.start();
                int width=display.getWidth();
                int height=display.getHeight();

                ViewPager vp1=(ViewPager)getActivity().findViewById(R.id.id_viewPager);
                FadingIndicator indicator = (FadingIndicator) getActivity().findViewById(R.id.circleIndicator);
                int pager_height=vp1.getHeight();
                int indicator_height=indicator.getHeight();
                Bitmap bm= Falcon.takeScreenshotBitmap(getActivity());
                Bitmap bmm=bm.createBitmap(bm,0,0,width,height-pager_height-indicator_height-15);////screen shot bitmap

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmm.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] imageInBytefromBitmap = stream.toByteArray();
                String byteToString= Base64.encodeToString(imageInBytefromBitmap, Base64.DEFAULT);
                increase=0;
                ImageView csb=(ImageView)getActivity().findViewById(R.id.image_tshirt);
                if(csb.getColorFilter()!=null) {
                    increase=increase+1;
                    Log.i("QQQQWWWW"," "+increase);
                }
                //get text
                /*TextView tv=(TextView)getActivity().findViewById(R.id.clipartTry);
                String tvString=tv.getText().toString();
                if(tvString.isEmpty()!=true) {
                    increase=increase+1;
                    Log.i("QQQWWW"," "+increase);
                }*/
                FrameLayout canvas=(FrameLayout)getActivity().findViewById(R.id.canvasView);
                int tot_inFrame=canvas.getChildCount();
                Log.i("wsawsawsa",""+tot_inFrame);
                increase=increase+tot_inFrame;
                //get design
                /*ImageView iv=(ImageView)getActivity().findViewById(R.id.image_from_grid);  ******************
                if(iv.getDrawable()!=null) {
                    increase=increase+1;
                    Log.i("QQWW"," "+increase);
                }*/
                //Log.i("INININ"," "+increase);
                String increaseString=increase.toString();
                SQLiteDatabase db=getActivity().openOrCreateDatabase("imageDB", Context.MODE_PRIVATE,null);
                //db.execSQL("DROP TABLE IF EXISTS imageTable");
                db.execSQL("CREATE TABLE IF NOT EXISTS imageTable(changes VARCHAR,image VARCHAR );");
                db.execSQL("INSERT INTO imageTable VALUES('"+increaseString+"','"+byteToString+"');");
                db.close();
            }
        });

        ImageView camerabutton=(ImageView)getView().findViewById(R.id.cameraimageView);
        camerabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,CAMERA_REQUEST);
            }
        });

        butt_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                StickerImageView stickerImageView=new StickerImageView(getActivity());
                stickerImageView.setControlItemsHidden(false);

                //seekBar=(SeekBar)getActivity().findViewById(R.id.seekbar_height);
                colorSeekBar=(ColorSeekBar)getActivity().findViewById(R.id.id_colorTextSlider);
                ViewPager vp1=(ViewPager)getActivity().findViewById(R.id.id_viewPager);
                colorSeekBar.setVisibility(View.INVISIBLE);
                //seekBar.setVisibility(View.INVISIBLE);
                int width=display.getWidth();
                int height=display.getHeight();
                int pager_height=vp1.getHeight();
                FadingIndicator indicator = (FadingIndicator) getActivity().findViewById(R.id.circleIndicator);
                int indicator_height=indicator.getHeight();
                Bitmap bm= Falcon.takeScreenshotBitmap(getActivity());
                Bitmap bmm=bm.createBitmap(bm,0,0,width,height-pager_height-indicator_height-15);

                Dialog d=new Dialog(getActivity());
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.setContentView(R.layout.screen_shots);
                d.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        return false;
                    }
                });

                Display display = getActivity().getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                final Bitmap bmp = Bitmap.createScaledBitmap(bmm,size.x,size.y,false);
                final ImageView img=(ImageView)d.findViewById(R.id.image_screen_shot);
                img.setImageBitmap(bmp);
                //d.setCancelable(false);
                d.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        //seekBar.setVisibility(View.VISIBLE);
                        //colorSeekBar.setVisibility(View.VISIBLE);
                    }
                });
                d.show();

                ImageView shareImage=(ImageView)d.findViewById(R.id.image_share_icon);
                shareImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //seekBar=(SeekBar)getActivity().findViewById(R.id.seekbar_height);
                        //colorSeekBar=(ColorSeekBar)getActivity().findViewById(R.id.id_colorTextSlider);
                        //colorSeekBar.setVisibility(View.INVISIBLE);
                        //seekBar.setVisibility(View.INVISIBLE);



                        String root= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
                        Log.i("jojo",root);
                        File myDir=new File(root+"/Fliptee");
                        Log.i("ROTO",root);
                        if(!myDir.exists())
                        {
                            myDir.mkdirs();
                            Log.i("huhu",";jiji");
                        }
                        Random generator = new Random();
                        int n = 10000;
                        n = generator.nextInt(n);
                        final String iname = "Image-" + n + ".png";
                        File file = new File(myDir, iname);
                        if (file.exists()) {
                            file.delete();
                            Toast.makeText(getActivity(),"Image Already Exist",Toast.LENGTH_LONG).show();
                        }
                        else {
                            try {
                                Toast.makeText(getActivity(),"Image Saved",Toast.LENGTH_LONG).show();
                                FileOutputStream out = new FileOutputStream(file);
                                bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
                                getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,Uri.parse("file://"+file.getAbsolutePath())));
                                //MediaStore.Images.Media.insertImage(getContext().getContentResolver(),bmp,iname, "drawing");
                                out.flush();
                                out.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        Uri uri=Uri.parse(file.getAbsolutePath());
                        Intent i=new Intent(Intent.ACTION_SEND);
                        i.putExtra(Intent.EXTRA_STREAM,uri);
                        i.setType("image/png");
                        //i.setPackage("com.whatsapp");
                        startActivity(i);
                        // seekBar.setVisibility(View.VISIBLE);
                        // colorSeekBar.setVisibility(View.VISIBLE);
                    }
                });
                final ImageView downloadImage=(ImageView)d.findViewById(R.id.image_download_icon);
                downloadImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String str;
                        // seekBar=(SeekBar)getActivity().findViewById(R.id.seekbar_height);
                        // colorSeekBar=(ColorSeekBar)getActivity().findViewById(R.id.id_colorTextSlider);
                        // colorSeekBar.setVisibility(View.INVISIBLE);
                        // seekBar.setVisibility(View.INVISIBLE);
                        String root= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
                        //String root1=Environment.getExternalStorageDirectory().toString();
                        //Log.i("YTYT",root1);
                        Log.i("jojo",root);
                        File myDir=new File(root+"/Fliptee");
                        if(!myDir.exists())
                        {
                            myDir.mkdirs();
                            Log.i("huhuuu",";jiji");
                        }
                        Random generator = new Random();
                        int n = 10000;
                        n = generator.nextInt(n);
                        final String iname = "Image-" + n + ".png";
                        File file = new File(myDir, iname);
                        if (file.exists()) {
                            file.delete();
                            Toast.makeText(getActivity(),"Image Already Exist",Toast.LENGTH_LONG).show();
                        }
                        else {
                            try {
                                Log.i("lklk",file.getAbsolutePath());
                                Toast.makeText(getActivity(),"Image Saved",Toast.LENGTH_LONG).show();
                                FileOutputStream out = new FileOutputStream(file);
                                bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
                                getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,Uri.parse("file://"+file.getAbsolutePath())));
                                //MediaStore.Images.Media.insertImage(getContext().getContentResolver(),bmp,iname, "drawing");
                                Log.i("DRDRDR",file.getAbsolutePath());
                                out.flush();
                                out.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        //seekBar.setVisibility(View.VISIBLE);
                        //colorSeekBar.setVisibility(View.VISIBLE);
                        //MediaScannerConnection.scanFile(getActivity(), new String[] { file.getPath() }, new String[] { "image/jpeg" }, null);
                    }
                });
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == request && resultCode == RESULT_OK && data != null)
       // if(data!=null && requestCode==request)
        {
            Uri URI = data.getData();
            String[] FILE = { MediaStore.Images.Media.DATA };
            Cursor cursor =getActivity(). getContentResolver().query(URI, FILE, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(FILE[0]);
            ImageDecode = cursor.getString(columnIndex);
            cursor.close();
            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_gallery);
            Button okButton=(Button)dialog.findViewById(R.id.id_dialog_ok);
            Button cancelButton=(Button)dialog.findViewById(R.id.id_dialog_cancal);
            ImageView iv=(ImageView)dialog.findViewById(R.id.id_dialog_gallery);
            iv.setImageBitmap(BitmapFactory.decodeFile(ImageDecode));
            okButton.setOnClickListener(new View.OnClickListener() {       //here add to admin database for validation
                @Override
                public void onClick(View v) {
                    //Toast.makeText(designerActivity.this,"Submitted",Toast.LENGTH_LONG).show();
                    FrameLayout canvas=(FrameLayout)getActivity().findViewById(R.id.canvasView);
                    StickerImageView iv_sticker = new StickerImageView(getActivity());
                    iv_sticker.setImageBitmap(BitmapFactory.decodeFile(ImageDecode));
                    canvas.addView(iv_sticker);
                    dialog.dismiss();

                }
            });
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
        else if(requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && data != null)
        {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            FrameLayout canvas=(FrameLayout)getActivity().findViewById(R.id.canvasView);
            StickerImageView iv_sticker = new StickerImageView(getActivity());
            iv_sticker.setImageBitmap(photo);
            canvas.addView(iv_sticker);
            //dialog.dismiss();
            //imageView.setImageBitmap(photo);
        }
        else
        {
            Toast.makeText(getActivity(),"Could Not Fetch Image",Toast.LENGTH_LONG).show();
        }
    }
    class CustomViewHolder
            extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView imageView;
        private TextView textView;


        CustomViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            imageView = (ImageView) view.findViewById(R.id.item_grid_image);
        }

        public void bindData(int position) {
            //imageView.setImageResource(R.mipmap.ic_launcher);
            imageView.setImageResource(iv_int[position]);//this also haas to be removed after we have database
        }
        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(), textView.getText().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    class CustomAdapter extends RecyclerView.Adapter<CustomViewHolder> {
        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_recycler_images, parent, false);
            return new CustomViewHolder(view);
        }
        @Override
        public void onBindViewHolder(CustomViewHolder holder, int position) {
            holder.bindData(position);
        }
        @Override
        public int getItemCount() {
            return iv_int.length;//thi sarray has to be removed after database
        }
    }
    class  Threading extends Thread
    {
        @Override
        public void run() {
            super.run();
            for(int i=0;i<=5000;i++)
            {

            }
        }
    }
}
