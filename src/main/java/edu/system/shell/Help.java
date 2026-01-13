package edu.system.shell;

public class Help implements Command {
    private final String description;

    public Help(String description) {
        this.description = description;
    }

    @Override
    public String name() {
        return "help";
    }

    @Override
    public Result execute() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("📘 INTERACTIVE LEARNING SYSTEM - HELP");
        System.out.println("=".repeat(50));
        System.out.println(description);
        System.out.println("\n🔧 AVAILABLE COMMANDS:");
        System.out.println("  exit     - Close the program");
        System.out.println("  help     - Show this message");
        System.out.println("  return   - Go back to previous menu");
        System.out.println("  login    - Sign in to the system");
        System.out.println("  start    - Begin learning course (simple mode)");
        System.out.println("  results  - View your learning results");
        System.out.println("  browse   - Explore learning content");
        System.out.println("  learn    - Start interactive learning (advanced mode)");
        System.out.println("  create   - Create new learning content");
        System.out.println("\n💡 TIP: Always use 'login' first to save your progress!");
        System.out.println("=".repeat(50));
        return Result.CONTINUE;
    }
}