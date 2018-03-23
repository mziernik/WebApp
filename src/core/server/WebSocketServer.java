package core.server;

import io.undertow.Undertow;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.server.handlers.resource.PathResourceManager;
import io.undertow.util.Headers;
import io.undertow.websockets.core.AbstractReceiveListener;
import io.undertow.websockets.core.BufferedTextMessage;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.core.WebSockets;

import java.nio.file.Paths;

import static io.undertow.Handlers.*;


public class WebSocketServer {

    public static void run() {
        Undertow server = Undertow.builder()
                .addHttpListener(8080, "localhost")


                .setHandler(path()

                        .addPrefixPath("res", resource(new PathResourceManager(Paths.get(System.getProperty("user.home")), 100))
                                .setDirectoryListingEnabled(true))

                        .addPrefixPath("exchange", exchange -> {
                            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
                            exchange.getResponseSender().send("Hello World");
                        })

                        .addPrefixPath("/myapp", websocket((exchange, channel) -> {
                            channel.getReceiveSetter().set(new AbstractReceiveListener() {

                                @Override
                                protected void onFullTextMessage(WebSocketChannel channel, BufferedTextMessage message) {
                                    WebSockets.sendText(message.getData(), channel, null);
                                }
                            });
                            channel.resumeReceives();
                        }))
                        .addPrefixPath("/", resource(new ClassPathResourceManager(WebSocketServer.class.getClassLoader(), WebSocketServer.class.getPackage())).addWelcomeFiles("index.html")))
                .build();
        server.start();
    }

}