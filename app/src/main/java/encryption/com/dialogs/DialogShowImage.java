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

import java.util.ArrayList;

import encryption.com.Database.DatabaseHelper;
import encryption.com.cybersafeencryption.R;


public class DialogShowImage extends DialogFragment implements View.OnClickListener {
    DatabaseHelper dbHelper;
    private ImageView imageViewBitmap;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("");
        View v = inflater.inflate(R.layout.dialog_show_image, null);
        imageViewBitmap = (ImageView) v.findViewById(R.id.bitmap_view);
        int numberBitmap = getArguments().getInt("nImage");
        Log.d("Number in database ", Integer.toString(numberBitmap));
        dbHelper = new DatabaseHelper(getActivity());
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query("table_image", null, null, null, null, null, null);
       // mListBitmaps = new ArrayList<>();
        byte[] image = null;
        int i = 0;
        if (cursor.moveToFirst()) {
            do {
                if(i == numberBitmap) {
                    image = cursor.getBlob(cursor.getColumnIndex("image_data"));
                }
                i++;
              //  mListBitmaps.add(convertBytesToBitmap(image));
            } while (cursor.moveToNext());
        }
        Bitmap bitmap1 = convertBytesToBitmap(image);
        imageViewBitmap.setImageBitmap(bitmap1);
        return v;
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_save_without_encryption:
                Log.d("BUTTON", "Button save without encryption");
                break;
            case R.id.button_encrypt_save:
                Log.d("BUTTON", "Button encryptn and save");
                break;
            case R.id.button_cancel:
                Log.d("BUTTON", "CANCEL");
                break;
        }
        dismiss();
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }

    // convert from byte array to bitmap
    public static Bitmap convertBytesToBitmap(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}