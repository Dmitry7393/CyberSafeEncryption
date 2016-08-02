package encryption.com.cybersafeencryption;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class CanvasView extends View  {

    private Stroke mStroke;
    private final int DEFAULT_COLOR_BACKGROUND = Color.rgb(132, 112, 255);
    private Paint mPaint;
    private float mX, mY;
    private static final float TOLERANCE = 5;
    private int mCurrentBackgroundColor = DEFAULT_COLOR_BACKGROUND;
    private ArrayList<Stroke> mStrokesOnCurrentScreen = new ArrayList<>();
    private ArrayList<Screen> listScreens;
    private int numberScreen = 0;

    public int getNumberOfScreens() {
        return numberScreen;
    }

    public CanvasView(Context c, AttributeSet attrs) {
        super(c, attrs);

        numberScreen = 0;
        listScreens = new ArrayList<>();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.DKGRAY);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(10.0f);
    }
    public boolean isImageExist() {
        if(mStrokesOnCurrentScreen.size() != 0) {
            return true;
        } else return false;
    }
    public void changeBackgroundColor(int color) {
        if (numberScreen < listScreens.size() && listScreens.size() != 0) {
            Screen s = new Screen(new ArrayList<>(mStrokesOnCurrentScreen), color);
            listScreens.set(numberScreen, s);
        } else
            mCurrentBackgroundColor = color;

        invalidate();
    }

    public void changeColor(int color) {
        mPaint.setColor(color);
    }

    // override onSizeChanged
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // your Canvas will draw onto the defined Bitmap
        Bitmap mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(mBitmap);
        mCanvas.drawColor(Color.BLUE);
    }

    public void moveToPreviousScreen() {
        if (numberScreen == listScreens.size()) {
            Screen screen = new Screen(new ArrayList<>(mStrokesOnCurrentScreen), mCurrentBackgroundColor);
            listScreens.add(screen);
            mStrokesOnCurrentScreen.clear();
            numberScreen = numberScreen - 1;
            mStrokesOnCurrentScreen = new ArrayList<>(listScreens.get(numberScreen).strokes);
            invalidate();
        } else {
            Screen screen = new Screen(new ArrayList<>(mStrokesOnCurrentScreen), mCurrentBackgroundColor);
            listScreens.set(numberScreen, screen);
            numberScreen = numberScreen - 1;
            mStrokesOnCurrentScreen = new ArrayList<>(listScreens.get(numberScreen).strokes);
            invalidate();
        }
    }
    public void moveToNextScreen() {
        numberScreen++;
        if (numberScreen > listScreens.size()) {
            //Save previous screen
            Screen screen = new Screen(new ArrayList<>(mStrokesOnCurrentScreen), mCurrentBackgroundColor);
            listScreens.add(screen);
            mStrokesOnCurrentScreen.clear();
            mCurrentBackgroundColor = DEFAULT_COLOR_BACKGROUND;
            invalidate();
            return;
        }
        if (numberScreen == listScreens.size()) {
            int previous_screen = numberScreen - 1;
            Screen screen = new Screen(new ArrayList<>(mStrokesOnCurrentScreen), mCurrentBackgroundColor);
            listScreens.set(previous_screen, screen);
            mStrokesOnCurrentScreen.clear();
            invalidate();
            return;
        }
        if (numberScreen < listScreens.size()) {
            int previous_screen = numberScreen - 1;
            Screen screen = new Screen(new ArrayList<>(mStrokesOnCurrentScreen), mCurrentBackgroundColor);
            listScreens.set(previous_screen, screen);
            mStrokesOnCurrentScreen.clear();
            mStrokesOnCurrentScreen = new ArrayList<>(listScreens.get(numberScreen).strokes);
            invalidate();
        }
    }

    // override onDraw
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (numberScreen < listScreens.size() && listScreens.size() != 0) {
            canvas.drawColor(listScreens.get(numberScreen).getColor());
            mCurrentBackgroundColor = listScreens.get(numberScreen).getColor();
        } else {

            canvas.drawColor(mCurrentBackgroundColor);
        }


        for (Stroke s : mStrokesOnCurrentScreen) {
            canvas.drawPath(s.getPath(), s.getPaint());
        }
    }

    // when ACTION_DOWN start touch according to the x,y values
    private void startTouch(float x, float y) {
        mStroke = new Stroke(new Paint(mPaint));
        mStroke.path.moveTo(x, y);
        mStrokesOnCurrentScreen.add(mStroke);
        mX = x;
        mY = y;
    }

    // when ACTION_MOVE move touch according to the x,y values
    private void moveTouch(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOLERANCE || dy >= TOLERANCE) {
            mStroke.path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    public void clearCanvas() {
        mStrokesOnCurrentScreen.clear();
        invalidate();
    }

    // when ACTION_UP stop touch
    private void upTouch() {
        mStroke.path.lineTo(mX, mY);
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
    public void setStrokeWidth(int width) {
        mPaint.setStrokeWidth(width);
    }

}