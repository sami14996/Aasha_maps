package kaaf.ashamaps;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class LoginActivity extends AppCompatActivity {

    private EditText mLoginEmail,mLoginPass;
    private Button mLoginbtn;
    private TextView regtv;

    private android.support.v7.widget.Toolbar mtoolbar;

    private ProgressDialog mLoginprogressDialog;
    //Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginprogressDialog = new ProgressDialog(this);

        mLoginEmail = findViewById(R.id.login_email);
        mLoginPass = findViewById(R.id.login_password);
        mLoginbtn = findViewById(R.id.login_btn);
        regtv = findViewById(R.id.Reg_Textview);

        //firebase
        mAuth = FirebaseAuth.getInstance();


        regtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regtv_intent = new Intent(LoginActivity.this,RegisterActivity.class);
                regtv_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(regtv_intent);
            }
        });

        mLoginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login_email = mLoginEmail.getText().toString().trim();
                String login_password = mLoginPass.getText().toString().trim();

                if (!TextUtils.isEmpty(login_email) || !TextUtils.isEmpty(login_password)){

                    mLoginprogressDialog.setTitle("Loging in User");
                    mLoginprogressDialog.setMessage("Please Wait");
                    mLoginprogressDialog.setCanceledOnTouchOutside(false);
                    mLoginprogressDialog.show();

                    Login_user(login_email,login_password);

                }
            }
        });


    }

    private void Login_user(String login_email, String login_password) {


        mAuth.signInWithEmailAndPassword(login_email, login_password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            mLoginprogressDialog.dismiss();

                            Intent Login_Intent = new Intent(LoginActivity.this,MainActivity.class);
                            Login_Intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(Login_Intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            mLoginprogressDialog.hide();
                            Toast.makeText(LoginActivity.this, "Login error!",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
}
