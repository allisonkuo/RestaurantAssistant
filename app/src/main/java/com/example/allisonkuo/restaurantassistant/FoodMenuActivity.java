package com.example.allisonkuo.restaurantassistant;

import android.content.res.Configuration;
import android.support.v4.util.Pair;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;

public class FoodMenuActivity extends AppCompatActivity {
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private String[] foodCategories;

    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;

    private MyCustomAdapter full_adapter;
    private MyCustomAdapter appetizers_adapter;
    private MyCustomAdapter burgers_adapter;
    private MyCustomAdapter sandwiches_adapter;
    private MyCustomAdapter desserts_adapter;
    private ArrayList<String> full_menu = new ArrayList<String>();
    private ArrayList<String> appetizers = new ArrayList<String>();
    private ArrayList<String> burgers = new ArrayList<String>();
    private ArrayList<String> sandwiches = new ArrayList<String>();
    private ArrayList<String> desserts = new ArrayList<String>();

    private int list_size;
    private String[] order_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu);

        // navigation bar
        mDrawerList = (ListView) findViewById(R.id.nav_list);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // initialize arrays for different menu items
        appetizers.add("nachos");
        appetizers.add("wings");
        appetizers.add("mozarella sticks");
        burgers.add("cheese");
        sandwiches.add("grilled cheese");

        full_menu.add("nachos");
        full_menu.add("wings");
        full_menu.add("mozarella sticks");
        full_menu.add("cheese");
        full_menu.add("grilled cheese");

        full_adapter = new MyCustomAdapter(full_menu, this);
        ListView listView = (ListView) findViewById(R.id.menu_items);
        listView.setAdapter(full_adapter);
    }

    // when order button clicked
    public void order(View view) {
        // TODO: get name and # of item(s) ordered
        String[] ordered_appetizers = new String[10];
        String[] ordered_burgers = new String[10];
        String[] ordered_sandwiches = new String[10];
        String[] ordered_desserts = new String[10];
        String[] ordered_full_menu = new String[10];

        // get orders from each food category
        switch (mActivityTitle) {
            case "Appetizers":
                list_size = appetizers_adapter.getCount(); // get number of items in list
                order_count = appetizers_adapter.getOrderCount(); // get order counts

                for (int i = 0; i < list_size; i++) {
                    String item = appetizers_adapter.getItem(i).toString() + "," + order_count[i];
                    ordered_appetizers[i] = item;
                }
                /*for (int i = 0; i < list_size; i++)
                    Log.v("output", ordered_appetizers[i]);*/
                break;

            case "Burgers":
                list_size = burgers_adapter.getCount();
                order_count = burgers_adapter.getOrderCount();

                for (int i = 0; i < list_size; i++) {
                    String item = burgers_adapter.getItem(i).toString() + "," + order_count[i];
                    ordered_burgers[i] = item;
                }
                break;

            case "Sandwiches":
                list_size =  sandwiches_adapter.getCount();
                order_count = sandwiches_adapter.getOrderCount();

                for (int i = 0; i < list_size; i++) {
                    String item = sandwiches_adapter.getItem(i).toString() + "," + order_count[i];
                    ordered_sandwiches[i] = item;
                }
                break;

            case "Desserts":
                list_size =  desserts_adapter.getCount();
                order_count = desserts_adapter.getOrderCount();

                for (int i = 0; i < list_size; i++) {
                    String item = desserts_adapter.getItem(i).toString() + "," + order_count[i];
                    ordered_desserts[i] = item;
                }
                break;

            default:
                list_size =  full_adapter.getCount();
                order_count = full_adapter.getOrderCount();

                for (int i = 0; i < list_size; i++) {
                    String item = full_adapter.getItem(i).toString() + "," + order_count[i];
                    ordered_full_menu[i] = item;
                }
                break;

        }

    }

    private void addDrawerItems() {
        // set navigation drawer list
        foodCategories = getResources().getStringArray(R.array.food_categories);
        mAdapter = new ArrayAdapter<String>(this, R.layout.drawer_list_item, foodCategories);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(FoodMenuActivity.this, foodCategories[position], Toast.LENGTH_SHORT).show();

                // populate menu items for chosen category
                MyCustomAdapter adapter;
                ListView listView = (ListView) findViewById(R.id.menu_items);

                switch (foodCategories[position]) {
                    case "Appetizers":
                        appetizers_adapter = new MyCustomAdapter(appetizers, FoodMenuActivity.this);
                        listView.setAdapter(appetizers_adapter);
                        break;

                    case "Burgers":
                        burgers_adapter = new MyCustomAdapter(burgers, FoodMenuActivity.this);
                        listView.setAdapter(burgers_adapter);
                        break;

                    case "Sandwiches":
                        sandwiches_adapter = new MyCustomAdapter(sandwiches, FoodMenuActivity.this);
                        listView.setAdapter(sandwiches_adapter);
                        break;

                    case "Desserts":
                        desserts_adapter = new MyCustomAdapter(desserts, FoodMenuActivity.this);
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
