import java.util.*;

class Card {
	private String rank;
	private String suit;
	private int value;

	public String getRank() {
		return rank;
	}

	public String getSuit() {
		return suit;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int cardValue) {
		value = cardValue;
	}

	public Card(String rk, String st, int val) {
		rank = rk;
		suit = st;
		value = val;
	}

	public String toString() {
		return rank + " of " + suit;
	}
}

class Deck {
	private Card[] aDeck;
	private int top;

	public void shuffle() {
		for (int i = 0; i < aDeck.length; i++) {
			Card temp = aDeck[i];
			int ran = (int) (Math.random() * 52);
			aDeck[i] = aDeck[ran];
			aDeck[ran] = temp;
		}
	}

	public Card dealCard() {
		return aDeck[top++];
	}

	public Deck() {
		aDeck = new Card[52];
		top = 0;
		String[] ranks = { " an ace", " a king", " a queen", " a jack", " a ten", " a nine", " an eight", " a seven",
				" a six", " a five", " a four", " a three", " a two" };
		String[] suits = { "spades. ", "hearts. ", "clubs. ", "diamonds. " };
		int[] values = { 11, 10, 10, 10, 10, 9, 8, 7, 6, 5, 4, 3, 2 };

		for (int i = 0; i < aDeck.length; i++) {
			aDeck[i] = new Card(ranks[i % 13], suits[i % 4], values[i % 13]);
		}

		this.shuffle();
	}
}

class Player {
	private String name;
	private double cash;
	private double bet;
	private ArrayList<Card> hand;

	public Player(double theCash) {
		cash = theCash;
		bet = 0;
		hand = new ArrayList<Card>();
	}

	public void hit(Card c) {
		hand.add(c);
	}

	public int totalHand() {
		int count = 0;
		for (int i = 0; i < hand.size(); i++) {
			Card c = hand.get(i);
			count = count + c.getValue();
		}
		if (count <= 21) {
			return count;
		}
		int i = 0;
		while (count > 21 && i < hand.size()) {
			Card c = hand.get(i);
			if (c.getValue() == 11) {
				c.setValue(1);
				count = count - 10;
				return count;
			}
			i++;
		}
		return count;
	}

	public void clearHand() {
		hand = new ArrayList<Card>();
	}

	public double totalCash() {
		return cash;
	}

	public double totalBet() {
		return bet;
	}

	public void makeBet(double theBet) {
		bet = theBet;
		cash -= theBet;
	}

	public void getMoney(double daCash) {
		cash += (2 * bet);
	}
}

class Blackjack {
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		Player me = new Player(1000);
		Player dealer = new Player(0);
		String gameAgain;
		do {

			Deck myDeck = new Deck();
			myDeck.shuffle();
			Card c = myDeck.dealCard();

			// Bet Phase
			System.out.println("You have " + me.totalCash() + " dollars left. ");
			System.out.println("How much would you like to bet?");
			double bet = in.nextDouble();
			me.makeBet(bet);
			in.nextLine();

			// Game Phase
			System.out.println("Your first card is" + c);
			me.hit(c);
			c = myDeck.dealCard();
			System.out.println("Dealer showing" + c);
			dealer.hit(c);
			c = myDeck.dealCard();
			System.out.println("Your second card is" + c);
			me.hit(c);
			c = myDeck.dealCard();
			dealer.hit(c);

			Card dealerHoldCard = c; // !!!!!!

			// player phase
			int dealerPhase = 1;
			System.out.println("Would you like another card?");
			String answer = in.nextLine();

			while (answer.equals("yes")) {
				c = myDeck.dealCard();
				me.hit(c);
				System.out.print("You got" + c);

				if (me.totalHand() > 21) {
					System.out.print("You bust! ");
					dealerPhase = 0;
					answer = "no";
				} else {
					c = myDeck.dealCard();
					System.out.println("Your hand is " + me.totalHand() + ". Would you like more cards?");
					answer = in.nextLine();
				}
			}

			// dealer phase
			if (dealerPhase == 1) {
				System.out.print("Dealer reveals" + dealerHoldCard);
				while (dealer.totalHand() < 17) {
					c = myDeck.dealCard();
					dealer.hit(c);
					System.out.println("The dealer draws" + c);
					if (dealer.totalHand() > 21) {
						System.out.print("The dealer busts. ");
					}
				}
			}

			// End eval - I may take out the dealer totalhand eval, I've already
			// checked for bust.
			if (me.totalHand() <= 21 && dealer.totalHand() <= 21 && me.totalHand() > dealer.totalHand()
					&& dealerPhase == 1) {
				System.out.println("You win! ");
				System.out.println("You won " + me.totalBet() + " dollars! ");
				me.getMoney((2 * me.totalBet()));
			} else if (dealer.totalHand() > 21 && dealerPhase == 1) {
				System.out.println("You win!");
				System.out.println("You won " + me.totalBet() + " dollars! ");
				me.getMoney((2 * me.totalBet()));
			} else if (me.totalHand() == dealer.totalHand() && dealerPhase == 1) {
				System.out.println("You push.");
				System.out.println("You get your money back. ");
				me.getMoney((0.5 * me.totalBet()));
			} else {
				System.out.println("You lose.");
				System.out.println("You lost " + me.totalBet() + " dollars... ");
			}

			me.clearHand();
			dealer.clearHand();
			System.out.println("Play again?");
			gameAgain = in.nextLine();
		} while (gameAgain.equals("yes"));

	}
}
