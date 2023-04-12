package ng.com.restaurantorders;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Dashboard extends AppCompatActivity {

    Dialog myDialog;
    ListView listView;
    TextView accepted, completed;

    ArrayList<Integer> Array_id = new ArrayList<>();
    ArrayList<String> Array_name = new ArrayList<>();
    ArrayList<String> Array_order = new ArrayList<>();
    ArrayList<String> Array_message = new ArrayList<>();
    ArrayList<String> Array_option = new ArrayList<>();
    ArrayList<String> Array_orderoption = new ArrayList<>();
    ArrayList<String> Array_payment = new ArrayList<>();
    ArrayList<String> Array_time = new ArrayList<>();
    ArrayList<String> Array_delivery_post_code = new ArrayList<>();
    ArrayList<String> Array_totalprice = new ArrayList<String>();

    private static final DecimalFormat df = new DecimalFormat("0.00");
    public static final String GET_RESTAURANT_ORDERS = "https://justorders.online/api/orders?filters[restaurant][id][$eq]=4&populate=*";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        accepted = findViewById(R.id.accepted);
        completed = findViewById(R.id.completed);
        listView = findViewById(R.id.listview);
        //show loading dialog
        myDialog = new Dialog(Dashboard.this);
        myDialog.setContentView(R.layout.custom_popup_loading);
        TextView loading = myDialog.findViewById(R.id.loadingtext);
        loading.setText("Loading Orders, please wait...");
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, GET_RESTAURANT_ORDERS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        System.out.println("Upload Response = "+response);

                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray data_jsonArray = jsonObject.getJSONArray("data");
                            int array_count = data_jsonArray.length();
                            accepted.setText(String.valueOf(array_count));
//                            System.out.println("Count = "+array_count);
                            for(int j=0; j<array_count; j++){
                                JSONObject section = data_jsonArray.getJSONObject(j);
                                int id = section.getInt("id");
                                String name = section.getString("name");
                                String order = section.getString("order");
                                String message = section.getString("message");
                                String option = section.getString("option");
                                String orderoption = section.getString("orderoption");
                                String payment = section.getString("payment");
                                String time = section.getString("time");
                                String deliverypostcode = section.getString("deliverypostcode");
                                double totalprice = section.getDouble("totalprice");

                                Array_id.add(id);
                                Array_name.add(name);
                                Array_order.add(order);
                                Array_message.add(message);
                                Array_option.add(option);
                                Array_orderoption.add(orderoption);
                                Array_payment.add(payment);
                                Array_time.add(time);
                                if(deliverypostcode.equals("") || deliverypostcode == null){
                                    Array_delivery_post_code.add("");
                                }else {
                                    Array_delivery_post_code.add(deliverypostcode);
                                }
                                Array_totalprice.add(df.format(totalprice));
                            }

                            list_adapter adapter = new list_adapter(Dashboard.this, Array_name, Array_time, Array_option, Array_payment, Array_totalprice, Array_delivery_post_code);
                            listView.setAdapter(adapter);


                            myDialog.dismiss();
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            myDialog.dismiss();
                            System.out.println("JSON Error = "+e.toString());
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
}