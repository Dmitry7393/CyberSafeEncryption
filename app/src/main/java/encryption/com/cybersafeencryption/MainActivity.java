package encryption.com.cybersafeencryption;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.DialogFragment;
import android.widget.ImageView;

import encryption.com.Database.DBHelper;
import encryption.com.Database.DatabaseHelper;
import encryption.com.adapters.DividerItemDecoration;
import encryption.com.adapters.MyRecyclerViewAdapter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnOpenEncryptTextActivity = (Button) findViewById(R.id.btn_open_encrypt_text_activity);
        Button btnOpenEncryptActivity = (Button) findViewById(R.id.btn_open_encrypt_files_activity);
        Button btnOpenDrawingActivity = (Button) findViewById(R.id.btn_open_drawing_activity);

        if (btnOpenEncryptTextActivity != null && btnOpenEncryptActivity != null && btnOpenDrawingActivity != null) {

            btnOpenEncryptTextActivity.setOnClickListener(this);
            btnOpenEncryptActivity.setOnClickListener(this);
            btnOpenDrawingActivity.setOnClickListener(this);
        }
        dbHelper = new DatabaseHelper(this);
        initRecyclerView(getBitmapsFromDatabase());

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

    /**
     * Creating RecyclerView
     */
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
                    new DividerItemDecoration(15);
            mRecyclerView.addItemDecoration(itemDecoration);
        }
    }

    private ArrayList<Bitmap> getBitmapsFromDatabase() {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query("table_image", null, null, null, null, null, null);
        ArrayList<Bitmap> listBitmaps = new ArrayList<>();
        byte[] image;
        if (cursor.moveToFirst()) {
            do {
                image = cursor.getBlob(cursor.getColumnIndex("image_data"));
                listBitmaps.add(convertBytesToBitmap(image));
            } while (cursor.moveToNext());
        }
        return listBitmaps;
    }

    // convert from byte array to bitmap
    public static Bitmap convertBytesToBitmap(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

}
