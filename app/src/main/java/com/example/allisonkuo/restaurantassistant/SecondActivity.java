package com.example.allisonkuo.restaurantassistant;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageButton;

public class SecondActivity extends AppCompatActivity {
    //reg variables
    private int playerId;
    private int bettingRound;
    private int prevBet;
    private int totalBet;
    private int calcHand;
    private int shownFlop;
    private int shownTurn;
    private int shownRiver;
    private int firstLoop;
    private int[] turnCheck = new int[4];
    private Player p1;
    private Player p2;
    private Player currPlayer;
    private Deck d;
    private Hand hand1;
    private Hand hand2;
    private int gameOver;
    //UI variables
    ImageButton bPlayer1;
    ImageButton bPlayer2;
    Button bCheckCall;
    Button bFold;
    Button bRaise;
    Button bRaiseH;
    Button bRestart;

    ImageView tCard1;
    ImageView tCard2;
    ImageView tCard3;
    ImageView tCard4;
    ImageView tCard5;
    ImageView pCard1;
    ImageView pCard2;

    TextView turnText;
    TextView turnTextVal;
    TextView oppBet;
    TextView oppBetVal;
    TextView currBet;
    TextView currBetVal;
    TextView wallet;
    TextView walletVal;
    TextView totalPot;
    TextView totalPotVal;
    TextView message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        //hide buttons till start is pressed
        //Player.giveTurnToPlayer(0);
        initUI();
        firstLoop = 1;
        //initialize state variables
        bettingRound = 0;
        prevBet = 0;
        totalBet = 0;
        shownFlop = 0;
        shownTurn = 0;
        shownRiver = 0;
        calcHand = 0;
        gameOver = 0;
        //default setup, if player1 we won't do anything different,
        //if player 2, we'll have to overwrite the hands
        d = new Deck();
        hand1 = new Hand(d);
        hand2 = new Hand(d);
        p1 = new Player(0, Player.getServBalance(0));
        p2 = new Player(1, Player.getServBalance(1));
        playerId = -1;
        //index the betting round we're on, value is 0 or 1 depending on if we made any actions yet
        turnCheck[0] = 0;
        turnCheck[1] = 0;
        turnCheck[2] = 0;
        turnCheck[3] = 0;
        //set up timer for loop
        final Handler h = new Handler();
        final int delay = 2000; //milliseconds

