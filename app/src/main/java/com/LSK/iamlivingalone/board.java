package com.LSK.iamlivingalone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class board extends AppCompatActivity {
    private static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView content;
        TextView title;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content);
            title = itemView.findViewById(R.id.title);
        }
    }




    private RecyclerView recyclerView;
    private RecyclerView.Adapter<board.RecyclerViewHolder> recyclerAdapter;
    private List<recylcleboard> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board);

        TextView xtitle=(TextView)findViewById(R.id.boardtitleid);
        TextView xcontent=(TextView)findViewById(R.id.boardtextid);

        Intent intent = getIntent();
        final String str = intent.getStringExtra("uid");
        String getti=intent.getStringExtra("title");
        String getco=intent.getStringExtra("content");


        xtitle.setText("제목 : "+getti);
        xcontent.setText("내용 : "+getco);

        Toolbar toolbar = findViewById(R.id.toolbarboard);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = findViewById(R.id.recyclerViewboard);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        recyclerAdapter = new RecyclerView.Adapter<board.RecyclerViewHolder>() {
            @NonNull
            @Override
            public board.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(board.this);
                return new board.RecyclerViewHolder(inflater.inflate(R.layout.activity, viewGroup, false));
            }

            @Override
            public void onBindViewHolder(@NonNull board.RecyclerViewHolder recyclerViewHolder, int i) {
                recyclerViewHolder.title.setText(items.get(i).title);
                recyclerViewHolder.content.setText(items.get(i).content);
            }

            @Override
            public int getItemCount() {
                return items.size();
            }
        };
        recyclerView.setAdapter(recyclerAdapter);

        Button writeButton = (Button) findViewById(R.id.writeButtonboard);
        writeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(board.this, Editboard.class);
                intent.putExtra("uid",str);
                startActivity(intent);
            }
        });


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myReference = database.getReference();
        myReference.child("boardcom").child("userID").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
                recylcleboard rec = dataSnapshot.getValue(recylcleboard.class);
                if(str.equals(rec.uid)){
                    items.add(rec);
                    recyclerAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

    class DrawerItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapter, View view, int position,
                                long id) {
        }
    }



}