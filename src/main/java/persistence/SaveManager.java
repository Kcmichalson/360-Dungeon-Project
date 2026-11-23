package persistence;

import model.GameState;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import java.io.Serial;
import java.io.Serializable;


/**
 * Reads and writes named game saves.
 */
public final class SaveManager {

    private static final String EXTENSION = ".sav";

    private final Path myDirectory;

    public SaveManager() {
        this(Path.of("saves"));
    }

    public SaveManager(final Path theDirectory) {
        if (theDirectory == null) {
            throw new IllegalArgumentException(
                    "Save directory is required.");
        }

        myDirectory = theDirectory.toAbsolutePath().normalize();
    }

    private Path savePath(final String theName) {
        if (theName == null
                || !theName.matches("[A-Za-z0-9][A-Za-z0-9 _-]{0,63}")) {
            throw new IllegalArgumentException(
                    "Use 1-64 letters, numbers, spaces, underscores "
                            + "or hyphens, starting with a letter or number.");
        }

        return myDirectory.resolve(theName + EXTENSION);
    }

    /**
     * Saves a complete game under a new name.
     * An existing save with that name is preserved.
     */
    public void save(final String theName,
                     final GameState theState) throws IOException {

        final Path destination = savePath(theName);

        if (theState == null
                || theState.getHero() == null
                || theState.getDungeon() == null) {
            throw new IllegalArgumentException(
                    "A complete game state is required.");
        }

        Files.createDirectories(myDirectory);

        final Path temporary =
                Files.createTempFile(myDirectory, "pending-", ".tmp");

        try {
            try (final ObjectOutputStream output =
                         new ObjectOutputStream(
                                 Files.newOutputStream(temporary))) {
                output.writeObject(theState);
            }

            Files.move(temporary, destination);
        } finally {
            Files.deleteIfExists(temporary);
        }
    }

    /**
     * Returns a restored game only after reading it successfully.
     */
    public GameState load(final String theName) throws IOException {
        final Path source = savePath(theName);

        try (final ObjectInputStream input =
                     new ObjectInputStream(
                             Files.newInputStream(source))) {

            final Object value = input.readObject();

            if (!(value instanceof GameState state)
                    || state.getHero() == null
                    || state.getDungeon() == null) {
                throw new IOException(
                        "The file does not contain a complete saved game.");
            }

            return state;

        } catch (final ClassNotFoundException exception) {
            throw new IOException(
                    "The saved game uses an unavailable class.",
                    exception);
        }
    }

    public List<String> listSaves() throws IOException {
        if (!Files.exists(myDirectory)) {
            return List.of();
        }

        try (final Stream<Path> files = Files.list(myDirectory)) {
            return files
                    .filter(Files::isRegularFile)
                    .map(path -> path.getFileName().toString())
                    .filter(name -> name.endsWith(EXTENSION))
                    .map(name -> name.substring(
                            0, name.length() - EXTENSION.length()))
                    .sorted()
                    .toList();
        }
    }
}
