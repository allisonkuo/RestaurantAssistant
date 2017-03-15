package com.example.allisonkuo.restaurantassistant;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.UUID;

public class ThirdActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private int tester;


    private static final String TAG = "ThirdActivity";

    BluetoothAdapter mBluetoothAdapter;
    Button btnEnableDisable_Discoverable;

    BluetoothConnectionService mBluetoothConnection;

    Button btnStartConnection;
    Button btnSend;

    EditText etSend;

    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    BluetoothDevice mBTDevice;

    public ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();

    public DeviceListAdapter mDeviceListAdapter;

    ListView lvNewDevices;


    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBluetoothAdapter.ERROR);

                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "onReceive: STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING ON");
                        break;
                }
            }
        }
    };

    /**
     * Broadcast Receiver for changes made to bluetooth states such as:
     * 1) Discoverability mode on/off or expire.
     */
    private final BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {

                int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);

                switch (mode) {
                    //Device is in Discoverable Mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Enabled.");
                        break;
                    //Device not in discoverable mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Disabled. Able to receive connections.");
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Disabled. Not able to receive connections.");
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        Log.d(TAG, "mBroadcastReceiver2: Connecting....");
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        Log.d(TAG, "mBroadcastReceiver2: Connected.");
                        break;
                }

            }
        }
    };


    /**
     * Broadcast Receiver for listing devices that are not yet paired
     * -Executed by btnDiscover() method.
     */
    private BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "onReceive: ACTION FOUND.");

            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mBTDevices.add(device);
                Log.d(TAG, "onReceive: " + device.getName() + ": " + device.getAddress());
                mDeviceListAdapter = new DeviceListAdapter(context, R.layout.device_adapter_view, mBTDevices);
                lvNewDevices.setAdapter(mDeviceListAdapter);
            }
        }
    };

    /**
     * Broadcast Receiver that detects bond state changes (Pairing status changes)
     */
    private final BroadcastReceiver mBroadcastReceiver4 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //3 cases:
                //case1: bonded already
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                    Log.d(TAG, "BroadcastReceiver: BOND_BONDED.");
                    //inside BroadcastReceiver4
                    mBTDevice = mDevice;
                }
                //case2: creating a bone
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDING) {
                    Log.d(TAG, "BroadcastReceiver: BOND_BONDING.");
                }
                //case3: breaking a bond
                if (mDevice.getBondState() == BluetoothDevice.BOND_NONE) {
                    Log.d(TAG, "BroadcastReceiver: BOND_NONE.");
                }
            }
        }
    };


    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: called.");
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver1);
        unregisterReceiver(mBroadcastReceiver2);
        unregisterReceiver(mBroadcastReceiver3);
        unregisterReceiver(mBroadcastReceiver4);
        //mBluetoothAdapter.cancelDiscovery();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        btnEnableDisable_Discoverable = (Button) findViewById(R.id.btnDiscoverable_on_off);
        lvNewDevices = (ListView) findViewById(R.id.lvNewDevices);
        mBTDevices = new ArrayList<>();

        btnStartConnection = (Button) findViewById(R.id.btnStartConnection);
        btnSend = (Button) findViewById(R.id.btnSend);

        //Broadcasts when bond state changes (ie:pairing)
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mBroadcastReceiver4, filter);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        lvNewDevices.setOnItemClickListener(ThirdActivity.this);




        btnStartConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startConnection();
            }
        });


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //byte[] bytes = etSend.getText().toString().getBytes(Charset.defaultCharset());
                //mBluetoothConnection.write(bytes);
                CreateGame();


            }
        });

    }

    //create method for starting connection
