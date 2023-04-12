package ng.com.restaurantorders;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView connect, login;
    private SharedPreferences loginPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor loginPrefsEditor = loginPreferences.edit();

        connect = findViewById(R.id.connect);
        login = findViewById(R.id.login);

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(loginPreferences.getString("email", "").equals("")){
                    Toast.makeText(MainActivity.this, "Kindly login and remember password to connect", Toast.LENGTH_LONG).show();
                }else{
                    startActivity(new Intent(getApplicationContext(), Dashboard.class));
                }

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
    }

    public void login(View view) {

    }
}