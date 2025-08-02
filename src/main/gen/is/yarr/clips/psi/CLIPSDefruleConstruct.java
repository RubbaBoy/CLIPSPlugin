// This is a generated file. Not intended for manual editing.
package is.yarr.clips.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface CLIPSDefruleConstruct extends PsiElement {

  @NotNull
  List<CLIPSConstraint> getConstraintList();

  @NotNull
  List<CLIPSDefName> getDefNameList();

  @NotNull
  List<CLIPSExpression> getExpressionList();

  @Nullable
  CLIPSRuleName getRuleName();

  @NotNull
  List<CLIPSSlotName> getSlotNameList();

}
