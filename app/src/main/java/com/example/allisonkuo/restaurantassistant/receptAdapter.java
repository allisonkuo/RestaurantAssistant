package com.example.allisonkuo.restaurantassistant;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
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
            prices = reader.getJSONObject("prices");
            foods = reader.getJSONObject("foods");
            drinks = reader.getJSONObject("drinks");
            total = reader.getDouble("total");

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
        TextView listItemText = (TextView) view.findViewById(R.id.list_item_string);
        listItemText.setText(list.get(position));

        // get price and quantity of each item

        String item_name = list.get(position);
        if(item_name != "TOTAL") {
            String price = "0.00";
            int quantity = 1;
            try {
                price = prices.getString(item_name);
                if (foods.has(item_name))
                    quantity = foods.getInt(item_name);
                else
                    quantity = drinks.getInt(item_name);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            final TextView price_text = (TextView) view.findViewById(R.id.price);
            price_text.setText("$" + price + " x " + quantity);

        }
        else
        {
            final TextView price_text = (TextView) view.findViewById(R.id.price);
            // TODO
            price_text.setTypeface(null, Typeface.BOLD);
            price_text.setText(String.valueOf(total));
        }
        return view;
    }

}
