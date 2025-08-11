// This is a generated file. Not intended for manual editing.
package is.yarr.clips.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface CLIPSConstraint extends PsiElement {

  @NotNull
  List<CLIPSConstant> getConstantList();

  @NotNull
  List<CLIPSGlobalVariableDef> getGlobalVariableDefList();

  @NotNull
  List<CLIPSMultifieldVariableElement> getMultifieldVariableElementList();

  @NotNull
  List<CLIPSPredicateConstraint> getPredicateConstraintList();

  @NotNull
  List<CLIPSVariableElement> getVariableElementList();

}
