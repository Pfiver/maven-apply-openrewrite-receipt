package net.patrickpfeifer;

import org.jetbrains.annotations.NotNull;
import org.openrewrite.Cursor;
import org.openrewrite.ExecutionContext;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.JavaTemplate;
import org.openrewrite.java.tree.J;

public class SayHelloRewriteVisitor extends JavaIsoVisitor<ExecutionContext> {

    private final JavaTemplate helloTemplate = JavaTemplate.builder(
            "public String hello() { return \"Hello from #{}!\"; }"
    ).build();

    @Override
    public J.@NotNull ClassDeclaration visitClassDeclaration(
            J.ClassDeclaration classDecl,
            @NotNull ExecutionContext executionContext
    ) {

        System.out.println("Visiting " + classDecl.getName());

        // Check if the class already has a method named "hello".
        // Making your recipe immutable helps make them idempotent and eliminates categories of possible bugs.
        boolean helloMethodExists = classDecl.getBody().getStatements().stream()
                .filter(statement -> statement instanceof J.MethodDeclaration)
                .map(J.MethodDeclaration.class::cast)
                .anyMatch(methodDeclaration -> methodDeclaration.getName().getSimpleName().equals("hello"));

        // If the class already has a `hello()` method, don't make any changes to it.
        if (helloMethodExists) {
            return classDecl;
        }

        // Interpolate the fullyQualifiedClassName into the template and use the resulting LST to update the class body
        classDecl = classDecl.withBody(helloTemplate.apply(
                new Cursor(
                        getCursor(),
                        classDecl.getBody()
                ),
                classDecl.getBody().getCoordinates().lastStatement(),
                classDecl.getName()
        ));

        return classDecl;
    }
}
