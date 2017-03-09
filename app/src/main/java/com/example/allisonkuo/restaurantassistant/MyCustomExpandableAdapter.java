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
        //price.setText("$12.00");
        //description.setTypeface(font);
        //description.setText(childText);

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
                description.setText("fried mozarella sticks with marinara sauce");
                price.setText("$7.50");
                break;
            case "Mini Corn Dogs":
                description.setText("served with honey mustard and hot mustard");
                price.setText("$7.50");
                break;
            case "Chips and Salsa":
                description.setText("served with pico de gallo, spicy salsa verde, guacamole");
                price.setText("$5.95");
                break;
            case "Jalapeno Poppers":
                description.setText("fried bacon-wrapped jalapenos stuffed with cream cheese");
                price.setText("$6.95");
                break;
            case "Fried Mushrooms":
                description.setText("stuffed with garlic cream cheese");
                price.setText("$8.95");
                break;
            case "Toothpicks":
                description.setText("fried jalapenos, onion strips");
                price.setText("$6.95");
                break;
            case "Hot Wings":
                description.setText("cajun or lemon pepper");
                price.setText("$12.95");
                break;
            case "Cheese Fries":
                description.setText("homemade fries, house cheese sauce, grilled onions");
                price.setText("$8.95");
                break;
            case "Fried Pickle Spears":
                description.setText("battered and fried pickles");
                price.setText("$6.95");
                break;
            case "Classic Cheeseburger":
                description.setText("cheddar, bacon, lettuce, tomato, grilled onions, house sauce");
                price.setText("$8.95");
                break;
            case "Sunny Side Burger":
                description.setText("parmesan, sunny side up egg, bacon, arugula, garlic aioli");
                price.setText("$11.95");
                break;
            case "Short Rib Sliders":
                description.setText("briased beef short rib, gruyere");
                price.setText("$11.95");
                break;
            case "Grilled Chicken":
                description.setText("grilled chicken breast, arugula, tomato, pickles, house sauce");
                price.setText("$9.95");
                break;
            case "Buffalo Chicken":
                description.setText("fried chicken breast, lettuce, tomato, buffalo wing sauce");
                price.setText("$10.95");
                break;
            case "Fried Chicken":
                description.setText("fried chicken breast, roasted red peppers, lettuce, house sauce");
                price.setText("$10.95");
                break;
            case "BLT":
                description.setText("bacon, lettuce, tomato, chipotle aioli");
                price.setText("$9.95");
                break;
            case "Club":
                description.setText("sliced turkey breast, glazed ham, lettuce, tomato, pepper jack");
                price.setText("$9.95");
                break;
            case "Philly Cheese Steak":
                description.setText("beef ribeye, grilled onions, roasted red peppers, monterey jack");
                price.setText("$10.95");
                break;
            case "Turkey Melt":
                description.setText("sliced turkey breast, tomato, monterey jack, chipotle aioli");
                price.setText("$8.95");
                break;
            case "Chocolate Cake":
                description.setText("");
                price.setText("$4.95");
                break;
            case "Cheese Cake":
                description.setText("");
                price.setText("$3.95");
                break;
            case "Ice Cream":
                description.setText("");
                price.setText("$2.95");
                break;
            case "Banana Cream Pie":
                description.setText("");
                price.setText("$3.95");
                break;
            case "Tiramisu":
                description.setText("");
                price.setText("$3.95");
                break;
            case "Coke":
                description.setText("");
                price.setText("$2.50");
                break;
            case "Diet Coke":
                description.setText("");
                price.setText("$2.50");
                break;
            case "Sprite":
                description.setText("");
                price.setText("$2.50");
                break;
            case "Root Beer":
                description.setText("");
                price.setText("$2.50");
                break;
            case "Iced Tea":
                description.setText("");
                price.setText("$2.50");
                break;
            case "Appletini":
                description.setText("apple schnapps, Smirnoff apple vodka, splash of Sierra Mist");
                price.setText("$12.00");
                break;
            case "Hawaiian Sunset":
                description.setText("vodka, hibiscus, lime, raspberry");
                price.setText("$12.00");
                break;
            case "Dark Side of the Moon":
                description.setText("tequila, lime, blackberry, basil");
                price.setText("$13.00");
                break;
            case "Baja Breeze":
                description.setText("tequila, passion fruit, lime, jalapeno mango IPA");
                price.setText("$13.00");
                break;
            case "French Negroni":
                description.setText("gin, sweet vermouth, green chartreuse, campari");
                price.setText("$14.00");
                break;
            case "Ruffino Prosecco Veneto":
                description.setText("");
                price.setText("$18.00");
                break;
            case "Rosatello Rose, Italy":
                description.setText("");
                price.setText("$28.00");
                break;
            case "Ruffino Lumina Pinot Grigio, Italy":
                description.setText("");
                price.setText("$28.00");
                break;
            case "Nobilo Suavignon Blanc":
                description.setText("");
                price.setText("$32.00");
                break;
            case "Franciscan Chardonnay":
                description.setText("");
                price.setText("$39.00");
                break;
            case "Hogue Merlot":
                description.setText("");
                price.setText("$28.00");
                break;
            case "Dreaming Tree Pinot Noir":
                description.setText("");
                price.setText("$32.00");
                break;
            case "Jack Daniels":
                description.setText("");
                price.setText("$10.00");
                break;
            case "Angel's Envy Rye":
                description.setText("");
                price.setText("$18.00");
                break;
            case "Lock Stock & Smoking Barrel":
                description.setText("");
                price.setText("$20.00");
                break;
            case "Jameson":
                description.setText("");
                price.setText("$10.00");
                break;
            case "Pappy Van Winkle":
                description.setText("");
                price.setText("$38.00");
                break;
            case "Johnny Walker":
                description.setText("");
                price.setText("$12.00");


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
