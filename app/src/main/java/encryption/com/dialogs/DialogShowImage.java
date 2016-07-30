package encryption.com.dialogs;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import encryption.com.Database.DatabaseHelper;
import encryption.com.cybersafeencryption.R;


public class DialogShowImage extends DialogFragment implements View.OnClickListener {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;
    private int mIdDatabase;
    private ImageView imageViewBitmap;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("");
        View v = inflater.inflate(R.layout.dialog_show_image, null);
        v.findViewById(R.id.btn_delete_bitmap).setOnClickListener(this);
        imageViewBitmap = (ImageView) v.findViewById(R.id.bitmap_view);
        int numberBitmap = getArguments().getInt("nImage");
        dbHelper = new DatabaseHelper(getActivity());
        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query("table_image", null, null, null, null, null, null);
        byte[] image = null;
        int i = 0;
        if (cursor.moveToFirst()) {
            do {
                if(i == numberBitmap) {
                    image = cursor.getBlob(cursor.getColumnIndex("image_data"));
                    mIdDatabase = cursor.getInt(0);
                }
                i++;
            } while (cursor.moveToNext());
        }
        imageViewBitmap.setImageBitmap(convertBytesToBitmap(image));
        return v;
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_delete_bitmap:
                  deleteImage();
                break;

        }
        dismiss();
    }
    private void deleteImage() {
        //Delete the row
        database.delete("table_image", "id" + " = ?", new String[] {Integer.toString(mIdDatabase) });
        database.close();
    }
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }

    // convert from byte array to bitmap
    public  Bitmap convertBytesToBitmap(byte[] image) {
        BitmapFactory.Options options=new BitmapFactory.Options();// Create object of bitmapfactory's option method for further option use
        options.inPurgeable = true; // inPurgeable is used to free up memory while required
        Bitmap songImage1 = BitmapFactory.decodeByteArray(image,0, image.length,options);//Decode image, "thumbnail" is the object of image file
       // Bitmap songImage = Bitmap.createScaledBitmap(songImage1, 300 , 200 , true);// convert decoded bitmap into well scalled Bitmap format.
    return songImage1;
    }
}