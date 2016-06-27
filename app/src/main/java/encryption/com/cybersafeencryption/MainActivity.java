package encryption.com.cybersafeencryption;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import encryption.com.AES.Decrypt;
import encryption.com.AES.Encrypt;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int FILE_SELECT_CODE = 0;
    private EditText mEditTextKey;
    private EditText mEditSourceText;
    private EditText mEditNameEncryptedFile;
    private TextView mTextViewEncrypted;
    private TextView mTextViewDecrypted;
    private Uri mUriSourceFile;
    private Uri mUriSingleFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditTextKey  = (EditText) findViewById(R.id.editTextKey);
        mEditSourceText = (EditText) findViewById(R.id.editTextSourceText);
        mEditNameEncryptedFile = (EditText) findViewById(R.id.editNameEncryptedFile);
        Button   btnEncryptText = (Button) findViewById(R.id.btnEncryptText);
        Button   btnDecryptText = (Button) findViewById(R.id.btnDecryptText);
        Button  btnSelectFile = (Button) findViewById(R.id.btnChooseFile);
        Button  btnChooseDirectory = (Button) findViewById(R.id.btnChooseDirectory);

        mTextViewEncrypted = (TextView) findViewById(R.id.textViewEncrypted);
        mTextViewDecrypted  = (TextView) findViewById(R.id.textViewDecrypted);

        if(btnEncryptText != null && btnDecryptText != null) {
            btnEncryptText.setOnClickListener(this);
            btnDecryptText.setOnClickListener(this);
            btnSelectFile.setOnClickListener(this);
            btnChooseDirectory.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnEncryptText:
                Encrypt encrText = new Encrypt(this, mEditTextKey.getText().toString());
                encrText.EncryptText(mEditSourceText.getText().toString());
                String cipher_text = encrText.getCipherText();
                mTextViewEncrypted.setText(cipher_text);
                break;
            case R.id.btnDecryptText:
                Decrypt decrypt = new Decrypt(mEditTextKey.getText().toString());
                decrypt.DecryptText(mTextViewEncrypted.getText().toString());
                mTextViewDecrypted.setText("");
                mTextViewDecrypted.setText(decrypt.get_text());
                break;
            case R.id.btnChooseFile:
                chooseFileAndUpload();
                break;
            case R.id.btnChooseDirectory:
                //First it is need to choose output directory
                Intent intent = new Intent(this, DirectoryPicker.class);
                // optionally set options here
                startActivityForResult(intent, DirectoryPicker.PICK_DIRECTORY);

                break;
        }
    }
    private void chooseFileAndUpload() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        }
        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
         case FILE_SELECT_CODE:
         if (resultCode == RESULT_OK) {
         // Get the Uri of the selected file
             mUriSourceFile = data.getData();
             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 && mUriSourceFile == null) {
                 Log.d("INTO CYCLE", "RRRRRRRRRRRRRRRR");
                 ClipData clipData = data.getClipData();
                 int count = clipData.getItemCount();
                 for(int i = 0; i < count; i++) {
                     mUriSourceFile = clipData.getItemAt(i).getUri();
                     if (mUriSourceFile != null) {
                         Log.d("UUUUU", mUriSourceFile.toString());
                         startUpload(mUriSourceFile);
                     }
                 }
             } else { //If we have single file
                  Log.d("QQQQQQQQQ2", "WE have single file");
                 File tf = new File(mUriSourceFile.getPath());
                 TextView t = (TextView) findViewById(R.id.textViewSourceFiles);
                 t.setText(tf.getName());
                 mUriSingleFile = mUriSourceFile;
             }
        }
        break;
        }
        if(requestCode == DirectoryPicker.PICK_DIRECTORY && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
           String pathDirectory = (String) extras.get(DirectoryPicker.CHOSEN_DIRECTORY);
            Log.d("PATH", pathDirectory);
            File myFile = new File(pathDirectory + "/" + mEditNameEncryptedFile.getText().toString());
            FileOutputStream stream = null;
                try {
                    stream  = new FileOutputStream(myFile);
                } catch(FileNotFoundException e) {
                }
                try {
                    Encrypt encrypt = new Encrypt(this, mEditTextKey.getText().toString());
                    encrypt.EncryptSingleFile(getContentResolver().openInputStream(mUriSingleFile), stream);
                } catch(IOException e) {
                }
        }
            super.onActivityResult(requestCode, resultCode, data);
        }
    private void  startUpload(Uri uri) {
        TextView t = (TextView) findViewById(R.id.textViewSourceFiles);
    //    Ringtone r = RingtoneManager.getRingtone(this, uri);
        File tempF = new File(uri.getPath());
        if(t != null) {
            String result = t.getText() + tempF.getName();
            t.setText(result);
        }
    }

    /*private void EncryptFile(InputStream is, FileOutputStream stream) throws IOException{
        int bytesCounter = 0;
        int value;
        while ((value = is.read()) != -1) {
               //System.out.print(String.format("0x%02X", (byte) value) );
               writeToFile(stream, (byte)value);
                if (bytesCounter == 15) {
                   // System.out.println();
                    bytesCounter = 0;

                } else {
                    bytesCounter++;
                    //System.out.println();
                }
            }
        is.close();
    }*/
    public void writeToFile(FileOutputStream fos, byte b) throws IOException
    {
            fos.write(b);
    }
    public static String getFileNameByUri(Context context, Uri uri)
    {
        String fileName="unknown";//default fileName
        Uri filePathUri = uri;
        if (uri.getScheme().toString().compareTo("content")==0)
        {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            if (cursor.moveToFirst())
            {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);//Instead of "MediaStore.Images.Media.DATA" can be used "_data"
                filePathUri = Uri.parse(cursor.getString(column_index));
                fileName = filePathUri.getLastPathSegment().toString();
            }
        }
        else if (uri.getScheme().compareTo("file")==0)
        {
            fileName = filePathUri.getLastPathSegment().toString();
        }
        else
        {
            fileName = fileName+"_"+filePathUri.getLastPathSegment();
        }
        return fileName;
    }

    public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index =             cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }
    public String getPath2(Uri _uri) {
        String filePath = null;
        Log.d("","URI = "+ _uri);
        if (_uri != null && "content".equals(_uri.getScheme())) {
            Cursor cursor = this.getContentResolver().query(_uri, new String[] { android.provider.MediaStore.Images.ImageColumns.DATA }, null, null, null);
            cursor.moveToFirst();
            filePath = cursor.getString(0);
            cursor.close();
        } else {
            filePath = _uri.getPath();
        }
        Log.d("","Chosen path = "+ filePath);
        return filePath;
    }
}
