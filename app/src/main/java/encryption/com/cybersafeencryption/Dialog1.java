package encryption.com.cybersafeencryption;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Dialog1 extends DialogFragment implements OnClickListener {

    private EditText titleNote;
    private  EditText note;
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        String  mStrEncryptedText = getArguments().getString("text");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View inflatedView = inflater.inflate(R.layout.dialog1, null);

        titleNote = (EditText) inflatedView.findViewById(R.id.editTitleNote);
        note = (EditText) inflatedView.findViewById(R.id.editNote);
        Button btnCancel = (Button) inflatedView.findViewById(R.id.button_cancel);
        Button btnSave = (Button) inflatedView.findViewById(R.id.button_save);
        btnCancel.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        titleNote.setText("Note");
        note.setText(mStrEncryptedText);

        dialog.setView(inflatedView);
        dialog.setTitle("Note saving");
        setCancelable(true);

        return dialog.create();
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.button_cancel:
                break;
            case R.id.button_save:
                ((EncryptTextActivity)getActivity()).saveNote(titleNote.getText().toString(), note.getText().toString());
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