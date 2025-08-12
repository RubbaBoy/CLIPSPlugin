// This is a generated file. Not intended for manual editing.
package is.yarr.clips.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface CLIPSDeffunctionConstruct extends PsiElement {

  @Nullable
  CLIPSDeffunctionName getDeffunctionName();

  @NotNull
  List<CLIPSExpression> getExpressionList();

  @Nullable
  CLIPSParameterList getParameterList();

  @Nullable
  PsiElement getMultifieldVariable();

}
