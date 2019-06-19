package com.LSK.iamlivingalone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class board extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board);

        TextView tx=(TextView)findViewById(R.id.boardtx);
        Intent intent =getIntent();
        String string= intent.getStringExtra("value").toString();

        tx.setText(string);

    }
}