        //button behaviors
        bPlayer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onP1Click();
                initGame();
                //start game
            }
        });
        bPlayer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Log.d("before","before waiting");
                onP2Click();
                //Log.d("after","after waiting");
                initGame();
            }
        });
        bRaise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRaiseClick(50);
            }
        });
        bRaiseH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRaiseClick(100);
            }
        });
        bFold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFoldClick();
            }
        });
        bRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstLoop = 1;
                finish();
                startActivity(getIntent());
            }
        });
        bCheckCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = bCheckCall.getText().toString();
                //if text is call, use call
                if (text.equals("Check")){
                    onCheckClick();
                } else if(text.equals("Call")){
                    onCallClick();
                }
                //if text is check, use check
            }
        });

        //start game logic
        h.postDelayed(new Runnable(){
        public void run(){
            //give to turn to player 1 on first loop

            if(firstLoop == 1){
                p1.giveTurnToPlayer(0);
                firstLoop = 0;
            }


            //set buttons
            if (p1.getCurrPlayerID() != playerId) {
                //if not player's turn, disable buttons
                bCheckCall.setEnabled(false);
                bRaise.setEnabled(false);
                bRaiseH.setEnabled(false);
                bFold.setEnabled(false);
                if(playerId == 0) {
                    turnTextVal.setText("Player 2");
                } else if(playerId == 1){
                    turnTextVal.setText("Player 1");
                }
            } else if (p1.getCurrPlayerID() == playerId) {
                bCheckCall.setEnabled(true);
                bRaise.setEnabled(true);
                bRaiseH.setEnabled(true);
                bFold.setEnabled(true);
                if(playerId == 0) {
                    turnTextVal.setText("Player 1");
                } else if(playerId == 1){
                    turnTextVal.setText("Player 2");
                }
            }

            //only advance if it is the current player's turn
            //see if we have to change check/call

            //we have started the game by choosing a player
            if(playerId != -1){
                //set text
                oppBetVal.setText(Integer.toString(currPlayer.getBet()-currPlayer.getCurrBetInRound()));
                currBetVal.setText(Integer.toString(currPlayer.getCurrBetInRound()));
                walletVal.setText(Integer.toString(currPlayer.getBalance()));
                totalPotVal.setText(Integer.toString(currPlayer.getPot()));
                if((currPlayer.getBet()-currPlayer.getCurrBetInRound())!= 0){
                    bCheckCall.setText("Call");
                }else {
                    bCheckCall.setText("Check");
                }

                //show cards
                if(currPlayer.getCurrBettingRound() == 1){
                    showFlop();
                }
                if(currPlayer.getCurrBettingRound() == 2){
                    showTurn();
                }
                if(currPlayer.getCurrBettingRound() == 3){
                    showRiver();
                }

                //check for fold
                if(currPlayer.getCurrPlayerID()!= playerId && gameOver == 0){
                    Card[] oppCards;
                    if(playerId == 0) {
                        oppCards = p2.getCards(1);
                    } else{
                        oppCards = p1.getCards(0);
                    }
                    if(oppCards[0].getRank() == -1)
                    {
                        //other player has folded
                        //you win
                        Log.d("Pot Before: ", Integer.toString(p1.getPot()));
                        onWin(p1.getPot());
                    }
                }

                //we finished betting, calculate hand
                if(currPlayer.getCurrBettingRound() == 4){
                    //if over, check if win
                    gameOver = 1;
                    int res = checkWin();
                    //if lose
                    if(res == -1){
                        //message.setText("You Lost!");
                       //.d("iflose", " you lost");
                        Log.d("Pot Before: ", Integer.toString(p1.getPot()));
                        onLose();
                    } else if(res == 0){
                        //message.setText("You Tied!");
                       // Log.d("iftie", " you tied");
                        Log.d("Pot Before: ", Integer.toString(p1.getPot()));
                         onTie();
                    } else if(res == 1){
                        //message.setText("You Won!");
                        //Log.d("ifwin", " you won");
                        Log.d("Pot Before: ", Integer.toString(p1.getPot()));
                        onWin(p1.getPot());
                    }
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    bRestart.setVisibility(View.VISIBLE);
                }
            }

            h.postDelayed(this, delay);
        }
        }, delay);
    }
    private void initUI(){
        bPlayer1 = (ImageButton) findViewById(R.id.button_player_1);
        bPlayer2 = (ImageButton) findViewById(R.id.button_player_2);
        bCheckCall = (Button) findViewById(R.id.button_call_check);
        bFold = (Button) findViewById(R.id.button_fold);
        bRaise = (Button) findViewById(R.id.button_raise);
        bRaiseH = (Button) findViewById(R.id.button_raise_high);
        bRestart = (Button) findViewById(R.id.button_restart);
        bRestart.setVisibility(View.GONE);
        bCheckCall.setVisibility(View.GONE);
        bRaise.setVisibility(View.GONE);
        bRaiseH.setVisibility(View.GONE);
        bFold.setVisibility(View.GONE);

        //set cards invisible
        tCard1 = (ImageView) findViewById(R.id.t_card1);
        tCard2 = (ImageView) findViewById(R.id.t_card2);
        tCard3 = (ImageView) findViewById(R.id.t_card3);
        tCard4 = (ImageView) findViewById(R.id.t_card4);
        tCard5 = (ImageView) findViewById(R.id.t_card5);
        pCard1 = (ImageView) findViewById(R.id.p_card1);
        pCard2 = (ImageView) findViewById(R.id.p_card2);
        tCard1.setVisibility(View.GONE);
        tCard2.setVisibility(View.GONE);
        tCard3.setVisibility(View.GONE);
        tCard4.setVisibility(View.GONE);
        tCard5.setVisibility(View.GONE);
        pCard1.setVisibility(View.GONE);
        pCard2.setVisibility(View.GONE);

        //set text font then make invisible
        turnText = (TextView) findViewById(R.id.text_turn);
        turnTextVal = (TextView) findViewById(R.id.text_turn_value);
        oppBet = (TextView) findViewById(R.id.text_opponent_bet);
        oppBetVal = (TextView) findViewById(R.id.text_opponent_bet_value);
        currBet = (TextView) findViewById(R.id.text_current_bet);
        currBetVal = (TextView) findViewById(R.id.text_current_bet_value);
        wallet = (TextView) findViewById(R.id.text_wallet);
        walletVal = (TextView) findViewById(R.id.text_wallet_value);
        totalPot = (TextView) findViewById(R.id.text_pot);
        totalPotVal = (TextView) findViewById(R.id.text_pot_val);
        message = (TextView) findViewById(R.id.text_message);
        Typeface font = Typeface.createFromAsset(getAssets(), "Lato-Light.ttf");
        Typeface bold = Typeface.createFromAsset(getAssets(), "Lato-Bold.ttf");
        turnText.setTypeface(bold);
        turnTextVal.setTypeface(font);
        oppBet.setTypeface(bold);
        oppBetVal.setTypeface(font);
        currBet.setTypeface(bold);
        currBetVal.setTypeface(font);
        wallet.setTypeface(bold);
        walletVal.setTypeface(font);
        totalPot.setTypeface(bold);
        totalPotVal.setTypeface(font);
        message.setTypeface(font);

        /*turnText.setTextSize(24);
        turnTextVal.setTextSize(24);
        oppBet.setTextSize(24);
        oppBetVal.setTextSize(24);
        currBet.setTextSize(24);
        currBetVal.setTextSize(24);
        wallet.setTextSize(24);
        walletVal.setTextSize(24);
        totalPot.setTextSize(24);
        totalPotVal.setTextSize(24);
        message.setTextSize(24);*/

        turnText.setVisibility(View.GONE);
        turnTextVal.setVisibility(View.GONE);
        oppBet.setVisibility(View.GONE);
        oppBetVal.setVisibility(View.GONE);
        currBet.setVisibility(View.GONE);
        currBetVal.setVisibility(View.GONE);
        wallet.setVisibility(View.GONE);
        walletVal.setVisibility(View.GONE);
        totalPot.setVisibility(View.GONE);
        totalPotVal.setVisibility(View.GONE);
    }
    private void initGame(){
        if (playerId == 0) {
            String p1Card1String = p1.getHand().getPlayerHand()[0].imageString();
            String p1Card2String = p1.getHand().getPlayerHand()[1].imageString();
            ImageView pCard1 = (ImageView) findViewById(R.id.p_card1);
            ImageView pCard2 = (ImageView) findViewById(R.id.p_card2);
            String pCard1uri = "@drawable/" + p1Card1String;
            String pCard2uri = "@drawable/" + p1Card2String;
            int imageResource1 = getResources().getIdentifier(pCard1uri, null, getPackageName());
            int imageResource2 = getResources().getIdentifier(pCard2uri, null, getPackageName());
            pCard1.setImageResource(imageResource1);
            pCard2.setImageResource(imageResource2);
        } else if( playerId == 1){
            Log.d("Curr Player 1: ", Integer.toString(Player.getCurrPlayerID()));
            String p2Card1String = p2.getHand().getPlayerHand()[0].imageString();
            String p2Card2String = p2.getHand().getPlayerHand()[1].imageString();
            ImageView pCard1 = (ImageView) findViewById(R.id.p_card1);
            ImageView pCard2 = (ImageView) findViewById(R.id.p_card2);
            String pCard1uri = "@drawable/" + p2Card1String;
            String pCard2uri = "@drawable/" + p2Card2String;
            int imageResource1 = getResources().getIdentifier(pCard1uri, null, getPackageName());
            int imageResource2 = getResources().getIdentifier(pCard2uri, null, getPackageName());
            pCard1.setImageResource(imageResource1);
            pCard2.setImageResource(imageResource2);
            Log.d("Curr Player 1: ", Integer.toString(Player.getCurrPlayerID()));
        }
    }
    public void showFlop(){
        if(shownFlop == 0) {
            String tCard1String = currPlayer.getHand().getDealersHand()[0].imageString();
            String tCard2String = currPlayer.getHand().getDealersHand()[1].imageString();
            String tCard3String = currPlayer.getHand().getDealersHand()[2].imageString();
            ImageView tCard1 = (ImageView) findViewById(R.id.t_card1);
            ImageView tCard2 = (ImageView) findViewById(R.id.t_card2);
            ImageView tCard3 = (ImageView) findViewById(R.id.t_card3);
            String tCard1uri = "@drawable/" + tCard1String;
            String tCard2uri = "@drawable/" + tCard2String;
            String tCard3uri = "@drawable/" + tCard3String;
            int imageResource1 = getResources().getIdentifier(tCard1uri, null, getPackageName());
            int imageResource2 = getResources().getIdentifier(tCard2uri, null, getPackageName());
            int imageResource3 = getResources().getIdentifier(tCard3uri, null, getPackageName());
            tCard1.setImageResource(imageResource1);
            tCard2.setImageResource(imageResource2);
            tCard3.setImageResource(imageResource3);
            shownFlop = 1;
            currPlayer.newBettingRound();
            oppBetVal.setText(Integer.toString(p2.getBet()-p1.getCurrBetInRound()));
        }
    }
    public void showTurn(){
        if (shownTurn == 0) {
            String tCard4String = currPlayer.getHand().getDealersHand()[3].imageString();
            ImageView tCard4 = (ImageView) findViewById(R.id.t_card4);
            String tCard4uri = "@drawable/" + tCard4String;
            int imageResource4 = getResources().getIdentifier(tCard4uri, null, getPackageName());
            tCard4.setImageResource(imageResource4);
            shownTurn = 1;
            currPlayer.newBettingRound();
            oppBetVal.setText(Integer.toString(p2.getBet()-p1.getCurrBetInRound()));
        }
    }
    public void showRiver(){
        if (shownRiver == 0) {
            String tCard5String = currPlayer.getHand().getDealersHand()[4].imageString();
            ImageView tCard5 = (ImageView) findViewById(R.id.t_card5);
            String tCard5uri = "@drawable/" + tCard5String;
            int imageResource5 = getResources().getIdentifier(tCard5uri, null, getPackageName());
            tCard5.setImageResource(imageResource5);
            shownRiver = 1;
            currPlayer.newBettingRound();
            oppBetVal.setText(Integer.toString(p2.getBet()-p1.getCurrBetInRound()));
        }
    }
    public void onP1Click(){
        currPlayer = p1;
        playerId = 0;

        //enable buttons
        bCheckCall.setVisibility(View.VISIBLE);
        bRaise.setVisibility(View.VISIBLE);
        bRaiseH.setVisibility(View.VISIBLE);
        bFold.setVisibility(View.VISIBLE);
        bPlayer1.setVisibility(View.GONE);
        bPlayer2.setVisibility(View.GONE);
        //enable cards
        tCard1.setVisibility(View.VISIBLE);
        tCard2.setVisibility(View.VISIBLE);
        tCard3.setVisibility(View.VISIBLE);
        tCard4.setVisibility(View.VISIBLE);
        tCard5.setVisibility(View.VISIBLE);
        pCard1.setVisibility(View.VISIBLE);
        pCard2.setVisibility(View.VISIBLE);
        //enable text
        turnText.setVisibility(View.VISIBLE);
        turnTextVal.setVisibility(View.VISIBLE);
        oppBet.setVisibility(View.VISIBLE);
        oppBetVal.setVisibility(View.VISIBLE);
        oppBetVal.setText(Integer.toString(p2.getBet()-p1.getCurrBetInRound()));
        currBet.setVisibility(View.VISIBLE);
        currBetVal.setVisibility(View.VISIBLE);
        currBetVal.setText(Integer.toString(currPlayer.getCurrBetInRound()));
        wallet.setVisibility(View.VISIBLE);
        walletVal.setVisibility(View.VISIBLE);
        walletVal.setText(Integer.toString(p1.getBalance()));
        totalPot.setVisibility(View.VISIBLE);
        totalPotVal.setVisibility(View.VISIBLE);
        totalPotVal.setText(Integer.toString(p1.getPot()));
        //if p1, we keep the stuff init before, upload to server so p2 can access
        p1.resetRound();
        p2.resetRound();
        Player.giveTurnToPlayer(0);

        p1.setHand(hand1);
        p2.setHand(hand2);
        //p1.setHandByID(1, hand2);

    }
    public void onP2Click(){
        currPlayer = p2;
        playerId = 1;
        //enable buttons
        bCheckCall.setVisibility(View.VISIBLE);
        bRaise.setVisibility(View.VISIBLE);
        bRaiseH.setVisibility(View.VISIBLE);
        bFold.setVisibility(View.VISIBLE);
        bPlayer1.setVisibility(View.GONE);
        bPlayer2.setVisibility(View.GONE);
        //enable cards
        tCard1.setVisibility(View.VISIBLE);
        tCard2.setVisibility(View.VISIBLE);
        tCard3.setVisibility(View.VISIBLE);
        tCard4.setVisibility(View.VISIBLE);
        tCard5.setVisibility(View.VISIBLE);
        pCard1.setVisibility(View.VISIBLE);
        pCard2.setVisibility(View.VISIBLE);
        //enable text
        turnText.setVisibility(View.VISIBLE);
        turnTextVal.setVisibility(View.VISIBLE);
        oppBet.setVisibility(View.VISIBLE);
        oppBetVal.setVisibility(View.VISIBLE);
        oppBetVal.setText(Integer.toString(p1.getBet()-currPlayer.getCurrBetInRound()));
        currBet.setVisibility(View.VISIBLE);
        currBetVal.setVisibility(View.VISIBLE);
        currBetVal.setText(Integer.toString(currPlayer.getCurrBetInRound()));
        wallet.setVisibility(View.VISIBLE);
        walletVal.setVisibility(View.VISIBLE);
        walletVal.setText(Integer.toString(p2.getBalance()));
        totalPot.setVisibility(View.VISIBLE);
        totalPotVal.setVisibility(View.VISIBLE);
        totalPotVal.setText(Integer.toString(p2.getPot()));
        //if p2, we have to grab p2 cards from server
        //and also rebuild p1's hand
        while(true)
        {
            Card[] myCards = p2.getCards(1);
            if(myCards[0].getRank() != -1)
            {
                Card[] dealerCards = p2.getCards(-101);
                if(dealerCards[0].getRank() != -1)
                {
                    Hand temp = new Hand(myCards, dealerCards);
                    p2.setHand(temp);
                    Card[] otherCards = p1.getCards(0);
                    Hand temp2 = new Hand(otherCards, dealerCards);
                    p1.setHand(temp2);
                    break;
                }
            }
            // If cards not set yet, then wait
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

    }
    // Call this function when the raise button is pressed
    public void onRaiseClick(int amount)
    {
        int currPlayerId = p1.getCurrPlayerID();
        if (playerId != currPlayerId)
            return;

        Player curr;
        if(currPlayerId == 0) {
            curr = p1;
        }else {
            curr = p2;
        }
        // Bets the amount
        int currBet = curr.getBet();
        int difference = currBet - curr.getCurrBetInRound();
        Log.d("Difference Raise: ", Integer.toString(difference));
        int sum = difference + amount;
        curr.bet(sum);
        //increase text
        currBetVal.setText(Integer.toString(curr.getCurrBetInGame()));
        oppBetVal.setText(Integer.toString(p2.getBet()-p1.getCurrBetInRound()));
        //set the turnCheck val
        turnCheck[curr.getCurrBettingRound()] = 1;
        curr.giveTurnToNextPlayer(2);
    }

    // This is called when a player clicks the button to fold
    public void onFoldClick()
    {
        int currPlayerId = p1.getCurrPlayerID();
        if (playerId != currPlayerId)
            return;

        Player curr;
        if(currPlayerId == 0) {
            curr = p1;
        }else {
            curr = p2;
        }
        curr.fold();
        onLose();
    }

    // This is called when the player clicks on the button to check
    public void onCheckClick()
    {
        int currPlayerId = p1.getCurrPlayerID();
        if (playerId != currPlayerId)
            return;

        Player curr;
        if(currPlayerId == 0) {
            curr = p1;
        }else {
            curr = p2;
        }
        /*
        * Make variable for who goes first,
        * If this current player is second and does a check, then call the function
        * curr.incBettingRound();
        * */

        //set the turnCheck val

        //check to see if we need to end the betting round
        //if the bet to match == 0 and we're player 1, pass turn like normal
        if(currPlayer.getCurrBetInRound() == 0 && playerId == 0){
            curr.giveTurnToNextPlayer(2);
        }else{
            turnCheck[curr.getCurrBettingRound()] = 1;
            //if thats not the case, then we advance betting round and assign turn to p1
            p1.newBettingRound();
            p2.newBettingRound();
            curr.giveTurnToPlayer(0);
            curr.incBettingRound();
            oppBetVal.setText(Integer.toString(p2.getBet()-p1.getCurrBetInRound()));
        }
    }

    // This is called when a player clicks on the button to call
    public void onCallClick()
    {
        int currPlayerId = p1.getCurrPlayerID();
        if (playerId != currPlayerId)
            return;

        Player curr;
        if(currPlayerId == 0) {
            curr = p1;
        }else {
            curr = p2;
        }
        int currBet = curr.getBet();
        int difference = currBet - curr.getCurrBetInRound();
        Log.d("Difference Call: ", Integer.toString(difference));
        curr.bet(difference);

        /*
        * Make variable for who goes first,
        * If this current player is second and does a call, then call the function
        * curr.incBettingRound();
        * curr.giveTurnToPlayer( first player );
        *
        * Else If it's betting round 0 and it's the first player, then do
        * curr.giveTurnToNextPlayer(2);
        *
        * Else
        * curr.incBettingRound();
        * curr.giveTurnToPlayer( first player );
        * */
        if(currPlayer.getCurrBetInRound() == 0 && playerId == 0){
            curr.giveTurnToNextPlayer(2);
        }else{
            turnCheck[curr.getCurrBettingRound()] = 1;
            //if thats not the case, then we advance betting round and assign turn to p1
            //if we are in betting round = 3 though, then time to calculate hands
            p1.newBettingRound();
            p2.newBettingRound();
            curr.giveTurnToPlayer(0);
            curr.incBettingRound();
            oppBetVal.setText(Integer.toString(p2.getBet()-p1.getCurrBetInRound()));
        }
    }

    public int checkWin()
    {
        // -1 = loss, 0 = tie, 1 = win
        int result = -1;

        Hand myHand;
        Hand otherHand;

        // if I am player 1, then I already have player 2's hand
        if(playerId == 0) {
            myHand = p1.getHand();

            otherHand = p2.getHand();
        }
        // If I'm player 2, I need to get player 1's cards from the server
        else {
            myHand = p2.getHand();
            otherHand = p1.getHand();
        }

        result = myHand.Compare(otherHand);
        Log.d("Result: ", Integer.toString(result));

        return result;
    }

    // Call this if you figure out you won
    public void onWin(int potVal)
    {
        int currPlayerId = p1.getCurrPlayerID();

        Player curr;
        if(currPlayerId == 0) {
            curr = p1;
        }else {
            curr = p2;
        }

        int amtBetInGame = curr.getCurrBetInGame();
        Log.d("Pot After: ", Integer.toString(potVal));

        // This part is designed for when your opponent goes all in, but you didn't have enough
        if(amtBetInGame * 2 >= potVal)
            curr.winMoney(potVal);
        else
            curr.winMoney(amtBetInGame * 2);

        //Log.d("win", " you win");
        message.setText("You Win!");
        walletVal.setText(Integer.toString(currPlayer.getBalance()));
        Player.setBalance(playerId, currPlayer.getBalance());
        // Give the loser 3 seconds before the server is reset...
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        bRestart.setVisibility(View.VISIBLE);
        // Winner is in charge of resetting the game, since the winner needs some server value before they are reset
        curr.resetRound();
    }

    public void onTie()
    {

        bRestart.setVisibility(View.VISIBLE);
        int currPlayerId = p1.getCurrPlayerID();


        Player curr;
        if(currPlayerId == 0) {
            curr = p1;
        }else {
            curr = p2;
        }
        int amtBetInGame = curr.getCurrBetInGame();
        curr.winMoney(amtBetInGame);
        //Log.d("tie", " you tied");
        message.setText("You Tied!");
        walletVal.setText(Integer.toString(currPlayer.getBalance()));
        // Give the loser 3 seconds before the server is reset...
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        bRestart.setVisibility(View.VISIBLE);
        // If there's a tie, it doesn't matter who resets the game
        curr.resetRound();
    }

    public void onLose()
    {

        bRestart.setVisibility(View.VISIBLE);
        int currPlayerId = p1.getCurrPlayerID();


        Player curr;
        if(currPlayerId == 0) {
            curr = p1;
        }else {
            curr = p2;
        }

        // Covers the case where you went all in (and lost), but the enemy player had less money
        int potVal = curr.getPot();
        int amtBet = curr.getCurrBetInGame();
        int difference = potVal - amtBet*2;
        if(difference > 0) {
            curr.winMoney(difference);
        }


        Log.d("balance1", Integer.toString(currPlayer.getBalance()));
        message.setText("You Lose!");
        Player.setBalance(playerId, currPlayer.getBalance());
        Log.d("balance2",  Integer.toString(Player.getServBalance(0)));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        bRestart.setVisibility(View.VISIBLE);
    }

    public boolean checkIfMyTurn()
    {
        int currPlayerId = p1.getCurrPlayerID();
        if(playerId != currPlayerId) {
            return false;
        }
        return true;
    }

}
