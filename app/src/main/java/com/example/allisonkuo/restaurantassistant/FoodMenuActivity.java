package com.example.allisonkuo.restaurantassistant;

import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class FoodMenuActivity extends AppCompatActivity {
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private String[] foodCategories;

    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;

    private ArrayList<String> appetizers = new ArrayList<String>();
    private ArrayList<String> burgers = new ArrayList<String>();
    private ArrayList<String> sandwiches = new ArrayList<String>();
    private ArrayList<String> desserts = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu);

        // navigation bar
        mDrawerList = (ListView) findViewById(R.id.navList);
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

        MyCustomAdapter adapter = new MyCustomAdapter(appetizers, this);
        ListView listView = (ListView) findViewById(R.id.menu_items);
        listView.setAdapter(adapter);
    }

    public void order(View view) {
        // when order button clicked
        // TODO: get name and # of item(s) ordered
        TextView temp = (TextView) findViewById(R.id.temp);
        temp.setText(appetizers.get(1));
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
                        adapter = new MyCustomAdapter(appetizers, FoodMenuActivity.this);
                        listView.setAdapter(adapter);
                        break;

                    case "Burgers":
                        adapter = new MyCustomAdapter(burgers, FoodMenuActivity.this);
                        listView.setAdapter(adapter);
                        break;

                    case "Sandwiches":
                        adapter = new MyCustomAdapter(sandwiches, FoodMenuActivity.this);
                        listView.setAdapter(adapter);
                        break;

                    case "Desserts":
                        adapter = new MyCustomAdapter(desserts, FoodMenuActivity.this);
                        listView.setAdapter(adapter);
                        break;
                }
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

}
