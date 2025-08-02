// This is a generated file. Not intended for manual editing.
package is.yarr.clips.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface CLIPSDefclassConstruct extends PsiElement {

  @Nullable
  CLIPSClassName getClassName();

  @NotNull
  List<CLIPSDefName> getDefNameList();

  @NotNull
  List<CLIPSSlotDefinition> getSlotDefinitionList();

}
