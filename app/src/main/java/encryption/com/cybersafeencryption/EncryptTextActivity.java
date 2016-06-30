package encryption.com.cybersafeencryption;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import encryption.com.AES.Decrypt;
import encryption.com.AES.Encrypt;

public class EncryptTextActivity extends AppCompatActivity  implements View.OnClickListener {
    private EditText mEditTextKey;
    private TextView mTextViewEncrypted;
    private TextView mTextViewDecrypted;
    private EditText mEditSourceText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypt_text);

        mEditTextKey  = (EditText) findViewById(R.id.editTextKey);
        mEditSourceText = (EditText) findViewById(R.id.editTextSourceText);

        Button btnEncryptText = (Button) findViewById(R.id.btnEncryptText);
        Button   btnDecryptText = (Button) findViewById(R.id.btnDecryptText);
        Button   btnSendSMS = (Button) findViewById(R.id.button_send_sms);
        Button   btnShare = (Button) findViewById(R.id.button_send_social_network);

        mTextViewEncrypted = (TextView) findViewById(R.id.editViewEncrypted);
        mTextViewDecrypted = (TextView) findViewById(R.id.editViewDecrypted);

        if(btnEncryptText != null && btnDecryptText != null) {
            btnEncryptText.setOnClickListener(this);
            btnDecryptText.setOnClickListener(this);
            btnSendSMS.setOnClickListener(this);
            btnShare.setOnClickListener(this);
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
                Decrypt decrypt = new Decrypt(this, mEditTextKey.getText().toString());
                decrypt.DecryptText(mTextViewEncrypted.getText().toString());
                mTextViewDecrypted.setText("");
                mTextViewDecrypted.setText(decrypt.get_text());
                break;
            case R.id.button_send_sms:
                Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address","3809");
                smsIntent.putExtra("sms_body", mTextViewEncrypted.getText());
                startActivity(smsIntent);
                break;
            case R.id.button_send_social_network:
                String TEXT = mTextViewEncrypted.getText().toString();
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_TEXT, TEXT);
                startActivity(Intent.createChooser(sendIntent, "Share the program:"));
                break;
        }
    }
}
