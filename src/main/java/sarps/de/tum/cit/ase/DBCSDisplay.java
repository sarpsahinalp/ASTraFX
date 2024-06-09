package sarps.de.tum.cit.ase;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DBCSDisplay extends Application {
    private static final int STANDARD_SPACING = 10;
    private static final int STAGE_WIDTH = 300;
    private static final int USABLE_STAGE_WIDTH = STAGE_WIDTH - STANDARD_SPACING * 2;
    private static final int HALF_USABLE_STAGE_WIDTH = (USABLE_STAGE_WIDTH - STANDARD_SPACING) / 2;
    private static final int STAGE_HEIGHT = 400;
    private static final int WARNINGPROCEED_HEIGHT = 50;
    private static final int USABLE_RECORD_HEIGHT = 400 - (WARNINGPROCEED_HEIGHT + 5 * STANDARD_SPACING);
    private static final String STAGE_TITLE = "DBCS";
    private static final String INDICATOR_TITLE = "Warning";
    private static final String WARNINGPROCEED_TEXT = "Warning/Proceed";
    private static final String RECORD_TEXT = "";
    private static final Pos STANDARD_ALIGNMENT = Pos.CENTER;
    private static final boolean INDICATOR_DISABLE = true;
    private static final boolean RECORD_EDITABLE = false;
    private static final String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";

    /**
     * Stores the current warning status.
     */
    private boolean isWarning = false;

    /**
     * Start-method for the JavaFx application. Add your code for tasks 1-5 here.
     * @param primaryStage Initial stage for the JavaFx application.
     */
    @Override
    public void start(Stage primaryStage) {
// TODO 1: Create the UI elements
        CheckBox indicator = new CheckBox(INDICATOR_TITLE);
        Button warningProceed = new Button(WARNINGPROCEED_TEXT);
        TextArea record = new TextArea(RECORD_TEXT);
// TODO 2: Create the layout elements
        HBox hBox = new HBox(STANDARD_SPACING, indicator, warningProceed);
        VBox vBox = new VBox(STANDARD_SPACING, hBox, record);
        primaryStage.setMinWidth(STAGE_WIDTH);
        primaryStage.setMinHeight(STAGE_HEIGHT);
        primaryStage.setTitle(STAGE_TITLE);
        indicator.setMinSize(HALF_USABLE_STAGE_WIDTH, WARNINGPROCEED_HEIGHT);
        indicator.setDisable(INDICATOR_DISABLE);
        warningProceed.setMinSize(HALF_USABLE_STAGE_WIDTH, WARNINGPROCEED_HEIGHT);
        record.setMinSize(USABLE_STAGE_WIDTH, USABLE_RECORD_HEIGHT);
        record.setEditable(RECORD_EDITABLE);
        hBox.setMinSize(STAGE_WIDTH, WARNINGPROCEED_HEIGHT);
        hBox.setAlignment(STANDARD_ALIGNMENT);
        hBox.setPadding(new Insets(STANDARD_SPACING));
        vBox.setMinSize(STAGE_WIDTH, STAGE_HEIGHT);
        vBox.setAlignment(STANDARD_ALIGNMENT);
        vBox.setPadding(new Insets(STANDARD_SPACING));
// TODO 5: Set the Warning/Proceed single click handler
        warningProceed.setOnAction(event -> warningProceedClickHandler(indicator, record));
// TODO 3: Create the scene
        Scene scene = new Scene(vBox, STAGE_WIDTH, STAGE_HEIGHT);
// TODO 4: Set the stage
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Click-handler for the "Warning/Proceed" button click. Add your code for task 6 here.
     * @param indicator Provide the CheckBox in the JavaFx application here.
     * @param record Provide the TextArea in the JavaFx application here.
     */
    public void warningProceedClickHandler(CheckBox indicator, TextArea record) {
// TODO 6: Re-adjust the UI elements on an Warning/Proceed single click
        isWarning = !isWarning;
        indicator.setSelected(isWarning);
        record.appendText((isWarning ? "Warning" : "Proceed") + " at " + getCurrentDate() + "\n");
    }

    /**
     * Creates the current date for the TextArea in the JavaFx application.
     * @return current date in the expected format.
     */
    private String getCurrentDate() {
        return new SimpleDateFormat(DATE_FORMAT).format(new Date());
    }

    /**
     * Run this method to start the application.
     * @param args Console arguments (not required for this exercise).
     */
    public static void main(String[] args) {
        launch(args);
    }
}
