package com.example.allisonkuo.restaurantassistant;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by allisonkuo on 3/1/17.
 */

public class receptAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private String json_reply;
    private Context context;
    private JSONObject reader = null;
    private JSONObject prices = null;
    private JSONObject foods = null;
    private JSONObject drinks = null;
    private double total = 0;

    public receptAdapter(ArrayList<String> list, String json, Context context) {
        this.list = list;
        this.json_reply = json;
        this.context = context;

        try {
            reader = new JSONObject(this.json_reply);
            total = reader.getDouble("total");
            prices = reader.optJSONObject("prices");
            foods = reader.optJSONObject("foods");
            drinks = reader.optJSONObject("drinks");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
        // just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.recept_item, null);
        }

        // handle TextView and display string from your list
        String item_name = list.get(position);
        RelativeLayout.LayoutParams llp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        if(item_name == "TOTAL")
        {
            TextView listItemText = (TextView) view.findViewById(R.id.list_item_string);
            listItemText.setTypeface(null, Typeface.BOLD);
            listItemText.setText(item_name);

            TextView price_text = (TextView) view.findViewById(R.id.price);
            price_text.setTypeface(null, Typeface.BOLD);
            price_text.setText(String.format("$%.2f", total));

        }

        else if(item_name == "FOODS" || item_name == "DRINKS" || item_name == "NO ORDERS")
        {
            TextView listItemText = (TextView) view.findViewById(R.id.list_item_string);
            listItemText.setTypeface(null, Typeface.BOLD);
            listItemText.setText(item_name);

            TextView price_text = (TextView) view.findViewById(R.id.price);
            price_text.setText("");
        }

        else
        {
            Typeface font = Typeface.createFromAsset(context.getAssets(), "Lato-Regular.ttf");
            Typeface fontlight = Typeface.createFromAsset(context.getAssets(), "Lato-Light.ttf");
            TextView listItemText = (TextView) view.findViewById(R.id.list_item_string);
            listItemText.setTypeface(fontlight);
            listItemText.setText("   " + item_name);
            String price = "0.00";
            int quantity = 1;
            try {
                price = prices.getString(item_name);
                if (foods != null && foods.has(item_name))
                    quantity = foods.getInt(item_name);
                else
                    quantity = drinks.getInt(item_name);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            final TextView price_text = (TextView) view.findViewById(R.id.price);

            price_text.setTypeface(font);
            price_text.setText(String.format("$%s x %s", price, quantity));

        }
        return view;
    }

}
