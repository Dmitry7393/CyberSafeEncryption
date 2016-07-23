package encryption.com.cybersafeencryption;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import encryption.com.AES.Decrypt;
import encryption.com.AES.Encrypt;

public class EncryptFilesActivity extends Activity implements View.OnClickListener{
    List<ContactInfo> mList;
    ListViewAdapter customAdapter;
    private static final int FILE_SELECT_CODE = 0;

    private List<Uri> mUriList = new ArrayList<>();
    List<String> mListOutputPaths = new ArrayList<>();
    private Boolean mBooleanEncrypt;
    EditText editKey;
    ListView tabListView;
    Handler mHandler;
    Button   btnEncrypt;
    Encrypt mEncrypt;
    Decrypt mDecrypt;
    Timer timerEncrypt;
    private ProgressBar mProgress;
    private long mSizeOfSourceFiles = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypt_files);

         tabListView = (ListView) findViewById(R.id.list_view_files);

        mList = new ArrayList<>();
        customAdapter = new ListViewAdapter(this, R.layout.item, mList);

        tabListView.setAdapter(customAdapter);
        Button   btnAdd = (Button) findViewById(R.id.button_select_files);
        btnEncrypt = (Button) findViewById(R.id.button_encrypt);
        Button   btnDecrypt = (Button) findViewById(R.id.button_decrypt);

        editKey = (EditText) findViewById(R.id.edit_key_encrypt_files);
        mProgress = (ProgressBar) findViewById(R.id.progress_bar);

        btnAdd.setOnClickListener(this);
        btnEncrypt.setOnClickListener(this);
        btnDecrypt.setOnClickListener(this);
        mHandler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if(msg.what == 1) {
                    long valuePercent = (100 * mEncrypt.getCommonSizeOfFiles()) / mSizeOfSourceFiles;
                    mProgress.setProgress((int) valuePercent);
                    Log.d("ENCRYPT1234567", "Message");
                    if(!mEncrypt.threadIsAlive()) {
                        Log.d("STOP", "THREADISNOTALIVE1");
                        timerEncrypt.cancel();
                        mProgress.setProgress(0);
                        mSizeOfSourceFiles = 0;
                    }
                }
                if(msg.what == 2) {
                    long valuePercent = (100 * mDecrypt.getCommonSizeOfFiles()) / mSizeOfSourceFiles;
                    mProgress.setProgress((int) valuePercent);
                    Log.d("DECRYPT1234567", "Message");
                    if(!mDecrypt.threadIsAlive()) {
                        Log.d("STOP", "THREADISNOTALIVE2");
                        timerEncrypt.cancel();
                        mProgress.setProgress(0);
                        mSizeOfSourceFiles = 0;
                    }
                }

            }
        };
    }
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.button_select_files:
                selectFiles();
                break;
            case R.id.button_encrypt:
                //First it is need to choose output directory
                Intent intent = new Intent(EncryptFilesActivity.this, DirectoryPicker.class);
                // optionally set options here
                startActivityForResult(intent, DirectoryPicker.PICK_DIRECTORY);
                mBooleanEncrypt = true;
                break;
            case R.id.button_decrypt:
                Intent intent2 = new Intent(this, DirectoryPicker.class);
                startActivityForResult(intent2, DirectoryPicker.PICK_DIRECTORY);
                mBooleanEncrypt = false;
                break;
        }
    }
    //METHOD WHICH WILL HANDLE DYNAMIC INSERTION
    public void addItems(Uri itemUri) {
        mUriList.add(itemUri);
        ContactInfo c = new ContactInfo();
        File fff = new File(itemUri.getPath());
        c.setNameEnterprise(fff.getName());
        mList.add(c);
        customAdapter.notifyDataSetChanged();
    }
    private void selectFiles() {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 && uri == null) {
                        ClipData clipData = data.getClipData();
                        int count = clipData.getItemCount();
                        for(int i = 0; i < count; i++) {
                            uri = clipData.getItemAt(i).getUri();
                            if (uri != null) {
                                addItems(uri);
                                ContentResolver cr = getContentResolver();
                                try {
                                    InputStream is = cr.openInputStream(uri);
                                    mSizeOfSourceFiles +=   is.available();
                                } catch (FileNotFoundException e) {

                                }
                                catch (IOException e) {

                                }
                                Log.d("SIZESIZESIZEMultiple", Long.toString(mSizeOfSourceFiles));
                            }
                        }
                    } else { //Single file
                        addItems(uri);
                        ContentResolver cr = getContentResolver();
                        try {
                            InputStream is = cr.openInputStream(uri);
                            mSizeOfSourceFiles +=   is.available();
                        } catch (FileNotFoundException e) {

                        }
                        catch (IOException e) {

                        }
                        Log.d("SIZESIZESIZESingle", Long.toString(mSizeOfSourceFiles));
                    }
                }
                break;
        }

        if(requestCode == DirectoryPicker.PICK_DIRECTORY && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            String pathDirectory = (String) extras.get(DirectoryPicker.CHOSEN_DIRECTORY);
            View tempView;
            EditText tempEditText;

            //Set the output paths
            for(int i = 0; i < mList.size(); i++) {
                tempView = tabListView.getChildAt(i);
                tempEditText  = (EditText) tempView.findViewById(R.id.editTextFiles);
                mListOutputPaths.add(pathDirectory + "/" + tempEditText.getText().toString());
            }
            if(mBooleanEncrypt) {
                mEncrypt = new Encrypt(getBaseContext(), editKey.getText().toString());
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mEncrypt.EncryptGroupsOfFiles(mUriList, mListOutputPaths);
                         timerEncrypt = new Timer();
                        timerEncrypt.schedule(new UpdateTimeTask(1), 0, 1000); //тикаем каждую секунду без задержки
                    }
                    });
                t.start();
            } else {
                 mDecrypt = new Decrypt(getBaseContext(), editKey.getText().toString());
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mDecrypt.DecryptGroupsOfFiles(mUriList, mListOutputPaths);
                        timerEncrypt = new Timer();
                        timerEncrypt.schedule(new UpdateTimeTask(2), 0, 1000); //тикаем каждую секунду без задержки
                    }
                });
                t.start();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    //задача для таймера
    class UpdateTimeTask extends TimerTask {
        private int type ;
        UpdateTimeTask(int n) {
            type = n;
        }
        public void run() {
            mHandler.sendEmptyMessage(type);
        }
    }
}
