package is.yarr.clips;

import com.intellij.lang.Language;

/**
 * Defines the CLIPS language for IntelliJ.
 * This is the core class that identifies the CLIPS language to the IDE.
 */
public class CLIPSLanguage extends Language {
    public static final CLIPSLanguage INSTANCE = new CLIPSLanguage();

    private CLIPSLanguage() {
        super("CLIPS");
    }
}