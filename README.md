# JavaFX Application Initialization and Functionality Testing with JavaParser

## Overview

This project provides a way to test the initialization and functionality of JavaFX UI elements in a Java application using JavaParser. It includes:
- **GeneralInitializationChecker**: A visitor to check if UI elements like `CheckBox`, `Button`, and `TextArea` are initialized correctly.
- **ButtonFunctionalityChecker**: A visitor to check if a button's action is set correctly.

## GeneralInitializationChecker

### Purpose

The `GeneralInitializationChecker` is used to verify the initialization of UI elements in a specified method. It ensures that elements like `CheckBox`, `Button`, and `TextArea` are initialized with the correct arguments.

### How It Works

The `GeneralInitializationChecker` extends `VoidVisitorAdapter` and overrides the `visit` method to traverse the AST of the specified method. It looks for variable declarations of the specified type and checks if they are initialized with the expected arguments.

### Example

```java
@Test
public void testCheckBoxInitialization() {
    GeneralInitializationChecker checkBoxChecker = new GeneralInitializationChecker(
            "start",
            "CheckBox",
            Collections.singletonList(
                    arg -> arg.isNameExpr() && arg.asNameExpr().getNameAsString().equals("INDICATOR_TITLE")
            )
    );
    compilationUnit.accept(checkBoxChecker, null);
    assertTrue(checkBoxChecker.isInitializedCorrectly(), "CheckBox is not initialized correctly.");
}
```
In this example, the test checks if a CheckBox is initialized with the argument INDICATOR_TITLE in the start method.


## ButtonFunctionalityChecker
### Purpose
The ButtonFunctionalityChecker is used to verify that a button's functionality is set correctly, specifically checking if the setOnAction method is called with the correct lambda expression.

### How It Works
The ButtonFunctionalityChecker extends VoidVisitorAdapter and overrides the visit method to traverse the AST of the specified method. It looks for method calls on the specified button and checks if the setOnAction method is called with the expected lambda expression.

Example
```java
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
```

In this example, the test checks if the warningProceed button has its setOnAction method set with the correct lambda expression in the start method.