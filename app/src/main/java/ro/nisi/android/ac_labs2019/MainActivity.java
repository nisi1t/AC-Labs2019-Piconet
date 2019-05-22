package ro.nisi.android.ac_labs2019;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ro.nisi.android.ac_labs2019.service.EasyPayService;
import ro.nisi.android.ac_labs2019.service.PayResponse;
import ro.nisi.android.ac_labs2019.service.StatusResponse;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "AC-LABS";

    // external services
    private FirebaseAuth mAuth;
    private EasyPayService service;

    // interface
    private EditText phoneEdit;
    private EditText prefixEdit;
    private EditText plateNrEdit;
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        initUi();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

        if (currentUser != null){
            this.service = initRetrofit("http://aclabs2019.nisi.ro/");

            refreshStatus();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater findMenuItems = getMenuInflater();
        findMenuItems.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                mAuth.signOut();
                updateUI(null);

                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {

            Log.d(TAG, user.getEmail() + " (" + user.getUid() + ")");
        } else {
            Intent i = new Intent(this, LoginActivity.class);

            startActivity(i);
        }
    }

    private void initUi() {
        phoneEdit = findViewById(R.id.phoneEdit);
        prefixEdit = findViewById(R.id.prefixEdit);
        plateNrEdit = findViewById(R.id.plateNrEdit);
        status = findViewById(R.id.status);

        status.setText("Inactive");

        findViewById(R.id.payBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payClick();
            }
        });
    }

    private EasyPayService initRetrofit(String serviceUrl){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(serviceUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(EasyPayService.class);
    }

    private void payClick(){
        if (service != null) {
            // create service call
            Call<PayResponse> payCall = service.pay(
                    phoneEdit.getText().toString(),
                    prefixEdit.getText().toString(),
                    plateNrEdit.getText().toString()
            );

            // enqueue to async list
            payCall.enqueue(new Callback<PayResponse>() {
                @Override
                public void onResponse(Call<PayResponse> call, Response<PayResponse> response) {
                    if (!response.isSuccessful()) {
                        Log.d(TAG, "No Success");
                    }

                    PayResponse resp = response.body();

                    if (resp != null) {
                        Log.d(TAG, "MSG: " + resp.message_id + ", " + resp.success);
                        Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_LONG).show();
                    } else {
                        Log.d(TAG, "Empty response");
                    }
                }

                @Override
                public void onFailure(Call<PayResponse> call, Throwable t) {
                    Log.d(TAG, "Fail: " + t.getMessage());
                    Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_LONG).show();
                }
            });

        } else {
            Log.d(TAG, "No service found");
        }
    }

    private void refreshStatus(){
        if (service != null) {
            // create service call
            Call<StatusResponse> payCall = service.status();

            // enqueue to async list
            payCall.enqueue(new Callback<StatusResponse>() {
                @Override
                public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                    if (!response.isSuccessful()) {
                        Log.d(TAG, "No Success");
                    }

                    StatusResponse resp = response.body();

                    if (resp != null) {
                        Log.d(TAG, "Service: " + resp.name + ", " + resp.version+ ", " + resp.status);

                        String statusTxt = String.format("%s (%s) %s", resp.name, resp.version, resp.status?"Active":"Inactive");
                        status.setText(statusTxt);
                    } else {
                        Log.d(TAG, "Empty response");
                    }
                }

                @Override
                public void onFailure(Call<StatusResponse> call, Throwable t) {
                    Log.d(TAG, "Fail: " + t.getMessage());
                    Toast.makeText(MainActivity.this, "Status retrieved failed", Toast.LENGTH_LONG).show();
                }
            });

        } else {
            Log.d(TAG, "No service found");
            status.setText("Inactive");
        }
    }
}
