package core.protocol;

public interface ExceptionHandler {
    void onException(Throwable e, Object source, String method);
}
