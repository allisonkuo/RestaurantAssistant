package com.example.allisonkuo.restaurantassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.util.Log;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton button = (ImageButton) findViewById(R.id.welcome_button_wifi);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSecondActivity();

                /* // Testing new Balance functions
                int x = Player.getBalance(0);
                Log.d("TEST", Integer.toString(x));

                Player.setBalance(0, x+100);

                x = Player.getBalance(0);
                Log.d("TEST", Integer.toString(x));
                */
                /*
                Player p1 = new Player(0, 1000);
                p1.giveTurnToPlayer(1);
                int x = 0;
                while(true)
                {
                    if(x == 5) {
                        p1.giveTurnToPlayer(0);
                    }
                    if(p1.getCurrPlayerID() == 0)
                        break;

                    x++;
                    Log.d("IN LOOP", "BOO");

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }

                Log.d("OUT OF LOOP", "AYY");
                */
            }
        });
        ImageButton blueButt = (ImageButton) findViewById(R.id.welcome_button_bluetooth);
        blueButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToThirdActivity();
            }
        });
    };
    private void goToSecondActivity() {
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        startActivity(intent);
    }

    private void goToThirdActivity() {
        Intent intent = new Intent(MainActivity.this, ThirdActivity.class);
        startActivity(intent);
    }
/*
    public void clickHandler(View view) {
        int total = 0;


        Deck d = new Deck();
        Hand hand1 = new Hand(d);
        Hand hand2 = new Hand(d);

        Player p1 = new Player(0, 10000);
        p1.setHand(hand1);

        Player p2 = new Player(1, 10000);
        p2.setHand(hand2);

        int betRound = p1.getCurrBettingRound();
        Log.d("Betting Round 1:", Integer.toString(betRound));
        p1.bet(400);
        int bet = p1.getBet();
        Log.d("Bet 1:", Integer.toString(bet));

        p1.newBettingRound();
        p1.incBettingRound();

        int betRound2 = p1.getCurrBettingRound();
        Log.d("Betting Round 2:", Integer.toString(betRound2));
        p1.bet(600);
        int bet2 = p1.getBet();
        Log.d("Bet 2:", Integer.toString(bet2));
        int pot = p1.getPot();
        Log.d("Pot:", Integer.toString(pot));

        p1.giveTurnToPlayer(0);
        int currID = p1.getCurrPlayerID();
        Log.d("Curr ID 1:", Integer.toString(currID));

        p1.giveTurnToNextPlayer(2);
        int currID2 = p1.getCurrPlayerID();
        Log.d("Curr ID 2:", Integer.toString(currID2));

        p2.giveTurnToNextPlayer(2);
        int currID3 = p1.getCurrPlayerID();
        Log.d("Curr ID 2:", Integer.toString(currID3));


        int winResult = (p1.getHand()).Compare(p2.getHand());
        if(winResult == 1)
            Log.d("Winner:", "Player 1");
        else if(winResult == -1)
            Log.d("Winner:", "Player 2");
        else
            Log.d("Winner:", "Tie");

        //Log.d("HAND 1:", Player1.toString());
        //Log.d("HAND 2:", Player2.toString());
        //Log.d("DEALER:", Player1.returnDealerHand());


    }*/
}
