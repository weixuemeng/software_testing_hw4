import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class GuessNumberIOTest {
    public class DeterministicRandom extends Random {
        @Override
        public int nextInt(int bound) {
            return 29;
        }
    }
    GuessNumber guessNumber;
    private ByteArrayOutputStream output;
    private  BufferedInputStream in;
    private PrintStream printStream;

    @BeforeEach
    public void setUp(){
        guessNumber = new GuessNumber();

    }

    @AfterEach
    void restoreSystemIO() {
        System.setOut(System.out); // Restore original System.out
        System.setIn(System.in);
    }

    /*
    Possible fault: When user input is less than 1 or greater than 100, the program should
    warn the user by saying the value is less than 1 or greater than 100 instead of saying
    less than X or greater than X.
     */

    @Test
    public void testOutOfBound(){

        byte[] data = "101\n0\n30\n".getBytes(StandardCharsets.UTF_8);
        in = new BufferedInputStream(new ByteArrayInputStream(data));

        System.setIn(in);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(output);
        System.setOut(printStream);

        guessNumber.guessingNumberGame(new DeterministicRandom());


        String string = output.toString(StandardCharsets.UTF_8);
        assertEquals("A number is chosen between 1 to 100. Guess the number within 5 trials.\nGuess the number: The number is less than 100\nGuess the number: The number is greater than 1\nGuess the number: Congratulations! You guessed the number.\n"
                , string);

    }
    /*
    Failure: InputMismatchException is happening. The fault is the program didn't handle
    the case when user input is a string. The program should set a warning and keep letting
    the user to try instead of throw an exception.
     */
    @Test
    public void testString(){

        byte[] data = "50\nTEST\n30\n".getBytes(StandardCharsets.UTF_8);
        in = new BufferedInputStream(new ByteArrayInputStream(data));

        System.setIn(in);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(output);
        System.setOut(printStream);

//        assertThrows(InputMismatchException.class, ()->{guessNumber.guessingNumberGame(new DeterministicRandom());});
        guessNumber.guessingNumberGame(new DeterministicRandom());
        String string = output.toString(StandardCharsets.UTF_8);
        assertEquals("A number is chosen between 1 to 100. Guess the number within 5 trials.\nGuess the number: The number is less than 50\nGuess the number: Your guess is invalid, please provide a number between 1 and 100\nGuess the number: Congratulations! You guessed the number.\n"
                , string);
    }
    /*
    Failure:  The fault is the program didn't handle the case when user didn't enter anything and just press 'return'.
    The program should set a warning and keep letting the user try instead of stuck.
     */
    @Test
    public void testEmpty(){

        byte[] data = "\n30\n".getBytes(StandardCharsets.UTF_8);
        in = new BufferedInputStream(new ByteArrayInputStream(data));

        System.setIn(in);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(output);
        System.setOut(printStream);
        guessNumber.guessingNumberGame(new DeterministicRandom());

        String string = output.toString(StandardCharsets.UTF_8);
        assertEquals("A number is chosen between 1 to 100. Guess the number within 5 trials.\nGuess the number: Your guess is invalid, please provide a number between 1 and 100\nGuess the number: Congratulations! You guessed the number.\n"
                , string);

    }
    /*
    Failure:  The fault is the program use Scanner.nextInt()didn't handle the case when user enter a
    invalid integer.
    The program should set a warning and keep letting the user try instead of throwing an exception.
     */
    @Test
    public void testLargeNumber(){

        byte[] data = "234242342423423\n30\n".getBytes(StandardCharsets.UTF_8);
        in = new BufferedInputStream(new ByteArrayInputStream(data));

        System.setIn(in);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(output);
        System.setOut(printStream);
        guessNumber.guessingNumberGame(new DeterministicRandom());

        String string = output.toString(StandardCharsets.UTF_8);
        assertEquals("A number is chosen between 1 to 100. Guess the number within 5 trials.\nGuess the number: Your guess is invalid, please provide a number between 1 and 100\nGuess the number: Congratulations! You guessed the number.\n"
                , string);

    }

    /*
Failure:  The fault is the program use Scanner.nextInt() didn't handle the case when user enter a
double.
The program should set a warning and keep letting the user try instead of throwing an exception.
 */
    @Test
    public void testDouble(){

        byte[] data = "29.9\n30\n".getBytes(StandardCharsets.UTF_8);
        in = new BufferedInputStream(new ByteArrayInputStream(data));

        System.setIn(in);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(output);
        System.setOut(printStream);
        guessNumber.guessingNumberGame(new DeterministicRandom());

        String string = output.toString(StandardCharsets.UTF_8);
        assertEquals("A number is chosen between 1 to 100. Guess the number within 5 trials.\nGuess the number: Your guess is invalid, please provide a number between 1 and 100\nGuess the number: Congratulations! You guessed the number.\n"
                , string);

    }

    /*
    Failure:  The fault is the program use Scanner.nextInt() didn't handle the case when user enter some
    spacing.
    The program should set a warning and keep letting the user try instead of stuck.
     */

    @Test
    public void testSpace(){

        byte[] data = "   \n30\n".getBytes(StandardCharsets.UTF_8);
        in = new BufferedInputStream(new ByteArrayInputStream(data));

        System.setIn(in);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(output);
        System.setOut(printStream);
        guessNumber.guessingNumberGame(new DeterministicRandom());

        String string = output.toString(StandardCharsets.UTF_8);
        assertEquals("A number is chosen between 1 to 100. Guess the number within 5 trials.\nGuess the number: Your guess is invalid, please provide a number between 1 and 100\nGuess the number: Congratulations! You guessed the number.\n"
                , string);

    }

}

