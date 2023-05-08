/**
 * Solaropoly Game
 */
package solaropoly;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * The executable class of the system, starting the Solaropoly command-line
 * game.
 * 
 * @author G17
 */
public class GameSystem {

	/// game parameter constants (rules)
	/// set from csv
	public static int startingBalance;
	public static int productionGoal;
	/// set here
	public static final int MIN_PLAYERS = 2;
	public static final int MAX_PLAYERS = 4;
	public static final String PRE = ""; /// resource prefix
	public static final String SUF = " GET"; /// resource suffix

	/// console colour keys
	// Regular
	public static final String RESET = "\u001B[0m";
	public static final String BLACK = "\u001B[30m";
	public static final String RED = "\u001B[31m";
	public static final String GREEN = "\u001B[32m";
	public static final String YELLOW = "\u001B[33m";
	public static final String BLUE = "\u001B[34m";
	public static final String PURPLE = "\u001B[35m";
	public static final String CYAN = "\u001B[36m";
	public static final String WHITE = "\u001B[37m";

	// Bold
	public static final String BLACK_BOLD = "\033[1;30m"; // BLACK
	public static final String RED_BOLD = "\033[1;31m"; // RED
	public static final String GREEN_BOLD = "\033[1;32m"; // GREEN
	public static final String YELLOW_BOLD = "\033[1;33m"; // YELLOW
	public static final String BLUE_BOLD = "\033[1;34m"; // BLUE
	public static final String PURPLE_BOLD = "\033[1;35m"; // PURPLE
	public static final String CYAN_BOLD = "\033[1;36m"; // CYAN
	public static final String WHITE_BOLD = "\033[1;37m"; // WHITE

	// Underline
	public static final String BLACK_UNDERLINED = "\033[4;30m"; // BLACK
	public static final String RED_UNDERLINED = "\033[4;31m"; // RED
	public static final String GREEN_UNDERLINED = "\033[4;32m"; // GREEN
	public static final String YELLOW_UNDERLINED = "\033[4;33m"; // YELLOW
	public static final String BLUE_UNDERLINED = "\033[4;34m"; // BLUE
	public static final String PURPLE_UNDERLINED = "\033[4;35m"; // PURPLE
	public static final String CYAN_UNDERLINED = "\033[4;36m"; // CYAN
	public static final String WHITE_UNDERLINED = "\033[4;37m"; // WHITE

	// Background
	public static final String BLACK_BACKGROUND = "\033[40m"; // BLACK
	public static final String RED_BACKGROUND = "\033[41m"; // RED
	public static final String GREEN_BACKGROUND = "\033[42m"; // GREEN
	public static final String YELLOW_BACKGROUND = "\033[43m"; // YELLOW
	public static final String BLUE_BACKGROUND = "\033[44m"; // BLUE
	public static final String PURPLE_BACKGROUND = "\033[45m"; // PURPLE
	public static final String CYAN_BACKGROUND = "\033[46m"; // CYAN
	public static final String WHITE_BACKGROUND = "\033[47m"; // WHITE

	// High Intensity
	public static final String BLACK_BRIGHT = "\033[0;90m"; // BLACK
	public static final String RED_BRIGHT = "\033[0;91m"; // RED
	public static final String GREEN_BRIGHT = "\033[0;92m"; // GREEN
	public static final String YELLOW_BRIGHT = "\033[0;93m"; // YELLOW
	public static final String BLUE_BRIGHT = "\033[0;94m"; // BLUE
	public static final String PURPLE_BRIGHT = "\033[0;95m"; // PURPLE
	public static final String CYAN_BRIGHT = "\033[0;96m"; // CYAN
	public static final String WHITE_BRIGHT = "\033[0;97m"; // WHITE

	// Bold High Intensity
	public static final String BLACK_BOLD_BRIGHT = "\033[1;90m"; // BLACK
	public static final String RED_BOLD_BRIGHT = "\033[1;91m"; // RED
	public static final String GREEN_BOLD_BRIGHT = "\033[1;92m"; // GREEN
	public static final String YELLOW_BOLD_BRIGHT = "\033[1;93m";// YELLOW
	public static final String BLUE_BOLD_BRIGHT = "\033[1;94m"; // BLUE
	public static final String PURPLE_BOLD_BRIGHT = "\033[1;95m";// PURPLE
	public static final String CYAN_BOLD_BRIGHT = "\033[1;96m"; // CYAN
	public static final String WHITE_BOLD_BRIGHT = "\033[1;97m"; // WHITE

