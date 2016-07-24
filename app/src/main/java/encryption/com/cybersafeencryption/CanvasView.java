package encryption.com.cybersafeencryption;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class CanvasView extends View {
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    Context context;
    private Paint mPaint;
    private float mX, mY;
    private static final float TOLERANCE = 5;
    private List<Path> listScreens;

    int numberScreen = 0;
    public CanvasView(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;
        numberScreen = 0;
        listScreens = new ArrayList<>();
        // we set a new Path
        mPath = new Path();

        // and we set a new Paint with the desired attributes
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(4f);
    }

    // override onSizeChanged
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // your Canvas will draw onto the defined Bitmap
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mCanvas.drawColor(Color.BLUE);
        Log.d("CreateBITMAP", "RRRRRRRR1");
    }
    public void moveToPreviousScreen() {
        if(numberScreen == listScreens.size()) {
            listScreens.add(new Path(mPath));
            mPath.reset();
            numberScreen = numberScreen - 1;
            mPath = new Path(listScreens.get(numberScreen));
            invalidate();
        } else {
            listScreens.set(numberScreen, new Path(mPath));
            mPath.reset();
            numberScreen = numberScreen - 1;
            mPath = new Path(listScreens.get(numberScreen));
            invalidate();
        }
    }
    public void moveToNextScreen() {
       numberScreen++;
        if(numberScreen > listScreens.size()) {
            //Save previous screen
            listScreens.add(new Path(mPath));
            mPath.reset();
            invalidate();
            return;
        }
        if(numberScreen == listScreens.size()) {
            int previous_screen = numberScreen - 1;
            listScreens.set(previous_screen, new Path(mPath));
            mPath.reset();
            invalidate();
            return;
        }
        if(numberScreen < listScreens.size())
        {
            int previous_screen = numberScreen - 1;
            listScreens.set(previous_screen, new Path(mPath));
            mPath.reset();
            mPath = new Path(listScreens.get(numberScreen));
            invalidate();
        }
    }
    // override onDraw
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // draw the mPath with the mPaint on the canvas when onDraw
        canvas.drawColor(Color.BLUE);
        canvas.drawPath(mPath, mPaint);
    }

    // when ACTION_DOWN start touch according to the x,y values
    private void startTouch(float x, float y) {
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    // when ACTION_MOVE move touch according to the x,y values
    private void moveTouch(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOLERANCE || dy >= TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    public void clearCanvas() {
        mPath.reset();
        invalidate();
    }

    // when ACTION_UP stop touch
    private void upTouch() {
        mPath.lineTo(mX, mY);
    }

    //override the onTouchEvent
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTouch(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                moveTouch(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                upTouch();
                invalidate();
                break;
        }
        return true;
    }
}