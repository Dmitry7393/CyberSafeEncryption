package encryption.com.cybersafeencryption;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import encryption.com.Database.DatabaseHelper;
import encryption.com.adapters.DividerItemDecoration;
import encryption.com.adapters.MyRecyclerViewAdapter;
import encryption.com.adapters.RecyclerItemClickListener;
import encryption.com.dialogs.DialogShowImage;
import encryption.com.service.EncryptService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private SQLiteDatabase database;
    private DialogShowImage mDialogFragmentShowImage;
    private ArrayList<Integer> mListImagesID;
    private Bitmap mBitmap;
    private Boolean mSaveWithEncryption;
    private String mFileName;
    private String mKey;

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
        //init database
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        database = dbHelper.getWritableDatabase();

        mDialogFragmentShowImage = new DialogShowImage();
        mListImagesID = new ArrayList<>();
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

    protected void onStart() {
        super.onStart();
        setImagesIDToArrayList();
        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        if (mRecyclerView != null) {
            mRecyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            mRecyclerView.setLayoutManager(layoutManager);

            RecyclerView.Adapter mAdapter = new MyRecyclerViewAdapter(mListImagesID, this);

            mRecyclerView.setAdapter(mAdapter);
            RecyclerView.ItemDecoration itemDecoration =
                    new DividerItemDecoration(15);
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

    public void setImagesIDToArrayList() {
        Cursor cursor = database.query("table_image", null, null, null, null, null, null);
        mListImagesID.clear();
        if (cursor.moveToFirst()) {
            do {
                mListImagesID.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public void saveImage(Bitmap bitmap, Boolean savingType, String fileName, String key) {
        mBitmap = bitmap;
        mSaveWithEncryption = savingType;
        mFileName = fileName;
        mKey = key;
        Intent intent = new Intent(this, DirectoryPicker.class);
        startActivityForResult(intent, DirectoryPicker.PICK_DIRECTORY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DirectoryPicker.PICK_DIRECTORY) {
            Bundle extras = data.getExtras();
            String pathDirectory = (String) extras.get(DirectoryPicker.CHOSEN_DIRECTORY);

            Intent intent = new Intent(this, EncryptService.class);

            byte[] byteArray = null;
            try {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byteArray = stream.toByteArray();
                stream.close();
            } catch (IOException e) {
            }
            intent.putExtra("fileName", mFileName);
            intent.putExtra("image", byteArray);
            intent.putExtra("pathDirectory", pathDirectory);
            if(mSaveWithEncryption) {
                Toast.makeText(MainActivity.this, "Encryption", Toast.LENGTH_LONG).show();
                intent.putExtra("type_saving", "save_with_encryption");
                intent.putExtra("key", mKey);
            } else {
                Toast.makeText(MainActivity.this, "Saving", Toast.LENGTH_LONG).show();
                intent.putExtra("type_saving", "save_without_encryption");
            }
            startService(intent);
           /* if (!mSaveWithEncryption) {
                try {
                    OutputStream stream = new FileOutputStream(pathDirectory + "/" + mFileName);
                    mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    stream.close();
                } catch (IOException e) {
                    Log.d("SAVING Image", "Not correct path");
                }
            } else {
                try {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    Toast.makeText(MainActivity.this, "TTT", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(this, EncryptService.class);
                    intent.putExtra("image", byteArray);
                    intent.putExtra("key", mKey);
                    intent.putExtra("pathDirectory", pathDirectory);
                    intent.putExtra("fileName", mFileName);

                    startService(intent);

                    stream.close();
                } catch (IOException e) {
                    Log.d("SAVING Image", "Not correct path");
                }
            }*/
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