	// High Intensity backgrounds
	public static final String BLACK_BACKGROUND_BRIGHT = "\033[0;100m";// BLACK
	public static final String RED_BACKGROUND_BRIGHT = "\033[0;101m";// RED
	public static final String GREEN_BACKGROUND_BRIGHT = "\033[0;102m";// GREEN
	public static final String YELLOW_BACKGROUND_BRIGHT = "\033[0;103m";// YELLOW
	public static final String BLUE_BACKGROUND_BRIGHT = "\033[0;104m";// BLUE
	public static final String PURPLE_BACKGROUND_BRIGHT = "\033[0;105m"; // PURPLE
	public static final String CYAN_BACKGROUND_BRIGHT = "\033[0;106m"; // CYAN
	public static final String WHITE_BACKGROUND_BRIGHT = "\033[0;107m"; // WHITE

	/// design language colours
	public static final String COLOUR_PLAYER = RED_BOLD_BRIGHT;
	public static final String COLOUR_OTHERPLAYER = RED_BOLD;
	public static final String COLOUR_INPUT = YELLOW;
	public static final String COLOUR_OPTION = YELLOW_BRIGHT;
	public static final String COLOUR_RESOURCE = GREEN_BRIGHT;
	public static final String COLOUR_LOCATION = CYAN_BOLD;

	/// essential components

	public static Board board = new Board();

	public static final Scanner SCANNER = new Scanner(System.in);

	public static ArrayList<Player> players = new ArrayList<Player>();

	public static final String BOARD_FILE = "solaropoly-london.csv";

	/**
	 * Stores only players that are still in game
	 */
	public static ArrayList<Player> playersInGame = new ArrayList<Player>();;

	/// executable code

