package javafx;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DBCSDisplayTest {
    private final String PACKAGE_DECLARATION = "de.tum.cit.ase";
    private final String CLASS_NAME = "DBCSDisplay";

    private final List<CompilationUnit> asts = ASTFactory.parseFromSourceRoot(Path.of("/home/sarps/IdeaProjects/itp2324teste03dbcsdexam-solution/src"), ParserConfiguration.LanguageLevel.JAVA_17).stream().filter(Optional::isPresent).map(Optional::get).toList();

    @Test
    public void testCheckBoxInitialization() throws FileNotFoundException {
        File sourceFile = new File("path/to/DBCSDisplayTest.java"); // Update with the correct path to your source file

        CompilationUnit compilationUnit = asts.stream()
                .filter(ast -> ast.getPackageDeclaration().orElseGet(PackageDeclaration::new).toString()
                        .trim().equals(String.format("package %s;", PACKAGE_DECLARATION))
                        && ast.getPrimaryTypeName().orElse("something").equals(CLASS_NAME))
                .findFirst()
                .orElseThrow(() ->
                        new AssertionError("COULD NOT FIND THE CLASS IN THE PACKAGE " + PACKAGE_DECLARATION));

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
}
