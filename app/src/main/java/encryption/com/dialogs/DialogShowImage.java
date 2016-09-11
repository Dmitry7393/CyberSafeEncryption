package encryption.com.dialogs;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

import encryption.com.Database.DatabaseHelper;
import encryption.com.cybersafeencryption.MainActivity;
import encryption.com.cybersafeencryption.R;


public class DialogShowImage extends DialogFragment implements View.OnClickListener {
    private SQLiteDatabase database;
    private int mIdDatabase;
    private Bitmap mBitmap;
    private EditText mEditTextKey;
    private EditText meEditTextFileName;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View v = inflater.inflate(R.layout.dialog_show_image, null);
        v.findViewById(R.id.btn_delete_bitmap).setOnClickListener(this);
        v.findViewById(R.id.button_save_bitmap).setOnClickListener(this);
        v.findViewById(R.id.button_encrypt_and_save).setOnClickListener(this);
        meEditTextFileName = (EditText) v.findViewById(R.id.editTextFileName);

        mEditTextKey = (EditText) v.findViewById(R.id.editTextKey);
        ImageView imageViewBitmap = (ImageView) v.findViewById(R.id.bitmap_view);
        int numberBitmap = getArguments().getInt("nImage");
        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
        database = dbHelper.getWritableDatabase();

        mBitmap = getBitmapByOrderNumber(numberBitmap);
        imageViewBitmap.setImageBitmap(mBitmap);

        String fileName = "image" + String.valueOf(mIdDatabase) + ".png";
        meEditTextFileName.setText(fileName);
        mEditTextKey.setText("dmitry.n50");
        return v;
    }

    private Bitmap getBitmapByOrderNumber(int orderNumber) {
        Cursor cursor = database.query("table_image", null, null, null, null, null, null);
        byte[] image = null;
        int i = 0;
        if (cursor.moveToFirst()) {
            do {
                if (i == orderNumber) {
                    image = cursor.getBlob(cursor.getColumnIndex("image_data"));
                    mIdDatabase = cursor.getInt(0);
                }
                i++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        return convertBytesToBitmap(image);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_delete_bitmap:
                deleteImage();
                break;
            case R.id.button_save_bitmap:
                ((MainActivity) getActivity()).saveImage(mBitmap, false, meEditTextFileName.getText().toString(), mEditTextKey.getText().toString());
                break;
            case R.id.button_encrypt_and_save:
                ((MainActivity) getActivity()).saveImage(mBitmap, true, meEditTextFileName.getText().toString(), mEditTextKey.getText().toString());
                break;
        }
        dismiss();
    }

    private void deleteImage() {
        //Delete the row
        database.delete("table_image", "id" + " = ?", new String[]{Integer.toString(mIdDatabase)});
        database.close();
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }

    public Bitmap convertBytesToBitmap(byte[] image) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        return BitmapFactory.decodeByteArray(image, 0, image.length, options);
    }


}