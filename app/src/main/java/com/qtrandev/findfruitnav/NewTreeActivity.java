package com.qtrandev.findfruitnav;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.firebase.client.Firebase;


public class NewTreeActivity extends ActionBarActivity {

    private double lat = 0;
    private double lon = 0;
    private String type = "Mango";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tree);
        setTitle("New Tree");

        Button saveButton = (Button) findViewById(R.id.button5);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveClicked();
            }
        });

        Button cancelButton = (Button) findViewById(R.id.button6);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                 cancelClicked();
            }
        });

        if (getIntent().getExtras() != null) {
            lat = getIntent().getExtras().getDouble("Lat");
            lon = getIntent().getExtras().getDouble("Lon");
        }
    }

    private void saveClicked() {
        Firebase ref = new Firebase("https://findfruit.firebaseio.com/");
        Firebase trees = ref.child("tree");
        Tree newTree = new Tree(type, lat, lon);
        trees.push().setValue(newTree.getTreeToWrite());
        finish();
    }

    private void cancelClicked() {
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_tree, menu);
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
}
