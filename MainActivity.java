package kaaf.ashamaps;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;

import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    //ANDROID

  private TextView placetext;

    //FIREBASE
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mRef;
    //Maps PlacePicker
    int PLACE_PICKER_REQUEST = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        placetext = findViewById(R.id.Place_text);

        //Getting the value from different intent
        //



    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            updateUI(currentUser);
        }
    }

    private void updateUI(FirebaseUser currentUser) {

        Intent Lint = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(Lint);
        Toast.makeText(MainActivity.this, "You need to login first", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu, menu);


        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.mainLogOut) {

            FirebaseAuth.getInstance().signOut();
            Intent startIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(startIntent);
            finish();
        }

        if (item.getItemId() == R.id.mapIcon) {


            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

            Intent Placeintent;
            try{

                Placeintent = builder.build(MainActivity.this);
                startActivityForResult(Placeintent,PLACE_PICKER_REQUEST);

            }
            catch (GooglePlayServicesRepairableException e){
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
            

            

        }
        return true;

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);

                String address = String.format("Place: %s", place.getAddress());

                placetext.setText(address);

                String name = getIntent().getStringExtra("user_name");

                firebaseDatabase = FirebaseDatabase.getInstance();

                mRef = firebaseDatabase.getReference().child("Users_location");
                mRef.child("name").setValue(name);

                mRef.child("location").setValue(placetext.getText().toString().trim());



            }
        }
    }


    }


