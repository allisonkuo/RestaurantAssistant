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
import java.util.concurrent.ExecutionException;

import helper.serverCall;

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
        appetizers.add("Cheese Sticks");
        appetizers.add("Mini Corn Dogs");
        appetizers.add("Chips and Salsa");
        appetizers.add("Jalapeno Poppers");
        appetizers.add("Fried mushrooms");
        appetizers.add("Toothpicks");
        appetizers.add("Hot Wings");
        appetizers.add("Cheese Fries");
        appetizers.add("Fried Pickle Spears");
        burgers.add("Classic Cheeseburger");
        burgers.add("Sunny Side Burger");
        burgers.add("Short Rib Sliders");
        burgers.add("Grilled Chicken");
        burgers.add("Buffalo Chicken");
        burgers.add("Fried Chicken");
        sandwiches.add("BLT");
        sandwiches.add("Club");
        sandwiches.add("Philly Cheese Steak");
        sandwiches.add("Turkey Melt");
        desserts.add("Chocolate Cake");
        desserts.add("Cheese Cake");
        desserts.add("Ice Cream");
        desserts.add("Banana Cream Pie");
        desserts.add("Tiramisu");

    }

    // when order button clicked
    public void order(View view) {
        // TODO: get name and # of item(s) ordered
        String[] ordered_appetizers = new String[10];
        String[] ordered_burgers = new String[10];
        String[] ordered_sandwiches = new String[10];
        String[] ordered_desserts = new String[10];
        String[] ordered_full_menu = new String[10];

        String[] all_orders = new String[50];

        // get orders from each food category
        switch (mActivityTitle) {
            case "Appetizers":
                list_size = appetizers_adapter.getCount(); // get number of items in list
                order_count = appetizers_adapter.getOrderCount(); // get order counts

                for (int i = 0; i < list_size; i++) {
                    String item = appetizers_adapter.getItem(i).toString() + "," + order_count[i];
                    ordered_appetizers[i] = item;
                    all_orders[i] = appetizers_adapter.getItem(i).toString();
                }
                break;

            case "Burgers":
                list_size = burgers_adapter.getCount();
                order_count = burgers_adapter.getOrderCount();

                for (int i = 0; i < list_size; i++) {
                    String item = burgers_adapter.getItem(i).toString() + "," + order_count[i];
                    ordered_burgers[i] = item;
                    all_orders[i] = burgers_adapter.getItem(i).toString();
                }
                break;

            case "Sandwiches":
                list_size =  sandwiches_adapter.getCount();
                order_count = sandwiches_adapter.getOrderCount();

                for (int i = 0; i < list_size; i++) {
                    String item = sandwiches_adapter.getItem(i).toString() + "," + order_count[i];
                    ordered_sandwiches[i] = item;
                    all_orders[i] = sandwiches_adapter.getItem(i).toString();
                }
                break;

            case "Desserts":
                list_size =  desserts_adapter.getCount();
                order_count = desserts_adapter.getOrderCount();

                for (int i = 0; i < list_size; i++) {
                    String item = desserts_adapter.getItem(i).toString() + "," + order_count[i];
                    ordered_desserts[i] = item;
                    all_orders[i] = desserts_adapter.getItem(i).toString();
                }
                break;

            default:
                list_size =  full_adapter.getCount();
                order_count = full_adapter.getOrderCount();

                for (int i = 0; i < list_size; i++) {
                    String item = full_adapter.getItem(i).toString() + "," + order_count[i];
                    ordered_full_menu[i] = item;
                    all_orders[i] = full_adapter.getItem(i).toString();
                }
                break;
        }

        String result = "";
        serverCall task = new serverCall();
        try
        {
            // Sorry Allison, here comes some solid coding
            switch (list_size){
                case 0:
                    result = task.execute("http://custom-env.hsqkmufkrn.us-west-1.elasticbeanstalk.com/scripts/restaurant/foodOrder.php","table","3").get();
                    break;
                case 1:
                    result = task.execute("http://custom-env.hsqkmufkrn.us-west-1.elasticbeanstalk.com/scripts/restaurant/foodOrder.php","table","3",
                            all_orders[0], order_count[0]
                    ).get();
                    break;
                case 2:
                    result = task.execute("http://custom-env.hsqkmufkrn.us-west-1.elasticbeanstalk.com/scripts/restaurant/foodOrder.php","table","3",
                            all_orders[0], order_count[0],
                            all_orders[1], order_count[1]
                    ).get();
                    break;
                case 3:
                    result = task.execute("http://custom-env.hsqkmufkrn.us-west-1.elasticbeanstalk.com/scripts/restaurant/foodOrder.php","table","3",
                            all_orders[0], order_count[0],
                            all_orders[1], order_count[1],
                            all_orders[2], order_count[2]

                    ).get();
                    break;
                case 4:
                    result = task.execute("http://custom-env.hsqkmufkrn.us-west-1.elasticbeanstalk.com/scripts/restaurant/foodOrder.php","table","3",
                            all_orders[0], order_count[0],
                            all_orders[1], order_count[1],
                            all_orders[2], order_count[2],
                            all_orders[3], order_count[3]
                    ).get();
                    break;
                case 5:
                    result = task.execute("http://custom-env.hsqkmufkrn.us-west-1.elasticbeanstalk.com/scripts/restaurant/foodOrder.php","table","3",
                            all_orders[0], order_count[0],
                            all_orders[1], order_count[1],
                            all_orders[2], order_count[2],
                            all_orders[3], order_count[3],
                            all_orders[4], order_count[4]
                    ).get();
                    break;
                case 6:
                    result = task.execute("http://custom-env.hsqkmufkrn.us-west-1.elasticbeanstalk.com/scripts/restaurant/foodOrder.php","table","3",
                            all_orders[0], order_count[0],
                            all_orders[1], order_count[1],
                            all_orders[2], order_count[2],
                            all_orders[3], order_count[3],
                            all_orders[4], order_count[4],
                            all_orders[5], order_count[5]
                    ).get();
                    break;
                case 7:
                    result = task.execute("http://custom-env.hsqkmufkrn.us-west-1.elasticbeanstalk.com/scripts/restaurant/foodOrder.php","table","3",
                            all_orders[0], order_count[0],
                            all_orders[1], order_count[1],
                            all_orders[2], order_count[2],
                            all_orders[3], order_count[3],
                            all_orders[4], order_count[4],
                            all_orders[5], order_count[5],
                            all_orders[6], order_count[6]
                    ).get();
                    break;
                case 8:
                    result = task.execute("http://custom-env.hsqkmufkrn.us-west-1.elasticbeanstalk.com/scripts/restaurant/foodOrder.php","table","3",
                            all_orders[0], order_count[0],
                            all_orders[1], order_count[1],
                            all_orders[2], order_count[2],
                            all_orders[3], order_count[3],
                            all_orders[4], order_count[4],
                            all_orders[5], order_count[5],
                            all_orders[6], order_count[6],
                            all_orders[7], order_count[7]
                    ).get();
                    break;
                case 9:
                    result = task.execute("http://custom-env.hsqkmufkrn.us-west-1.elasticbeanstalk.com/scripts/restaurant/foodOrder.php","table","3",
                            all_orders[0], order_count[0],
                            all_orders[1], order_count[1],
                            all_orders[2], order_count[2],
                            all_orders[3], order_count[3],
                            all_orders[4], order_count[4],
                            all_orders[5], order_count[5],
                            all_orders[6], order_count[6],
                            all_orders[7], order_count[7],
                            all_orders[8], order_count[8]
                    ).get();
                    break;
                case 10:
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
                            all_orders[9], order_count[9]
                    ).get();
                    break;
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
// server call's response is saved into result
        Log.v("server response: ", result);
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
                TextView header1 = (TextView) findViewById(R.id.header1);

                switch (foodCategories[position]) {
                    case "Appetizers":
                        header1.setText("APPETIZERS");
                        appetizers_adapter = new MyCustomAdapter(appetizers, FoodMenuActivity.this);
                        listView.setAdapter(appetizers_adapter);
                        break;

                    case "Burgers":
                        header1.setText("BURGERS");
                        burgers_adapter = new MyCustomAdapter(burgers, FoodMenuActivity.this);
                        listView.setAdapter(burgers_adapter);
                        break;

                    case "Sandwiches":
                        header1.setText("SANDWICHES");
                        sandwiches_adapter = new MyCustomAdapter(sandwiches, FoodMenuActivity.this);
                        listView.setAdapter(sandwiches_adapter);
                        break;

                    case "Desserts":
                        header1.setText("DESSERTS");
                        desserts_adapter = new MyCustomAdapter(desserts, FoodMenuActivity.this);
                        listView.setAdapter(desserts_adapter);
                        break;

/*                    default:
                        TextView header1 = (TextView) findViewById(R.id.header1);
                        header1.setText("Appetizers");
                        appetizers_adapter = new MyCustomAdapter(appetizers, FoodMenuActivity.this);
                        listView = (ListView) findViewById(R.id.menu_items);
                        listView.setAdapter(appetizers_adapter);

                        TextView header2 = (TextView) findViewById(R.id.header2);
                        header2.setText("Burgers");
                        burgers_adapter = new MyCustomAdapter(burgers, FoodMenuActivity.this);
                        ListView listView2 = (ListView) findViewById(R.id.menu_items2);
                        listView2.setAdapter(burgers_adapter);

                        TextView header3 = (TextView) findViewById(R.id.header3);
                        header3.setText("Sandwiches");
                        sandwiches_adapter = new MyCustomAdapter(sandwiches, FoodMenuActivity.this);
                        ListView listView3 = (ListView) findViewById(R.id.menu_items3);
                        listView3.setAdapter(sandwiches_adapter);

                        TextView header4 = (TextView) findViewById(R.id.header4);
                        header4.setText("Burgers");
                        desserts_adapter = new MyCustomAdapter(desserts, FoodMenuActivity.this);
                        ListView listView4 = (ListView) findViewById(R.id.menu_items4);
                        listView4.setAdapter(desserts_adapter);
                        break;*/
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
