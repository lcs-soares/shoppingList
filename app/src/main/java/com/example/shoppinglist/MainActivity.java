package com.example.shoppinglist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText email;
    private EditText pass;
    private Button btnLogin;

    private FirebaseAuth mAuth;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        //if (mAuth.getCurrentUser() != null){
        //    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        //}

        mDialog = new ProgressDialog(this);


        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mEmail = email.getText().toString().trim();
                String mPass = pass.getText().toString().trim();

                if (TextUtils.isEmpty(mEmail)){
                    email.setError("Campo obrigatório");
                    return;
                }
                if (TextUtils.isEmpty(mPass)){
                    pass.setError("Campo obrigatório");
                    return;
                }

                mDialog.setMessage("Processando ...");
                mDialog.show();

                mAuth.signInWithEmailAndPassword(mEmail, mPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            Toast.makeText(getApplicationContext(), "Sucesso!", Toast.LENGTH_SHORT);
                            mDialog.dismiss();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Erro", Toast.LENGTH_SHORT);
                            mDialog.dismiss();
                        }
                    }
                });
            }
        });
    }
}
