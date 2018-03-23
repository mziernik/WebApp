package core;

import netscape.javascript.JSObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

/**
 * @author milosz
 */
public interface JsBridge {

    default void consoleLog(Object text) {
        System.out.println(text);
    }

    default void consoleError(Object text) {
        System.err.println(text);
    }

    default void onError(String msg, String file, Integer line, Integer column, JSObject ex) {
        System.err.println(msg);
    }

    default String loadFile(String fileName) throws IOException {
        return new String(Files.readAllBytes(new File(AppConfig.DATA, fileName).toPath()), Charset.forName("UTF-8"));
    }
}
