package com.example.taskees;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Model.Data;

public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView mTitle;
    private FloatingActionButton fab;

    List<Data> list;

//    private DatabaseReference mDataBase;
    private FirebaseAuth mAuth;

    private RecyclerView recyclerView;

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabase;
    private DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbar);
        mTitle = toolbar.findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbar);
        mTitle.setText("Taskees");

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser mUser = mAuth.getCurrentUser();

        final String uid=mUser.getUid();

        mDatabase = db.getInstance().getReference().child("Tasks").child(uid);

        // RecyclerView
        recyclerView = findViewById(R.id.rview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true); // items are moved and deleted and added frequently
        recyclerView.setLayoutManager(layoutManager);


        fab = findViewById(R.id.fab_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder myDialog = new AlertDialog.Builder(HomeActivity.this);
                LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);
                View view = inflater.inflate(R.layout.custom_input_field, null);
                myDialog.setView(view);
                final AlertDialog dialog = myDialog.create();

                final EditText title = view.findViewById(R.id.edt_title);
                final EditText note = view.findViewById(R.id.edt_note);



                Button btnDone = view.findViewById(R.id.done_btn);

                btnDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mTitle = title.getText().toString().trim();
                        String mNote = note.getText().toString().trim();

                        // String id = "1";

                        if(TextUtils.isEmpty(mTitle)){
                            title.setError("Required Field");
                            return;
                        }

                        if(TextUtils.isEmpty(mNote)){
                            note.setError("Required Field");
                            return;
                        }

                        String id = mDatabase.push().getKey();
                        String date = DateFormat.getDateInstance().format(new Date());

                        // String currentTime = Calendar.getInstance().getTime().toString();

                        Data data=new Data(mTitle,mNote,date, id);

                        mDatabase.child(id).setValue(data);

                        Toast.makeText(getApplicationContext(), "Task Added", Toast.LENGTH_SHORT).show();

                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });


        myRef = db.getReference(uid);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<Data>();
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Data value = dataSnapshot1.getValue(Data.class);

                    Data d = new Data();
                    String title = value.getTitle();
                    String note = value.getNote();
                    String date = value.getDate();
                    d.setTitle(title);
                    d.setNote(note);
                    d.setDate(date);
                    Log.d("Value :" , title);
                    list.add(d);
                }


                RecyclerAdapter recyclerAdapter = new RecyclerAdapter((list),HomeActivity.this);
                recyclerView.setAdapter(recyclerAdapter);
                recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Hello", "Failed to read value.", databaseError.toException());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

}
