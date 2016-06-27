package encryption.com.cybersafeencryption;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
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
    private EditText mEditTextKey;
    private EditText mEditSourceText;
    private Button btnEncryptText;
    private Button btnDecryptText;
    private Button btnSelectFile;
    private TextView mTextViewEncrypted;
    private TextView mTextViewDecrypted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditTextKey  = (EditText) findViewById(R.id.editTextKey);
        mEditSourceText = (EditText) findViewById(R.id.editTextSourceText);
        btnEncryptText = (Button) findViewById(R.id.btnEncryptText);
        btnDecryptText = (Button) findViewById(R.id.btnDecryptText);
        btnSelectFile = (Button) findViewById(R.id.btnChooseFile);


        mTextViewEncrypted = (TextView) findViewById(R.id.textViewEncrypted);
        mTextViewDecrypted  = (TextView) findViewById(R.id.textViewDecrypted);

        btnEncryptText.setOnClickListener(this);
        btnDecryptText.setOnClickListener(this);
        btnSelectFile.setOnClickListener(this);
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
               // showFileChooser();
              //  WriteText1();
                Write2();
                Log.d("WWWW", "GGGG");
                break;
        }
    }
    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

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
            Log.d("SS", "File Uri: " + uri.toString());
             String path = "/data/data/lalallalaa.txt";
             File file = new File(path);
             //   File file = new File(path);

                   /*  try {
                        // if (!file.exists()) {
                           //  file.createNewFile();
                           //  Log.d("FILEFIL1E", "CREATION");
                        // }
                     } catch(IOException e) {

                     }*/

                    FileOutputStream stream = null;
                    try {
                     //   stream = new FileOutputStream(file);
                        stream  = new FileOutputStream(path);
                    } catch(FileNotFoundException e) {
                    }
             try {
                     showBytes(getContentResolver().openInputStream(uri), stream);
                } catch(IOException e) {
             }
        }
        break;
        }
            super.onActivityResult(requestCode, resultCode, data);
        }
    private void showBytes(InputStream is, FileOutputStream stream) throws IOException{
        int bytesCounter = 0;
        int value;
        Log.d("IMA4512G1E", "showBytes");
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
         Log.d("writeToFile4545", Byte.toString(b));
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
            File myFile = new File("/sdcard/mysdfile.txt");
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
