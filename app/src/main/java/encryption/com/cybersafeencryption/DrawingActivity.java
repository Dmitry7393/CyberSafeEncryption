package encryption.com.cybersafeencryption;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import android.view.View.OnTouchListener;
public class DrawingActivity extends AppCompatActivity implements OnTouchListener {
    Bitmap bitmap;
    Canvas canvas;
    Paint paint;
    float downx = 0, downy = 0, upx = 0, upy = 0;
    DrawView drawView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        drawView = new DrawView(this);
        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(8);
        super.onCreate(savedInstanceState);
        drawView.setOnTouchListener(this);
        setContentView(drawView);
    }
    class DrawView extends SurfaceView implements SurfaceHolder.Callback {
        private DrawThread drawThread;
        public DrawView(Context context) {
            super(context);
            getHolder().addCallback(this);
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            drawThread = new DrawThread(getHolder());
            drawThread.setRunning(true);
            drawThread.start();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            boolean retry = true;
            drawThread.setRunning(false);
            while (retry) {
                try {
                    drawThread.join();
                    retry = false;
                } catch (InterruptedException e) {
                }
            }
        }

        class DrawThread extends Thread {

            private Paint p;
            private boolean running = false;
            private SurfaceHolder surfaceHolder;

            public DrawThread(SurfaceHolder surfaceHolder) {
                this.surfaceHolder = surfaceHolder;
                p = new Paint();
                p.setColor(Color.RED);
                p.setStrokeWidth(10);
            }

            public void setRunning(boolean running) {
                this.running = running;
            }

            @Override
            public void run() {
            //    Canvas canvas;
                while (running) {
                    canvas = null;
                    try {
                        canvas = surfaceHolder.lockCanvas(null);
                        if (canvas == null)
                            continue;
                        canvas.drawColor(Color.BLUE);
                        canvas.drawLine(downx, downy, upx, upy, paint);
                      //  canvas.drawLine(100, 100, 200, 300, p);
                    } finally {
                        if (canvas != null) {
                            surfaceHolder.unlockCanvasAndPost(canvas);
                        }
                    }
                }
            }
        }
    }
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
              //  canvas.drawLine(100, 100, 200, 300, paint);
                downx = event.getX();
                downy = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                upx = event.getX();
                upy = event.getY();
                Log.d("DRAW","LINE");

                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                break;
        }
        return true;
    }
}
