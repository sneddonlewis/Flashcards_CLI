package flashcards;

import flashcards.repository.InMemoryCardRepository;

import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){

        Scanner scanner = new Scanner(System.in);
        Logger logger = new Logger(new LinkedList<>());
        UserInputOutput io = new UserInputOutput(logger, scanner);
        Options options = Options.fromArgs(args);

        var ui = new CommandLineInterface(
                io,
                new InMemoryCardRepository(),
                options
        );

        ui.start();
    }
}