	/**
	 * Main method - execution starts here
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		try {

			setupBoard();

			welcome();

			/// set number of players
			int numPlayers = setNumPlayers();

			/// register players
			players = registerPlayers(numPlayers);
			playersInGame.addAll(players);

			/// TODO Replace board map with list that can accommodate other than 12 squares
//			board.visualMap();

			/// game starts - cycling through players until game
			/// end triggered
			int firstPlayerIndex = 0;
			for (int playerIndex = firstPlayerIndex; !gameEndTrigger(); playerIndex = (playerIndex + 1) % numPlayers) {

				if (playersInGame.contains(players.get(playerIndex))) {

					players.get(playerIndex).getAttention();
					Thread.sleep(1000);

					if (players.get(playerIndex).getTurns() < 0) {
						System.out.println("Number of turns to skip: " + Math.abs(players.get(playerIndex).getTurns())
								+ ". Turn skipped...");
						players.get(playerIndex).increaseTurns();
						continue;
					}

					players.get(playerIndex).increaseTurns();

					do {
						turn(players.get(playerIndex));
						players.get(playerIndex).decreaseTurns();
					} while (players.get(playerIndex).getTurns() > 0);

				}

				System.out.println(GameSystem.RESET + "Turn ended. Next player...");

			}

			/// game ending
			System.out.println("\nGAME OVER!\nAssets accumulated:");

			/// TODO put this into a method so it can also be called when players leave
			/// early
			for (Player player : players) {

				int propertyValue = 0;

				for (Square area : player.getOwnedSquares()) {
					propertyValue += ((Area) area).getCost();
				}

				System.out.printf(
						"%s%s%s has %s%s%,d%s%s and owns %s%s%,d%s%s of assets %s for a total of %s%s%,d%s%s.%n",
						COLOUR_PLAYER, player.getName(), RESET, COLOUR_RESOURCE, PRE, player.getBalance(), SUF, RESET,
						COLOUR_RESOURCE, PRE, propertyValue, SUF, RESET, player.getOwnedSquares().toString(),
						COLOUR_RESOURCE, PRE, player.getBalance() + propertyValue, SUF, RESET);

			}

			System.out.println("Thank you for playing SOLAROPOLY");

		} catch (InterruptedException e) {

			System.out.println("Sorry, the sleep was interrupted and the softwear engine crashed.");
			e.printStackTrace();

		} catch (Exception e) {

			System.out.println("Sorry, something went wrong and the softwear engine crashed.");
			e.printStackTrace();

		}

	}

	private static void welcome() {

		System.out.println(WHITE_BOLD + "\n    Welcome to SOLAROPOLY!    \n\n" + RESET
				+ "In this game, you'll each take the role of a solar energy startup competing for "
				+ "space to set up your infrastructure production facilities and solar farms. \n\n"
				+ "Starting the game with " + COLOUR_RESOURCE + PRE + String.format("%,d", startingBalance) + SUF
				+ RESET + " (Green Energy Tokens), "
				+ "the goal is to maximise energy production among all players. But the player whose "
				+ "production tips the total energy capture over " + COLOUR_RESOURCE + PRE
				+ String.format("%,d", productionGoal) + SUF + RESET
				+ " recieves a legendary commemorative ScamCoin! So compete and collaborate wisely.\n");

	}

	/**
	 * Reads game, square and group parameters from csv. The csv should include
	 * Group data before Square data. The read order of Game parameters and Cards
	 * doesn't matter.
	 */
	private static void setupBoard() {

		ArrayList<Square> squares = new ArrayList<Square>(14);
		ArrayList<Group> groups = new ArrayList<Group>(4);
		ArrayList<Card> cards = new ArrayList<Card>();

		File file = new File(BOARD_FILE);

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {

			String line;
			br.readLine();
			line = br.readLine();
			int counter = 1;

			while (line != null) {

				String[] data = line.split(",");

				// switch on datatype
				switch (data[0]) {
				case "Game":

					switch (data[3]) {

					case "startingBalance":

						startingBalance = Integer.parseInt(data[4]);
						break;

					case "productionGoal":

						productionGoal = Integer.parseInt(data[4]);
						break;

					default:

						System.err.println(
								"Line " + counter + " (Game) in board setup csv skipped due to invalid \"name\" value");

					}
					break;

				case "Group":

					String groupName = data[3];
					int minorDevCost = Integer.parseInt(data[5]);
					int majorDevCost = Integer.parseInt(data[9]);

					groups.add(new Group(groupName, minorDevCost, majorDevCost));
					break;

				case "Square":

					switch (data[2]) {

					case "Area":

						String areaName = data[3];
						int cost = Integer.parseInt(data[4]);
						int baseRent = Integer.parseInt(data[5]);
						int oneDev = Integer.parseInt(data[6]);
						int twoDev = Integer.parseInt(data[7]);
						int threeDev = Integer.parseInt(data[8]);
						int majorDev = Integer.parseInt(data[9]);
						int groupIndex = Integer.parseInt(data[10]);

						int[] monopolyProfile = { baseRent, baseRent * 2 };
						int[] developmentProfile = { baseRent, oneDev, twoDev, threeDev, majorDev };

						Area area = new Area(areaName, groups.get(groupIndex), cost, monopolyProfile,
								developmentProfile);
						squares.add(area);
						break;

					case "Sunrise":

						squares.add(new Sunrise(data[3], Integer.parseInt(data[4])));
						break;

					case "Holiday":

						squares.add(new Holiday(data[3]));
						break;

					case "Failure":

						squares.add(new Failure(data[3], Integer.parseInt(data[4])));
						break;

					case "Event":

						// TODO add cards
						squares.add(new Event(data[3], null));
						break;

					default:

						System.err.println("Line " + counter
								+ " (Square) in board setup csv skipped due to invalid \"type\" value");

					}

					break;

				case "Card":

					String eventText = data[3];
					int move = Integer.parseInt(data[11]);
					int earn = Integer.parseInt(data[12]);
					int turns = Integer.parseInt(data[13]);

					cards.add(new Card(eventText, move, earn, turns));

					break;

				default:

					System.err.println("Line " + counter + " in board setup csv skipped due to invalid \"data\" value");

				}

				line = br.readLine();
				counter++;

			}

			board.setSquares(squares);

			for (Group group : groups) {
				group.setAreas(squares);
			}

			board.setGroups(new HashSet<Group>(groups));

			for (Square square : squares) {
				if (square instanceof Event) {
					((Event) square).addCards(cards);
				}
			}

		} catch (FileNotFoundException e) {

			System.out.println("File not found - check BOARD_FILE in Game.java");
			e.printStackTrace();

		} catch (IOException e) {

			System.out.println("Input exception - check csv file for issues");
			e.printStackTrace();

		}

	}

