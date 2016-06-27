package encryption.com.cybersafeencryption;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;

import encryption.com.AES.Decrypt;
import encryption.com.AES.Encrypt;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int FILE_SELECT_CODE = 0;
    private static final int DIRECTORY_SELECT_CODE = 2;
    private String pathDirectory = "";
    private EditText mEditTextKey;
    private EditText mEditSourceText;
    private Button btnEncryptText;
    private Button btnDecryptText;
    private Button btnSelectFile;
    private Button btnChooseDirectory;
    private TextView mTextViewEncrypted;
    private TextView mTextViewDecrypted;

    private String[] mFileList;
    private File mPath = new File(Environment.getExternalStorageDirectory() + "//yourdir//");
    private String mChosenFile;
    private static final String FTYPE = ".txt";
    private static final int DIALOG_LOAD_FILE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditTextKey  = (EditText) findViewById(R.id.editTextKey);
        mEditSourceText = (EditText) findViewById(R.id.editTextSourceText);
        btnEncryptText = (Button) findViewById(R.id.btnEncryptText);
        btnDecryptText = (Button) findViewById(R.id.btnDecryptText);
        btnSelectFile = (Button) findViewById(R.id.btnChooseFile);
        btnChooseDirectory = (Button) findViewById(R.id.btnChooseDirectory);

        mTextViewEncrypted = (TextView) findViewById(R.id.textViewEncrypted);
        mTextViewDecrypted  = (TextView) findViewById(R.id.textViewDecrypted);

        btnEncryptText.setOnClickListener(this);
        btnDecryptText.setOnClickListener(this);
        btnSelectFile.setOnClickListener(this);
        btnChooseDirectory.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnEncryptText:
                Encrypt encrText = new Encrypt(mEditTextKey.getText().toString());
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
                showFileChooser();
              //  WriteText1();
              //  Write2();
               // Log.d("WWWW", "GGGG");
                break;
            case R.id.btnChooseDirectory:
                //chooseDirectory();
              //  openFolder();
                Intent intent = new Intent(this, DirectoryPicker.class);
                // optionally set options here
                startActivityForResult(intent, DirectoryPicker.PICK_DIRECTORY);
                break;
        }
    }
    public void openFolder()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath());
        intent.setDataAndType(uri, "text/csv");
        startActivity(Intent.createChooser(intent, "Open folder"));
    }
    private void chooseDirectory() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        Toast.makeText(this, "Choosing directory", Toast.LENGTH_SHORT).show();
        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a Directory to Upload"),
                    DIRECTORY_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }
    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        Log.d("showFileChooser", "showFileChooser");
        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
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
            Log.d("SAVINGFILE", "File Uri: " + uri.toString());
             File myFile = new File(pathDirectory + "/Encrypted.png");
                    FileOutputStream stream = null;
                    try {
                        stream  = new FileOutputStream(myFile);
                    } catch(FileNotFoundException e) {
                    }
             try {
                     showBytes(getContentResolver().openInputStream(uri), stream);
                } catch(IOException e) {
             }
        }
        break;
        }
        if(requestCode == DirectoryPicker.PICK_DIRECTORY && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            String path = (String) extras.get(DirectoryPicker.CHOSEN_DIRECTORY);
            pathDirectory = path;
            Log.d("PATHPATH", path);
        }
            super.onActivityResult(requestCode, resultCode, data);


        }
    private void showBytes(InputStream is, FileOutputStream stream) throws IOException{
        int bytesCounter = 0;
        int value;
        while ((value = is.read()) != -1) {
               System.out.print(String.format("0x%02X", (byte) value) );
               writeToFile(stream, (byte)value);
                if (bytesCounter == 15) {
                    System.out.println();
                    bytesCounter = 0;

                } else {
                    bytesCounter++;
                    System.out.println();
                }
            }
        is.close();
    }
    public void writeToFile(FileOutputStream fos, byte b) throws IOException
    {
         Log.d("writeToFile", Byte.toString(b));
            fos.write(b);
    }
    public void WriteText1() {
        EditText txt=(EditText)findViewById(R.id.txtwrite);

        Log.d("112sa2", "writeText");
        try {
            BufferedWriter fos = new BufferedWriter(new FileWriter(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+ "File.txt"));
            fos.write(txt.getText().toString().trim());
            fos.close();
            Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
    public void Write2() {
        try {
            File myFile = new File("/sdcard/mysdfile.png");
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter =new OutputStreamWriter(fOut);
            myOutWriter.append("dfgdhg");
            myOutWriter.close();
            fOut.close();
            Toast.makeText(this,"Done writing SD 'mysdfile.txt'", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}
