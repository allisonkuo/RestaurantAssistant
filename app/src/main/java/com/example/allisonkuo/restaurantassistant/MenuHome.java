package com.example.allisonkuo.restaurantassistant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.concurrent.ExecutionException;

import helper.serverCall;

import static android.support.v7.widget.AppCompatDrawableManager.get;


public class MenuHome extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_home);

    }

    /** Called when the user clicks the food menu button */
    public void test(View view) {
        String result = "";
        serverCall task = new serverCall();
        try
        {
            // ONLY PART YOU HAVE TO CHANGE
            result = task.execute("http://custom-env.hsqkmufkrn.us-west-1.elasticbeanstalk.com/scripts/test.php","input","*returnedValue*").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
// server call's response is saved into result
        Log.v("server response: ", result);
    }
}
