package is.yarr.clips;

import com.intellij.lang.Language;

/**
 * Defines the CLIPS language for IntelliJ.
 * This is the foundation class for all CLIPS language support features.
 */
public class CLIPSLanguage extends Language {
    public static final CLIPSLanguage INSTANCE = new CLIPSLanguage();

    private CLIPSLanguage() {
        super("CLIPS");
    }

    @Override
    public String getDisplayName() {
        return "CLIPS";
    }
}