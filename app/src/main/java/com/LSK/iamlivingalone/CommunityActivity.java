package com.LSK.iamlivingalone;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommunityActivity extends AppCompatActivity {

    public static final String TAG = CommunityActivity.class.getSimpleName();

    private static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView title;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.recycletitle);
        }
    }

    ListView listview;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter<RecyclerViewHolder> recyclerAdapter;
    private List<recylcleboard> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("맛집 정보");
        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        recyclerAdapter = new RecyclerView.Adapter<RecyclerViewHolder>() {
            @NonNull
            @Override
            public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(CommunityActivity.this);
                return new RecyclerViewHolder(inflater.inflate(R.layout.recyclecommunity, viewGroup, false));
            }

            @Override
            public void onBindViewHolder(@NonNull final RecyclerViewHolder recyclerViewHolder, final int i) {
                recyclerViewHolder.title.setText(items.get(i).title);
//                recyclerViewHolder.content.setText(items.get(i).content);
                recyclerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CommunityActivity.this, board.class);

                        String title = items.get(i).title;
                        String content = items.get(i).content;
                        String uid = items.get(i).uid;

                        intent.putExtra("uid", uid);
                        intent.putExtra("title", title);
                        intent.putExtra("content", content);
//                        FirebaseDatabase database = FirebaseDatabase.getInstance();
//                        DatabaseReference myRef = database.getReference();
//                        final String key = myRef.child("echo").child("userID").push().getKey();
//                        myRef.child("community").child("userID").child(key).updateChildren();
//
//
//                        Map<String, Object> map = new HashMap<>();
//                        map.put("title", title);
//                        map.put("content",content);


                        startActivity(intent);
                    }
                });
            }

            @Override
            public int getItemCount() {
                return items.size();
            }
        };
        recyclerView.setAdapter(recyclerAdapter);

        Button writeButton = (Button) findViewById(R.id.writeButton);
        writeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommunityActivity.this, EditorActivity.class);


                startActivity(intent);
            }
        });

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myReference = database.getReference();
        myReference.child("community").child("userID").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                recylcleboard rec = dataSnapshot.getValue(recylcleboard.class);
                rec.uid = s;
                items.add(rec);
                recyclerAdapter.notifyDataSetChanged();
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
