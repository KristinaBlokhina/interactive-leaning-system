package edu.system.shell;

public class Return implements Command {

    @Override
    public Result execute() {
        return Result.RETURN;
    }

    @Override
    public String name() {
        return "return";
    }
}

