package ng.com.restaurantorders;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    EditText email, password;
    String myEmail, myPassword;
    CheckBox remember;
    Button login, force_login;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    Dialog myDialog;

    public static final String LOGIN = "https://justorders.online/api/auth/local";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        remember = findViewById(R.id.remember);
        login = findViewById(R.id.login);
        force_login = findViewById(R.id.force_login);

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            email.setText(loginPreferences.getString("email", ""));
            password.setText(loginPreferences.getString("password", ""));
            remember.setChecked(true);
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myEmail = email.getText().toString().trim();
                myPassword = password.getText().toString().trim();

                myDialog = new Dialog(Login.this);
                myDialog.setContentView(R.layout.custom_popup_loading);
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.setCanceledOnTouchOutside(false);
                myDialog.show();



                StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                System.out.println("Upload Response = "+response);

                                try{
                                    JSONObject jsonObject = new JSONObject(response);
                                    String jwt = jsonObject.getString("jwt");
                                    if (!jwt.equals("")){
                                        myDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "Successfully Logged in", Toast.LENGTH_LONG).show();

                                        if (remember.isChecked()) {
                                            loginPrefsEditor.putBoolean("saveLogin", true);
                                            loginPrefsEditor.putString("email", myEmail);
                                            loginPrefsEditor.putString("password", myPassword);
                                            loginPrefsEditor.commit();
                                        } else {
                                            loginPrefsEditor.clear();
                                            loginPrefsEditor.commit();
                                        }

                                        // goto dashboard
                                        Intent i = new Intent(getApplicationContext(), Dashboard.class);
                                        startActivity(i);
                                    }

                                }
                                catch (JSONException e){
                                    e.printStackTrace();
                                    myDialog.dismiss();
                                    System.out.println("JSON Error = "+e.toString());
                                    Toast.makeText(getApplicationContext(), "Login failed. Please try again", Toast.LENGTH_LONG).show();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {

                                if(volleyError == null){
                                    System.out.println("Volley Error = "+volleyError.toString());
//
                                    return;
                                }

                            }
                        }){
                    @Override
                    protected Map<String, String> getParams(){
                        Map<String, String> params = new HashMap<>();
                        params.put("identifier", myEmail);
                        params.put("password", myPassword);
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(retryPolicy);
                requestQueue.add(stringRequest);
                requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
                    @Override
                    public void onRequestFinished(Request<Object> request) {
                        requestQueue.getCache().clear();
                    }
                });

            }
        });

    }
}