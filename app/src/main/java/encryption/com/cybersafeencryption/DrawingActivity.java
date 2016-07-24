package encryption.com.cybersafeencryption;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import android.view.View.OnTouchListener;
import android.widget.Button;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class DrawingActivity extends AppCompatActivity implements Runnable, OnTouchListener {
    private SurfaceHolder holder;
    private boolean locker=true;
    private Thread thread;
    private LinearLayout CamView;
    int previous_x, previous_y;
    ArrayList<Line> lines = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);

        CamView = (LinearLayout) findViewById(R.id.lnr); //ANY LAYOUT OF YOUR XML

        Button btnSaveImage = (Button) findViewById(R.id.button_save_image);

      final SurfaceView surface = (SurfaceView) findViewById(R.id.surfaceView);

        //MMM
        surface.setDrawingCacheEnabled(true);

        if(surface != null)
        surface.setOnTouchListener(this);


        if(surface != null)
        holder = surface.getHolder();


        thread = new Thread(this);
        thread.start();



        if(btnSaveImage != null)
            btnSaveImage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Log.d("SAVE_IMAGE","SSSSSSSSSS5");
                CamView.setDrawingCacheEnabled(true); //CamView OR THE NAME OF YOUR LAYOUR
                CamView.buildDrawingCache(true);
                Bitmap bmp = Bitmap.createBitmap(CamView.getDrawingCache());
                CamView.setDrawingCacheEnabled(false); // clear drawing cache
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                byte[] bitmapdata = bos.toByteArray();
                ByteArrayInputStream fis = new ByteArrayInputStream(bitmapdata);

                String picId= "rrr"; //String.valueOf(nu);
                String myfile="Ghost"+picId+".jpeg";

                File dir_image = new  File(Environment.getExternalStorageDirectory()+//<---
                        File.separator+"Ultimate Entity Detector");          //<---
                dir_image.mkdirs();                                                  //<---
                //^IN THESE 3 LINES YOU SET THE FOLDER PATH/NAME . HERE I CHOOSE TO SAVE
                //THE FILE IN THE SD CARD IN THE FOLDER "Ultimate Entity Detector"

                try {
                    File tmpFile = new File(dir_image,myfile);
                    FileOutputStream fos = new FileOutputStream(tmpFile);

                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = fis.read(buf)) > 0) {
                        fos.write(buf, 0, len);
                    }
                    fis.close();
                    fos.close();
                    Toast.makeText(getApplicationContext(),
                            "The file is saved at :SD/Ultimate Entity Detector",Toast.LENGTH_LONG).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while(locker){
            //checks if the lockCanvas() method will be success,and if not, will check this statement again
            if(!holder.getSurface().isValid()){
                continue;
            }
            /** Start editing pixels in this surface.*/
            Canvas canvas = holder.lockCanvas();

            //ALL PAINT-JOB MAKE IN draw(canvas); method.
            draw(canvas);

            // End of painting to canvas. system will paint with this canvas,to the surface.
            holder.unlockCanvasAndPost(canvas);
        }
    }
    /**This method deals with paint-works. Also will paint something in background*/
    private void draw(Canvas canvas) {
        // paint a background color
        canvas.drawColor(Color.BLUE);
        Paint paint = new Paint();
        paint.setARGB(200, 135, 135, 135); //paint color GRAY+SEMY TRANSPARENT
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(8);

        for(int i = 0; i < lines.size(); i++)
        {
            canvas.drawLine(lines.get(i).x1, lines.get(i).y1, lines.get(i).x2, lines.get(i).y2, paint);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        pause();
    }

    private void pause() {
        //CLOSE LOCKER FOR run();
        locker = false;
        while(true){
            try {
                //WAIT UNTIL THREAD DIE, THEN EXIT WHILE LOOP AND RELEASE a thread
                thread.join();
            } catch (InterruptedException e) {e.printStackTrace();
            }
            break;
        }
        thread = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        resume();
    }

    private void resume() {
        //RESTART THREAD AND OPEN LOCKER FOR run();
        locker = true;

    }
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        int x, y;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                previous_x  = (int) event.getX();
                previous_y  = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                x = (int) event.getX();
                y = (int) event.getY();
                Line line = new Line(previous_x, previous_y, x, y);
                previous_x = x;
                previous_y = y;
                lines.add(line);
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                break;
        }
        return true;
    }
}