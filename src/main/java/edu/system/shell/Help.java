package edu.system.shell;

public class Help implements Command {

    private final String description;

    public Help(String description) {
        this.description = description;
    }

    @Override
    public Result execute() {
        System.out.println(description);
        return Result.CONTINUE;
    }

    @Override
    public String name() {
        return "help";
    }
}
