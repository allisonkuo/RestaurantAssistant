package com.example.allisonkuo.restaurantassistant;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by allisonkuo on 3/1/17.
 */

public class MyCustomAdapter extends BaseAdapter implements ListAdapter {
    private String[] list;
    private Context context;
    private String[] order_count = new String[10];

    public MyCustomAdapter(int resourceId, Context context, String[] orderCount) {
        this.list = context.getResources().getStringArray(resourceId);
        this.context = context;
        this.order_count = orderCount;
    }

    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public Object getItem(int pos) {
        return list[pos];
    }

    @Override
    public long getItemId(int pos) {
        return 0;
        // just return 0 if your list items do not have an Id variable.
    }

    /*
    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }*/

    public String[] getOrderCount() {
        return order_count;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.menu_item, null);
        }

        // handle TextView and display string from your list
        TextView listItemText = (TextView) view.findViewById(R.id.list_item_string);
        listItemText.setText(list[position]);

        // handle price and description
        TextView price = (TextView) view.findViewById(R.id.price);
        TextView description = (TextView) view.findViewById(R.id.description);
        price.setText("$12.00");
        description.setText(list[position]);
        switch(list[position]) {
            case "Cheese Sticks":
                break;
            case "Mini Corn Dogs":
                break;
            case "Chips and Salsa":
                break;
            case "Jalapeno Poppers":
                break;
            case "Fried Mushrooms":
                break;
            case "Toothpicks":
                break;
            case "Hot Wings":
                break;
            case "Cheese Fries":
                break;
            case "Fried Pickle Spears":
                break;
            case "Classic Cheeseburger":
                break;
            case "Sunny Side Burger":
                break;
            case "Short Rib Sliders":
                break;
            case "Grilled Chicken":
                break;
            case "Buffalo Chicken":
                break;
            case "Fried Chicken":
                break;
            case "BLT":
                break;
            case "Club":
                break;
            case "Philly Cheese Steak":
                break;
            case "Turkey Melt":
                break;
            case "Chocolate Cake":
                break;
            case "Cheese Cake":
                break;
            case "Ice Cream":
                break;
            case "Banana Cream Pie":
                break;
            case "Tiramisu":
                break;

        }

        // handle buttons and add onClickListeners
        Button minusButton = (Button) view.findViewById(R.id.minus);
        Button plusButton = (Button) view.findViewById(R.id.plus);
        final TextView itemCount = (TextView) view.findViewById(R.id.count);

        // set saved count
        itemCount.setText(order_count[position]);

        // add order count
        order_count[position] = itemCount.getText().toString();

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // decrement item count
                int count = Integer.parseInt(itemCount.getText().toString());
                if (count > 0) { // can't order less than 0
                    count -= 1;
                    String newCount = Integer.toString(count);

                    itemCount.setText(newCount);
                    order_count[position] = newCount;
                }

                notifyDataSetChanged();
            }
        });
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // increment item count
                int count = Integer.parseInt(itemCount.getText().toString());
                count += 1;
                String newCount = Integer.toString(count);

                itemCount.setText(newCount);
                order_count[position] = newCount;

                notifyDataSetChanged();
            }
        });

        return view;
    }

}
