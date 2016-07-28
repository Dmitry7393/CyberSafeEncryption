package encryption.com.cybersafeencryption;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
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
import encryption.com.Database.DatabaseHelper;
import encryption.com.dialogs.DialogSaveBitmap;


public class DrawingActivity extends AppCompatActivity implements DialogSaveBitmap.saveBitmapInterface,
        ColorPicker.OnColorChangedListener {
    RelativeLayout holder;
    CanvasView canvasView;
    TextView txtViewNumberScreen;
    DialogSaveBitmap mDialogSaveBitmap;
    String mfileName;
    private DatabaseHelper dbHelper;
    String mKey;
    int mSaveWithoutEncryption = 0;
    private boolean setBackgroundColor;
    private Activity activity;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        this.activity = this;
        canvasView = (CanvasView) findViewById(R.id.signature_canvas);
        holder = (RelativeLayout) findViewById(R.id.layout_canvas);
        txtViewNumberScreen = (TextView) findViewById(R.id.number_screen);
        txtViewNumberScreen.setText(String.valueOf(canvasView.getNumberOfScreens() + 1));
        mDialogSaveBitmap = new DialogSaveBitmap();
        dbHelper = new DatabaseHelper(this);
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
        if (canvasView.getNumberOfScreens() != 0)
            canvasView.moveToPreviousScreen();

        txtViewNumberScreen.setText(String.valueOf(canvasView.getNumberOfScreens() + 1));
    }

    public void moveToNextScreen(View v) {
        canvasView.moveToNextScreen();
        txtViewNumberScreen.setText(String.valueOf(canvasView.getNumberOfScreens() + 1));
    }

    public Bitmap loadBitmapFromView(View v) {
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

    public void saveWithoutEncryption(String fileName) {
        mfileName = fileName;
        mSaveWithoutEncryption = 1;
        Intent intent = new Intent(this, DirectoryPicker.class);
        startActivityForResult(intent, DirectoryPicker.PICK_DIRECTORY);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DirectoryPicker.PICK_DIRECTORY && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            String pathDirectory = (String) extras.get(DirectoryPicker.CHOSEN_DIRECTORY);
            if (mSaveWithoutEncryption == 1) {
                try {
                    OutputStream stream = new FileOutputStream(pathDirectory + "/" + mfileName);
                    Bitmap myBitmap = loadBitmapFromView(holder);
                    myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    stream.close();
                } catch (FileNotFoundException e) {
                } catch (IOException e) {
                }
            } else {
                try {
                    OutputStream stream = new FileOutputStream(pathDirectory + "/" + mfileName);
                    Bitmap myBitmap = loadBitmapFromView(holder);
                    myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                    Encrypt encrypt = new Encrypt(this, mKey);
                    encrypt.EncryptBitmap(convertBitmapToByteArray(myBitmap), pathDirectory + "/" + mfileName);
                    stream.close();
                } catch (FileNotFoundException e) {
                } catch (IOException e) {
                }
            }
        }
    }

    public void encryptAndSave(String key, String fileName) {
        mfileName = fileName;
        mKey = key;
        mSaveWithoutEncryption = 0;
        Intent intent = new Intent(this, DirectoryPicker.class);
        startActivityForResult(intent, DirectoryPicker.PICK_DIRECTORY);
    }

    public void getColorStroke(View v) {
        new ColorPicker(activity, DrawingActivity.this, Color.WHITE)
                .show();
        setBackgroundColor = false;
    }

    public void colorChanged(int color) {
        if (setBackgroundColor)
            canvasView.changeBackgroundColor(color);
        else canvasView.changeColor(color);
    }

    public void changeBackgroundColor(View v) {
        new ColorPicker(activity, DrawingActivity.this, Color.WHITE)
                .show();
        setBackgroundColor = true;
    }

    public void saveBitmapToDatabase(View v) {
        Bitmap myBitmap = loadBitmapFromView(holder);
        addImageToDatabase("image", getBytes(myBitmap));
    }

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    public void addImageToDatabase(String name, byte[] image) throws SQLiteException {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("image_data", image);
        database.insert("table_image", null, cv);
    }
}