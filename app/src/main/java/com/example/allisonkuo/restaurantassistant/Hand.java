package com.example.allisonkuo.restaurantassistant;

import java.util.Collections;
import java.util.Arrays;
import java.util.ArrayList;

public class Hand {
    private Card[] DealersHand;
    private Card[] PlayerHand;
    private Card[] PlayerCombination;

	private ArrayList<Card> flushCards;
	private ArrayList<Card> straightCards;
	private ArrayList<Card> straightFlushCards;
	private ArrayList<Integer> pairCards;
	private ArrayList<Integer> threeOfKind;
	private ArrayList<Integer> fourOfKind;
	private ArrayList<Integer> noCombo;

	private int[] val;
    private int finalHand;
    private int FullHouseHigh;
    private int FullHouseLow;
    private int HighStraightCard;
	private int HighStraightFlushCard;


	public Card[] getPlayerHand()
	{
		return PlayerHand;
	}

	public Card[] getDealersHand()
	{
		return DealersHand;
	}

	private void Init()
	{
		finalHand = -1;
		FullHouseHigh = -1;
		FullHouseLow = -1;
		HighStraightCard = -1;
		HighStraightFlushCard = -1;

		PlayerHand = new Card[2];
		DealersHand = new Card[5];
	}

    Hand(Deck TableDeck) {
		Init();

		PlayerHand[0] = TableDeck.drawCard();
		PlayerHand[1] = TableDeck.drawCard();
		DealersHand = TableDeck.onTable();

		HandComputations();
	}

	Hand(Card[] PlayerCards, Card[] DealerCards)
	{
		Init();

		for(int i = 0; i < 2; i++)
		{
			PlayerHand[i] = PlayerCards[i];
		}

		for(int i = 0; i < 5; i++)
			DealersHand[i] = DealerCards[i];

		HandComputations();
	}

