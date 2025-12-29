package edu.system.shell;

public interface Command {
    Result execute();
    String name();
}
