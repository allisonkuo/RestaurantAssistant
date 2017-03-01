package com.example.allisonkuo.restaurantassistant;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MenuHome extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_home);

        ImageButton button_menu = (ImageButton) findViewById(R.id.button_menu);
        int btn_size = button_menu.getLayoutParams().width;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(btn_size, btn_size);
        button_menu.setLayoutParams(params);
    }

    /** Called when the user clicks the Send button */
    public void sendMessage(View view) {

    }
}