	private void HandComputations()
	{
		flushCards = new ArrayList<Card>();
		straightCards = new ArrayList<Card>();


		PlayerCombination = new Card[7];
		for (int x = 0; x < 2; x++)
			PlayerCombination[x] = PlayerHand[x];
		for (int x = 2; x < 7; x++)
			PlayerCombination[x] = DealersHand[x - 2];

		// Sorts PlayerCombination in ascending order by rank
		Arrays.sort(PlayerCombination);


		int[] ranks = new int[13];
		int[] suits = new int[4];

		int BestHand = 0;
		//9 = royal flush
		//8 = straight flush
		//7 = 4 of kind
		//6 = full house
		//5 = flush
		//4 = straight
		//3 = 3 of kind
		//2 = 2 pair
		//1 = 1 pair
		//0 = high card

		int sumCounter = 0;

		for (int x = 0; x < 4; x++)
			suits[x] = 0;

		for (int x = 0; x < 13; x++)
			ranks[x] = 0;

		//see how many of each rank and each suit there are and store them
		for (int x = 0; x < 7; x++) {
			ranks[PlayerCombination[x].getRank()]++;
			suits[PlayerCombination[x].getSuit()]++;
		}

		//check for flush
		for (int x = 0; x < 4; x++) {
			if (suits[x] >= 5) {
				BestHand = 5;
				// Add all cards of the flush suit to flushCards in descending order by rank
				for(int i = 6; i >= 0; i--)
				{
					if(PlayerCombination[i].getSuit() == x)
						flushCards.add(PlayerCombination[i]);
				}

				break;
			}
		}

		// Copy flushCards Card values into straightFlushCards
		straightFlushCards = new ArrayList<Card>(flushCards);

		//check for straight
		short maxConsecCards = 1;
		short consecCards = 1;
		int highestCardinStraight = -1;
		int highestCardinStraightFlush = -1;

		for (int x = 0; x < 11; x++) {
			if (ranks[x] != 0 && ranks[x + 1] != 0) {
				consecCards++;

				if(x == 0)
				{
					if(ranks[12] != 0)
					{
						consecCards++;
						// Add all aces to straightCards
						for(int i = 0; i < 7; i++)
							if(PlayerCombination[i].getRank() == 12)
								straightCards.add(PlayerCombination[i]);
					}
					// Add all 2's to straightCards
					for(int i = 0; i < 7; i++)
						if(PlayerCombination[i].getRank() == 0)
							straightCards.add(PlayerCombination[i]);
				}

				// Add the cards to straightCards
				for(int i = 0; i < 7; i++)
					if(PlayerCombination[i].getRank() == x + 1)
						straightCards.add(PlayerCombination[i]);

				if (consecCards > maxConsecCards)
					maxConsecCards = consecCards;

				if (consecCards >= 5)
					highestCardinStraight = x + 1;
			} else {
				consecCards = 1;
				if(maxConsecCards < 5)
					straightCards.clear();
			}

		}

		// Clear straightCards if a straight is not found
		if(maxConsecCards < 5)
			straightCards.clear();

		//record highest straight card for ties
		HighStraightCard = highestCardinStraight;

		Collections.reverse(straightCards);

		/* // Bug: Having a straight and a flush doesn't mean having a straight flush
		//check for straight flush
		if (maxConsecCards >= 5)
			if (BestHand == 5)
				BestHand = 8;
			else
				BestHand = 4;*/

		// If we found a straight
		if (maxConsecCards >= 5) {
			// If we saw that we have a flush, check for straight flush
			if (BestHand == 5) {
				highestCardinStraightFlush = getStraightFlush();

				if(highestCardinStraightFlush > 0)
					BestHand = 8;
			} else
				BestHand = 4;
		}

		HighStraightFlushCard  = highestCardinStraightFlush;

		/* // Bug: What if you have a straight flush of 9 to K, but the A is a diff suit?
		//check for royal flush here
		if (BestHand == 8) {
			if (ranks[12] >= 1)
				if (ranks[11] >= 1)
					if (ranks[10] >= 1)
						if (ranks[9] >= 1)
							if (ranks[8] >= 1)
								BestHand = 9;
		*/

		//check for royal flush here
		if (highestCardinStraightFlush == 12) {
			BestHand = 9;
		}


		/* //Bug: This fails on two triples (i.e. A,A,A,K,K,K,5)
		//full house
		int FullDoubleSum = 0;
		int FullTripleSum = 0;

		for (int x = 0; x < 13; x++) {
			if (ranks[x] == 3)
				if (x > FullTripleSum)
					FullTripleSum = x;
			if (ranks[x] == 2)
				if (x > FullDoubleSum)
					FullDoubleSum = x;
		}

		//set Fullhouse as true
		if (FullDoubleSum != 0 && FullTripleSum != 0)
			if (BestHand < 6)
				BestHand = 6;

		FullHouseHigh = FullTripleSum;
		FullHouseLow = FullDoubleSum;
		*/

		int fullHouseDouble = -1;
		int fullHouseTriple = -1;

		for(int i = 0; i < 13; i++)
		{
			// If we encounter a triple
			if(ranks[i] == 3)
			{
				// The old triple gets counted as a double
				fullHouseDouble = fullHouseTriple;

				// Set the new triple to i
				fullHouseTriple = i;
			}
			// If we encounter a double
			if(ranks[i] == 2 ) {
				// Set the new double to i
				fullHouseDouble = i;
			}
		}

		if(fullHouseDouble != -1 && fullHouseTriple != -1)
			if (BestHand < 6)
				BestHand = 6;

		FullHouseHigh = fullHouseTriple;
		FullHouseLow = fullHouseDouble;

		//4/3/2 of kind
		int maxKind = 0;
		int numPairs = 0;

		pairCards = new ArrayList<Integer>();
		threeOfKind = new ArrayList<Integer>();
		fourOfKind = new ArrayList<Integer>();
		noCombo = new ArrayList<Integer>();

		for(int i = 12; i >= 0; i--)
		{
			if(ranks[i] == 2) {
				numPairs++;
				if(maxKind < 2)
					maxKind = 2;

				// Keep track of the highest 2 pairs
				if(numPairs < 3) {
					pairCards.add(i);
					pairCards.add(i);
				}
			}
			if(ranks[i] == 3)
			{
				if(maxKind < 3)
					maxKind = 3;
				if (threeOfKind.isEmpty())
				{
					threeOfKind.add(i);
					threeOfKind.add(i);
					threeOfKind.add(i);
				}
			}
			if(ranks[i] == 4)
			{
				maxKind = 4;

				fourOfKind.add(i);
				fourOfKind.add(i);
				fourOfKind.add(i);
				fourOfKind.add(i);
			}
		}

		ArrayList<Integer> exclude = new ArrayList<Integer>();

		// If four of a kind is found
		if(!fourOfKind.isEmpty())
		{
			exclude.add(fourOfKind.get(0));
			if(BestHand < 7)
				BestHand = 7;
			fillHighCards(fourOfKind, exclude);
		}
		else if(!threeOfKind.isEmpty())
		{
			exclude.add(threeOfKind.get(0));
			if(BestHand < 3)
				BestHand = 3;
			fillHighCards(threeOfKind, exclude);
		}
		else if(numPairs >= 2)
		{
			exclude.add(pairCards.get(0));
			exclude.add(pairCards.get(2));
			if(BestHand < 2)
				BestHand = 2;
			fillHighCards(pairCards, exclude);
		}
		else if(numPairs == 1)
		{
			exclude.add(pairCards.get(0));
			if(BestHand < 1)
				BestHand = 1;
			fillHighCards(pairCards, exclude);
		}
		else
		{
			if(BestHand < 0)
				BestHand = 0;
			fillHighCards(noCombo, null);
		}

		/* // Bug: Always goes into second if statement after first pair is encountered
		int twoPairHighSum = 0;
		int twoPair5thCard = 0;
		int PairCounter = 0;
		for (int x = 0; x < 13; x++) {
			if (ranks[x] == 2)
				PairCounter++;
			if (PairCounter >= 1) {
				if (x * 2 > twoPairHighSum) {
					twoPairLowSum = twoPairHighSum;
					twoPairHighSum = x * 2;
					twoPairHigh = twoPairHighSum;
					if (BestHand < 2) {
						BestHand = 2;
						for (int y = 12; y >= 0; y--) {
							if (ranks[y] == 1 && y > twoPair5thCard) {
								twoPair5thCard = y;
								twoPair5thHigh = twoPair5thCard;
								y = -1;
							}
						}
					}
				}
			}

			if (ranks[x] > maxKind) {
				maxKind = ranks[x];
				sumCounter = ranks[x] * x;
				//3 of kind
				if (maxKind == 3)
					if (BestHand < 3)
						BestHand = 3;
				//4 of kind
				if (maxKind == 4)
					if (BestHand < 7)
						BestHand = 7;
			}
		}
		*/



		// SevenCardSum is worthless and gives little information about what hand is better
		/*
		if (numPairs == 1) {
			if (BestHand == 0)
				BestHand = 1;
			SevenCardSum = 0;
			for (int x = 0; x < 13; x++) {
				if (ranks[x] > 0)
					SevenCardSum += (ranks[x] * x);
			}
		}
		*/
		finalHand = BestHand;
	}

