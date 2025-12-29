package edu.system.model;

public class NodeBuilder {

    private final Node node = new Node();

    private NodeBuilder() {
        node.setId(0);
        node.setName("noname");
        node.setActive(true);
        node.setDescription("no description");
    }

    public static NodeBuilder create() {
        return new NodeBuilder();
    }

    public NodeBuilder name(String name) {
        node.setName(name);
        return this;
    }

    public NodeBuilder id(int id) {
        node.setId(id);
        return this;
    }

    public NodeBuilder active(boolean active) {
        node.setActive(active);
        return this;
    }

    public NodeBuilder description(String description) {
        node.setDescription(description);
        return this;
    }

    public Node build() {
        if (node.getId() < 0)
            throw new RuntimeException("Id cannot be negative");
        if (node.getName() == null)
            throw new RuntimeException("Name is required");
        return node;
    }
}
