package flashcards;

import flashcards.model.Card;

import java.io.*;
import java.util.List;

public class FileManager {
    public static List<Card> loadFromFile(String filename) throws IOException {
        try (var fileInput = new FileInputStream(filename);
             var ois = new ObjectInputStream(fileInput);
        ) {
            return (List<Card>) ois.readObject();
        } catch (ClassNotFoundException e) {
            System.out.println("Data in file in incorrect format");
            throw new RuntimeException(e);
        }
    }

    public static void saveToFile(String filename, List<Card> data) {
        try (var fileOutput = new FileOutputStream(filename);
             var oos = new ObjectOutputStream(fileOutput);
        ) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveLogToFile(String filename, List<String> log) {
        try (var fileOutput = new FileOutputStream(filename);
             var writer = new DataOutputStream(new BufferedOutputStream(fileOutput));
        ) {
            for (String logEntry : log) {
                writer.writeUTF(logEntry);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
