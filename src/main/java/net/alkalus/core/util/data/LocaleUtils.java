package net.alkalus.core.util.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LocaleUtils {

    public static void writeToFile(final String S) {
        try {
            final File F = new File("config/GTplusplus/en_US.lang");
            BufferedWriter writer;
            writer = new BufferedWriter(new FileWriter(F, true));
            writer.write(S);
            writer.newLine();
            writer.close();
        } catch (final IOException e) {
        }
    }

}
