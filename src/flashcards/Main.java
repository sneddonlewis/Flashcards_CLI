package flashcards;

import flashcards.configuration.Options;
import flashcards.repository.InMemoryCardRepository;
import flashcards.service.CardService;
import flashcards.service.CardServiceImpl;
import flashcards.ui.CommandLineInteraction;
import flashcards.ui.UserInputOutput;
import flashcards.util.Logger;

import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){

        Scanner scanner = new Scanner(System.in);
        Logger logger = new Logger(new LinkedList<>());
        UserInputOutput io = new UserInputOutput(logger, scanner);
        Options options = Options.fromArgs(args);
        CardService service = new CardServiceImpl(
                new InMemoryCardRepository()
        );

        var ui = new CommandLineInteraction(
                io,
                service,
                options
        );

        ui.start();
    }
}
