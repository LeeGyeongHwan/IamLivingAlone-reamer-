package com.LSK.iamlivingalone;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditSell extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commentedit);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Intent intent=getIntent();
        final String comuid=intent.getStringExtra("uid");

        final TextInputEditText titleEditText = findViewById(R.id.titleEditTextcomment);
        final TextInputEditText contentEditText = findViewById(R.id.contentEditTextcomment);

        Button writeButton = findViewById(R.id.writeButtoncomment);
        writeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference();
                sellboard sellb = new sellboard();
                sellb.title = "ID : " + titleEditText.getText().toString();
                sellb.content = contentEditText.getText().toString();
                sellb.uid = comuid;
                reference.child("sell").child("userID").push().setValue(sellb);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
