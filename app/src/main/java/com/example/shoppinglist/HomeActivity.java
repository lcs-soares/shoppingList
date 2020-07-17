package com.example.shoppinglist;

import android.app.AlertDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppinglist.Model.Shopping;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private FloatingActionButton fab_button;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private RecyclerView recyclerView;

    private TextView totalResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Shopping List");

        totalResult = findViewById(R.id.total);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String uId = mUser.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Shopping List").child(uId);

        mDatabase.keepSynced(true);

        //TOTAL
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double total = 0;
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Shopping shopping = snapshot.getValue(Shopping.class);

                    total += shopping.getPrice();

                    String stTotal = String.valueOf(total);

                    totalResult.setText(stTotal);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        recyclerView = findViewById(R.id.recyler_home);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        fab_button = findViewById(R.id.fab);

        fab_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialog();
            }
        });
    }

    private void CustomDialog(){
        AlertDialog.Builder myDialog = new AlertDialog.Builder(HomeActivity.this);

        LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);
        View myView = inflater.inflate(R.layout.input_data, null);

        final AlertDialog dialog = myDialog.create();

        dialog.setView(myView);

        final EditText name = myView.findViewById(R.id.edt_type);
        final EditText qtd = myView.findViewById(R.id.edt_ammount);
        final EditText price  = myView.findViewById(R.id.edt_price);
        Button btnSave = myView.findViewById(R.id.btn_save);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mName = name.getText().toString().trim();
                String mQtd = qtd.getText().toString().trim();
                String mPrice = price.getText().toString().trim();

                if (TextUtils.isEmpty(mName)){
                    name.setError("Campo obrigatório");
                    return;
                }
                if (TextUtils.isEmpty(mQtd)){
                    qtd.setError("Campo obrigatório");
                    return;
                }
                if (TextUtils.isEmpty(mPrice)){
                    price.setError("Campo obrigatório");
                    return;
                }

                int qtd = Integer.parseInt(mQtd);
                double price = Double.parseDouble(mPrice);

                String id = mDatabase.push().getKey();
                Shopping shopping = new Shopping(mName, qtd, price, id);
                mDatabase.child(id).setValue(shopping);

                Toast.makeText(getApplicationContext(), "Item adicionado", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Shopping, MyViewHolder> adapter = new FirebaseRecyclerAdapter<Shopping, MyViewHolder>(Shopping.class, R.layout.item_data, MyViewHolder.class, mDatabase) {
            @Override
            protected void populateViewHolder(MyViewHolder myViewHolder, Shopping shopping, int i) {
                myViewHolder.setName(shopping.getName());
                myViewHolder.setQtd(shopping.getQtd());
                myViewHolder.setPrice(shopping.getPrice());
            }
        };

        recyclerView.setAdapter(adapter);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        View myView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myView = itemView;
        }

        public void setName(String name){
            TextView mName = myView.findViewById(R.id.name);
            mName.setText(name);
        }

        public void setQtd(int qtd){
            TextView mQtd = myView.findViewById(R.id.qtd);
            String stam = String.valueOf(qtd);
            mQtd.setText("Qtd: " + stam);
        }

        public void setPrice(double price){
            TextView mPrice = myView.findViewById(R.id.price);
            String stam = String.valueOf(price);
            mPrice.setText("R$ " + stam);
        }
    }
}
