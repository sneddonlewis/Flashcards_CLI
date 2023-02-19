package flashcards.configuration;

import java.util.Optional;

public class Options {
    /**
     * The name of the file that the Flashcards will be
     * loaded from on program start.
     */
    private final String importFile;

    /**
     * The name of the file that the Flashcards will be
     * persisted to on program exit.
     */
    private final String exportFile;

    private Options(String importFile, String exportFile) {
        this.importFile = importFile;
        this.exportFile = exportFile;
    }

    // TODO handle index oob ex for bad cli args
    public static Options fromArgs(String[] args) {
        String importTemp = null;
        String exportTemp = null;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-import")) {
                importTemp = args[i + 1];
            }
            if (args[i].equals("-export")) {
                exportTemp = args[i + 1];
            }
        }
        return new Options(importTemp, exportTemp);
    }

    public Optional<String> getExportFile() {
        return exportFile == null ? Optional.empty() : Optional.of(exportFile);
    }

    public Optional<String> getImportFile() {
        return importFile == null ? Optional.empty() : Optional.of(importFile);
    }
}
