package kaaf.ashamaps;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText mRegName, mEmail, mPassword;
    private Button mCreateAccbtnBtn;
    private TextView logintv;

    private Toolbar mtoolbar;

    private ProgressDialog mRegprogressDialog;

    //firebasae
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //FIREBASE

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        /*mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();*/


        mRegName = findViewById(R.id.reg_name);
        mEmail = findViewById(R.id.reg_email);
        mPassword = findViewById(R.id.reg_password);
        mCreateAccbtnBtn = findViewById(R.id.reg_btn);
        logintv = findViewById(R.id.Login_textview);



        logintv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logintv_intent = new Intent(RegisterActivity.this, LoginActivity.class);
                logintv_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logintv_intent);
            }
        });


        mRegprogressDialog = new ProgressDialog(this);


    }

    public void onRegisterButtonClicked(View view) {

        final String name, email, password;
        name = mRegName.getText().toString().trim();
        email = mEmail.getText().toString().trim();
        password = mPassword.getText().toString().trim();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

            mRegprogressDialog.setTitle("Registering User");
            mRegprogressDialog.setMessage("Creating account,Please Wait");
            mRegprogressDialog.setCanceledOnTouchOutside(false);
            mRegprogressDialog.show();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                        String uId;
                        assert mCurrentUser != null;
                        uId = mCurrentUser.getUid();


                        mRef = firebaseDatabase.getReference().child("Users").child(uId);
                        mRef.child("name").setValue(name);
                       final String UserNameKey = mRef.getKey();

                        mRegprogressDialog.dismiss();

                        String name_value = mRegName.getText().toString();
                        Intent mainIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                        mainIntent.putExtra("user_name",UserNameKey);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(mainIntent);

                    } else {
                        // If sign in fails, display a message to the user.
                        mRegprogressDialog.hide();

                        Toast.makeText(RegisterActivity.this, "Sign in error ",
                                Toast.LENGTH_SHORT).show();

                    }

                }
            });

        }
    }
    }
