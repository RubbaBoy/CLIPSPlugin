package com.example.simplelang;

import com.example.simplelang.psi.SimpleEntityDeclaration;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Utility methods for working with Simple language PSI elements.
 */
public class SimpleUtil {
    /**
     * Finds all entity declarations in the project.
     *
     * @param project the current project
     * @return a list of all entity declarations
     */
    public static List<SimpleEntityDeclaration> findEntities(Project project) {
        var result = new ArrayList<SimpleEntityDeclaration>();
        var virtualFiles = FileTypeIndex.getFiles(SimpleFileType.INSTANCE, GlobalSearchScope.allScope(project));
        
        for (var virtualFile : virtualFiles) {
            var simpleFile = (SimpleFile) PsiManager.getInstance(project).findFile(virtualFile);
            if (simpleFile != null) {
                var entityDeclarations = PsiTreeUtil.getChildrenOfTypeAsList(simpleFile, SimpleEntityDeclaration.class);
                result.addAll(entityDeclarations);
            }
        }
        
        return result;
    }

    /**
     * Finds an entity declaration by name.
     *
     * @param project the current project
     * @param name the name of the entity to find
     * @return the entity declaration, or null if not found
     */
    @Nullable
    public static SimpleEntityDeclaration findEntity(Project project, String name) {
        var entities = findEntities(project);
        for (var entity : entities) {
            if (name.equals(entity.getName())) {
                return entity;
            }
        }
        return null;
    }

    /**
     * Finds all entity declarations with the given name.
     *
     * @param project the current project
     * @param name the name of the entities to find
     * @return a list of entity declarations with the given name
     */
    @NotNull
    public static List<SimpleEntityDeclaration> findEntities(Project project, String name) {
        var result = new ArrayList<SimpleEntityDeclaration>();
        var entities = findEntities(project);
        
        for (var entity : entities) {
            if (name.equals(entity.getName())) {
                result.add(entity);
            }
        }
        
        return result;
    }
}