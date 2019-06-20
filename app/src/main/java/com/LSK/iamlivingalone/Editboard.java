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

public class Editboard extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boardedit);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Intent intent=getIntent();
        final String comuid=intent.getStringExtra("uid");

        final TextInputEditText titleEditText = findViewById(R.id.titleEditTextboarde);
        final TextInputEditText contentEditText = findViewById(R.id.contentEditTextboarde);

        Button writeButton = findViewById(R.id.writeButtonboarde);
        writeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference();
                recylcleboard recy = new recylcleboard();
                recy.title = "ID : " + titleEditText.getText().toString();
                recy.content = contentEditText.getText().toString();
                recy.uid = comuid;
                reference.child("boardcom").child("userID").push().setValue(recy);
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
