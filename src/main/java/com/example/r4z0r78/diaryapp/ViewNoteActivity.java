package com.example.r4z0r78.diaryapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.r4z0r78.diaryapp.database.Note;
import com.example.r4z0r78.diaryapp.database.NotesDBHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;


public class ViewNoteActivity extends Activity implements View.OnClickListener {

    private SearchView searchView;
    private ListView listView;
    private Button btnLoadThread;
    private Button btnLoadAsync;

    private ArrayAdapter<Note> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);

        searchView = (SearchView) findViewById(R.id.svSearch);
        listView = (ListView) findViewById(R.id.lvNotes);

        btnLoadAsync = (Button) findViewById(R.id.btnLoadAsync);
        btnLoadAsync.setOnClickListener(this);
        btnLoadThread = (Button) findViewById(R.id.btnLoadThread);
        btnLoadThread.setOnClickListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Note n = (Note) parent.getItemAtPosition(position);


                if (n != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ViewNoteActivity.this);

                    builder.setTitle(n.getTitle());

                    builder.setMessage(n.getText());

                    builder.setNeutralButton("OK", null);

                    builder.show();
                }
            }
        });

        // FIlter the adapter when the searchView is modified


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //return false;

                adapter.getFilter().filter(newText);
                return true;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_note, menu);
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
        if (v == btnLoadThread){
            LoadThread lt = new LoadThread();

            lt.start();
        }else if(v == btnLoadAsync){
            LoadAsyncTask lat = new LoadAsyncTask();
            lat.execute();
        }
    }


    private class LoadAsyncTask extends AsyncTask<Void, Void, List<Note>>{

        private ProgressDialog pd;

        @Override
        protected List<Note> doInBackground(Void... params) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            NotesDBHelper dbHelper = new NotesDBHelper(ViewNoteActivity.this);

            return dbHelper.getNotes();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(ViewNoteActivity.this);
            pd.setTitle("Async task fetching data...");
            pd.setMessage("Loading...");

            pd.show();
        }

        @Override
        protected void onPostExecute(List<Note> notes) {
            super.onPostExecute(notes);

            if (notes != null){
                adapter = new ArrayAdapter<Note>(ViewNoteActivity.this, android.R.layout.simple_list_item_1, notes);

                listView.setAdapter(adapter);

                Toast.makeText(ViewNoteActivity.this, "Load Thread finished", Toast.LENGTH_SHORT).show();
            }

            pd.dismiss();
        }
    }

    private class LoadThread extends Thread{
        @Override
        public void run() {

            super.run();


            try {

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                NotesDBHelper dbHelper = new NotesDBHelper(ViewNoteActivity.this);

                adapter = new ArrayAdapter<Note>(ViewNoteActivity.this, android.R.layout.simple_list_item_1, dbHelper.getNotes());

                android.os.Handler handler = new android.os.Handler(getMainLooper()); // MainLooper = main UI thread

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listView.setAdapter(adapter);
                        Toast.makeText(ViewNoteActivity.this, "Load Thread finished", Toast.LENGTH_SHORT).show();
                    }
                });

                // set the adapter



            } catch (SQLiteException ex) {
                ex.printStackTrace();

                Log.i(getString(R.string.database_error), ex.getMessage());
                Toast.makeText(ViewNoteActivity.this, getString(R.string.database_error), Toast.LENGTH_SHORT).show();

            }
        }
    }

    // TODO display title and content of note when lvNotes is clicked
}
