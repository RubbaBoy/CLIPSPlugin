package is.yarr.clips;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Defines the CLIPS file type and associates it with the .clp extension.
 */
public class CLIPSFileType extends LanguageFileType {
    public static final CLIPSFileType INSTANCE = new CLIPSFileType();

    private CLIPSFileType() {
        super(CLIPSLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "CLIPS";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "CLIPS language file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "clp";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return CLIPSIcons.FILE;
    }
}