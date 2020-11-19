package com.example.to_do;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<String> items;
    Button add;
    EditText additemtext;
    RecyclerView rvitemlist;
    ItemsAdapter itemsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        add = findViewById(R.id.add);
        additemtext = findViewById(R.id.additemtext);
        rvitemlist = findViewById(R.id.rvitemlist);


        loadItems();

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {

            @Override
            public void onItemLongClicked(int position) {
                //delete item from model
                items.remove(position);
                //modify adapter of position
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item removed!", Toast.LENGTH_SHORT);
                saveItems();
            }
        };
        itemsAdapter = new ItemsAdapter(items, onLongClickListener);
        rvitemlist.setAdapter(itemsAdapter);
        rvitemlist.setLayoutManager(new LinearLayoutManager(this));

        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String newitem = additemtext.getText().toString();
                //add item to model
                items.add(newitem);
                //notify adapter of change
                itemsAdapter.notifyItemInserted(items.size() - 1);
                additemtext.setText("");
                Toast.makeText(getApplicationContext(), "Item added!", Toast.LENGTH_SHORT).show();
            saveItems();
            }
        });
    }
        private File getDataFile(){
            return new File(getFilesDir(), "data.txt");
        }
        //load items by reading every line of data.txt
    private void loadItems(){
        try {
            items=new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            items=new ArrayList<>();
        }
    }
    private void saveItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);

        }
    }

}