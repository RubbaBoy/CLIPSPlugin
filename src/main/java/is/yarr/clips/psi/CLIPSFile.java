package is.yarr.clips.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import is.yarr.clips.CLIPSFileType;
import is.yarr.clips.CLIPSLanguage;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a CLIPS file in the PSI tree.
 */
public class CLIPSFile extends PsiFileBase {
    public CLIPSFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, CLIPSLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return CLIPSFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "CLIPS File";
    }
}