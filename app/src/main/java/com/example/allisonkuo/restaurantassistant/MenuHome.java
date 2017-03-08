package com.example.allisonkuo.restaurantassistant;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import helper.serverCall;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MenuHome extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    public boolean waiter_called = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_home);

    }

    /** Called when the user clicks the food menu button */
    public void openMenu(View view) {
        Intent intent = new Intent(this, FoodMenuActivity.class);
        startActivity(intent);
    }

    public void callWaiter(View view)
    {
        String result = "";
        serverCall task = new serverCall();
        try
        {
            // ONLY PART YOU HAVE TO CHANGE
            // template: result = task.execute("http://custom-env.hsqkmufkrn.us-west-1.elasticbeanstalk.com/scripts/SCRIPT","KEY1","VALUE1","KEY2", "VALUE2",...).get();
            if(!this.waiter_called) {
                result = task.execute("http://custom-env.hsqkmufkrn.us-west-1.elasticbeanstalk.com/scripts/restaurant/callWaiter.php","table","3").get();
            }
            else {
                result = task.execute("http://custom-env.hsqkmufkrn.us-west-1.elasticbeanstalk.com/scripts/restaurant/resolveCallWaiter.php","table","3").get();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        // server call's response is saved into result
        if(!result.equals(""))
            Log.v("server response: ", result);

        if(result.equals("success") && !waiter_called) {
            ImageButton waiterImage = (ImageButton) view.findViewById(R.id.button_waiter);
            waiterImage.setImageResource(R.drawable.button_waiter);

            Toast toast = Toast.makeText(MenuHome.this, "Waiter is on the way", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0,100);
            toast.show();
            waiter_called = true;
        }
        else if(result.equals("success") && waiter_called) {
            ImageButton waiterImage = (ImageButton) view.findViewById(R.id.button_waiter);
            waiterImage.setImageResource(R.drawable.button_waiter_empty);
            Toast toast = Toast.makeText(MenuHome.this, "Waiter request cancelled", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0,100);
            toast.show();
            waiter_called = false;
        }
        else {
            Toast.makeText(MenuHome.this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    public void viewReceipt(View view) {
        Intent intent = new Intent(this, receptActivity.class);
        startActivity(intent);
    }
}
