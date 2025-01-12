package swen221.assignment4.cards.variations;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import swen221.assignment4.cards.core.Card;
import swen221.assignment4.cards.core.IllegalMove;
import swen221.assignment4.cards.core.Player;
import swen221.assignment4.cards.core.Trick;
import swen221.assignment4.cards.core.Player.Direction;
import swen221.assignment4.cards.util.AbstractCardGame;

public class KnockOutWhist extends AbstractCardGame implements Serializable{
	private int hand = 13;
	
	public KnockOutWhist() {

	}

	public String getName() {
		return "Knock-Out Whist";
	}
	
	public boolean isGameFinished() {
		return hand == 0;		
	}
		
	public void deal(List<Card> deck) {		
		currentTrick = null;
		for (Player.Direction d : Player.Direction.values()) {
			players.get(d).getHand().clear();
		}
		Player.Direction d = Player.Direction.NORTH;
		for (int i = 0; i < hand * 4; ++i) {
			Card card = deck.get(i);
			players.get(d).getHand().add(card);
			d = d.next();
		}			
	}	
	
	public void endHand() {
		super.endHand();
		hand = hand - 1;
	}
}
