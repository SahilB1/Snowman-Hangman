import acm.program.*;
import acm.util.*;
import java.io.*;// for File
import java.util.*;// for Scanner

import com.sun.prism.paint.Color;

public class Snowman extends SnowmanProgram {
	
	// Done by: Raven O'kennedy & Sahil Bagga
	
	// This program will play a game against the user that is called Snowman.
	// The user will input guesses of a secret word and try to guess it.
	// The user will have a certain number of guesses and will be told when they get a letter in the word.
	// Also the number of letters will be displayed on the screen, and when a correct guess is entered 
	// they will be notified and the letter will fill the blank space in the word.
	// When a wrong guess is entered a part of a snowman will come up, until the entire snowman is built with enough wrong guesses.
	public void run() {
		intro();
		println();
		String theRandomWord = "";
		playOneGame(getRandomWord(theRandomWord));
	}

	// This method is called intro.
	// It will print out a introductory message that helps the user understand how to play the game "Snowman".
	// It does this by using println.
	private void intro() {
		println("CS 106A Snowman!");
		println("I will think of a random word.");
		println("You'll try to guess its letters.");
		println("Every time you guess a letter");
		println("that isn't in my word, a new");
		println("piece of the snowman appears.");
		println("Guess correctly to avoid");
		println("bringing him to life in the sun!");
	}

	// Initializes an variable to an empty string and calls createHint 
	// so that the user will start with no previous guesses
	private int playOneGame(String secretWord) {
		String guessedLetters = "";
		createHint(secretWord, guessedLetters);
		return 0;
	}

	// createHint is bearing most of the weight in our program. 
	// We initialize many variables outside of the while loop so that we can modify their values
	// without them reinitializing every time the loop runs.
	// This method outputs the status of the user's guesses and shows them how close they are to 
	// guessing the secret word.
	private String createHint(String secretWord, String guessedLetters) {
		String lines = "";
		String theRandomWord = "";
		int numGuesses = 8;
		displaySnowman(numGuesses);
		int gamesPlayed = 0;
		int gamesWon = 0;
		int bestNumGuessesRemaining = 0;
		gamesPlayed++;
		String result = guessedLetters;
		String actResult = guessedLetters;
		String secretWordUpperCase = secretWord.toUpperCase();
		for(int i = 0; i < secretWord.length(); i++) {
			lines += "-";
		}
		while(lines.contains("-") && numGuesses > 0) {
			println("#######");
			println("Secret word: " + lines);
			println("Your guesses: " + result);
			println("Guesses left: " + numGuesses);
			result += readGuess(guessedLetters);
			String userCharToString = String.valueOf(result.charAt(result.length() - 1));
			if(!secretWordUpperCase.contains(userCharToString) && !actResult.contains(userCharToString)) {
				actResult += result;
				println("Incorrect.");
				println();
				numGuesses--;
				displaySnowman(numGuesses);
			}
			else if(secretWordUpperCase.contains(userCharToString) && !actResult.contains(userCharToString)) {
				actResult += result;
				println("Correct!");
				println();
			} else {
				for(int i = 0; i < result.length(); i++) {
					for(int j = 0; j < result.length(); j++) {
						if(i == j) {
							break;
						}
						if(result.charAt(j) == result.charAt(i)) {
							result = result.substring(0, result.length() - 1);
							println("You already guessed that letter");
							break;
						}

						else {
							result = result.substring(0, result.length());
						}
					}
				}
			}
			for(int i = 0; i < secretWord.length(); i++) {
				for(int j = 0; j < result.length(); j++) {
					if(result.charAt(j) == secretWordUpperCase.charAt(i)) {
						lines = lines.substring(0, i) + result.charAt(j) + lines.substring(i + 1, lines.length());
					}
				}
			}
		}
		if(!lines.equals(secretWord)) {
			println("You lose! My word was " + "\"" + secretWordUpperCase + "\".");
			if(numGuesses > bestNumGuessesRemaining) {
				bestNumGuessesRemaining = numGuesses;
			}
			if(readBoolean("Run again? ", "y", "n")) {
				playOneGame(getRandomWord(theRandomWord));
			} else {
				stats(gamesPlayed, gamesWon, bestNumGuessesRemaining);
			}
		}
		else {
			println("You win! My word was " + "\"" + lines + "\".");
			if(numGuesses > bestNumGuessesRemaining) {
				gamesWon++;
				bestNumGuessesRemaining = numGuesses;
			}
			if(readBoolean("Run again? ", "y", "n")) {
				playOneGame(getRandomWord(theRandomWord));
			} else {
				stats(gamesPlayed, gamesWon, bestNumGuessesRemaining);
			}
		}
		return(lines);
	}

	// Asks the user for their guess and checks to see and confirm that
	// what the user enters is in fact a single letter.
	private String readGuess(String guessedLetters) {
		String userInput = readLine("Your guess? ");
		while(userInput.length() != 1 || !Character.isLetter(userInput.charAt(0))) {
			println("Type a single letter from A-Z.");
			userInput = readLine("Your guess? ");
		}
		guessedLetters = userInput.toUpperCase();
		return(guessedLetters);
	}


	// Automatically displays one of the display.txt files based on the current status of the users
	// guesses remaining.
	private void displaySnowman(int guessCount) {
		try {
			canvas.clear();
			Scanner input = new Scanner(new File("res", "display" + guessCount + ".txt"));
			while (input.hasNextLine()){
				String filename = input.nextLine();
				canvas.println(filename);
			}
			guessCount--; 
		} catch(FileNotFoundException ex) {
			println("Error reading the file: " + ex);
		}
	}

	// Shows the user their statistics for a game.
	private void stats(int gamesCount, int gamesWon, int best) {
		println("Overall statistics:");
		println("Games played: " + gamesCount);
		println("Games won: " + gamesWon);
		println("Win percentage: " + ((gamesWon / gamesCount) * 100) + "%");
		println("Best game: " + best + " guess(es) remaining");
		println("Thanks for playing!");
	}

	// Prompts the user for a file name and attempts to retrieve that file and select a random word
	// by reading a random number of lines that's within the range of the file's length and returning that
	// random word.
	private String getRandomWord(String filename) {
		int tries = 0;
		while(tries == 0) {
			try {
				String userFilename = readLine("Dictionary file name? ");
				println();
				File acceptFile = new File("res", userFilename);
				Scanner randomWord = new Scanner(acceptFile);
				String numberLinesInFile = randomWord.nextLine();
				int numberLinesInFileInt = Integer.valueOf(numberLinesInFile);
				for(int i = 0; i < Math.random() * numberLinesInFileInt; i++) {
					filename = randomWord.nextLine();
				}
				tries++;
			} catch(FileNotFoundException ex) {
				println("Unable to open that file. Try again.");
			}
		}
		return(filename);
	}
}