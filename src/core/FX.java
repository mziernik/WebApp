package core;


import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;

public class FX {
//Platform.isFxApplicationThread()

    public static void showException(final Throwable ex) {

        Platform.runLater(() -> {
            Throwable e = ex;
            while (e != null && e.getCause() != null
                    && (e instanceof InvocationTargetException || e instanceof RuntimeException))
                e = e.getCause();

            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle(ex.getClass().getSimpleName());
            alert.setHeaderText(ex.getMessage());

            alert.setWidth(500);

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String exceptionText = sw.toString();

            Label label = new Label("Stos wywołań:");

            TextArea textArea = new TextArea(exceptionText);
            textArea.setEditable(false);
            textArea.setWrapText(false);

            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(label, 0, 0);
            expContent.add(textArea, 0, 1);

            alert.getDialogPane().setExpandableContent(expContent);

            alert.showAndWait();
        });
    }

    public static void alertInfo(String text) {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText(text);
            alert.showAndWait();
        });

    }

    public static void alertWarning(String text) {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText(text);
            alert.showAndWait();
        });
    }

    public static void alertError(String text) {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText(text);
            alert.showAndWait();
        });
    }
    /*
     public static ButtonType confirm(String question) {
     sync(() -> {

     Alert alert = new Alert(AlertType.CONFIRMATION);
     alert.setHeaderText(null);
     alert.setContentText(question);
     alert.show();
     return alert.getResult();
     });
     }

     public static ButtonType question(String question, ButtonType... buttons) {
     sync(() -> { Alert alert = new Alert(AlertType.CONFIRMATION);
     alert.setHeaderText(null);
     alert.setContentText(question);
     ObservableList<ButtonType> btns = alert.getButtonTypes();
     btns.clear();
     if (buttons != null && buttons.length > 0)

     btns.addAll(Arrays.asList(buttons));
     else
     btns.addAll(ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
     alert.show();
     return alert.getResult();
     });
     }
     */
}
