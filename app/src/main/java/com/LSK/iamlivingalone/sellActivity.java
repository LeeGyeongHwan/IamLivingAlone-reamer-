package com.LSK.iamlivingalone;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

public class sellActivity extends AppCompatActivity {
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
    private RecyclerView.Adapter<sellActivity.RecyclerViewHolder> recyclerAdapter;
    private List<sellboard> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        TextView xtitle=(TextView)findViewById(R.id.selltitleid);
        TextView xcontent=(TextView)findViewById(R.id.selltextid);
        ImageView imageView=(ImageView)findViewById(R.id.sellImage);

        Intent intent = getIntent();
        final String str = intent.getStringExtra("key");
        String getti=intent.getStringExtra("title");
        String getco=intent.getStringExtra("content");
        String base=intent.getStringExtra("img");

        byte[] a = Base64.decode(base,Base64.DEFAULT);
        Bitmap bit = BitmapFactory.decodeByteArray(a,0,a.length);
        imageView.setImageBitmap(bit);


        xtitle.setText("제목 : "+getti);
        xcontent.setText("내용 : "+getco);

        Toolbar toolbar = findViewById(R.id.toolbarsell);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("중고 팝니다~");
        }

        recyclerView = findViewById(R.id.recyclerViewsell);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        recyclerAdapter = new RecyclerView.Adapter<sellActivity.RecyclerViewHolder>() {
            @NonNull
            @Override
            public sellActivity.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(sellActivity.this);
                return new sellActivity.RecyclerViewHolder(inflater.inflate(R.layout.activity, viewGroup, false));
            }

            @Override
            public void onBindViewHolder(@NonNull sellActivity.RecyclerViewHolder recyclerViewHolder, int i) {
                recyclerViewHolder.title.setText(items.get(i).title);
                recyclerViewHolder.content.setText(items.get(i).content);
            }

            @Override
            public int getItemCount() {
                return items.size();
            }
        };
        recyclerView.setAdapter(recyclerAdapter);

        Button writeButton = (Button) findViewById(R.id.writeButtonsell);
        writeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(sellActivity.this, EditSell.class);
                intent.putExtra("uid",str);
                startActivity(intent);
            }
        });


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myReference = database.getReference();
        myReference.child("sell").child("userID").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                sellboard sellb = dataSnapshot.getValue(sellboard.class);
                if(str.equals(sellb.uid)){
                    items.add(sellb);
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
