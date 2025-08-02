// This is a generated file. Not intended for manual editing.
package is.yarr.clips.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface CLIPSDefmoduleConstruct extends PsiElement {

  @NotNull
  List<CLIPSExportSpec> getExportSpecList();

  @NotNull
  List<CLIPSImportSpec> getImportSpecList();

  @Nullable
  CLIPSModuleName getModuleName();

}
