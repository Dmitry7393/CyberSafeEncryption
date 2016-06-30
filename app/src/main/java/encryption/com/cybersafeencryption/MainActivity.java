package encryption.com.cybersafeencryption;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button   btnOpenEncryptTextActivity = (Button) findViewById(R.id.btn_open_encrypt_text_activity);
        Button   btnOpenEncryptActivity = (Button) findViewById(R.id.btn_open_encrypt_files_activity);


        if(btnOpenEncryptTextActivity != null && btnOpenEncryptActivity != null) {

            btnOpenEncryptTextActivity.setOnClickListener(this);
            btnOpenEncryptActivity.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_open_encrypt_text_activity:
                Intent intent = new Intent(MainActivity.this, EncryptTextActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_open_encrypt_files_activity:
                Intent intent2 = new Intent(MainActivity.this, EncryptFilesActivity.class);
                startActivity(intent2);
                break;
        }
    }

}
