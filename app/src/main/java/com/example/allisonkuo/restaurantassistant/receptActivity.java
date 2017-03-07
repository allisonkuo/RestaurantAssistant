package com.example.allisonkuo.restaurantassistant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import helper.serverCall;

public class receptActivity extends AppCompatActivity {
    private receptAdapter adapter;
    private ArrayList<String> orders = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recept);

        // get JSON string from  server
        String result = "";
        serverCall task = new serverCall();
        try
        {
            // ONLY PART YOU HAVE TO CHANGE
            // template: result = task.execute("http://custom-env.hsqkmufkrn.us-west-1.elasticbeanstalk.com/scripts/SCRIPT","KEY1","VALUE1","KEY2", "VALUE2",...).get();
            result = task.execute("http://custom-env.hsqkmufkrn.us-west-1.elasticbeanstalk.com/scripts/restaurant/getRecept.php","table","3").get(); //TODO: change to app's table number

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        // server call's response is saved into result
        if(result != "")
            Log.v("server response: ", result);

        // get all order names
        JSONObject prices = null;
        JSONObject foods = null;
        try {
            JSONObject reader = new JSONObject(result);
            prices = reader.getJSONObject("prices");
            foods = reader.getJSONObject("foods");
            boolean split = false;
            if(prices.length() > 0)
            {
                orders.add("DRINKS");
            }
            else
            {
                orders.add("NO ORDERS");
            }
            for (int i = 0; i < prices.length(); i++)
            {
                if(split == false && foods.has(prices.names().get(i).toString()))
                {
                    orders.add("FOODS");
                    split = true;
                }
                orders.add(prices.names().get(i).toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        orders.add("TOTAL");

        adapter = new receptAdapter(orders, result, this);
        ListView listView = (ListView) findViewById(R.id.menu_items);
        listView.setAdapter(adapter);
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


        return super.onOptionsItemSelected(item);

    }
}
