package org.futurepages.core.exception;

public interface ExceptionLogger {

    public void execute(Throwable throwable);
    public void execute(Throwable throwable, long protocolNum);
}
