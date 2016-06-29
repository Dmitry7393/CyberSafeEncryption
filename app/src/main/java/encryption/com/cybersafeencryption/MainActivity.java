package encryption.com.cybersafeencryption;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
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
import java.util.ArrayList;
import java.util.List;

import encryption.com.AES.Decrypt;
import encryption.com.AES.Encrypt;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int FILE_SELECT_CODE = 0;
    private EditText mEditTextKey;
    private EditText mEditSourceText;
    private EditText mEditNameEncryptedFile;
    private TextView mTextViewEncrypted;
    private TextView mTextViewDecrypted;
    private Uri mUriSingleFile;
    private Boolean saveEncryptedFile;
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
        Button  btnSelectdecryptedFile = (Button) findViewById(R.id.btnSelectDecryptedFile);
        Button  btnDecryptile = (Button) findViewById(R.id.btnDecryptFile);

        mTextViewEncrypted = (TextView) findViewById(R.id.textViewEncrypted);
        mTextViewDecrypted  = (TextView) findViewById(R.id.textViewDecrypted);

        try {
                btnEncryptText.setOnClickListener(this);
                btnDecryptText.setOnClickListener(this);
                btnSelectFile.setOnClickListener(this);
                btnChooseDirectory.setOnClickListener(this);
                btnSelectdecryptedFile.setOnClickListener(this);
                btnDecryptile.setOnClickListener(this);
        } catch(java.lang.NullPointerException e) {

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
                saveEncryptedFile = true;
                break;
            case R.id.btnDecryptFile:
                Intent intent2 = new Intent(this, DirectoryPicker.class);
                startActivityForResult(intent2, DirectoryPicker.PICK_DIRECTORY);
                saveEncryptedFile = false;
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
            Uri uri = data.getData();
             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 && uri == null) {
                 Log.d("INTO CYCLE", "RRRRRRRRRRRRRRRR");
                 ClipData clipData = data.getClipData();
                 int count = clipData.getItemCount();
                 for(int i = 0; i < count; i++) {
                     uri = clipData.getItemAt(i).getUri();
                     if (uri != null) {
                         Log.d("UUUUU", uri.toString());
                         showFileNames(uri);
                     }
                 }
             } else { //If we have single file
                 File tf = new File(uri.getPath());
                 TextView t = (TextView) findViewById(R.id.textViewSourceFiles);
                 t.setText(tf.getName());
                 mEditNameEncryptedFile.setText(tf.getName());
                 mUriSingleFile = uri;
             }
        }
        break;
        }
        if(requestCode == DirectoryPicker.PICK_DIRECTORY && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
           String pathDirectory = (String) extras.get(DirectoryPicker.CHOSEN_DIRECTORY);
            Log.d("PATH1", pathDirectory);
            File myFile = new File(pathDirectory + "/" + mEditNameEncryptedFile.getText().toString());
            FileOutputStream stream = null;
                try {
                    stream  = new FileOutputStream(myFile);
                } catch(FileNotFoundException e) {
                }
                try {
                    if(saveEncryptedFile) {
                        Encrypt encrypt = new Encrypt(this, mEditTextKey.getText().toString());
                        encrypt.EncryptSingleFile(getContentResolver().openInputStream(mUriSingleFile), stream);
                    } else {
                        Decrypt decrypt = new Decrypt(mEditTextKey.getText().toString());
                        decrypt.DecryptSingleFile(getContentResolver().openInputStream(mUriSingleFile), stream);
                    }

                } catch(IOException e) {
                }
        }
            super.onActivityResult(requestCode, resultCode, data);
        }
    private void  showFileNames(Uri uri) {
        TextView t = (TextView) findViewById(R.id.textViewSourceFiles);
        File tempF = new File(uri.getPath());
        if(t != null) {
            String result = t.getText() + tempF.getName();
            t.setText(result);
        }
    }
}
