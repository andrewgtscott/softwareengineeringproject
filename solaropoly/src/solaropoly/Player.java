/**
 * 
 */
package solaropoly;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * This class holds properties about the player's status in game, including name, balance, position
 * and assets
 * @author G17
 */
public class Player {
	
	/**
	 * Separator to use in the getPlayerAttention to separate the action for each player
	 */
	private static final char SEPARATOR_CHAR = '_';
	
	/**
	 * Force the business rule if required to stop the game if there's a limit of loops on the board
	 */
	private static final int LOOPS_LIMIT = 1;
	
	/**
	 * The result of a die should be minimum 1. Set the limit based on the number of dice the game should use
	 */
	private static final int DICE_NUMBER = 2;
	
	// instance vars
	private boolean play;
	private String name;
	private int balance;
	private int position;
	private HashSet<Square> ownedSquares; // TODO make this TreeSet/ArrayList
	private ArrayList<Group> ownedGroups;
	
	/**
	 * Default constructor
	 */
	public Player() {}
	
	/**
	 * Constructor with arguments
	 * @param name
	 * @param balance
	 * @param position
	 */
	public Player(String name, int balance, int position, boolean play) throws IllegalArgumentException, IllegalStateException {
		this.setName(name);
		this.balance = balance;
		this.setPosition(position);
		this.ownedSquares = new HashSet<Square>();
		this.play = play;
	}
	
	/**
	 * @return the play, it says if the player is playing or not
	 */
	public boolean isPlay() {
		return play;
	}

	/**
	 * @param play the play to set, it set the decision of the player to stop to play
	 */
	public void setPlay(boolean play) {
		this.play = play;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) throws IllegalArgumentException, IllegalStateException {
		if (name.strip() == "" || name == null) {
			throw new IllegalArgumentException("Need at least one non-whitespace character");
		} else if (name.length() > 20) {
			this.name = name.substring(0, 19);
			System.out.println("We did have to shorten that - sorry!");
		} else {
			this.name = name;
		}
	}
	
	/**
	 * @return the balance
	 */
	public int getBalance() {
		return balance;
	}
	
	/**
	 * @param balance the balance to set
	 */
	public void setBalance(int balance) {
		this.balance = balance;
	}
	
	/**
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}
	
	/**
	 * @param position the position to set
	 */
	public void setPosition(int position) throws IllegalArgumentException {
		if (position >= (GameSystem.board.getSize() * LOOPS_LIMIT) || position < 0) {
			throw new IllegalArgumentException("Invalid player position. Try to reduce the dice sides or the number of dice");
		} else {
			this.position = position;
		}
	}
	
	/**
	 * @return the ownedSquares
	 */
	public HashSet<Square> getOwnedSquares() {
		return ownedSquares;
	}
	
	/**
	 * @param ownedSquares the ownedSquares to set
	 */
	public void setOwnedSquares(HashSet<Square> ownedSquares) {
		this.ownedSquares = ownedSquares;
	}
	
	/**
	 * @return the ownedGroups
	 */
	public ArrayList<Group> getOwnedGroups() {
		return ownedGroups;
	}

	/**
	 * @param ownedGroups the ownedGroups to set
	 */
	public void setOwnedGroups(ArrayList<Group> ownedGroups) {
		this.ownedGroups = ownedGroups;
	}
	
	@Override
	public String toString() {
		return "Player [name=" + GameSystem.RED_BRIGHT + name + GameSystem.RESET + ", balance=" + balance + ", position=" + position + ", ownedSquares="
				+ ownedSquares + ", ownedGroups=" + ownedGroups + "]";
	}
	
	// Methods
	
	/**
	 * This method prints in console a long line that cover the width of the terminal automatically.
	 * It uses the current terminal width and prints the line separation to apply when the player
	 * attention should be caught because an action is required. Then it prints the name of the player
	 * asking to take an action.
	 */
	public void getAttention() {
		int consoleWidth = 80; // Default console width
        try {
            consoleWidth = Integer.parseInt(System.getenv("COLUMNS"));
        } catch (NumberFormatException e) {
            // Ignore the exception and use the default console width
        }
        System.out.println(String.valueOf(SEPARATOR_CHAR).repeat(consoleWidth));
        System.out.printf("Player %s, take action!%n%n", this.name);
	}
	
	/**
	 * This method returns the square where the player actually is.
	 */
	public Square getLandedSquare() {
		return GameSystem.board.getSquare(this.position);
	}
	
	/**
	 * This method move the player by a given valid dice result value
	 */
	public void move(int roll) throws IllegalArgumentException {
		if (roll >= DICE_NUMBER) {
			BoardPosition boardPosition = GameSystem.board.getBoardPosition(this.position, roll);
			for (int i = 0; i < boardPosition.getStartPassed(); i++) {
				GameSystem.board.getSquare(0).act(this);
			}
			this.setPosition(boardPosition.getPosition());
			boardPosition.getSquare().act(this);
		} else {
			throw new IllegalArgumentException("Invalid dice roll. Try to change the number of dice to roll");
		}
	}
	
	public void displayBalance() {
		System.out.printf("%s%s%s, your current balance is £%,d.%n", GameSystem.RED_BRIGHT, this.name, GameSystem.RESET, this.balance);
		// TODO another version showing properties too
	}
	
	public void increaseBalance(int credit) {
		this.setBalance(this.balance + credit);
	}
	
	public void decreaseBalance(int debit) {
		this.setBalance(this.balance - debit);
	}
	
	public void gainOwnership(Square square) {
		this.ownedSquares.add(square);
	}
}
