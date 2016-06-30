package encryption.com.cybersafeencryption;


import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class Dialog1 extends DialogFragment implements OnClickListener {

    final String LOG_TAG = "myLogs";
    EditText titleNote;
    EditText note;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String strEncryptedText = getArguments().getString("text");
        getDialog().setTitle("Saving note");
        View v = inflater.inflate(R.layout.dialog1, null);
         titleNote = (EditText) v.findViewById(R.id.editTitleNote) ;
        note= (EditText) v.findViewById(R.id.editNote) ;

        titleNote.setText("Note");
        note.setText(strEncryptedText);
        v.findViewById(R.id.button_cancel).setOnClickListener(this);
        v.findViewById(R.id.button_save).setOnClickListener(this);
        return v;
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.button_cancel:
                Log.d(LOG_TAG, "cancel");
                break;
            case R.id.button_save:
                Log.d(LOG_TAG, "save");
                ((EncryptTextActivity)getActivity()).saveNote(titleNote.getText().toString(), note.getText().toString());
                break;
        }
        dismiss();
    }


   /* public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d(LOG_TAG, "Dialog 1: onDismiss");
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d(LOG_TAG, "Dialog 1: onCancel");
    }*/
}