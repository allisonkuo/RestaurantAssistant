package com.example.allisonkuo.restaurantassistant;

public class Card implements Comparable<Card>{

    private int rank, suit;
    private static String[] suits = {"Hearts", "Spades", "Diamonds", "Clubs"};
    private static String[]ranks = { "2","3","4","5","6","7","8","9","10","Jack", "Queen", "King","Ace"};

	Card(int suit, int rank)
	{
		this.rank = rank;
		this.suit = suit;
	}

    public static String returnRankString(int m_rank) {
        return ranks[m_rank];
    }
    public String returnFullCardString() {
        return ranks[rank] + " of " + suits[suit];
    }
    public int getRank() {
        return rank;
    }

    public int getSuit() {
        return suit;
    }

    public String toString()
    {
        return ranks[rank] + " of " + suits[suit];
    }

    public String imageString() {return "c" + ranks[rank].toLowerCase() +"_of_" + suits[suit].toLowerCase();}

    @Override
	public int compareTo(Card c)
	{
        if(rank > c.getRank())
            return 1;

		if(rank < c.getRank())
            return -1;
        else
            return 0;
	}

    @Override
    public boolean equals(Object other)
    {
        if (other == null)
            return false;
        if (other == this)
            return true;
        if (!(other instanceof Card))
            return false;
        Card otherCard = (Card)other;
        if(getRank() == otherCard.getRank() && getSuit() == otherCard.getSuit())
            return true;
        else
            return false;
    }
}
