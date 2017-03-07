package com.example.allisonkuo.restaurantassistant;

import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.util.Pair;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ListPopupWindowCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import junit.runner.Version;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import helper.serverCall;

public class FoodMenuActivity extends AppCompatActivity {
    private ListView mDrawerList;
    protected DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private String[] foodCategories;

    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;

    private MyCustomAdapter appetizers_adapter;
    private MyCustomAdapter burgers_adapter;
    private MyCustomAdapter sandwiches_adapter;
    private MyCustomAdapter desserts_adapter;

    private int list_size;
    private String[] order_count = new String[50];
    private String[] appetizers_order_count = {"0", "0", "0", "0", "0", "0", "0", "0", "0", "0"};
    private String[] burgers_order_count = {"0", "0", "0", "0", "0", "0", "0", "0", "0", "0"};
    private String[] sandwiches_order_count = {"0", "0", "0", "0", "0", "0", "0", "0", "0", "0"};
    private String[] desserts_order_count = {"0", "0", "0", "0", "0", "0", "0", "0", "0", "0"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu);

        // navigation bar
        mDrawerList = (ListView) findViewById(R.id.nav_list);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        mActivityTitle = "Appetizers";

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // initialize first page
        getSupportActionBar().setTitle("Appetizers");
        TextView header1 = (TextView) findViewById(R.id.header1);
        header1.setText("APPETIZERS");
        appetizers_adapter = new MyCustomAdapter(R.array.appetizers, FoodMenuActivity.this, appetizers_order_count);
        ListView listView = (ListView) findViewById(R.id.menu_items);
        listView.setAdapter(appetizers_adapter);

