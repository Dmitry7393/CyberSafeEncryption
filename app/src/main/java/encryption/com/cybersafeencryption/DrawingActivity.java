package encryption.com.cybersafeencryption;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class DrawingActivity extends AppCompatActivity {
    RelativeLayout holder;
    CanvasView canvasView;
    TextView txtViewNumberScreen;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);
        canvasView = (CanvasView) findViewById(R.id.signature_canvas);
        holder = (RelativeLayout) findViewById(R.id.layout_canvas);
        txtViewNumberScreen = (TextView) findViewById(R.id.number_screen);
        txtViewNumberScreen.setText(Integer.toString(canvasView.numberScreen));
    }
    public void clearCanvas(View v) {
        canvasView.clearCanvas();
    }
    public void saveImage(View v) {
        String fileName = Environment.getExternalStorageDirectory() + "/myimage.png";
        try {
            OutputStream stream = new FileOutputStream(fileName);
            Bitmap myBitmap = loadBitmapFromView(holder);
            myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.close();
            Log.d("SUCCESS123458", "TTTT234uuuu3");
        }
        catch(FileNotFoundException e) {
            Log.d("ERROR1", "TTTT");
        }
        catch(IOException e) {
            Log.d("ERROR2", "TTTT2");
        }
    }
    public void moveToPreviousScreen(View v) {
        Log.d("LEFT", "left");
        if(canvasView.numberScreen != 0)
          canvasView.moveToPreviousScreen();

        txtViewNumberScreen.setText(Integer.toString(canvasView.numberScreen));
    }
    public void moveToNextScreen(View v) {
      Log.d("RIGHT", "rightttttttttttt");
        canvasView.moveToNextScreen();

        txtViewNumberScreen.setText(Integer.toString(canvasView.numberScreen));
    }
    public  Bitmap loadBitmapFromView(View v) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        v.measure(View.MeasureSpec.makeMeasureSpec(dm.widthPixels, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(dm.heightPixels, View.MeasureSpec.EXACTLY));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        ((RelativeLayout) v).setGravity(Gravity.CENTER);
        Bitmap returnedBitmap = Bitmap.createBitmap(v.getMeasuredWidth(),
                v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(returnedBitmap);
        v.draw(c);

        return returnedBitmap;
    }
}