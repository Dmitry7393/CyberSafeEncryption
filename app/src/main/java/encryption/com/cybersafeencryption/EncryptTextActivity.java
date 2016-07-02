package encryption.com.cybersafeencryption;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import encryption.com.AES.Decrypt;
import encryption.com.AES.Encrypt;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import encryption.com.Database.*;
import encryption.com.dialogs.DialogFragmentListNotes;

public class EncryptTextActivity extends AppCompatActivity  implements View.OnClickListener, DialogFragmentListNotes.EditNameDialogListener {
    private EditText mEditTextKey;
    private EditText mTextViewOutputText;
    private EditText mEditSourceText;
    final String LOG_TAG = "myLohs167";
    protected List<Note> mListNotes;
    DBHelper dbHelper;
    DialogFragment dlg1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypt_text);

        mEditTextKey  = (EditText) findViewById(R.id.editTextKey);
        mEditSourceText = (EditText) findViewById(R.id.editTextSourceText);

        Button btnEncryptText = (Button) findViewById(R.id.btnEncryptText);
        Button   btnDecryptText = (Button) findViewById(R.id.btnDecryptText);
        Button   btnShare = (Button) findViewById(R.id.button_send_social_network);
        Button   btnSaveNote = (Button) findViewById(R.id.button_save_note);
        Button   btnOpenAllNotes = (Button) findViewById(R.id.button_watch_all_notes);

        mTextViewOutputText = (EditText) findViewById(R.id.editViewOutputText);

        if(btnEncryptText != null)
            btnEncryptText.setOnClickListener(this);
        if(btnDecryptText != null)
            btnDecryptText.setOnClickListener(this);
        if(btnShare != null)
            btnShare.setOnClickListener(this);
        if(btnSaveNote != null)
            btnSaveNote.setOnClickListener(this);
        if(btnOpenAllNotes != null)
            btnOpenAllNotes.setOnClickListener(this);

        mListNotes = new ArrayList<>();
        dbHelper = new DBHelper(this);
        dlg1 = new Dialog1();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnEncryptText:
                Encrypt encrText = new Encrypt(this, mEditTextKey.getText().toString());
                encrText.EncryptText(mEditSourceText.getText().toString());
                String cipher_text = encrText.getCipherText();
                mTextViewOutputText.setText(cipher_text);
                break;
            case R.id.btnDecryptText:
                Decrypt decrypt = new Decrypt(this, mEditTextKey.getText().toString());
                try {
                    decrypt.DecryptText(mEditSourceText.getText().toString());
                    mTextViewOutputText.setText(decrypt.get_text());
                } catch(IllegalArgumentException e) {
                    Toast.makeText(this, "Incorrect ciphertext", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.button_send_social_network:
                String TEXT = mTextViewOutputText.getText().toString();
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_TEXT, TEXT);
                startActivity(Intent.createChooser(sendIntent, "Share the program:"));
                break;
            case R.id.button_save_note:
                Bundle args = new Bundle();
                args.putString("text", mTextViewOutputText.getText().toString());
                dlg1.setArguments(args);
                dlg1.show(getFragmentManager(), "dlg1");
                break;
            case R.id.button_watch_all_notes:
                updateListNotes();
                FragmentManager manager = getFragmentManager();
                DialogFragmentListNotes dialog = new DialogFragmentListNotes();
                dialog.show(manager, "dialog");
                break;

        }



                 /*   Log.d(LOG_TAG, "--- Clear mytable: ---");
                    // удаляем все записи
                    int clearCount = db.delete("table_notes", null, null);
                    Log.d(LOG_TAG, "deleted rows count = " + clearCount);*/


            // закрываем подключение к БД
         //   dbHelper.close();
        }
    private void updateListNotes() {
        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
          Log.d(LOG_TAG, "READ ROWS FROM DATA56BASE12");
                    // делаем запрос всех данных из таблицы mytable, получаем Cursor
                    Cursor c = db.query("table_notes", null, null, null, null, null, null);
                    mListNotes.clear();
                    // ставим позицию курсора на первую строку выборки
                    // если в выборке нет строк, вернется false
                    if (c.moveToFirst()) {
                        // определяем номера столбцов по имени в выборке
                        int titleNoteColIndex = c.getColumnIndex("title_note");
                        int noteColIndex = c.getColumnIndex("note");
                        int dateColIndex = c.getColumnIndex("date");
                        do {
                            Note tempNote = new Note();
                            tempNote.setTitleNote(c.getString(titleNoteColIndex));
                            tempNote.setNote(c.getString(noteColIndex));
                            tempNote.setDate(c.getString(dateColIndex));

                            mListNotes.add(tempNote);
                        } while (c.moveToNext());
                    } else
                        Log.d(LOG_TAG, "0 rows");
                    c.close();
    }
    protected void saveNote(String titleNote, String note) {
        // создаем объект для данных
        ContentValues cv = new ContentValues();
        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "--- Inser23t in2 table_notes2323: ---");
        String date =  java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        cv.put("title_note", titleNote);
        cv.put("note", note);
        cv.put("date", date);
        // вставляем запись и получаем ее ID
        db.insert("table_notes", null, cv);
        db.close();
    }
    public List<Note> getlistOfNotes() {
        return mListNotes;
    }
     public  void onFinishEditDialog(String inputText) {
       mEditSourceText.setText(inputText);
    }

    public void deleteNotes(List<Note> listNotes) {
        for(int i = 0; i < listNotes.size(); i++) {
            if(listNotes.get(i).getCheckBox()) {
                deleteNoteFromDB(listNotes.get(i).getDate());
            }
        }
    }

    private void deleteNoteFromDB(String date) {
        Log.d("DE233223L", "3333333333333");
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("table_notes", "date" + " = ?", new String[] { date });
        db.close();
    }
}