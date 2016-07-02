package encryption.com.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import encryption.com.cybersafeencryption.EncryptTextActivity;
import encryption.com.cybersafeencryption.ListNotesViewAdapter;
import encryption.com.cybersafeencryption.Note;
import encryption.com.cybersafeencryption.R;


public class DialogFragmentListNotes extends DialogFragment implements
        OnItemClickListener, View.OnClickListener {

    ListNotesViewAdapter customAdapter;

    public interface EditNameDialogListener {
        void onFinishEditDialog(String inputText);
        void deleteNotes(List<Note> listNotes);
    }

    List<Note> mListNotes;
    ListView mylist;
    TextView txtViewNote;
    Button btnDelete;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment, null, false);

         btnDelete = (Button) view.findViewById(R.id.button_delete_note);
        btnDelete.setOnClickListener(this);

        mylist = (ListView) view.findViewById(R.id.listDFragment);


        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return view;
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.button_delete_note:
                Log.d("click", "delete button2");
                List<Note> dataUpdated = customAdapter.getUpdatedListNote();
                EditNameDialogListener activity = (EditNameDialogListener) getActivity();
                activity.deleteNotes(dataUpdated);
                break;
        }
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListNotes = new ArrayList<>();
        mListNotes = ((EncryptTextActivity)getActivity()).getlistOfNotes();

        if(mListNotes.size() == 0) {
            btnDelete.setVisibility(View.INVISIBLE);
        }
        customAdapter = new ListNotesViewAdapter(getActivity().getApplicationContext(), R.layout.item_notes, mListNotes);
        mylist.setAdapter(customAdapter);
        mylist.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        view.setSelected(true);
        txtViewNote = (TextView) view.findViewById(R.id.txt_view_note);
        EditNameDialogListener activity = (EditNameDialogListener) getActivity();
        activity.onFinishEditDialog(txtViewNote.getText().toString());

        dismiss();
        Toast.makeText(getActivity(), mListNotes.get(position).getTitleNote(), Toast.LENGTH_SHORT)
                .show();
    }

}
