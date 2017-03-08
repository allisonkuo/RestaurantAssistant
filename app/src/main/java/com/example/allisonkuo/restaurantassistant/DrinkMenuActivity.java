package com.example.allisonkuo.restaurantassistant;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import helper.serverCall;

public class DrinkMenuActivity extends Activity {
    MyCustomExpandableAdapter listAdapter;
    ExpandableListView expListView;
    String[] listDataHeader;
    HashMap<String, List<String>> listDataChild;
    HashMap<String, Integer> softdrinks_order_count;
    HashMap<String, Integer> cocktails_order_count;
    HashMap<String, Integer> beer_order_count;
    HashMap<String, Integer> wine_order_count;
    HashMap<String, Integer> whiskey_order_count;
    HashMap<String, Integer> order_count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_menu);

        listDataHeader = getResources().getStringArray(R.array.drink_categories);
        listDataChild = new HashMap<String, List<String>>();
        softdrinks_order_count = new HashMap<String, Integer>();
        cocktails_order_count = new HashMap<String, Integer>();
        beer_order_count = new HashMap<String, Integer>();
        wine_order_count = new HashMap<String, Integer>();
        whiskey_order_count = new HashMap<String, Integer>();

        order_count = new HashMap<String, Integer>();

        // get all child data
        String[] soft = getResources().getStringArray(R.array.soft_drinks);
        String[] cock = getResources().getStringArray(R.array.cocktails);
        String[] bee = getResources().getStringArray(R.array.beer);
        String[] win = getResources().getStringArray(R.array.wine);
        String[] whis = getResources().getStringArray(R.array.whiskey);

        // convert to lists for child data
        List<String> softdrinks = new ArrayList<String>(Arrays.asList(soft));
        List<String> cocktails = new ArrayList<String>(Arrays.asList(cock));
        List<String> beer = new ArrayList<String>(Arrays.asList(bee));
        List<String> wine = new ArrayList<String>(Arrays.asList(win));
        List<String> whiskey = new ArrayList<String>(Arrays.asList(whis));

        listDataChild.put(listDataHeader[0], softdrinks); // header, child data
        listDataChild.put(listDataHeader[1], cocktails);
        listDataChild.put(listDataHeader[2], beer);
        listDataChild.put(listDataHeader[3], wine);
        listDataChild.put(listDataHeader[4], whiskey);

        // initialize order count
        for (int i = 0; i < soft.length; i++) {
            order_count.put(listDataChild.get(listDataHeader[0]).get(i), 0);
        }
        for (int i = 0; i < cock.length; i++) {
            order_count.put(listDataChild.get(listDataHeader[1]).get(i), 0);
        }
        for (int i = 0; i < bee.length; i++) {
            order_count.put(listDataChild.get(listDataHeader[2]).get(i), 0);
        }
        for (int i = 0; i < win.length; i++) {
            order_count.put(listDataChild.get(listDataHeader[3]).get(i), 0);
        }
        for (int i = 0; i < whis.length; i++) {
            order_count.put(listDataChild.get(listDataHeader[4]).get(i), 0);
        }

        // set the list view
        expListView = (ExpandableListView) findViewById(R.id.menu_items);
        listAdapter = new MyCustomExpandableAdapter(this, listDataHeader, listDataChild, order_count);
        expListView.setAdapter(listAdapter);

    }

    // when view order button (shopping cart) is clicked
    public void viewOrder(View view) {
        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.popup_window, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        String[] soft = getResources().getStringArray(R.array.soft_drinks);
        String[] cock = getResources().getStringArray(R.array.cocktails);
        String[] beer = getResources().getStringArray(R.array.beer);
        String[] wine = getResources().getStringArray(R.array.wine);
        String[] whis = getResources().getStringArray(R.array.whiskey);
        String total = "";

        for (String i : soft) {
            int count = order_count.get(i);
            if (count != 0) {
                String temp = Integer.toString(order_count.get(i)) + " " + i + "\n";
                total += temp;
            }
        }
        for (String i : cock) {
            int count = order_count.get(i);
            if (count != 0) {
                String temp = Integer.toString(order_count.get(i)) + " " + i + "\n";
                total += temp;
            }
        }
        for (String i : beer) {
            int count = order_count.get(i);
            if (count != 0) {
                String temp = Integer.toString(order_count.get(i)) + " " + i + "\n";
                total += temp;
            }
        }
        for (String i : wine) {
            int count = order_count.get(i);
            if (count != 0) {
                String temp = Integer.toString(order_count.get(i)) + " " + i + "\n";
                total += temp;
            }
        }
        for (String i : whis) {
            int count = order_count.get(i);
            if (count != 0) {
                String temp = Integer.toString(order_count.get(i)) + " " + i + "\n";
                total += temp;
            }
        }

        // display amounts planning on ordering
        TextView textView = (TextView) popupView.findViewById(R.id.view_order_popup);
        if (total.equals("")) {
            textView.setText("Nothing chosen");
        }
        else {
            textView.setText(total);
        }

        FloatingActionButton button = (FloatingActionButton) findViewById(R.id.view_order_button);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(button, Gravity.CENTER, 0, 0);
        dimBehind(popupWindow);

        // close popup window when exit button hit
        Button exitButton = (Button) popupView.findViewById(R.id.exit);
        exitButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    public static void dimBehind(PopupWindow popupWindow) {
        View container;
        if (popupWindow.getBackground() == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                container = (View) popupWindow.getContentView().getParent();
            } else {
                container = popupWindow.getContentView();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                container = (View) popupWindow.getContentView().getParent().getParent();
            } else {
                container = (View) popupWindow.getContentView().getParent();
            }
        }
        Context context = popupWindow.getContentView().getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.6f;
        wm.updateViewLayout(container, p);
    }

    // when order button is clicked
    public void order(View view) {
        // create an alert to confirm order
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DrinkMenuActivity.this);
        alertDialogBuilder.setTitle("CONFIRM");

        // set dialog message
        alertDialogBuilder
                .setMessage("Are you sure you want to order?")
                .setCancelable(true)
                .setPositiveButton("order", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String result = "";
                        serverCall task = new serverCall();
                        try
                        {

                            // ONLY PART YOU HAVE TO CHANGE
                            // template: result = task.execute("http://custom-env.hsqkmufkrn.us-west-1.elasticbeanstalk.com/scripts/SCRIPT","KEY1","VALUE1","KEY2", "VALUE2",...).get();
                            Object[] keys = order_count.keySet().toArray();
                            Log.v("key:", keys[0].toString());
                            Log.v("val:", order_count.get(keys[0].toString()).toString());
                            result = task.execute("http://custom-env.hsqkmufkrn.us-west-1.elasticbeanstalk.com/scripts/restaurant/drinkOrder.php","table","3",
                                    keys[0].toString(),order_count.get(keys[0].toString()).toString(),
                                    keys[1].toString(),order_count.get(keys[1].toString()).toString(),
                                    keys[2].toString(),order_count.get(keys[2].toString()).toString(),
                                    keys[3].toString(),order_count.get(keys[3].toString()).toString(),
                                    keys[4].toString(),order_count.get(keys[4].toString()).toString(),
                                    keys[5].toString(),order_count.get(keys[5].toString()).toString(),
                                    keys[6].toString(),order_count.get(keys[6].toString()).toString(),
                                    keys[7].toString(),order_count.get(keys[7].toString()).toString(),
                                    keys[8].toString(),order_count.get(keys[8].toString()).toString(),
                                    keys[9].toString(),order_count.get(keys[9].toString()).toString(),
                                    keys[10].toString(),order_count.get(keys[10].toString()).toString(),
                                    keys[11].toString(),order_count.get(keys[11].toString()).toString(),
                                    keys[12].toString(),order_count.get(keys[12].toString()).toString(),
                                    keys[13].toString(),order_count.get(keys[13].toString()).toString(),
                                    keys[14].toString(),order_count.get(keys[14].toString()).toString(),
                                    keys[15].toString(),order_count.get(keys[15].toString()).toString(),
                                    keys[16].toString(),order_count.get(keys[16].toString()).toString(),
                                    keys[17].toString(),order_count.get(keys[17].toString()).toString(),
                                    keys[18].toString(),order_count.get(keys[18].toString()).toString(),
                                    keys[19].toString(),order_count.get(keys[19].toString()).toString(),
                                    keys[20].toString(),order_count.get(keys[20].toString()).toString(),
                                    keys[21].toString(),order_count.get(keys[21].toString()).toString(),
                                    keys[22].toString(),order_count.get(keys[22].toString()).toString(),
                                    keys[23].toString(),order_count.get(keys[23].toString()).toString()
                            ).get();

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        // server call's response is saved into result
                        if(!result.equals(""))
                        {
                            Log.v("server response: ", result);
                            Toast.makeText(DrinkMenuActivity.this, "DRINKS ORDERED!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(DrinkMenuActivity.this, "ERROR, PLEASE TRY AGAIN", Toast.LENGTH_SHORT).show();
                        }



                        // closes menu
                        DrinkMenuActivity.this.finish();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        // create alert dialog and show it
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
