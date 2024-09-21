import java.util.InputMismatchException;
import java.io.*;
import java.util.Scanner;
import java.util.Random;

public class RPSLSReview {
	
	// Game stats.
	static int gamesPlayed = 0, wins = 0, losses = 0, ties = 0;
	static Scanner input = new Scanner(System.in);



	/**
	 * Displays introduction
	 */
	static void displayIntro() {
		System.out.print("""
				Rock, Paper, Scissors, Lizard, Spock
				====================================
				Rules of the Game:
				You will choose your throw. I will choose my throw. The winner will be determined based on the the following rules:
				ROCK breaks SCISSORS and crushes LIZARD
				PAPER covers ROCK and disproves SPOCK
				SCISSORS cuts PAPER and decapitates LIZARD
				LIZARD poisons SPOCK and eats PAPER
				SPOCK breaks SCISSORS and vaporizes ROCK
				==========================================
				""");
	}
	
	/**
	 * Shows the game record
	 * @param gamesPlayed int num of games played
	 * @param wins int num wins
	 * @param losses int num losses
	 * @param ties int num ties
	 */
	static void showRecord() {
		System.out.println("Total games played: " + gamesPlayed);
		System.out.printf("Wins: %d Losses: %d Ties: %d%n%n", wins, losses, ties);
	}
	
	/**
	 * Gets player throw
	 * @return int num from 1 - 5
	 */
	static int getPlayerHand() {
		input = new Scanner(System.in);
		
		int choice = 0;
		while (true) {
			try {
				System.out.println("What do you throw: 1 = ROCK, 2 = PAPER, 3 = SCISSORS, 4 = LIZARD, 5 = SPOCK");
				System.out.print("Choice: ");
				choice = input.nextInt();
				if (choice < 1 || choice > 5) throw new InputMismatchException();
				break;
				
			} catch (InputMismatchException e) {
				System.out.println("Please enter a number from 1 - 5");
				input.nextLine();
			}
		}
		return choice;
	}

	/**
	 * Gets the computer throw
	 * @return returns random int from 1 - 5
	 */
	static int getComputerHand() {
		Random rand = new Random();
		return rand.nextInt(1, 6);
	}
	
	/**
	 * Get the result for the player
	 * @param playerThrow 	int player choice
	 * @param computerThrow int computer choice
	 * @return string letter showing result
	 */
	static String getResult(int playerThrow, int computerThrow) {

		if (playerThrow == computerThrow) return "t";
		else if ((playerThrow == 1 && (computerThrow == 3 || computerThrow == 4)) ||
				(playerThrow == 2 && (computerThrow == 1 || computerThrow == 5)) ||
				(playerThrow == 3 && (computerThrow == 2 || computerThrow == 4)) ||
				(playerThrow == 4 && (computerThrow == 5 || computerThrow == 2)) ||
				(playerThrow == 5 && (computerThrow == 3 || computerThrow == 1))
				) return "w";
		else return "l";
	}
	
	/**
	 * Gets the string for an action
	 * @param choice int choice of throw
	 * @return return the string version of the thrown action
	 */
	static String getStringThrow(int choice) {
		if (choice == 1) return "ROCK";
		else if (choice == 2) return "PAPER";
		else if (choice == 3) return "SCISSORS";
		else if (choice == 4) return "LIZARD";
		else return "SPOCK";
	}
	
	/**
	 * Updates txt file with game statistics
	 * @param file File object to alter
	 * @param gamesPlayed int gamesplayed
	 * @param wins int number of wins
	 * @param losses int number of losses
	 * @param ties int number of ties
	 */
	static void updateFile(File file) {
		try {
			FileWriter fout = new FileWriter(file);
			BufferedWriter writer = new BufferedWriter(fout);
			
			writer.write(String.valueOf(gamesPlayed));
			writer.newLine();
			writer.write(String.valueOf(wins));
			writer.newLine();
			writer.write(String.valueOf(losses));
			writer.newLine();
			writer.write(String.valueOf(ties));
			
			writer.close();
			fout.close();
		} catch (IOException e) {
			System.out.println("IO Exception: " + e.getMessage());
			
		}
	}
	
	/**
	 * Creates a game record if there is no file already, or updates game stats with previous numbers
	 * @param file File object to create a file for
	 */
	static void createFile(File file) {
		
		if (file.exists()) {
			while(true) {
				try {
					System.out.println("Do you want to load old data [l] or start over [s]? ");
					String choice = input.next();
					if (choice.equals("l")) break;
					else if (choice.equals("s")) {
						updateFile(file);
						return;
					} else throw new InputMismatchException();
					
				} catch (InputMismatchException e) {
					System.out.println("Please enter \"l\" or \"s\". ");
					input.nextLine();
				}
				
			}		
			
			try {
				FileReader fin = new FileReader(file);
				BufferedReader reader = new BufferedReader(fin);
				gamesPlayed = Integer.parseInt(reader.readLine());
				wins = Integer.parseInt(reader.readLine());
				losses = Integer.parseInt(reader.readLine());
				ties = Integer.parseInt(reader.readLine());
				
				reader.close();
				fin.close();
			} catch (FileNotFoundException e) {
				System.out.println("File not found: " + e.getMessage());
			} catch (IOException e) {
				System.out.println("IO Exception: " + e.getMessage());
			} catch (NumberFormatException e) {
				
			}
		} else {
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.out.println("Error opening file!");
				System.out.println("IO Exception: " + e.getMessage());
			}
		}
	}
	
	public static void main(String[] args) {
		
		File file = new File("rpslsRecord");
		
		int playerChoice, computerChoice;
		String stringComputer, stringPlayer;
		
		displayIntro();
		createFile(file);
		
		// Main game loop
		String result;
		String playAgain;
		boolean done = false;
		while (!done) {
			System.out.println("Current Record: ");
			showRecord();
			gamesPlayed++;
			
			playerChoice = getPlayerHand();
			computerChoice = getComputerHand();
			result = getResult(playerChoice, computerChoice);
			
			stringPlayer = getStringThrow(playerChoice);
			
			stringComputer = getStringThrow(computerChoice);
			
			System.out.println(stringPlayer + " VERSUS... " + stringComputer);
			if (result.equals("t")) {
				System.out.println("It’s a tie!");
				ties++;
			}
			else if (result.equals("l")) {
				System.out.println("YOU LOSE - " + stringComputer + " beats " + stringPlayer);
				losses++;
			}
			else {
				System.out.println("You win!");
				wins++;
			}
			
			while(true) {
				try {
					
					System.out.print("Would you like to play again? [Y]es or [N]o: ");
					playAgain = input.next().toUpperCase();
					if (playAgain.equals("Y")) done = false;
					else if (playAgain.equals("N")) done = true;
					else throw new InputMismatchException();
					break;
				
				} catch(InputMismatchException e) {
					System.out.println("Please enter \"Y\" or \"N\".");
					input.nextLine();
				}
			}
		}		
		System.out.println("Thanks for playing. Here is your final record: ");
		showRecord();
		updateFile(file);
	}
}
