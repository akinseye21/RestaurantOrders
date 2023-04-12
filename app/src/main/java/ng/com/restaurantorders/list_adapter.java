package ng.com.restaurantorders;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class list_adapter extends BaseAdapter {

    private Context context;
    ArrayList<String> arr_name;
    ArrayList<String> arr_time;
    ArrayList<String> arr_delivery;
    ArrayList<String> arr_payment;
    ArrayList<String> arr_amount;
    ArrayList<String> arr_delivery_post_code;

    Dialog myDialog;
    public static final String GET_RESTAURANT_ORDERS = "https://justorders.online/api/orders?filters[restaurant][id][$eq]=4&populate=*";

    public list_adapter(Context context, ArrayList<String> name, ArrayList<String> time, ArrayList<String> delivery, ArrayList<String> payment, ArrayList<String> amount, ArrayList<String> delivery_post_code){
        //Getting all the values
        this.context = context;
        this.arr_name = name;
        this.arr_time = time;
        this.arr_delivery = delivery;
        this.arr_payment = payment;
        this.arr_amount = amount;
        this.arr_delivery_post_code = delivery_post_code;
    }

    @Override
    public int getCount() {
        return arr_name.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        LayoutInflater inflaInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflaInflater.inflate(R.layout.list_orders, parent, false);
        }

        TextView name = convertView.findViewById(R.id.name);
        TextView time = convertView.findViewById(R.id.time);
        TextView delivery = convertView.findViewById(R.id.delivery);
        TextView paymenttype = convertView.findViewById(R.id.paymenttype);
        TextView amounts = convertView.findViewById(R.id.amounts);
        LinearLayout accept_lin = convertView.findViewById(R.id.accept);
        TextView accepttext = convertView.findViewById(R.id.accepttext);

        name.setText(arr_name.get(i));
        time.setText(arr_time.get(i)+" MIN");
        delivery.setText(arr_delivery_post_code.get(i));
        paymenttype.setText(arr_payment.get(i));
        amounts.setText("£"+arr_amount.get(i));

        accept_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog = new Dialog(context);
                myDialog.setContentView(R.layout.custom_popup_accept);
                TextView plus = myDialog.findViewById(R.id.plus);
                TextView time = myDialog.findViewById(R.id.time);
                TextView minus = myDialog.findViewById(R.id.minus);
                LinearLayout accept = myDialog.findViewById(R.id.accept);
                time.setText(arr_time.get(i));
                plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int count = Integer.parseInt(time.getText().toString());
                        count = count + 10;
                        time.setText(String.valueOf(count));
                    }
                });
                minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int count = Integer.parseInt(time.getText().toString());
                        count = count - 10;
                        time.setText(String.valueOf(count));
                    }
                });
                accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //update the DB using the PUT request

                        accepttext.setText("Delivering");
                        accept_lin.setBackgroundResource(R.drawable.delivery_bg);
                        myDialog.dismiss();
                    }
                });
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.setCanceledOnTouchOutside(true);
                myDialog.show();
            }
        });

        return convertView;
    }
}
