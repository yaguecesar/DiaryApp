package com.example.r4z0r78.diaryapp;

import android.app.Activity;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.r4z0r78.diaryapp.database.Note;
import com.example.r4z0r78.diaryapp.database.NotesDBHelper;

public class NewNoteActivity extends Activity implements View.OnClickListener {

    private EditText editTitle;
    private EditText editText;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        editText = (EditText) findViewById(R.id.editText);
        editTitle = (EditText) findViewById(R.id.editTitle);
        btnSave = (Button) findViewById(R.id.btnSave);

        btnSave.setOnClickListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v== btnSave){
            saveNote();
        }
    }

    private void saveNote(){
        String title = editTitle.getText().toString();
        String text = editText.getText().toString();

        if (title.isEmpty() || text.isEmpty()){
            Toast.makeText(this, getString(R.string.note_lenght_error), Toast.LENGTH_SHORT).show();
        }else{

            Note n = new Note();
            n.setText(text);
            n.setTitle(title);

            // Create a new i
            NotesDBHelper dbHelper = new NotesDBHelper(this);
            try{
                dbHelper.saveNote(n);

                editTitle.setText("");
                editText.setText("");

                Toast.makeText(this, "Note saved succesfully"  + "and amount is" + dbHelper.getNotes().size(), Toast.LENGTH_SHORT).show();

            }catch(SQLiteException ex){
                ex.printStackTrace();

                Log.i(getString(R.string.database_error), ex.getMessage());
                Toast.makeText(this, getString(R.string.database_error), Toast.LENGTH_SHORT).show();

            }

        }

    }



}