//***remember the conncction will fail and app will crash if you haven't paired first
    public void startConnection() {
        startBTConnection(mBTDevice, MY_UUID_INSECURE);
    }

    /**
     * starting chat service method
     */
    public void startBTConnection(BluetoothDevice device, UUID uuid) {
        Log.d(TAG, "startBTConnection: Initializing RFCOM Bluetooth Connection.");

        mBluetoothConnection.startClient(device, uuid);
    }


    public void enableDisableBT() {
        if (mBluetoothAdapter == null) {
            Log.d(TAG, "enableDisableBT: Does not have BT capabilities.");
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Log.d(TAG, "enableDisableBT: enabling BT.");
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
        }
        if (mBluetoothAdapter.isEnabled()) {
            Log.d(TAG, "enableDisableBT: disabling BT.");
            mBluetoothAdapter.disable();

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
        }

    }


    public void btnEnableDisable_Discoverable(View view) {
        Log.d(TAG, "btnEnableDisable_Discoverable: Making device discoverable for 300 seconds.");

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 1000);
        startActivity(discoverableIntent);

        IntentFilter intentFilter = new IntentFilter(mBluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        registerReceiver(mBroadcastReceiver2, intentFilter);

    }

    public void btnDiscover(View view) {
        Log.d(TAG, "btnDiscover: Looking for unpaired devices.");

        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
            Log.d(TAG, "btnDiscover: Canceling discovery.");

            //check BT permissions in manifest
            checkBTPermissions();

            mBluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
        }
        if (!mBluetoothAdapter.isDiscovering()) {

            //check BT permissions in manifest
            checkBTPermissions();

            mBluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
        }
    }

    /**
     * This method is required for all devices running API23+
     * Android must programmatically check the permissions for bluetooth. Putting the proper permissions
     * in the manifest is not enough.
     * <p>
     * NOTE: This will only execute on versions > LOLLIPOP because it is not needed otherwise.
     */
    private void checkBTPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {

            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }

        } else {
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //first cancel discovery because its very memory intensive.
        mBluetoothAdapter.cancelDiscovery();

        Log.d(TAG, "onItemClick: You Clicked on a device.");
        String deviceName = mBTDevices.get(i).getName();
        String deviceAddress = mBTDevices.get(i).getAddress();

        Log.d(TAG, "onItemClick: deviceName = " + deviceName);
        Log.d(TAG, "onItemClick: deviceAddress = " + deviceAddress);

        //create the bond.
        //NOTE: Requires API 17+? I think this is JellyBean
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Log.d(TAG, "Trying to pair with " + deviceName);
            mBTDevices.get(i).createBond();

            mBTDevice = mBTDevices.get(i);
            mBluetoothConnection = new BluetoothConnectionService(ThirdActivity.this);
        }
    }

    public void SendDataBT(String sendOut) {
        //ByteBuffer b = ByteBuffer.allocate(4);
        //b.putInt(tester);
        //byte[] result = b.array();
        //byte[] bytes = etSend.getText().toString().getBytes(Charset.defaultCharset());

        byte[] bytes = sendOut.getBytes(Charset.defaultCharset());
        mBluetoothConnection.write(bytes);
    }


    public int playerId;
    private BTPlayer p1;
    private BTPlayer p2;
    private int P2handDelivered; //0=no, 1=yes

    //reg variables
    private int prevBet;
    private int totalBet;
    private int calcHand;
    private int shownFlop;
    private int shownTurn;
    private int shownRiver;
    private int firstLoop;
    private int[] turnCheck = new int[4];
    private BTPlayer currPlayer;
    private Deck d;
    private Hand hand1;
    private Hand hand2;
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

    String[] DataSync;

    int nextRound = 1;

    int ReceivedTurn=0;
    final int delay = 2000; //milliseconds

    int prevBT=0;

    // BEGINNING
    public void CreateGame() {
        nextRound = 1;
        prevBet = 0;

        //set up timer
        final Handler h = new Handler();

        setContentView(R.layout.activity_second);
        //hide buttons till start is pressed
        initUI();
        firstLoop = 1;
        //initialize state variables
        prevBet = 0;
        totalBet = 0;
        shownFlop = 0;
        shownTurn = 0;
        shownRiver = 0;
        calcHand = 0;
        //default setup, if player1 we won't do anything different,
        //if player 2, we'll have to overwrite the hands
        d = new Deck();
        hand1 = new Hand(d);
        hand2 = new Hand(d);
        p1 = new BTPlayer(0, 10000);
        p2 = new BTPlayer(1, 10000);
        playerId = -1;
        //index the betting round we're on, value is 0 or 1 depending on if we made any actions yet
        turnCheck[0] = 0;
        turnCheck[1] = 0;
        turnCheck[2] = 0;
        turnCheck[3] = 0;

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
                onP2Click();
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
                finish();
                startActivity(getIntent());
            }
        });
        bCheckCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = bCheckCall.getText().toString();
                //if text is call, use call
                if (text.equals("Check")) {
                    onCheckClick();
                } else if (text.equals("Call")) {
                    onCallClick();
                }
                //if text is check, use check
            }
        });

        //start game logic
        h.postDelayed(new Runnable() {
            public void run() {
                //give to turn to player 1 on first loop
                if (firstLoop == 1 && currPlayer!=null) {
                    currPlayer.giveTurnToPlayer(0);
                    firstLoop = 0;
                    ReceivedTurn = 0;
                }
                //set buttons
                if (currPlayer!=null)
                    if (currPlayer.getCurrPlayerID() != playerId) {
                        //if not player's turn, disable buttons
                        bCheckCall.setEnabled(false);
                        bRaise.setEnabled(false);
                        bRaiseH.setEnabled(false);
                        bFold.setEnabled(false);
                        if (playerId == 0) {
                            turnTextVal.setText("Player 2");
                        } else if (playerId == 1) {
                            turnTextVal.setText("Player 1");
                        }
                    } else if (currPlayer.getCurrPlayerID() == playerId) {
                        bCheckCall.setEnabled(true);
                        bRaise.setEnabled(true);
                        bRaiseH.setEnabled(true);
                        bFold.setEnabled(true);
                        if (playerId == 0) {
                            turnTextVal.setText("Player 1");
                        } else if (playerId == 1) {
                            turnTextVal.setText("Player 2");
                        }
                    }

                //only advance if it is the current player's turn
                //see if we have to change check/call
                //we have started the game by choosing a player
                if (playerId != -1) {
                    String[] TempData;
                    if(mBluetoothConnection.returnInbound()!=null) {
                        TempData = mBluetoothConnection.returnInbound().split(",");
                        int tempInt = Integer.parseInt(TempData[0]);
                        int curRound = Integer.parseInt((TempData[1]));

                        Log.d("nextRound", String.valueOf(nextRound));
                        Log.d("curRound", String.valueOf(curRound));
                        if (playerId == 0 && nextRound == curRound && ReceivedTurn == 0 &&tempInt==playerId){
                            nextRound++;
                        //&& Integer.parseInt(TempData[0]) == playerId && currPlayer.getCurrPlayerID()==playerId) {
                            ReceivedTurn = 1;
                            //prevBT=tempInt;
                            BegTurnSync();
                            EndTurnSend();
                        }
                        else if (playerId == 1 && nextRound == curRound && ReceivedTurn == 0 &&tempInt==playerId){
                            nextRound++;
                            //&& Integer.parseInt(TempData[0]) == playerId && currPlayer.getCurrPlayerID()==playerId) {
                            ReceivedTurn = 1;
                            //prevBT=tempInt;
                            BegTurnSync();
                            EndTurnSend();
                        }


                    }

                    //set text
                    oppBetVal.setText(Integer.toString(currPlayer.getBet() - currPlayer.getCurrBetInRound()));
                    currBetVal.setText(Integer.toString(currPlayer.getCurrBetInRound()));
                    walletVal.setText(Integer.toString(currPlayer.getBalance()));
                    totalPotVal.setText(Integer.toString(currPlayer.getPot()));
                    if ((currPlayer.getBet() - currPlayer.getCurrBetInRound()) != 0) {
                        bCheckCall.setText("Call");
                    } else {
                        bCheckCall.setText("Check");
                    }

                    //show cards
                    if (currPlayer.getCurrBettingRound() == 1) {
                        showFlop();
                    }
                    if (currPlayer.getCurrBettingRound() == 2) {
                        showTurn();
                    }
                    if (currPlayer.getCurrBettingRound() == 3) {
                        showRiver();
                    }

                    //we finished betting, calculate hand
                    if (currPlayer.getCurrBettingRound() == 4) {
                        //if over, check if win
                        int res = checkWin();
                        //if lose
                        if (res == -1) {
                            //message.setText("You Lost!");
                            //.d("iflose", " you lost");
                            onLose();
                        } else if (res == 0) {
                            //message.setText("You Tied!");
                            onTie();
                        } else if (res == 1) {
                            //message.setText("You Won!");
                            onWin();
                        }
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        currPlayer.resetRound();
                        bRestart.setVisibility(View.VISIBLE);

                    }

                }

                h.postDelayed(this, delay);
            }
        }, delay);


    }

    public String ConvertArrayToString(String[] Index) {
        String result = "";
        for (int i = 0; i < 13; i++) {
            result += Index[i] + ",";
        }
        return result;
    }

    void EndTurnSend() {
/*
            if(currPlayer!=null) {
                SendDataBT(ConvertArrayToString(currPlayer.GetBluetoothArray()));
                SendDataBT(ConvertArrayToString(currPlayer.GetBluetoothArray()));
                //double tap...
            }
            else
            {
                SendDataBT(ConvertArrayToString(p1.GetBluetoothArray()));
                SendDataBT(ConvertArrayToString(p1.GetBluetoothArray()));
             }
*/
        if(DataSync!=null)
        SendDataBT(ConvertArrayToString(DataSync));
    }

    void BegTurnSync()
    {
        String TempDatastr;
        if(mBluetoothConnection.returnInbound()!=null) {
            TempDatastr = mBluetoothConnection.returnInbound();
            DataSync = TempDatastr.split(",");
            p1.SetBTDatabase(TempDatastr, DataSync);
            p2.SetBTDatabase(TempDatastr, DataSync);
            if (currPlayer != null)
                currPlayer.SetBTDatabase(TempDatastr, DataSync);
        }
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
        //set text invisible
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
        p1.setHand(hand1);
        p2.setHand(hand2);

        DataSync = p1.GetBluetoothArray();

        String[] temp;
        temp = p2.GetBluetoothArray();

        DataSync[6]=temp[6];
        DataSync[7]=temp[7];
        //at this point, Datasync will contain the bluetooth data for all fields

        String result="";
        result = ConvertArrayToString(DataSync);
        p1.SetBluetoothData(result);
        p2.SetBluetoothData(result);

        //p1 sends data to p2
        SendDataBT(result);

        prevBT=1;

    }
    public void onP2Click(){
        //wait for P1s signal to go through
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        BegTurnSync();
        nextRound = 0;

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


        // Bets the amount
        int currBet = currPlayer.getBet();
        int difference = currBet - currPlayer.getCurrBetInRound();
        int sum = difference + amount;
        currPlayer.bet(sum);
        //increase text
        currBetVal.setText(Integer.toString(currPlayer.getCurrBetInGame()));
        //set the turnCheck val
        turnCheck[currPlayer.getCurrBettingRound()] = 1;
        currPlayer.giveTurnToNextPlayer(2);
        DataSync=currPlayer.GetBluetoothArray();
        EndTurnSend();
        ReceivedTurn=0;

    }

    // This is called when a player clicks the button to fold
    public void onFoldClick()
    {
        int currPlayerId = currPlayer.getCurrPlayerID();
        if (playerId != currPlayerId)
            return;
        currPlayer.fold();
    }

    // This is called when the player clicks on the button to check
    public void onCheckClick()
    {
        int currPlayerId = p1.getCurrPlayerID();
        if (playerId != currPlayerId)
            return;


        /*
        * Make variable for who goes first,
        * If this current player is second and does a check, then call the function
        * curr.incBettingRound();
        * */

        //set the turnCheck val

        //check to see if we need to end the betting round
        //if the bet to match == 0 and we're player 1, pass turn like normal
        if(currPlayer.getCurrBetInRound() == 0 && playerId == 0){
            currPlayer.giveTurnToNextPlayer(2);
            DataSync = currPlayer.GetBluetoothArray();
            EndTurnSend();
            ReceivedTurn=0;
        }else{
            turnCheck[currPlayer.getCurrBettingRound()] = 1;
            //if thats not the case, then we advance betting round and assign turn to p1
            p1.newBettingRound();
            p2.newBettingRound();
            currPlayer.giveTurnToPlayer(0);
            currPlayer.incBettingRound();
            DataSync= currPlayer.GetBluetoothArray();
            EndTurnSend();

            String result="";
            result = ConvertArrayToString(DataSync);
            mBluetoothConnection.SetIncomeString(result);
            ReceivedTurn=0;
            ReceivedTurn=0;
        }
    }

    // This is called when a player clicks on the button to call
    public void onCallClick()
    {
        int currPlayerId = p1.getCurrPlayerID();
        if (playerId != currPlayerId)
            return;



        int currBet = currPlayer.getBet();
        int difference = currBet - currPlayer.getCurrBetInRound();
        currPlayer.bet(difference);

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
            currPlayer.giveTurnToNextPlayer(2);
            DataSync = currPlayer.GetBluetoothArray();
            EndTurnSend();
            ReceivedTurn=0;
        }else{
            turnCheck[currPlayer.getCurrBettingRound()] = 1;
            //if thats not the case, then we advance betting round and assign turn to p1
            //if we are in betting round = 3 though, then time to calculate hands
            p1.newBettingRound();
            p2.newBettingRound();
            currPlayer.giveTurnToPlayer(0);
            currPlayer.incBettingRound();
            DataSync=currPlayer.GetBluetoothArray();
            EndTurnSend();
            ReceivedTurn=0;
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

        return result;
    }

    // Call this if you figure out you won
    public void onWin()
    {
        int currPlayerId = currPlayer.getCurrPlayerID();

        int potVal = currPlayer.getPot();
        int amtBetInGame = currPlayer.getCurrBetInGame();

        // This part is designed for when your opponent goes all in, but you didn't have enough
        if(amtBetInGame * 2 >= potVal)
            currPlayer.winMoney(potVal);
        else
            currPlayer.winMoney(amtBetInGame * 2);

        message.setText("You Win!");
        // Give the loser 3 seconds before the server is reset...
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        // Winner is in charge of resetting the game, since the winner needs some server value before they are reset
        currPlayer.resetRound();
    }

    public void onTie()
    {
        int currPlayerId = currPlayer.getCurrPlayerID();

        int amtBetInGame = currPlayer.getCurrBetInGame();
        currPlayer.winMoney(amtBetInGame);
        //Log.d("tie", " you tied");
        message.setText("You Tied!");
        // Give the loser 3 seconds before the server is reset...
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        // If there's a tie, it doesn't matter who resets the game
        currPlayer.resetRound();
    }

    public void onLose()
    {
        int currPlayerId = p1.getCurrPlayerID();


        // Covers the case where you went all in (and lost), but the enemy player had less money
        int potVal = currPlayer.getPot();
        int amtBet = currPlayer.getCurrBetInGame();
        int difference = potVal - amtBet*2;
        if(difference > 0) {
            currPlayer.winMoney(difference);
        }
        //Log.d("lose", " you lost");
        message.setText("You Lose!");
    }








    //TODO: make it so that during the beginning of each players turn, they update their own data
    //and at the end of the turn, they send their data to the other phone






















}
