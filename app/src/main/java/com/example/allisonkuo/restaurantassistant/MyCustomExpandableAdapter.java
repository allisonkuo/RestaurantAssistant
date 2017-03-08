package com.example.allisonkuo.restaurantassistant;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;

/**
* Created by allisonkuo on 3/7/17.
*/

public class MyCustomExpandableAdapter extends BaseExpandableListAdapter {

    private Context context;
    private String[] listDataHeader; // header titles
    private HashMap<String, List<String>> listDataChild; // child data in format of header title, child title
    private HashMap<String, Integer> order_count;


    public MyCustomExpandableAdapter(Context context, String[] listDataHeader,
                                 HashMap<String, List<String>> listChildData, HashMap<String, Integer> order_count) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
        this.order_count = order_count;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listDataChild.get(this.listDataHeader[groupPosition])
                .get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public HashMap getOrderCount() {
        return order_count;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        View view = convertView;
        final String childText = (String) getChild(groupPosition, childPosition);

        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.menu_item, null);
        }

        // get fonts
        Typeface font = Typeface.createFromAsset(context.getAssets(), "Lato-Regular.ttf");
        Typeface fontbold= Typeface.createFromAsset(context.getAssets(), "Lato-Bold.ttf");

        // set food category names
        TextView txtListChild = (TextView) view.findViewById(R.id.list_item_string);
        txtListChild.setTypeface(fontbold);
        txtListChild.setText(childText);

        // TODO: add description and price
        TextView price = (TextView) view.findViewById(R.id.price);
        TextView description = (TextView) view.findViewById(R.id.description);
        price.setTypeface(font);
        price.setText("$12.00");
        //description.setTypeface(fontmed);
        description.setText(childText);

        // set color of each section
        switch (listDataHeader[groupPosition]) {
            case "APPETIZERS":
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.appetizerColor));
                break;
            case "BURGERS":
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.burgersColor));
                break;
            case "SANDWICHES":
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.sandwichesColor));
                txtListChild.setTextColor(ContextCompat.getColor(context, R.color.morelight));
                price.setTextColor(ContextCompat.getColor(context, R.color.morelight)); // set text color lighter
                description.setTextColor(ContextCompat.getColor(context, R.color.morelight));
                break;
            case "DESSERTS":
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.dessertsColor));
                txtListChild.setTextColor(ContextCompat.getColor(context, R.color.morelight));
                price.setTextColor(ContextCompat.getColor(context, R.color.morelight));
                description.setTextColor(ContextCompat.getColor(context, R.color.morelight));
                break;
            case "SOFT DRINKS":
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.softdrinksColor));
                break;
            case "COCKTAILS":
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.cocktailColor));
                break;
            case "BEER":
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.beerColor));
                break;
            case "WINE":
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.wineColor));
                break;
            case "WHISKEY":
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.whiskeyColor));
                break;
        }

        switch(childText) {
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

        // set saved counts
        itemCount.setText(Integer.toString(order_count.get(childText)));

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // decrement item count
                int count = Integer.parseInt(itemCount.getText().toString());
                if (count > 0) {
                    count -= 1;
                    String newCount = Integer.toString(count);

                    itemCount.setText(newCount);
                    order_count.put(childText, count);
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
                order_count.put(childText, count);

                notifyDataSetChanged();
            }
        });


        return view;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChild.get(this.listDataHeader[groupPosition])
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataHeader[groupPosition];
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.length;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        // set header names
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        Typeface font = Typeface.createFromAsset(context.getAssets(), "Lato-Light.ttf");
        lblListHeader.setTypeface(font);
        lblListHeader.setText(headerTitle);

        // set header images and colors
        ImageView imageView = (ImageView) convertView.findViewById(R.id.header_image);

        switch (listDataHeader[groupPosition]) {
            case "APPETIZERS":
                convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.appetizerColor));
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.appetizers));
                break;
            case "BURGERS":
                convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.burgersColor));
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.burgers));
                break;
            case "SANDWICHES":
                convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.sandwichesColor));
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.sandwiches));
                break;
            case "DESSERTS":
                convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.dessertsColor));
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.desserts));
                break;
            case "SOFT DRINKS":
                convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.softdrinksColor));
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.softdrinks));
                break;
            case "COCKTAILS":
                convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.cocktailColor));
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.cocktails));
                break;
            case "BEER":
                convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.beerColor));
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.beers));
                break;
            case "WINE":
                convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.wineColor));
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.wines));
                break;
            case "WHISKEY":
                convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.whiskeyColor));
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.whiskeys));
                break;
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