	/**
	 * Takes user input at the start of the game to set the number of players whose
	 * names will be registered.
	 * 
	 * @return the number of players
	 */
	private static int setNumPlayers() {

		int num = 0;

		while (num < MIN_PLAYERS || num > MAX_PLAYERS) {

			System.out.println(RESET + "How many are playing? Type a number between " + COLOUR_OPTION + MIN_PLAYERS
					+ RESET + " and " + COLOUR_OPTION + MAX_PLAYERS + RESET + " and press Enter." + COLOUR_INPUT);

			try {

				num = SCANNER.nextInt();
				if (num < MIN_PLAYERS || num > MAX_PLAYERS)
					System.out.println(RESET + "Sorry, invalid number.");

			} catch (InputMismatchException e) {

				System.out.println(RESET + "Sorry, need a whole number." + COLOUR_INPUT);
				SCANNER.nextLine();

			} catch (Exception e) {

				System.out.println(RESET + "Sorry, try again." + COLOUR_INPUT);

			}

		}

		players = new ArrayList<Player>(num);
		return num;

	}

	/**
	 * Registers players by name, including validation
	 * 
	 * @return an ArrayList of Players
	 */
	private static ArrayList<Player> registerPlayers(int numPlayers) {

		ArrayList<Player> playersBuilder = new ArrayList<Player>();
		ArrayList<String> names = new ArrayList<>();
		boolean resolved;
		SCANNER.nextLine(); /// not sure why I need this

		for (int playerNum = 1; playerNum <= numPlayers; playerNum++) {

			resolved = false;

			while (!resolved) {

				try {

					System.out.printf("%n%sPlayer %d, please enter your name: %s", RESET, playerNum, COLOUR_INPUT);

					String name = SCANNER.nextLine();

					if (names.contains(name)) {

						System.out.printf(
								"%sSorry, %s, players need unique names - maybe call yourself 'Funky %s'?%n%s", RESET,
								name, name, COLOUR_INPUT);

					} else {

						playersBuilder.add(new Player(name, startingBalance, 0));
						names.add(name);
						resolved = true;
						System.out.printf("%sWelcome %s%s%s! You start the game with a balance of %s%s%,d%s%s.%n",
								RESET, COLOUR_PLAYER, playersBuilder.get(playerNum - 1).getName(), RESET,
								COLOUR_RESOURCE, PRE, startingBalance, SUF, RESET);

					}

				} catch (Exception e) {

					System.out.println(RESET + "Sorry, that wasn't a valid name." + COLOUR_INPUT);
					e.printStackTrace();
				}

			}

		}

		System.out.println(RESET + "Right everyone, let's go catch some rays!");

		return playersBuilder;
	}

