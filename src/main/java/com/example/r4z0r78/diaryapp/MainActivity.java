package com.example.r4z0r78.diaryapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends Activity implements View.OnClickListener {

    private Button btnNewNote;
    private Button btnViewNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        btnNewNote = (Button) findViewById(R.id.btnNewNote);
        btnNewNote.setOnClickListener(this);
        btnViewNotes = (Button) findViewById(R.id.btnViewNotes);
        btnViewNotes.setOnClickListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){

            case R.id.action_settings:
                Toast.makeText(this, "Settings pressed", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menuOpenNotes:
                openActivity(ViewNoteActivity.class);
                break;
            case R.id.menuNewNote:
                openActivity(NewNoteActivity.class);
                break;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnNewNote:
                openActivity(NewNoteActivity.class);
                break;
            case R.id.btnViewNotes:
                openActivity(ViewNoteActivity.class);
                break;
        }
    }

    private void openActivity(Class activity){

        Intent i = new Intent(this, activity);
        startActivity(i);
    }
}