	public void fillHighCards(ArrayList<Integer> dest, ArrayList<Integer> excludedRanks)
	{
		boolean excluded = false;
		for(int i = 6; i >= 0 && dest.size() < 5; i--)
		{
			Card currCard = PlayerCombination[i];
			int currRank = currCard.getRank();
			if(excludedRanks != null)
				for(int ex : excludedRanks) {
					if (ex == currRank) {
						excluded = true;
						break;
					}
				}

			if(excluded)
			{
				excluded = false;
				continue;
			}

			dest.add(currRank);
		}
	}

	public int getStraightFlush()
	{
		int highCard = 0;
		// Get the intersection of flush (which was copied into straightFlushCards) and straight cards
		straightFlushCards.retainAll(straightCards);

		int length = straightFlushCards.size();
		if(length >= 5)
		{
			highCard = getStraight(straightFlushCards);
		}

		return highCard;
	}

	public int getStraight(ArrayList<Card> hand)
	{
		int highCard = 0;

		int[] ranks = new int[13];
		for (int x = 0; x < 13; x++)
			ranks[x] = 0;

		int length = hand.size();
		for (int x = 0; x < length; x++) {
			ranks[hand.get(x).getRank()]++;
		}

		//check for straight
		short maxConsecCards = 1;
		short consecCards = 1;

		for (int x = 0; x < 11; x++) {
			if (ranks[x] != 0 && ranks[x + 1] != 0) {
				consecCards++;

				if(x == 0)
				{
					if(ranks[12] != 0) {
						consecCards++;
					}
				}

				if (consecCards > maxConsecCards)
					maxConsecCards = consecCards;

				if (consecCards >= 5)
					highCard = x + 1;
			} else {
				consecCards = 1;
			}
		}
		return highCard;
	}

