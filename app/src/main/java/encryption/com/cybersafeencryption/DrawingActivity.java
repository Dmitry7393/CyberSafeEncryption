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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import android.widget.RelativeLayout;
import android.widget.TextView;

import encryption.com.AES.Encrypt;
import encryption.com.dialogs.DialogSaveBitmap;


public class DrawingActivity extends AppCompatActivity implements DialogSaveBitmap.saveBitmapInterface {
    RelativeLayout holder;
    CanvasView canvasView;
    TextView txtViewNumberScreen;
    DialogSaveBitmap mDialogSaveBitmap;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);
        canvasView = (CanvasView) findViewById(R.id.signature_canvas);
        holder = (RelativeLayout) findViewById(R.id.layout_canvas);
        txtViewNumberScreen = (TextView) findViewById(R.id.number_screen);
        txtViewNumberScreen.setText(String.valueOf(canvasView.numberScreen));
        mDialogSaveBitmap = new DialogSaveBitmap();
    }
    public void clearCanvas(View v) {
        canvasView.clearCanvas();
    }
    public void saveImage(View v) {
        mDialogSaveBitmap.show(getFragmentManager(), "dlg1");
    }
    public static byte[] convertBitmapToByteArray(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        } else {
            byte[] b = null;
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
                b = byteArrayOutputStream.toByteArray();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return b;
        }
    }
    public void moveToPreviousScreen(View v) {
        if(canvasView.numberScreen != 0)
          canvasView.moveToPreviousScreen();

        txtViewNumberScreen.setText(String.valueOf(canvasView.numberScreen+1));
    }
    public void moveToNextScreen(View v) {
        canvasView.moveToNextScreen();
        txtViewNumberScreen.setText(String.valueOf(canvasView.numberScreen+1));
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
    public void saveWithoutEncryption(String fileName1) {
        String fileName = Environment.getExternalStorageDirectory() + "/" + fileName1;
        try {
            OutputStream stream = new FileOutputStream(fileName);
            Bitmap myBitmap = loadBitmapFromView(holder);
            myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.close();
        }
        catch(FileNotFoundException e) {
        }
        catch(IOException e) {
        }
    }
    public void encryptAndSave(String key, String fileName1) {
        String fileName = Environment.getExternalStorageDirectory() + "/" + fileName1;
        try {
            OutputStream stream = new FileOutputStream(fileName);
            Bitmap myBitmap = loadBitmapFromView(holder);
            myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

            Encrypt encrypt = new Encrypt(this, key);
            encrypt.EncryptBitmap(convertBitmapToByteArray(myBitmap));
            stream.close();
        }
        catch(FileNotFoundException e) {
        }
        catch(IOException e) {
        }
    }
}