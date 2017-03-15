package com.example.allisonkuo.restaurantassistant;


import java.util.concurrent.ExecutionException;

import helper.serverCall;

public class Player {
    private int money;
    private Hand myHand;
    private int currBetInRound;
    private int currBetInGame;
    private int PlayerID;

    private void Init()
    {
        myHand = null;
        currBetInRound = 0;
        currBetInGame = 0;
    }

    Player(int ID, int amount)
    {
        Init();
        PlayerID = ID;
        money = amount;
    }

    public Hand getHand()
    {
        return myHand;
    }

    // Uploads a specific id's hand
    public void setHandByID(int id, Hand hand)
    {
        Card[] PlayerHand = hand.getPlayerHand();
        int[] handVal = new int[2];

        // Each card is encoded into the server as a single number,
        // where the number = suit * 13 + rank
        for(int i = 0; i < 2; i++)
        {
            handVal[i] = PlayerHand[i].getSuit() * 13 + PlayerHand[i].getRank();
        }

        // Uploads to the server the state of my hand
        serverCall task = new serverCall();
        try
        {
            task.execute("http://custom-env.hsqkmufkrn.us-west-1.elasticbeanstalk.com/scripts/poker/setCards.php",
                    "PlayerID", Integer.toString(id), "Card1", Integer.toString(handVal[0]), "Card2", Integer.toString(handVal[1])).get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    // Uploads my cards and dealer cards to the server
    public void setHand(Hand hand)
    {
        myHand = hand;
        Card[] PlayerHand = myHand.getPlayerHand();
        int[] handVal = new int[2];

        // Each card is encoded into the server as a single number,
        // where the number = suit * 13 + rank
        for(int i = 0; i < 2; i++)
        {
            handVal[i] = PlayerHand[i].getSuit() * 13 + PlayerHand[i].getRank();
        }

        // Uploads to the server the state of my hand
        serverCall task = new serverCall();
        try
        {
            task.execute("http://custom-env.hsqkmufkrn.us-west-1.elasticbeanstalk.com/scripts/poker/setCards.php",
                    "PlayerID", Integer.toString(PlayerID), "Card1", Integer.toString(handVal[0]), "Card2", Integer.toString(handVal[1])).get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Card[] DealerHand = myHand.getDealersHand();
        int[] dealerVal = new int[5];

        // Each card is encoded into the server as a single number,
        // where the number = suit * 13 + rank
        for(int i = 0; i < 5; i++)
        {
            dealerVal[i] = DealerHand[i].getSuit() * 13 + DealerHand[i].getRank();
        }

        // Uploads the dealer/table cards to the server (note the dealer's ID is -101
        task = new serverCall();
        try
        {
            task.execute("http://custom-env.hsqkmufkrn.us-west-1.elasticbeanstalk.com/scripts/poker/setCards.php",
                    "PlayerID", "-101", "Card1", Integer.toString(dealerVal[0]), "Card2", Integer.toString(dealerVal[1]),
                    "Card3", Integer.toString(dealerVal[2]), "Card4", Integer.toString(dealerVal[3]), "Card5", Integer.toString(dealerVal[4])).get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
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


        String serverResult = "";
        serverCall task = new serverCall();
        // Request from the server the cards of the requested ID
        try
        {
            serverResult = task.execute("http://custom-env.hsqkmufkrn.us-west-1.elasticbeanstalk.com/scripts/poker/getCards.php", "PlayerID", Integer.toString(ID)).get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        // Note that cardValStr[0] will be the ID
        String[] cardValStr = serverResult.split(",");

        for(int i = 0; i < numCards; i++) {
            int cardVal = Integer.parseInt(cardValStr[i]);
            if(cardVal != -1){
                result[i] = new Card(cardVal/13, cardVal % 13);
            }else{
                result[i] = new Card(-1,-1);
            }
        }

        return result;
    }

    // Used once the current poker game is over
    public void resetRound()
    {
        myHand = null;
        currBetInRound = 0;
        currBetInGame = 0;

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
    }

    // Returns the current amount of money in the pot
    public int getPot()
    {
        String serverResult = "0";
        serverCall task = new serverCall();

        // Request from the server the value of the pot
        try
        {
            serverResult = task.execute("http://custom-env.hsqkmufkrn.us-west-1.elasticbeanstalk.com/scripts/poker/getPot.php").get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return Integer.parseInt(serverResult);
    }

    // Gets the ID of the player whose turn it is
    public static int getCurrPlayerID()
    {
        String serverResult = "0";
        serverCall task = new serverCall();

        // Request from the server the ID of the current player
        try
        {
            serverResult = task.execute("http://custom-env.hsqkmufkrn.us-west-1.elasticbeanstalk.com/scripts/poker/getCurrPlayer.php").get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if(!serverResult.equals(""))
             return Integer.parseInt(serverResult);
        else
            return getCurrPlayerID();
    }

    // Gets the current amount of money that the player must match to play
    public int getBet()
    {
        String serverResult = "0";
        serverCall task = new serverCall();

        // Request from the server the current highest bet of the current betting round
        try
        {
            serverResult = task.execute("http://custom-env.hsqkmufkrn.us-west-1.elasticbeanstalk.com/scripts/poker/getBet.php").get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return Integer.parseInt(serverResult);
    }

    // Gets the current stage of the game (0 = no table cards, 1 = flop, 2 = turn, 3 = river)
    public int getCurrBettingRound()
    {
        String serverResult = "0";
        serverCall task = new serverCall();

        // Request from the server the stage in which the game is at
        try
        {
            serverResult = task.execute("http://custom-env.hsqkmufkrn.us-west-1.elasticbeanstalk.com/scripts/poker/getBettingRound.php").get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if(!serverResult.equals(""))
            return Integer.parseInt(serverResult);
        else
            return getCurrBetInRound();
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


        serverCall task = new serverCall();
        // Increase the pot value in the server
        try
        {
            String resultStr = Integer.toString(result);
            task.execute("http://custom-env.hsqkmufkrn.us-west-1.elasticbeanstalk.com/scripts/poker/increasePot.php", "Amount", resultStr).get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        currBetInGame += result;
        currBetInRound += result;
        result = currBetInRound;

        // Update the server's highest bet that players must match in the current betting round
        task = new serverCall();

        try
        {
            String PlayerIDStr = Integer.toString(PlayerID);
            String resultStr = Integer.toString(result);
            task.execute("http://custom-env.hsqkmufkrn.us-west-1.elasticbeanstalk.com/scripts/poker/setBet.php", "PlayerID", PlayerIDStr, "Amount", resultStr).get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return result;
    }


    // Sets the current player to the next player (aka after betting, folding, etc)
    public void giveTurnToNextPlayer(int numPlayers)
    {
        int nextPlayerID = (PlayerID + 1) % numPlayers;

        serverCall task = new serverCall();

        // Have the server update whose turn it is
        try
        {
            String PlayerIDStr = Integer.toString(nextPlayerID);
            task.execute("http://custom-env.hsqkmufkrn.us-west-1.elasticbeanstalk.com/scripts/poker/setCurrPlayer.php", "PlayerID", PlayerIDStr).get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    // Would be used on a new betting round to give the turn to the first player
    public static void giveTurnToPlayer(int ID)
    {
        int nextPlayerID = ID;

        serverCall task = new serverCall();

        // Have the server update whose turn it is
        try
        {
            String PlayerIDStr = Integer.toString(nextPlayerID);
            task.execute("http://custom-env.hsqkmufkrn.us-west-1.elasticbeanstalk.com/scripts/poker/setCurrPlayer.php", "PlayerID", Integer.toString(ID)).get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    // Used at the end of a betting round
    public void newBettingRound()
    {
        currBetInRound = 0;
        serverCall task = new serverCall();

        // Reset the current betting round bet amount to 0
        try
        {
            task.execute("http://custom-env.hsqkmufkrn.us-west-1.elasticbeanstalk.com/scripts/poker/setBet.php", "Amount", "0").get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    // Used once everyone is done betting/checking to update how many cards are shown on the table
    public void incBettingRound()
    {
        serverCall task = new serverCall();

        // Have the server advance the betting round to the next one
        try
        {
            task.execute("http://custom-env.hsqkmufkrn.us-west-1.elasticbeanstalk.com/scripts/poker/incBettingRound.php").get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void fold()
    {
        myHand = null;
        currBetInRound = 0;
        currBetInGame = 0;

        serverCall task = new serverCall();
        try
        {
            task.execute("http://custom-env.hsqkmufkrn.us-west-1.elasticbeanstalk.com/scripts/poker/setCards.php",
                    "PlayerID", Integer.toString(PlayerID)).get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void setBalance(int id, int amount)
    {
        // Sets the balance of the specified id to the input ammount
        serverCall task = new serverCall();
        try
        {
            task.execute("http://custom-env.hsqkmufkrn.us-west-1.elasticbeanstalk.com/scripts/poker/setBalance.php",
                    "PlayerID", Integer.toString(id), "Amount", Integer.toString(amount)).get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static int getServBalance(int id)
    {
        // Gets the balance stored in the server for the specified id
        String serverResult = "0";
        serverCall task = new serverCall();
        try
        {
            serverResult = task.execute("http://custom-env.hsqkmufkrn.us-west-1.elasticbeanstalk.com/scripts/poker/getBalance.php",
                    "PlayerID", Integer.toString(id)).get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return (Integer.parseInt(serverResult));
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
}
