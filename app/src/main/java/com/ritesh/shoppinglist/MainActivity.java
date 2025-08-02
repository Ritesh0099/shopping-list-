package com.ritesh.shoppinglist;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> shoppingList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    int selectedItemPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = findViewById(R.id.list);

        // Sample items
        shoppingList.add(" Milk");
        shoppingList.add(" Bread");
        shoppingList.add(" Eggs");

        // Custom layout for items
        adapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.item_text, shoppingList);
        listView.setAdapter(adapter);

        // Register context menu for long-press
        registerForContextMenu(listView);

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            selectedItemPosition = position;
            return false;
        });
    }

    // Options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // Handle option menu selections
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add_item) {
            showAddItemDialog();
        } else if (id == R.id.clear_all) {
            showClearAllDialog();
        } else if (id == R.id.about) {
            showAboutDialog();
        }

        return true;
    }

    // Context menu for removing item
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.remove_item && selectedItemPosition >= 0) {
            String itemToRemove = shoppingList.get(selectedItemPosition);
            new AlertDialog.Builder(this)
                    .setTitle("Remove Item")
                    .setMessage("Are you sure you want to remove \"" + itemToRemove + "\"?")
                    .setPositiveButton("Remove", (dialog, which) -> {
                        shoppingList.remove(selectedItemPosition);
                        adapter.notifyDataSetChanged();
                        selectedItemPosition = -1;
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
        return true;
    }

    // Dialog to add new item
    private void showAddItemDialog() {
        EditText input = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("Add Item")
                .setMessage("Enter a new shopping item")
                .setView(input)
                .setPositiveButton("Add", (dialog, which) -> {
                    String item = input.getText().toString().trim();
                    if (!item.isEmpty()) {
                        shoppingList.add(item);
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Item can't be empty", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // Dialog to clear all items
    private void showClearAllDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Clear All")
                .setMessage("Are you sure you want to clear the entire shopping list?")
                .setPositiveButton("Clear", (dialog, which) -> {
                    shoppingList.clear();
                    adapter.notifyDataSetChanged();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // Dialog for about info
    private void showAboutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("About")
                .setMessage("🛒 Shopping List App v1.0\nDeveloped by HARSHADA GAIKAR")
                .setPositiveButton("OK", null)
                .show();
    }
}
