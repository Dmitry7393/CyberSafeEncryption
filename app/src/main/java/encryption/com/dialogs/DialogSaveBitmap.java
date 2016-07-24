package encryption.com.dialogs;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;

import encryption.com.cybersafeencryption.R;

public class DialogSaveBitmap extends DialogFragment implements OnClickListener {

    EditText editTextKey;
    EditText editTextFileName;
    public interface saveBitmapInterface {
        void saveWithoutEncryption(String fileName);
        void encryptAndSave(String key, String fileName);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("Title!");
        View v = inflater.inflate(R.layout.dialog_save_bitmap, null);
        v.findViewById(R.id.btn_save_without_encryption).setOnClickListener(this);
        v.findViewById(R.id.button_encrypt_save).setOnClickListener(this);
        v.findViewById(R.id.button_cancel).setOnClickListener(this);
        editTextKey = (EditText) v.findViewById(R.id.editText_key);
        editTextFileName =  (EditText) v.findViewById(R.id.editText_name);
        return v;
    }

    public void onClick(View v) {
        final saveBitmapInterface activity = (saveBitmapInterface) getActivity();
        switch(v.getId()) {
            case R.id.btn_save_without_encryption:
                Log.d("BUTTON", "Button save without encryption");
                activity.saveWithoutEncryption(editTextFileName.getText().toString());
                break;
            case R.id.button_encrypt_save:
                Log.d("BUTTON", "Button encryptn and save");
                activity.encryptAndSave(editTextKey.getText().toString(), editTextFileName.getText().toString());
                break;
            case R.id.button_cancel:
                Log.d("BUTTON", "CANCEL");
                break;
        }
        dismiss();
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }
}