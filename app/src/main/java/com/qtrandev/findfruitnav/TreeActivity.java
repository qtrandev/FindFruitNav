package com.qtrandev.findfruitnav;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;


public class TreeActivity extends ActionBarActivity {

    private TextView titleTextView;
    private String treeId;
    private String treeType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree);
        setTitle("Tree");

        titleTextView = (TextView) findViewById(R.id.textView8);

        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cancelClicked();
            }
        });

        Button deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                deleteClicked();
            }
        });

        if (getIntent().getExtras() != null) {
            treeId = getIntent().getExtras().getString("Id");
            treeType = getIntent().getExtras().getString("Type");
            titleTextView.setText(treeType+" Tree");
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tree, menu);
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

    private void cancelClicked() {
        finish();
    }

    private void deleteClicked() {
        new AlertDialog.Builder(this)
                .setMessage("Remove the "+treeType+" tree?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteTree();
                        Toast.makeText(TreeActivity.this, treeType+" tree removed", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteTree() {
        Firebase ref = new Firebase("https://findfruit.firebaseio.com/");
        Firebase trees = ref.child("tree");
        trees.child(treeId).removeValue(new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    System.out.println(firebaseError.getMessage());
                }
            }
        });
    }
}
