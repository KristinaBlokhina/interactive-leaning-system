package edu.system.shell;

public class Exit implements Command {

    @Override
    public Result execute() {
        return Result.EXIT;
    }

    @Override
    public String name() {
        return "exit";
    }
}
