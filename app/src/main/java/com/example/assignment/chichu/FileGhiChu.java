package com.example.assignment.chichu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment.MainActivity;
import com.example.assignment.R;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.HashSet;

public class FileGhiChu extends AppCompatActivity {
    FloatingActionMenu addfolde;
    static ArrayList<String> notes = new ArrayList<String>();
    static ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_ghi_chu);
        getSupportActionBar().setTitle("GHI CHÚ THU CHI");

    addfolde = findViewById(R.id.addnote);
    addfolde.setOnMenuButtonClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), GhiChuEditer.class);
            startActivity(intent);
        }
    });


        ListView listView = (ListView)findViewById(R.id.listView);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.tanay.thunderbird.deathnote", Context.MODE_PRIVATE);
        HashSet<String> set = (HashSet<String>)sharedPreferences.getStringSet("notes", null);



        if(set == null || set.size() == 0)
        {
            Toast.makeText(FileGhiChu.this, "Chưa có ghi chú nào cả", Toast.LENGTH_SHORT).show();
        }

        else
        {
            notes = new ArrayList<>(set);         // to bring all the already stored data in the set to the notes ArrayList
        }


        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, notes);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(getApplicationContext(), GhiChuEditer.class);
                intent.putExtra("noteID", position);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id)
            {
                new AlertDialog.Builder(FileGhiChu.this)                   // we can't use getApplicationContext() here as we want the activity to be the context, not the application
                        .setTitle("Thông báo")
                        .setMessage("Có chắc bạn muốn xóa nó không?")
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)                        // to remove the selected note once "Yes" is pressed
                            {
                                notes.remove(position);
                                arrayAdapter.notifyDataSetChanged();

                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.tanay.thunderbird.deathnote", Context.MODE_PRIVATE);
                                HashSet<String> set = new HashSet<>(FileGhiChu.notes);
                                sharedPreferences.edit().putStringSet("notes", set).apply();
                            }
                        })

                        .setNegativeButton("Hủy", null)
                        .show();

                return true;               // this was initially false but we change it to true as if false, the method assumes that we want to do a short click after the long click as well
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ghichunote, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.chiase:
                new AlertDialog.Builder(FileGhiChu.this)                   // we can't use getApplicationContext() here as we want the activity to be the context, not the application
                        .setTitle("Thông báo")
                        .setMessage("Bạn muốn thoát ghi chú? Chúng tôi đã lưu dữ liệu của bạn!")
                        .setPositiveButton("Thoát", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)                        // to remove the selected note once "Yes" is pressed
                            {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }
                        })

                        .setNegativeButton("Hủy", null)
                        .show();
                return true;
            case R.id.thoat:

                androidx.appcompat.app.AlertDialog.Builder aBuildera = new androidx.appcompat.app.AlertDialog.Builder(FileGhiChu.this);
                aBuildera.setMessage("Xác nhận thoát?");
                aBuildera.setPositiveButton("Thoát", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        aBuildera.show();
                        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                        homeIntent.addCategory( Intent.CATEGORY_HOME );
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }
                });
                //nút không
                aBuildera.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                aBuildera.show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(FileGhiChu.this)                   // we can't use getApplicationContext() here as we want the activity to be the context, not the application
                .setTitle("Thông báo")
                .setMessage("Bạn muốn thoát ghi chú? Chúng tôi đã lưu dữ liệu của bạn!")
                .setPositiveButton("Thoát", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)                        // to remove the selected note once "Yes" is pressed
                    {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                })

                .setNegativeButton("Hủy", null)
                .show();
    }
}