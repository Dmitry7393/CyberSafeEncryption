package encryption.com.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import encryption.com.cybersafeencryption.EncryptTextActivity;
import encryption.com.cybersafeencryption.ListNotesViewAdapter;
import encryption.com.cybersafeencryption.Note;
import encryption.com.cybersafeencryption.R;


public class DialogFragmentListNotes extends DialogFragment implements
        OnItemClickListener {

    ListNotesViewAdapter customAdapter;

    public interface EditNameDialogListener {
        void onFinishEditDialog(String inputText);
        void deleteNotes(List<Note> listNotes);
        void deleteNoteFromDB(String date);
    }

    List<Note> mListNotes;
    ListView mylist;
    TextView txtViewNote;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment, null, false);

        mylist = (ListView) view.findViewById(R.id.listDFragment);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return view;
    }
   private void createAlertDialog(final int i) {
       final EditNameDialogListener activity = (EditNameDialogListener) getActivity();
       AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
       builder.setCancelable(false)
               .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       activity.deleteNoteFromDB(mListNotes.get(i).getDate());
                       dismiss();
                   }
               })
               .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       dialog.cancel();
                   }
               })
               .setNeutralButton("Decrypt", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       activity.onFinishEditDialog(txtViewNote.getText().toString());
                       dismiss();
                   }
               });
       AlertDialog alert = builder.create();
       alert.show();
   }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListNotes = new ArrayList<>();
        mListNotes = ((EncryptTextActivity)getActivity()).getlistOfNotes();

        customAdapter = new ListNotesViewAdapter(getActivity().getApplicationContext(), R.layout.item_notes, mListNotes);
        mylist.setAdapter(customAdapter);
        mylist.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        txtViewNote = (TextView) view.findViewById(R.id.txt_view_note);
        createAlertDialog(position);
    }
}
