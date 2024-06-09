package javafx;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;
import java.util.function.Predicate;

public class GeneralInitializationChecker extends VoidVisitorAdapter<Void> {
    private final String methodName;
    private final String className;
    private final String variableType;
    private final List<Predicate<Expression>> argumentCheckers;
    private boolean isInitializedCorrectly;

    public GeneralInitializationChecker(String methodName, String className, String variableType, List<Predicate<Expression>> argumentCheckers) {
        this.methodName = methodName;
        this.className = className;
        this.variableType = variableType;
        this.argumentCheckers = argumentCheckers;
        this.isInitializedCorrectly = false;
    }

    @Override
    public void visit(MethodDeclaration method, Void arg) {
        if (method.getNameAsString().equals(methodName)) {
            BlockStmt body = method.getBody().orElse(null);
            if (body != null) {
                body.accept(new VoidVisitorAdapter<Void>() {
                    @Override
                    public void visit(VariableDeclarator variable, Void arg) {
                        if (variable.getType().asString().equals(variableType) &&
                                variable.getInitializer().isPresent()) {
                            Expression initializer = variable.getInitializer().get();
                            if (initializer instanceof ObjectCreationExpr objectCreationExpr) {
                                if (objectCreationExpr.getType().asString().equals(variableType)) {
                                    isInitializedCorrectly = checkArguments(objectCreationExpr.getArguments());
                                }
                            }
                        }
                        super.visit(variable, arg);
                    }
                }, null);
            }
        }
        super.visit(method, arg);
    }

    private boolean checkArguments(List<Expression> arguments) {
        if (arguments.size() != argumentCheckers.size()) {
            return false;
        }
        for (int i = 0; i < arguments.size(); i++) {
            if (!argumentCheckers.get(i).test(arguments.get(i))) {
                return false;
            }
        }
        return true;
    }

    public boolean isInitializedCorrectly() {
        return isInitializedCorrectly;
    }
}
