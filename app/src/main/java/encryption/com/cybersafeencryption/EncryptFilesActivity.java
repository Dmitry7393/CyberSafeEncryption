package encryption.com.cybersafeencryption;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
        Button   btnEncrypt = (Button) findViewById(R.id.button_encrypt);
        Button   btnDecrypt = (Button) findViewById(R.id.button_decrypt);

        editKey = (EditText) findViewById(R.id.edit_key_encrypt_files);

        btnAdd.setOnClickListener(this);
        btnEncrypt.setOnClickListener(this);
        btnDecrypt.setOnClickListener(this);
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
                            }
                        }
                    } else { //Single file
                        addItems(uri);
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
                Log.d("INFORMATIONFROMLISTVI12EW", mListOutputPaths.get(mListOutputPaths.size()-1));
            }
            if(mBooleanEncrypt) {
                Encrypt encrypt = new Encrypt(getBaseContext(), editKey.getText().toString());
                encrypt.EncryptGroupsOfFiles(mUriList, mListOutputPaths);
            } else {
                Decrypt decrypt = new Decrypt(getBaseContext(), editKey.getText().toString());
                decrypt.DecryptGroupsOfFiles(mUriList, mListOutputPaths);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
