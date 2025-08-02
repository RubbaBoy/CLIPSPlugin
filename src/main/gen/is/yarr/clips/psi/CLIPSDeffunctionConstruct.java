// This is a generated file. Not intended for manual editing.
package is.yarr.clips.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface CLIPSDeffunctionConstruct extends PsiElement {

  @Nullable
  CLIPSDefName getDefName();

  @NotNull
  List<CLIPSExpression> getExpressionList();

  @Nullable
  CLIPSGlobalVariableDef getGlobalVariableDef();

  @Nullable
  CLIPSParameterList getParameterList();

  @Nullable
  CLIPSVariableElement getVariableElement();

  @Nullable
  PsiElement getBuiltinFunction();

  @Nullable
  PsiElement getKeyword();

  @Nullable
  PsiElement getMultifieldVariable();

}
