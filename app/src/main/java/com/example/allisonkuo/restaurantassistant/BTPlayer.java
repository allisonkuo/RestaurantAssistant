package com.example.allisonkuo.restaurantassistant;

public class BTPlayer {

    private String[] BluetoothDatabase={"0","0","0","0","0","0","0","0","0","0","0","0","0"};;
    private String BluetoothDataunparsed;

    private int money;
    private Hand myHand;
    private int currBetInRound;
    private int currBetInGame;
    private int PlayerID;

    private int[] handVal;



    private void Init()
    {
        myHand = null;
        currBetInRound = 0;
        currBetInGame = 0;


    }

    BTPlayer(int ID, int amount)
    {
        Init();
        PlayerID = ID;
        money = amount;
    }

    public String[] GetBluetoothArray()
    {
        return BluetoothDatabase;
    }

    public void SetBluetoothData(String DatatoWrite)
    {
        BluetoothDataunparsed = DatatoWrite;
    }



    public Hand getHand()
    {
        return myHand;
    }


    // Uploads my cards and dealer cards to the server
    public void setHand(Hand hand)
    {
        myHand = hand;
        Card[] PlayerHand = myHand.getPlayerHand();
        handVal = new int[2];

        // Each card is encoded into the server as a single number,
        // where the number = suit * 13 + rank
        for(int i = 0; i < 2; i++)
        {
            handVal[i] = PlayerHand[i].getSuit() * 13 + PlayerHand[i].getRank();
        }



        if(PlayerID==0) {
            //P1 Server Update
            BluetoothDatabase[4]=Integer.toString(handVal[0]);
            BluetoothDatabase[5]=Integer.toString(handVal[1]);

            Card[] DealerHand = myHand.getDealersHand();
            int[] dealerVal = new int[5];

            // Each card is encoded into the server as a single number,
            // where the number = suit * 13 + rank
            for (int i = 0; i < 5; i++) {
                dealerVal[i] = DealerHand[i].getSuit() * 13 + DealerHand[i].getRank();
            }

            //dealer server update
            for(int i=8;i<13;i++)
            {
                BluetoothDatabase[i]=Integer.toString(dealerVal[i-8]);
            }
        }
        else
        {
            BluetoothDatabase[6]=Integer.toString(handVal[0]);
            BluetoothDatabase[7]=Integer.toString(handVal[1]);
        }




    }

    public Card[] getCards(int ID)
    {
        Card[] result;
        int numCards;
        if(ID == -101)
            numCards = 5;
        else
            numCards = 2;

        result = new Card[numCards];

        // Note that cardValStr[0] will be the ID


        String[] cardValStr = BluetoothDataunparsed.split(",");

        //dealer cards are located in spots 9-13 of the BTData
        if(ID==-101)
            for(int i = 0; i < numCards; i++) {
                int cardVal = Integer.parseInt(cardValStr[8+i]);
                result[i] = new Card(cardVal/13, cardVal % 13);
            }

        else if(ID==0)
            for(int i = 0; i < numCards; i++) {
                int cardVal = Integer.parseInt(cardValStr[i+4]);
                result[i] = new Card(cardVal/13, cardVal % 13);
            }
        else if(ID==1)
            for(int i = 0; i < numCards; i++) {
                int cardVal = Integer.parseInt(cardValStr[i+6]);
                result[i] = new Card(cardVal/13, cardVal % 13);
            }



        return result;
    }

    // Used once the current poker game is over
    public void resetRound()
    {
        myHand = null;
        currBetInRound = 0;
        currBetInGame = 0;

        /*
        serverCall task = new serverCall();

        // Request from the server the value of the pot
        try
        {
            task.execute("http://custom-env.hsqkmufkrn.us-west-1.elasticbeanstalk.com/scripts/poker/resetRound.php").get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        */
    }

    // Returns the current amount of money in the pot
    public int getPot()
    {
        return Integer.parseInt(BluetoothDatabase[3]);
    }

    // Gets the ID of the player whose turn it is
    public int getCurrPlayerID()
    {
        return Integer.parseInt(BluetoothDatabase[0]);
    }

    // Gets the current amount of money that the player must match to play
    public int getBet()
    {
        // Request from the server the current highest bet of the current betting round
        return Integer.parseInt(BluetoothDatabase[2]);
    }

    // Gets the current stage of the game (0 = no table cards, 1 = flop, 2 = turn, 3 = river)
    public int getCurrBettingRound()
    {
        // Request from the server the stage in which the game is at
        return Integer.parseInt(BluetoothDatabase[1]);
    }

    // Analogous to raise
    // Adds amount to the pot
    // Check would be implemented as bet(0)
    // Returns the current highest bet in the current betting round
    public int bet(int amount)
    {
        int result = 0;
        if(amount > money) {
            result = money;
            money = 0;
        }
        else
        {
            money -= amount;
            result = amount;
        }


        int temp = Integer.parseInt(BluetoothDatabase[3]);
        temp+=result;
        BluetoothDatabase[3] = Integer.toString(temp);

        currBetInGame += result;
        currBetInRound += result;
        result = currBetInRound;

        // Update the server's highest bet that players must match in the current betting round

            String resultStr = Integer.toString(result);
            BluetoothDatabase[2]=resultStr;


        return result;
    }


    // Sets the current player to the next player (aka after betting, folding, etc)
    public void giveTurnToNextPlayer(int numPlayers)
    {
        int nextPlayerID = (PlayerID + 1) % numPlayers;

        String PlayerIDStr = Integer.toString(nextPlayerID);
        BluetoothDatabase[0]=PlayerIDStr;

    }

    // Would be used on a new betting round to give the turn to the first player
    public void giveTurnToPlayer(int ID)
    {
        int nextPlayerID = ID;
        String PlayerIDStr = Integer.toString(nextPlayerID);
        BluetoothDatabase[0]=PlayerIDStr;

    }

    // Used at the end of a betting round
    public void newBettingRound()
    {
        currBetInRound = 0;
        BluetoothDatabase[2]=Integer.toString(currBetInRound);
    }

    // Used once everyone is done betting/checking to update how many cards are shown on the table
    public void incBettingRound()
    {
        int BetInt = Integer.parseInt(BluetoothDatabase[1]);
        BetInt+=1;
        BluetoothDatabase[1] = Integer.toString(BetInt);

    }

    public void fold()
    {
        myHand = null;
        currBetInRound = 0;
        currBetInGame = 0;

        if(PlayerID==0)
        {
            BluetoothDatabase[5]="-1";
            BluetoothDatabase[6]="-1";
        }
        else if(PlayerID==1)
        {
            BluetoothDatabase[7]="-1";
            BluetoothDatabase[8]="-1";
        }
    }

    public void SetBTDatabase(String Datastr, String[] DataArr)
    {
        BluetoothDatabase = DataArr;
        BluetoothDataunparsed = Datastr;
    }

    public int getCurrBetInRound()
    {
        return currBetInRound;
    }

    public int getCurrBetInGame()
    {
        return currBetInGame;
    }

    public void setMoney(int amount)
    {
        money = amount;
    }

    public int getBalance()
    {
        return money;
    }

    public void winMoney(int amount)
    {
        money += amount;
    }

    public int getPlayerHandVal(int x) {return handVal[x];}
}
