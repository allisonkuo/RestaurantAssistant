package com.example.allisonkuo.restaurantassistant;

import java.util.Collections;
import java.util.Arrays;
import java.util.ArrayList;

public class PokerHandler {
    Player[] players;
    int numPlayers;
    // Amount of money for big and small blind
    int bigBlind, smallBlind;
    // Player that goes first in the current round
    int firstPlayer;
    // Amount of money in the pot
    int currPot, currBet;
    int currMoney;


    private void Init()
    {
        players = new Player[numPlayers];
        bigBlind = 100;
        smallBlind = 50;
        firstPlayer = 0;
        currPot = 0;
        currBet = bigBlind;
        currMoney = 5000;

        // Start out all players with 5000 moneys
        for(int i = 0; i < numPlayers; i++)
        {
            players[i] = new Player(i, currMoney);
        }
    }

    PokerHandler()
    {
        Init();
        startRound();
    }

    // 1 on success, -1 on error
    private int startRound()
    {
        currPot = 0;
        int currPlayer = firstPlayer;
        int currBet = bigBlind;
        // This array keeps track of how much each player has bet
        // It is used in case one player bets more money than what the other person has
        int[] currAmountBet = new int[numPlayers];

        // The two players that have to pay a blind at the beginning
        int bigBlindPlayer, smallBlindPlayer;
        if(firstPlayer - 1 == -1) {
            bigBlindPlayer = numPlayers - 1;
            smallBlindPlayer = numPlayers - 2;
        }
        else if(firstPlayer - 2 == -1)
        {
            bigBlindPlayer = 0;
            smallBlindPlayer = numPlayers - 1;
        }
        else
        {
            bigBlindPlayer = firstPlayer - 1;
            smallBlindPlayer = firstPlayer - 2;
        }

        // If the players can't afford the blinds, the game can't start
        if(players[bigBlindPlayer].getBalance() < bigBlind)
            return -1;
        if(players[smallBlindPlayer].getBalance() < smallBlind)
            return -1;

        players[bigBlindPlayer].bet(bigBlind);
        currPot += bigBlind;
        players[smallBlindPlayer].bet(smallBlind);
        currPot += smallBlind;


        bettingRound();
        ;// Reveal the flop
        bettingRound();
        ;// Reveal the turn
        bettingRound();
        ;// Reveal the river
        bettingRound();

        resolveWinner();

        for(int i = 0; i < numPlayers; i++)
            players[i].resetRound();

        firstPlayer = (firstPlayer + 1) % numPlayers;
        return -1;
    }


    public void bettingRound()
    {
        while(true)
        {
            // Continue until everyone checks/bets/folds
            break;
        }
    }

    public void resolveWinner()
    {

    }
}
