package core;

import java.io.File;

public class AppConfig {

    public final static File HOME;
    public final static File WEBAPP;
    public final static File VAR;
    public final static File TEMP;
    public final static File ETC;
    public final static File DATA;

    static {
        HOME = new File("").getAbsoluteFile();
        WEBAPP = new File(HOME, "webapp");
        VAR = new File(HOME, "var");
        TEMP = new File(HOME, "tmp");
        ETC = new File(HOME, "etc");
        DATA = new File(HOME, "data");

        System.setProperty("java.io.tmpdir", TEMP.getAbsolutePath());
    }
}