        // initialize order_count
        for (int i = 0; i < 50; i++)
            order_count[i] = "0";
    }

    public void viewOrder(View view) {
        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.popup_window, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        // save each item name into all_orders, and each count into order_count
        String[] app = getResources().getStringArray(R.array.appetizers);
        String[] burg = getResources().getStringArray(R.array.burgers);
        String[] sand = getResources().getStringArray(R.array.sandwiches);
        String[] dess = getResources().getStringArray(R.array.desserts);

        String[] all_orders = new String[app.length + burg.length + sand.length + dess.length];

        int current = 0;
        int i = 0;
        for (i = 0; i < app.length; i++) {
            if (!appetizers_order_count[i].equals("0")) {
                all_orders[i + current] = app[i];
                order_count[i + current] = appetizers_order_count[i];
            }
        }
        current += i;
        // burgers
        for (i = 0; i < burg.length; i++) {
            if (!burgers_order_count[i].equals("0")) {
                all_orders[i + current] = burg[i];
                order_count[i + current] = burgers_order_count[i];
            }
        }
        current += i;
        // sandwiches
        for (i = 0; i < sand.length; i++) {
            if (!sandwiches_order_count[i].equals("0")) {
                all_orders[i + current] = sand[i];
                order_count[i + current] = sandwiches_order_count[i];
            }
        }
        current += i;
        // desserts
        for (i = 0; i < dess.length; i++) {
            if (!desserts_order_count[i].equals("0")) {
                all_orders[i + current] = dess[i];
                order_count[i + current] = desserts_order_count[i];
            }
        }
        list_size = current + i;

        String total = "";
        for (int k = 0; k < all_orders.length; k++) {
            if (!order_count[k].equals("0")) {
                String temp = order_count[k] + " " + all_orders[k] + "\n";
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

    // when order button clicked
    public void order(View view) {
        // create an alert to confirm order
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FoodMenuActivity.this);
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
                            // save each item name into all_orders, and each count into order_count
                            String[] app = getResources().getStringArray(R.array.appetizers);
                            String[] burg = getResources().getStringArray(R.array.burgers);
                            String[] sand = getResources().getStringArray(R.array.sandwiches);
                            String[] dess = getResources().getStringArray(R.array.desserts);

                            String[] all_orders = new String[app.length + burg.length + sand.length + dess.length];

                            int current = 0;
                            int i = 0;
                            for (i = 0; i < app.length; i++) {
                                all_orders[i + current] = app[i];
                                order_count[i+current] = appetizers_order_count[i];
                            }
                            current += i;
                            // burgers
                            for (i = 0; i < burg.length; i++) {
                                all_orders[i + current] = burg[i];
                                order_count[i+current] = burgers_order_count[i];
                            }
                            current += i;
                            // sandwiches
                            for (i = 0; i < sand.length; i++) {
                                all_orders[i + current] = sand[i];
                                order_count[i+current] = sandwiches_order_count[i];
                            }
                            current += i;
                            // desserts
                            for (i = 0; i < dess.length; i++) {
                                all_orders[i + current] = dess[i];
                                order_count[i+current] = desserts_order_count[i];
                            }
                            list_size = current + i;

                            // Sorry Allison, here comes some solid coding
                            switch (list_size){
                                case 24:
                                    result = task.execute("http://custom-env.hsqkmufkrn.us-west-1.elasticbeanstalk.com/scripts/restaurant/foodOrder.php","table","3",
                                        all_orders[0], order_count[0],
                                        all_orders[1], order_count[1],
                                        all_orders[2], order_count[2],
                                        all_orders[3], order_count[3],
                                        all_orders[4], order_count[4],
                                        all_orders[5], order_count[5],
                                        all_orders[6], order_count[6],
                                        all_orders[7], order_count[7],
                                        all_orders[8], order_count[8],
                                        all_orders[9], order_count[9],
                                        all_orders[10],order_count[10],
                                        all_orders[11],order_count[11],
                                        all_orders[12],order_count[12],
                                        all_orders[13],order_count[13],
                                        all_orders[14],order_count[14],
                                        all_orders[15],order_count[15],
                                        all_orders[16],order_count[16],
                                        all_orders[17],order_count[17],
                                        all_orders[18],order_count[18],
                                        all_orders[19],order_count[19],
                                        all_orders[20],order_count[20],
                                        all_orders[21],order_count[21],
                                        all_orders[22],order_count[22],
                                        all_orders[23],order_count[23]
                                        ).get();
                                        break;
                                default:
                                    Log.v("here", String.valueOf(list_size));
                            }


                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        // server call's response is saved into result
                        if(result != "")
                            Log.v("server response: ", result);

                        if(result == "success")
                            Toast.makeText(FoodMenuActivity.this, "FOOD ORDERED!", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(FoodMenuActivity.this, "ERROR, PLEASE TRY AGAIN", Toast.LENGTH_SHORT).show();

                        // closes menu
                        FoodMenuActivity.this.finish();
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

    private void addDrawerItems() {
        // set navigation drawer list
        foodCategories = getResources().getStringArray(R.array.food_categories);
        mAdapter = new ArrayAdapter<String>(this, R.layout.drawer_list_item, foodCategories);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // populate menu items for chosen category
                MyCustomAdapter adapter;
                ListView listView = (ListView) findViewById(R.id.menu_items);
                TextView header1 = (TextView) findViewById(R.id.header1);
                Log.v("output", getSupportActionBar().getTitle().toString());

                String prev_category = getSupportActionBar().getTitle().toString();
                switch (prev_category) {
                    case "Appetizers":
                        appetizers_order_count = appetizers_adapter.getOrderCount();
                        Log.v("output", appetizers_order_count[0]);
                        break;
                    case "Burgers":
                        burgers_order_count = burgers_adapter.getOrderCount();
                        break;
                    case "Sandwiches":
                        sandwiches_order_count = sandwiches_adapter.getOrderCount();
                        break;
                    case "Desserts":
                        desserts_order_count = desserts_adapter.getOrderCount();
                        break;
                }

                switch (foodCategories[position]) {
                    case "Appetizers":
                        header1.setText("APPETIZERS");
                        appetizers_adapter = new MyCustomAdapter(R.array.appetizers, FoodMenuActivity.this, appetizers_order_count);
                        listView.setAdapter(appetizers_adapter);
                        break;

                    case "Burgers":
                        header1.setText("BURGERS");
                        burgers_adapter = new MyCustomAdapter(R.array.burgers, FoodMenuActivity.this, burgers_order_count);
                        listView.setAdapter(burgers_adapter);
                        break;

                    case "Sandwiches":
                        header1.setText("SANDWICHES");
                        sandwiches_adapter = new MyCustomAdapter(R.array.sandwiches, FoodMenuActivity.this, sandwiches_order_count);
                        listView.setAdapter(sandwiches_adapter);
                        break;

                    case "Desserts":
                        header1.setText("DESSERTS");
                        desserts_adapter = new MyCustomAdapter(R.array.desserts, FoodMenuActivity.this, desserts_order_count);
                        listView.setAdapter(desserts_adapter);
                        break;

                }
                // set the title to category chosen
                mActivityTitle = foodCategories[position];
                getSupportActionBar().setTitle(mActivityTitle);
            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. Displays activity title */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

}
