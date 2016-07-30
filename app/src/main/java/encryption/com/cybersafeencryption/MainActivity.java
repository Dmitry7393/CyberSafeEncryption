package encryption.com.cybersafeencryption;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;

import encryption.com.Database.DatabaseHelper;
import encryption.com.adapters.DividerItemDecoration;
import encryption.com.adapters.MyRecyclerViewAdapter;
import encryption.com.adapters.RecyclerItemClickListener;
import encryption.com.dialogs.DialogShowImage;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;
    private DialogShowImage mDialogFragmentShowImage;
    private ImageView singleImageView;
    private int numberCurrentImage = -1;
    private static final int SPACE_BETWEEN_IMAGES = 15;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnOpenEncryptTextActivity = (Button) findViewById(R.id.btn_open_encrypt_text_activity);
        Button btnOpenEncryptActivity = (Button) findViewById(R.id.btn_open_encrypt_files_activity);
        Button btnOpenDrawingActivity = (Button) findViewById(R.id.btn_open_drawing_activity);

        singleImageView = (ImageView) findViewById(R.id.single_bitmap);
        if (btnOpenEncryptTextActivity != null && btnOpenEncryptActivity != null && btnOpenDrawingActivity != null) {

            btnOpenEncryptTextActivity.setOnClickListener(this);
            btnOpenEncryptActivity.setOnClickListener(this);
            btnOpenDrawingActivity.setOnClickListener(this);
        }
        //init database
        dbHelper = new DatabaseHelper(this);
        database = dbHelper.getWritableDatabase();

        mDialogFragmentShowImage = new DialogShowImage();

        initRecyclerView(getBitmapsFromDatabase());
        openBitmap(this.findViewById(R.id.single_bitmap));
        Log.d("START", "RRRR");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_open_encrypt_text_activity:
                Intent intent = new Intent(MainActivity.this, EncryptTextActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_open_encrypt_files_activity:
                Intent intent2 = new Intent(MainActivity.this, EncryptFilesActivity.class);
                startActivity(intent2);
                break;
            case R.id.btn_open_drawing_activity:
                Intent intent3 = new Intent(MainActivity.this, DrawingActivity.class);
                startActivity(intent3);
                break;
        }
    }

    private void initRecyclerView(ArrayList<Bitmap> mListImages) { //[Comment] What's wrong with formatting??? Ctrl + Shift + L. It's not a C++.
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        if (mRecyclerView != null) {
            mRecyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            mRecyclerView.setLayoutManager(layoutManager);

            RecyclerView.Adapter mAdapter = new MyRecyclerViewAdapter(mListImages, this);

            mRecyclerView.setAdapter(mAdapter);
            RecyclerView.ItemDecoration itemDecoration =
                    new DividerItemDecoration(SPACE_BETWEEN_IMAGES);
            mRecyclerView.addItemDecoration(itemDecoration);

            mRecyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(this, mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Bundle bundle = new Bundle();
                            bundle.putInt("nImage", position);
                            mDialogFragmentShowImage.setArguments(bundle);
                            mDialogFragmentShowImage.show(getFragmentManager(), "dlg1");
                        }

                        @Override
                        public void onLongItemClick(View view, int position) {
                            Log.d("onLongItemClick3", Integer.toString(position));
                        }
                    })
            );
        }
    }

    private ArrayList<Bitmap> getBitmapsFromDatabase() {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query("table_image", null, null, null, null, null, null);
        ArrayList<Bitmap> mListBitmaps = new ArrayList<>();
        byte[] image;
        if (cursor.moveToFirst()) {
            do {
                image = cursor.getBlob(cursor.getColumnIndex("image_data"));
                mListBitmaps.add(convertBytesToBitmap(image));
            } while (cursor.moveToNext());
        }
        return mListBitmaps;
    }

    /*  // convert from byte array to bitmap
      public static Bitmap convertBytesToBitmap(byte[] image) {
          return BitmapFactory.decodeByteArray(image, 0, image.length);
      }*/
    // convert from byte array to bitmap
    public Bitmap convertBytesToBitmap(byte[] image) {
        BitmapFactory.Options options = new BitmapFactory.Options();// Create object of bitmapfactory's option method for further option use
        // options.inJustDecodeBounds = true;
        // options.inJustDecodeBounds = true;
        options.inPurgeable = true; // inPurgeable is used to free up memory while required
        return BitmapFactory.decodeByteArray(image, 0, image.length, options);
    }

    public void openBitmap(View v) {
        numberCurrentImage++;
        Cursor cursor = database.query("table_image", null, null, null, null, null, null);
        byte[] image = null;
        int i = 0;
        if (cursor.moveToFirst()) {
            do {
                if (i == numberCurrentImage) {
                    image = cursor.getBlob(cursor.getColumnIndex("image_data"));
                    singleImageView.setImageBitmap(convertBytesToBitmap(image));
                    break;
                }
                i++;
            } while (cursor.moveToNext());
        }
    }
}
