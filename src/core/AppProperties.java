package core;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.Properties;

public class AppProperties {

    private final static File FILE = new File(AppConfig.VAR, "app.properties");
    private static Double width;
    private static Double height;
    private static boolean maximized;

    public static void start(Stage stage, Scene scene) {


        Properties props = new Properties();
        props.put("width", Objects.toString(width));
        props.put("height", Objects.toString(height));
        props.put("maximized", Objects.toString(maximized));

        if (FILE.exists())
            try (InputStreamReader in = new InputStreamReader(new BufferedInputStream(new FileInputStream(FILE)),
                    Charset.forName("UTF-8"))) {
                props.load(in);

                String prop = props.getProperty("width");
                if (prop != null && !prop.isEmpty())
                    stage.setWidth(width = Double.parseDouble(prop));

                prop = props.getProperty("height");
                if (prop != null && !prop.isEmpty())
                    stage.setHeight(height = Double.parseDouble(prop));

                prop = props.getProperty("maximized");
                if (prop != null && !prop.isEmpty())
                    stage.setMaximized(maximized = Boolean.parseBoolean(prop));

            } catch (IOException e) {
                e.printStackTrace();
            }

        try (OutputStreamWriter out = new OutputStreamWriter(new BufferedOutputStream(
                new FileOutputStream(FILE)), Charset.forName("UTF-8"))) {
            props.store(out, null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            maximized = stage.isMaximized();
            if (!maximized)
                width = (Double) newVal;
        });

        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            maximized = stage.isMaximized();
            if (!maximized)
                height = (Double) newVal;
        });
    }

    public static void stop() {
        Properties props = new Properties();
        props.put("width", Objects.toString(width));
        props.put("height", Objects.toString(height));
        props.put("maximized", Objects.toString(maximized));

        try (OutputStreamWriter out = new OutputStreamWriter(new BufferedOutputStream(
                new FileOutputStream(FILE)), Charset.forName("UTF-8"))) {
            props.store(out, null);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
