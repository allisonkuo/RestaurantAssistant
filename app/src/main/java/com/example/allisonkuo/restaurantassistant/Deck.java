package com.example.allisonkuo.restaurantassistant;
import java.util.ArrayList;
import java.util.Random;

public class Deck {

    private Card[] DealersHand;
    private ArrayList<Card> deckOfCards;
    Deck()
    {
        deckOfCards = new ArrayList<Card>();
        //adds all 52 cards to deck
        for(int a = 0; a < 4; a++)
        {
            for(int b = 0; b < 13; b++) {
                deckOfCards.add(new Card(a, b));
            }
        }

		int sizeOfDeck = deckOfCards.size() - 1;
		int index1, index2;
		Random randNum = new Random();
		Card temp;

			//shuffles deck 150 times
		for ( short i = 0; i< 150; i++)
		{
			index1 = randNum.nextInt(sizeOfDeck);
			index2 = randNum.nextInt(sizeOfDeck);
			temp = (Card) deckOfCards.get (index2);
			deckOfCards.set(index2, deckOfCards.get(index1));
			deckOfCards.set(index1,temp);
		}

		//5 cards dealt on table
		DealersHand = new Card[5];
		for(int x = 0; x < 5; x++)
			DealersHand[x] = drawCard();

	} //end deck constructor

	//draws a card and returns it
	public Card drawCard()
	{
		//return deckOfCards.remove(deckOfCards.size() - 1);
		return deckOfCards.remove(0);
	}
	public int CardsRemaining() {
		return deckOfCards.size();
	}

	public Card[] onTable(){
		return DealersHand;
	}
}