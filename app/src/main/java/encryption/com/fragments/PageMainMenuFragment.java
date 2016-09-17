package encryption.com.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import encryption.com.cybersafeencryption.DrawingActivity;
import encryption.com.cybersafeencryption.EncryptFilesActivity;
import encryption.com.cybersafeencryption.EncryptTextActivity;
import encryption.com.cybersafeencryption.R;

public class PageMainMenuFragment  extends Fragment implements View.OnClickListener {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView  = inflater.inflate(R.layout.fragment_main_menu, container, false);


        Button btnOpenEncryptTextActivity = (Button)  rootView.findViewById(R.id.btn_open_encrypt_text_activity);
        Button btnOpenEncryptActivity = (Button)  rootView.findViewById(R.id.btn_open_encrypt_files_activity);
        Button btnOpenDrawingActivity = (Button)  rootView.findViewById(R.id.btn_open_drawing_activity);

        if (btnOpenEncryptTextActivity != null && btnOpenEncryptActivity != null && btnOpenDrawingActivity != null) {
            btnOpenEncryptTextActivity.setOnClickListener(this);
            btnOpenEncryptActivity.setOnClickListener(this);
            btnOpenDrawingActivity.setOnClickListener(this);
        }
        return rootView;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_open_encrypt_text_activity:
                Intent intent = new Intent(getContext(), EncryptTextActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_open_encrypt_files_activity:
                Intent intent2 = new Intent(getContext(), EncryptFilesActivity.class);
                startActivity(intent2);
                break;
            case R.id.btn_open_drawing_activity:
                Intent intent3 = new Intent(getContext(), DrawingActivity.class);
                startActivity(intent3);
                break;
        }
    }
}
