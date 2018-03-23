package core;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.AppBridge;

public class Main extends Application {

    public final AppBridge bridge = new AppBridge();
    public final WebKit webKit = new WebKit("webapp:///index.html", bridge);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Hello World");

        Scene scene = new Scene(webKit.webView, 600, 400);
        AppProperties.start(stage, scene);
        stage.setScene(scene);


        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case F5:
                    webKit.reload();
                    break;
                case F12:
                    webKit.debugger();
                    break;
            }
        });


        stage.show();
        webKit.reload();
    }

    @Override
    public void stop() {
        AppProperties.stop();
        System.exit(0);
    }
}
