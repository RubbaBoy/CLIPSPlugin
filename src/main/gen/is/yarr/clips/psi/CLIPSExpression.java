// This is a generated file. Not intended for manual editing.
package is.yarr.clips.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface CLIPSExpression extends PsiElement {

  @Nullable
  CLIPSConstant getConstant();

  @Nullable
  CLIPSFunctionCall getFunctionCall();

  @Nullable
  CLIPSGlobalVariableDef getGlobalVariableDef();

  @Nullable
  CLIPSMultifieldVariableElement getMultifieldVariableElement();

  @Nullable
  CLIPSVariableElement getVariableElement();

}
