package org.futurepages.core.exception;

public interface ExceptionLoggerImpl {

    public void execute(Throwable throwable);
    public void execute(Throwable throwable, long protocolNum);
}
