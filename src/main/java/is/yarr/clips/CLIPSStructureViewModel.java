package is.yarr.clips;

import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.StructureViewModelBase;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import com.intellij.psi.PsiFile;
import is.yarr.clips.psi.*;
import org.jetbrains.annotations.NotNull;

/**
 * Defines the structure view model for CLIPS files.
 * This determines how the structure view is organized and what elements are displayed.
 */
public class CLIPSStructureViewModel extends StructureViewModelBase implements StructureViewModel.ElementInfoProvider {
    public CLIPSStructureViewModel(PsiFile psiFile) {
        super(psiFile, new CLIPSStructureViewElement(psiFile));
        
        // Register the types of elements to show in the structure view
        withSuitableClasses(
            CLIPSDeftemplateConstruct.class,
            CLIPSDefruleConstruct.class,
            CLIPSDeffunctionConstruct.class,
            CLIPSDefglobalConstruct.class,
            CLIPSDefmoduleConstruct.class,
            CLIPSDefclassConstruct.class,
            CLIPSDeffactsConstruct.class
        );
    }

    @Override
    public Sorter @NotNull [] getSorters() {
        return new Sorter[]{Sorter.ALPHA_SORTER}; // Sort elements alphabetically
    }

    @Override
    public boolean isAlwaysShowsPlus(StructureViewTreeElement element) {
        // Return true if the element can have children even when none are currently present
        return element.getValue() instanceof CLIPSFile;
    }

    @Override
    public boolean isAlwaysLeaf(StructureViewTreeElement element) {
        // Return true if the element can never have children
        Object value = element.getValue();
        return value instanceof CLIPSTemplateName ||
               value instanceof CLIPSRuleName ||
               value instanceof CLIPSSlotName ||
               value instanceof CLIPSParameter ||
               value instanceof CLIPSVariableElement ||
               value instanceof CLIPSGlobalVariableDef;
    }
}