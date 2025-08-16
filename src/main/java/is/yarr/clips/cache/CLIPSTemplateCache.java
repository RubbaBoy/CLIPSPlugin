package is.yarr.clips.cache;

import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.CachedValue;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiModificationTracker;
import com.intellij.psi.util.PsiTreeUtil;
import is.yarr.clips.psi.CLIPSDefaultAttribute;
import is.yarr.clips.psi.CLIPSDeftemplateConstruct;
import is.yarr.clips.psi.CLIPSSlotDefinition;
import is.yarr.clips.psi.CLIPSSlotName;
import is.yarr.clips.psi.CLIPSTemplateName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class CLIPSTemplateCache {
    private static final Key<CachedValue<Map<String, TemplateInfo>>> CACHED_TEMPLATES_KEY =
            Key.create("clips.cached.templates");

    public static Map<String, TemplateInfo> getTemplates(@NotNull PsiFile file) {
        return CachedValuesManager.getManager(file.getProject()).getCachedValue(
                file,
                CACHED_TEMPLATES_KEY,
                () -> {
                    Map<String, TemplateInfo> map = buildTemplatesForFile(file);
                    // Invalidate on any PSI modification in the project; or use a finer-grained tracker if available
                    return CachedValueProvider.Result.create(map, PsiModificationTracker.getInstance(file.getProject()).getModificationCount());
                },
                false
        );
    }

    private static Map<String, TemplateInfo> buildTemplatesForFile(PsiFile file) {
        Map<String, TemplateInfo> out = new HashMap<>();
        PsiTreeUtil.processElements(file, el -> {
            if (el instanceof CLIPSDeftemplateConstruct template) {
                String name = Optional.ofNullable(PsiTreeUtil.findChildOfType(template, CLIPSTemplateName.class))
                        .map(PsiElement::getText).orElse(null);
                if (name == null || name.isEmpty()) return true;

                Map<String, SlotInfo> slots = new LinkedHashMap<>();
                for (CLIPSSlotDefinition slotDef : PsiTreeUtil.findChildrenOfType(template, CLIPSSlotDefinition.class)) {
                    String slotName = Optional.ofNullable(PsiTreeUtil.findChildOfType(slotDef, CLIPSSlotName.class))
                            .map(PsiElement::getText).orElse(null);
                    if (slotName == null) continue;
                    SlotInfo info = new SlotInfo(PsiTreeUtil.findChildrenOfType(slotDef, CLIPSDefaultAttribute.class).isEmpty(), extractSlotType(slotDef));
                    slots.put(slotName, info);
                }
                out.put(name, new TemplateInfo(name, slots));
            }
            return true;
        });
        return out;
    }

    private static @Nullable SlotType extractSlotType(CLIPSSlotDefinition slotDef) {
        // TODO: parse slot constraints
        return null;
    }

    public record TemplateInfo(String name, Map<String, SlotInfo> slots) {}
    public record SlotInfo(boolean required, @Nullable SlotType type, List<String> allowedValues) {
        public SlotInfo(boolean required, @Nullable SlotType type) {
            this(required, type, Collections.emptyList());
        }
    }

    public enum SlotType {
        NUMBER, STRING, SYMBOL, MULTIFIELD
    }
}