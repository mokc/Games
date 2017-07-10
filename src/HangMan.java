import java.io.*;
import java.util.*;

public class HangMan {
	public static final int NUM_WORDS = 69318;
	public static final int GUESSES_LEFT = 6;
	
	public static void main(String[] args) throws FileNotFoundException {
		final String word = selectWord();
		final int guessesLeft = guess(word);
		endGame(guessesLeft, word);
	}
	
	// the outro; states if you've won or lost
	public static void endGame(final int guessesLeft, final String word) {
		if(guessesLeft > 0) { 
			System.out.println("Congrats! You've guessed the word - " + word + "!");
		} else { 
			System.out.println("Game Over. You've ran out of guesses. The word was " + word + "."); 
		}
	}
	
	// while user has guesses remaining, ask for input and update status lines; 
	// returns # of guesses left
	public static int guess(final String word) {
		final Scanner input = new Scanner(System.in);		// to ask for user input
		int guessesLeft = GUESSES_LEFT;						// tracks # of remaining guesses
		StringBuilder wrongGuesses = new StringBuilder();	// tracks incorrect letters
		String status = drawLines(word);					// draws initial lines
		
		// while there are guesses remaining & status doesn't contain _, keep guessing & updating
		while (guessesLeft > 0 && status.contains("_")) {
			System.out.println(status);				
			System.out.println("Remaining number of guesses: " + guessesLeft + "\nIncorrectly " +
					"guessed: " + wrongGuesses + "\nType a letter: ");
			String guess = input.next();
			System.out.println();

			// ensures input is only one letter; keep asking until one letter
			while(guess.length() != 1) {
				System.out.println("Incorrect format. Try again.");
				guess = input.next();
			}

			// fencepost issue for list of incorrectly guessed letters
			if(!word.contains(guess)) {
				guessesLeft--;
				if (wrongGuesses.length() == 0) {
					wrongGuesses.append(guess);
				} else {
					wrongGuesses.append(", ");
					wrongGuesses.append(guess);
				}
			}
			status = updateLines(word, status, guess);		// update status
		}
		return guessesLeft;
	}
	
	// draws initial lines
	public static String drawLines(final String word) {
		StringBuilder lines = new StringBuilder();		// to be returned
		
		// adds _ to lines for each letter in word w/ space inbetween each letter
		for(int i = 0; i < word.length(); i++) {
			if(word.charAt(i) == '-') {
				lines.append(" - ");
			} else {
				lines.append("_ ");
			}
		}		
		return lines.toString();
	}
	
	// draws lines of letters to be guessed, excludes hyphens
	public static String updateLines(String word, String status, final String guess) {
		// while word still contains the guessed letter, update status
		while(word.contains(guess)) {
			final int indexOfGuess = word.indexOf(guess);			// finds index of first guessed letter
			final int wordToStatusIndex = indexOfGuess * 2;			// converts to index for status
																	// status has 2x length than word
			// updates status
			status = status.substring(0, wordToStatusIndex) + guess +
					 status.substring(wordToStatusIndex + 1, status.length());
			
			// replaces guess from word with empty space (maintains original indexing)
			word = word.substring(0, indexOfGuess) + " " + word.substring(indexOfGuess + 1);
		}
		return status;
	}
	
	// selects the word from file of words and returns it
	public static String selectWord() throws FileNotFoundException {
		final File file = new File("WordList");		// to access file
		final Scanner scan = new Scanner(file);				// to scan file
		final Random rand = new Random();					// to generate random #
		int line = rand.nextInt(NUM_WORDS) + 1;				// picks random #
		for(int i = 0; i < line - 1; i++) {					// gets to the "lineth" word
			scan.next();
		}
		final String word = scan.next();					// saves word in variable
		return word;
	}
}
