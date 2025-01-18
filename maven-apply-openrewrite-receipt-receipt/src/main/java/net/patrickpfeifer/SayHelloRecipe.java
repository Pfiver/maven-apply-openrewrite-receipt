package net.patrickpfeifer;

import org.jetbrains.annotations.NotNull;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;


public class SayHelloRecipe extends Recipe {

    @Override
    public @NotNull String getDisplayName() {
        return "Say hello";
    }

    @Override
    public @NotNull String getDescription() {
        return "Adds a \"hello\" method to the specified class.";
    }

    @Override
    public @NotNull TreeVisitor<?, ExecutionContext> getVisitor() {
        return new SayHelloRewriteVisitor();
    }
}
