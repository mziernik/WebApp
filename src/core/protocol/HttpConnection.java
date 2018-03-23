package core.protocol;

import core.MimeMappings;
import core.protocol.webapp.Handler;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class HttpConnection extends HttpURLConnection {

    private final Map<String, String> headers = new HashMap<>();
    private final File location;
    InputStream in;

    public HttpConnection(File location, URL url) throws IOException, URISyntaxException {
        super(url);
        headers.put("content-type", MimeMappings.get(url.getFile()));
        this.location = location;
    }

    @Override
    public void disconnect() {
        try {
            if (in != null)
                in.close();
        } catch (IOException ex) {
            Handler.exceptionHandlers.stream().forEach((handler) -> {
                handler.onException(ex, this, "disconnect");
            });
        }
    }

    @Override
    public boolean usingProxy() {
        return false;
    }

    @Override
    public void connect() throws IOException {
        try {
            String file = getURL().getFile();

            URL url = new File(location, file).toURI().toURL();


            in = url.openStream();
            if (in == null)
                return;

            headers.put("", "HTTP/1.1 200 OK");
            headers.put("content-length", "" + in.available());

            headers.put("Cache-Control", "no-store, must-revalidate");
            headers.put("Pragma", "no-cache");
            headers.put("Expires", "0");

            doInput = true;
            connected = true;

            //  conn.connected = true;
            // conn.allowUserInteraction = true;
        } catch (Exception e) {
            Handler.exceptionHandlers.stream().forEach((handler) -> {
                handler.onException(e, this, "connect");
            });
            e.printStackTrace();
            throw new IOException(e);
        }
    }

    @Override
    public synchronized InputStream getInputStream() throws IOException {
        return in != null ? in : new ByteArrayInputStream(new byte[0]);
    }

    @Override
    public String getHeaderField(String name) {
        return headers.get(name);
    }

    @Override
    public Map<String, List<String>> getHeaderFields() {
        final Map<String, List<String>> map = new LinkedHashMap<>();
        headers.forEach((String t, String u) -> {
            List<String> list = new LinkedList<>();
            list.add(u);
            map.put(t, list);
        });
        return map;
    }

    @Override
    public int getResponseCode() throws IOException {
        return in != null ? 200 : 404;
    }

    @Override
    public String getResponseMessage() throws IOException {
        return in != null ? "OK" : "Not found";
    }

}