	public String toString()
	{
		String result = "";
		for(int i = 0; i < 2; i++)
		{
			result += PlayerHand[i].toString();
			result += "\n";
		}

		for(int i = 0; i < 5; i++)
		{
			result += DealersHand[i].toString();
			result += "\n";
		}

		return result;
	}


    public int returnBestHand(){
        return finalHand;
    }

    public int returnFullHigh(){
        return FullHouseHigh;
    }

    public int returnFullLow(){
        return FullHouseLow;
    }

    public int returnHighStraight(){
        return HighStraightCard;
    }


	// Returns 1 if this wins, 0 if tie, -1 if other wins
	public int Compare(Hand other)
	{
		int result = 0;
		int thisScore = returnBestHand();
		int otherScore = other.returnBestHand();
		if(thisScore > otherScore)
			result = 1;
		else if (thisScore < otherScore)
			result = -1;
		else // thisScore == otherScore
		{
			switch(thisScore)
			{
				case 9:
					break;
				case 8:
					if(HighStraightFlushCard > other.HighStraightFlushCard)
						return 1;
					else if(HighStraightFlushCard < other.HighStraightFlushCard)
						return -1;
					break;
				case 7:
					for(int i = 0; i < 5; i++)
					{
						if(fourOfKind.get(i) > other.fourOfKind.get(i)) {
							return 1;
						}
						else if(fourOfKind.get(i) < other.fourOfKind.get(i)){
							return -1;
						}
					}
					break;
				case 6:
					if(FullHouseHigh > other.FullHouseHigh)
						return 1;
					else if(FullHouseHigh < other.FullHouseHigh)
						return -1;
					else
					{
						if(FullHouseLow > other.FullHouseLow)
							return 1;
						else if(FullHouseLow < other.FullHouseLow)
							return -1;
					}
					break;
				case 5:
					for(int i = 0; i < 5; i++)
					{
						if(flushCards.get(i).getRank() > other.flushCards.get(i).getRank()) {
							return 1;
						}
						else if(flushCards.get(i).getRank() < other.flushCards.get(i).getRank()){
							return -1;
						}
					}
					break;
				case 4:
					if(HighStraightCard > other.HighStraightCard)
						return 1;
					else if (HighStraightCard < other.HighStraightCard)
						return -1;
					break;
				case 3:
					for(int i = 0; i < 5; i++)
					{
						if(threeOfKind.get(i) > other.threeOfKind.get(i)) {
							return 1;
						}
						else if(threeOfKind.get(i) < other.threeOfKind.get(i)){
							return -1;
						}
					}
					break;
				case 2:
				case 1:
					for(int i = 0; i < 5; i++)
					{
						if(pairCards.get(i) > other.pairCards.get(i)) {
							return 1;
						}
						else if(pairCards.get(i) < other.pairCards.get(i)){
							return -1;
						}
					}
					break;
				case 0:
					for(int i = 0; i < 5; i++)
					{
						if(noCombo.get(i) > other.noCombo.get(i)) {
							return 1;
						}
						else if(noCombo.get(i) < other.noCombo.get(i)){
							return -1;
						}
					}
					break;
			}
		}

		return result;
	}
}
