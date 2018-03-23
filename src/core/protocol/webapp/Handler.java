package core.protocol.webapp;


import core.AppConfig;
import core.protocol.ExceptionHandler;
import core.protocol.HttpConnection;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.LinkedList;
import java.util.List;

public class Handler extends URLStreamHandler {

    public final static List<ExceptionHandler> exceptionHandlers = new LinkedList<>();

    /**
     * Rejestruje handler dla danego protokołu. Klasa musi nazywać się "Handler"
     * pakiet musi pokrywać się z nazwą protokołu
     */
    public static void register() {
        if (!"Handler".equals(Handler.class.getSimpleName()))
            throw new RuntimeException("Klasa musi nazywać się 'Handler'");
        String name = Handler.class.getPackage().getName();
        String protocol = name.substring(name.lastIndexOf(".") + 1);
        System.setProperty("java.protocol.handler.pkgs", name.substring(0, name.lastIndexOf(".")));
    }


    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        try {
            System.out.println(u);
            return new HttpConnection(AppConfig.WEBAPP, u);
        } catch (IOException | URISyntaxException e) {
            exceptionHandlers.stream().forEach((handler) -> {
                handler.onException(e, this, "openConnection");
            });
            e.printStackTrace();
            throw new IOException(e);
        }
    }

}

