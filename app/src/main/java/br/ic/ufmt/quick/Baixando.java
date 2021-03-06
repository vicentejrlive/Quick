package br.ic.ufmt.quick;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.sql.Date;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import database.SharedFileCRUD;
import model.SharedFile;
import model.SharedFileAdapter;

public class Baixando extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baixando);
        //ProgressBar bar = (ProgressBar)findViewById(R.id.progressBar1);

        populateSharedFileList();

    }

    public void populateSharedFileList(){

        final SharedFileCRUD dbHelper = new SharedFileCRUD();

        final ArrayList<SharedFile> files = (ArrayList<SharedFile>) dbHelper.findAll();

        final SharedFileAdapter adapter = new SharedFileAdapter(this, files);

        final ListView listView = (ListView) findViewById(R.id.list_pocone);

        listView.setAdapter(adapter);

        final Timer[] t = {new Timer()};
        t[0].scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<SharedFile> files = (ArrayList<SharedFile>) dbHelper.findAll();
                        ArrayList<SharedFile> remover = new ArrayList<SharedFile>();
                        for (int i = 0; i < adapter.getCount(); i++){
                            if (!files.contains(adapter.getItem(i))){
                                remover.add(adapter.getItem(i));
                            }
                        }

                        for (SharedFile sf : remover){
                            adapter.remove(sf);
                        }

                        for (SharedFile sf : files){
                            if (adapter.getPosition(sf) < 0){
                                adapter.add(sf);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        },0, 2000);


    }
}
