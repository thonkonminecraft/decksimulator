package thonk.decksimulator.simulation;

import java.util.ArrayList;
import java.util.Random;

public class Deck {

    private ArrayList<Card> deck;

    public Deck(ArrayList<Card> deck) {
        this.deck = new ArrayList<>(deck);
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public Card popRandom() {
        if (deck.size() > 0) {
            Random r = new Random();
            return deck.remove(r.nextInt(deck.size()));
        } else {
            return null;
        }
    }

    public void addCard(Card c) {
        deck.add(c);
    }

    public boolean isDeckEmpty() {
        return deck.isEmpty();
    }
}
