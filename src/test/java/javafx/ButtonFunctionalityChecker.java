package javafx;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.function.Predicate;

public class ButtonFunctionalityChecker extends VoidVisitorAdapter<Void> {
    private final String methodName;
    private final String buttonName;
    private final Predicate<MethodCallExpr> functionalityChecker;
    private boolean isFunctionalityCorrect;

    public ButtonFunctionalityChecker(String methodName, String buttonName, Predicate<MethodCallExpr> functionalityChecker) {
        this.methodName = methodName;
        this.buttonName = buttonName;
        this.functionalityChecker = functionalityChecker;
        this.isFunctionalityCorrect = false;
    }

    @Override
    public void visit(MethodDeclaration method, Void arg) {
        if (method.getNameAsString().equals(methodName)) {
            method.getBody().ifPresent(body -> body.accept(new VoidVisitorAdapter<Void>() {
                @Override
                public void visit(MethodCallExpr methodCall, Void arg) {
                    if (methodCall.getScope().isPresent() && methodCall.getScope().get().isNameExpr()) {
                        NameExpr scope = methodCall.getScope().get().asNameExpr();
                        if (scope.getNameAsString().equals(buttonName) && functionalityChecker.test(methodCall)) {
                            isFunctionalityCorrect = true;
                        }
                    }
                    super.visit(methodCall, arg);
                }
            }, null));
        }
        super.visit(method, arg);
    }

    public boolean isFunctionalityCorrect() {
        return isFunctionalityCorrect;
    }
}

