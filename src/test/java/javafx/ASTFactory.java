package javafx;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ClassLoaderTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.utils.SourceRoot;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ASTFactory {

    /**
     * Parse all Java files in the source root
     *
     * @param pathToSourceRoot Path to the source root
     * @param level            JavaParser Language Level
     * @return List of CompilationUnit
     */
    public static List<Optional<CompilationUnit>> parseFromSourceRoot(Path pathToSourceRoot, ParserConfiguration.LanguageLevel level) {
        CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
        combinedTypeSolver.add(new ReflectionTypeSolver());
        combinedTypeSolver.add(new JavaParserTypeSolver(new File(pathToSourceRoot.toString())));
        combinedTypeSolver.add(new ClassLoaderTypeSolver(ASTFactory.class.getClassLoader()));

        // Configure JavaParser to use type resolution
        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(combinedTypeSolver);

        SourceRoot sourceRoot = new SourceRoot(pathToSourceRoot, new ParserConfiguration().setSymbolResolver(symbolSolver).setLanguageLevel(level));

        List<ParseResult<CompilationUnit>> parseResults;
        parseResults = sourceRoot.tryToParseParallelized();

        return parseResults.stream().map(ParseResult::getResult).collect(Collectors.toList());
    }
}