	/**
	 * This method is called for each player in turn from the main method, and
	 * organizes the inputs and results that will be a part of their turn, including
	 * (1) deciding whether to abandon the game or roll and move; (2) developing any
	 * fields they have monopolized; and (3) trading assets with other players
	 * 
	 * @param player
	 */
	private static void turn(Player player) {

		boolean consent = consent(player);

		if (consent) {

			// TODO Replace map with list that can accommodate other than 12 Squares
//			board.visualMap();

			int roll = rollDice(player);

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}

			player.move(roll);

		} else {

			playersInGame.remove(player);

			System.out.println(RESET + "You quit the game - all your properties will now be made available.");
			// TODO not handling a player leaving the game
			for (Square square : player.getOwnedSquares()) {
				((Area) square).removeOwnership(player);
			}

		}

	}

	/**
	 * This method is called before each turn to ask the player if they want to
	 * continue or leave the game.
	 * 
	 * @param player - the player that needs to consent the turn
	 * @return boolean - the decision
	 */
	private static boolean consent(Player player) {
		player.displayBalance();
		String input = "";
		System.out.printf(
				"%sIf you would like to take your turn, press Enter.%n"
						+ "Otherwise, type %sQuit%s and press Enter to leave the game. %n%s",
				RESET, COLOUR_OPTION, RESET, COLOUR_INPUT);

		while (true) {
			input = GameSystem.SCANNER.nextLine();

			if (input.equalsIgnoreCase("Quit") || input.equalsIgnoreCase("")) {
				break;
			} else {
				System.out.printf("%sWrong input - please either type %sQuit%s or press Enter to continue:%s", RESET,
						COLOUR_OPTION, RESET, COLOUR_INPUT);
			}
		}

		if (input.equals("")) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * @author andrewscott
	 * 
	 * @param player
	 */
	public static void developArea(Player player) {
		boolean groupStatus = true;
		boolean areaStatus = true;
		boolean isGroupFound = false;
		ArrayList<Area> fullyDevelopedAreaStatus = new ArrayList<Area>();
		try {
			if (player.getOwnedGroups().size() > 0) {

				do {
					displayMenu(player);
					System.out.println();
					System.out.println(
							"If you wish to develop within a group please enter the group name, else, to quit developing press Enter");
					String inputGroup = SCANNER.nextLine();

					if (inputGroup == null || inputGroup == "") {
						System.out.println("System recognises you do not wish to develop");
						groupStatus = false;
					} else {

						for (Group group : player.getOwnedGroups()) {
							try {
								if (group.getName().equalsIgnoreCase(inputGroup)) {
									do {

										System.out.println(
												"Please enter which area you would like to develop, else, to develop another area or quit press Enter ");
										String inputArea = SCANNER.nextLine();

										if (inputArea == null || inputArea == "") {
											System.out.println("System recognises you no longer wish to develop this area\n");
											break;

										} else {
											// developing areas loop
											for (Square square : player.getOwnedSquares()) {
												try {
													if (square.getName().equalsIgnoreCase(inputArea) && ((Area) square).getGroup().equals(group)) {

														Area area;
														try {
															if (square instanceof Area) {
																area = (Area) square;
																try {
																	if (group.canAreaBeDeveloped(area, group)) {

																		if (area.getDevelopmentLevel() < 3) {
																			try {
																				if (player.getBalance() >= area
																						.getGroup()
																						.getMinorDevelopmentCost()) {
																					area.incrementDevelopmentLevel();
																					player.decreaseBalance(area.getGroup().getMinorDevelopmentCost());
																					System.out.println(area.getName()
																							+ " developed. Development level: "
																							+ area.getDevelopmentLevel()+"\nUpdated player balance: "+player.getBalance());
																					break;

																				}
																			} catch (Exception e5) {
																				System.out
																						.println("Insufficient funds");
																			}

																		} else if (area.getDevelopmentLevel() == 3) {
																			try {
																				if (player.getBalance() >= area
																						.getGroup()
																						.getMajorDevelopmentCost()) {
																					area.incrementDevelopmentLevel();
																					player.decreaseBalance(area.getGroup().getMajorDevelopmentCost());
																					System.out.println(
																							"Major development achieved. "
																									+ area.getName()
																									+ " developed "
																									+ area.getDevelopmentLevel()
																									+ " times\nUpdated player balance: "+player.getBalance());
																					fullyDevelopedAreaStatus.add(area);
																					break;
																				}
																			} catch (Exception e4) {
																				System.out
																						.println("Insufficient funds");
																			}

																		} else if(area.getDevelopmentLevel()==4){
																			System.out.println("Area fully developed, please enter a different area.");
																			break;
																			
																		} else {
																		
																			throw new IllegalArgumentException(
																					"Error: not able to develop area.");

																		}

																	} else {
																		throw new IllegalArgumentException(
																				"Area cannot be developed\n");
																	}
																} catch (Exception e3) {
																	System.out
																			.println("Areas must be developed equally");
																}

															} else {
																throw new IllegalArgumentException("Not an area\n");
															}
														} catch (IllegalArgumentException e2) {
															System.out.println("Please enter a square that is an area");
														}

													} else {
														throw new IllegalArgumentException("Not an area owned\n");
													}
													// printing 5 times??? if not area
													// does this when insufficient funds as well
													// also when ever i want to develop area
													// also when ever i have a fully developed group
												} catch (IllegalArgumentException e1) {
													System.out.println("Please enter an area that you own");
												}

											}
											
											if (fullyDevelopedAreaStatus.size() == player.getOwnedSquares().size()) {
												System.out.println("All areas owned are fully developed.");
												break;
											}

										}

									} while (true);
									// TODO this messages appears when group not looking for is passed through
								 
								} else if (!group.getName().equalsIgnoreCase(inputGroup)){
									
									isGroupFound = true;
									throw new IllegalArgumentException("Invalid group name\n");
								}
							} catch (Exception e0) {
								System.out.println( "Please enter an group that you own");
								
							}
						}
					}

				} while (groupStatus != false);
			} else {
				throw new IllegalArgumentException("No groups owned\n");
			}
			
		} catch (IllegalArgumentException e01) {
			System.out.println(e01 + "You do not own any groups.");
		}
	}

	/**
	 * generates the menu using the getmenuitems method
	 * 
	 * @author andrewscott
	 * @param player
	 */
	private static void displayMenu(Player player) {

		System.out.println("Player balance: $" + player.getBalance());
		System.out.format("%-15s%-15s%-15s%s%n", "Field", "Square", "Level", "Upgrade Cost");

		List<MenuItem> menuItems = getMenuItems(player);

		for (MenuItem menuItem : menuItems) {
			System.out.format("%-15s%-15s%-15s$%d%n", menuItem.getField(), menuItem.getSquare(), menuItem.getLevel(),
					menuItem.getUpgradeCost());
		}

	}

	/**
	 * generates a each row for each property owned by each player during
	 * development to see their owned properties and associated costs with
	 * development
	 * 
	 * @author andrewscott
	 * @param player
	 * @return
	 */
	public static List<MenuItem> getMenuItems(Player player) {

		List<MenuItem> menuItems = new ArrayList<>();

		int playerBalance = player.getBalance();

		for (Group group : player.getOwnedGroups()) {
			
			for (Square square : player.getOwnedSquares()) {

				if (square instanceof Area && ((Area) square).getGroup().equals(group)) {
					Area area = (Area) square;
					String groupName = group.getName();
					String squareName = square.getName();
					String developmentLevel = "";
					int developmentCost; // if costs are in an array, can iterate through them for each cost

					if (player.getOwnedSquares().contains(area)) {
						developmentLevel = Integer.toString(area.getDevelopmentLevel());

						if (area.getDevelopmentLevel() < 3) {
							developmentCost = area.getGroup().getMinorDevelopmentCost();
							menuItems.add(new MenuItem(groupName, squareName, developmentLevel, developmentCost));
						} else if (area.getDevelopmentLevel() == 3) {
							developmentCost = area.getGroup().getMajorDevelopmentCost();
							menuItems.add(new MenuItem(groupName, squareName, developmentLevel, developmentCost));
						}  else if (area.getDevelopmentLevel() == 4) {
							developmentCost = area.getGroup().getMajorDevelopmentCost();
							menuItems.add(new MenuItem(groupName, squareName, developmentLevel, 0));
						}
						
					
						
					}

				

				} else if (square instanceof Area && !((Area) square).getGroup().equals(group)) {
					continue;
				} else {
					// so this is basically if someone owns a square thats like a train station
					// so not sure it has a group or a development level etc?

					String squareName = square.getName();

					menuItems.add(new MenuItem("", squareName, "", 0));
				}
			}
		}

		return menuItems;
	}

	/**
	 * 
	 * @author andrewscott class to store the menu item objects
	 */
	private static class MenuItem {
		private String field;
		private String square;
		private String level;
		private int upgradeCost;

		// contructor with args
		public MenuItem(String field, String square, String level, int upgradeCost) {
			this.field = field;
			this.square = square;
			this.level = level;
			this.upgradeCost = upgradeCost;
		}

		// getters and setters
		public String getField() {
			return field;
		}

		public String getSquare() {
			return square;
		}

		public String getLevel() {
			return level;
		}

		public int getUpgradeCost() {
			return upgradeCost;
		}
	}

	/**
	 * rollDice method called from turn method. imitates 2 dice.
	 */
	private static int rollDice(Player player) {

		Die die1 = new Die(), die2 = new Die();

		int rollA = die1.roll();
		int rollB = die2.roll();

		int total = rollA + rollB;

		System.out.printf("%s%s%s, you've rolled a %d and a %d for %d total.%n", COLOUR_PLAYER, player.getName(), RESET,
				rollA, rollB, total);

		return total;

	}

	/**
	 * Game end trigger - called before every turn
	 * 
	 * @return
	 */
	public static boolean gameEndTrigger() {

		// trigger game end if only one player
		if (players.size() < 2 || playersInGame.size() < 2)
			return true;

		// trigger game end if productionGoal reached
		int totalResource = 0;
		for (Player player : players)
			totalResource += player.getBalance();
		return (totalResource >= productionGoal);

	}

}
