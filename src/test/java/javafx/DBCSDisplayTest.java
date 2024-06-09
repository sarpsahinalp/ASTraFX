package javafx;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DBCSDisplayTest {
    private static final String PACKAGE_DECLARATION = "de.tum.cit.ase";
    private static final String CLASS_NAME = "DBCSDisplay";
    private static CompilationUnit compilationUnit;

    private static final List<CompilationUnit> asts = ASTFactory.parseFromSourceRoot(Path.of("/home/sarps/IdeaProjects/itp2324teste03dbcsdexam-solution/src"), ParserConfiguration.LanguageLevel.JAVA_17).stream().filter(Optional::isPresent).map(Optional::get).toList();

    @BeforeAll
    static void setup() {
        compilationUnit = asts.stream()
                .filter(ast -> ast.getPackageDeclaration().orElseGet(PackageDeclaration::new).toString()
                        .trim().equals(String.format("package %s;", PACKAGE_DECLARATION))
                        && ast.getPrimaryTypeName().orElse("something").equals(CLASS_NAME))
                .findFirst()
                .orElseThrow(() ->
                        new AssertionError("COULD NOT FIND THE CLASS IN THE PACKAGE " + PACKAGE_DECLARATION));
    }

    @Test
    public void testCheckBoxInitialization() {

        // Check CheckBox initialization
        GeneralInitializationChecker checkBoxChecker = new GeneralInitializationChecker(
                "start",
                "DBCSDisplay",
                "CheckBox",
                Collections.singletonList(
                        arg -> arg.isNameExpr() && arg.asNameExpr().getNameAsString().equals("INDICATOR_TITLE")
                )
        );
        compilationUnit.accept(checkBoxChecker, null);
        assertTrue(checkBoxChecker.isInitializedCorrectly(), "CheckBox is not initialized correctly.");

        // Check Button initialization
        GeneralInitializationChecker buttonChecker = new GeneralInitializationChecker(
                "start",
                "DBCSDisplay",
                "Button",
                Collections.singletonList(
                        arg -> arg.isNameExpr() && arg.asNameExpr().getNameAsString().equals("WARNINGPROCEED_TEXT")
                )
        );
        compilationUnit.accept(buttonChecker, null);
        assertTrue(buttonChecker.isInitializedCorrectly(), "Button is not initialized correctly.");

        // Additional checks can be added similarly
    }

    @Test
    public void testButtonFunctionality() {
        Predicate<MethodCallExpr> functionalityChecker = methodCall ->
                methodCall.getNameAsString().equals("setOnAction") &&
                        methodCall.getArguments().size() == 1 &&
                        methodCall.getArguments().get(0).isLambdaExpr();

        ButtonFunctionalityChecker buttonFunctionalityChecker = new ButtonFunctionalityChecker(
                "start",
                "warningProceed",
                functionalityChecker
        );
        compilationUnit.accept(buttonFunctionalityChecker, null);
        assertTrue(buttonFunctionalityChecker.isFunctionalityCorrect(), "Button functionality is not set correctly.");
    }
}
