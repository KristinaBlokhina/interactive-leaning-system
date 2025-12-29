package edu.system.model;

import java.util.Objects;

public class Node {

    private String name;
    private int id;
    private boolean active;
    private String description;

    public Node() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return "[name=%s, id=%d, active=%s, description=%s]"
                .formatted(name, id, active, description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node node)) return false;
        return id == node.id &&
                active == node.active &&
                Objects.equals(name, node.name) &&
                Objects.equals(description, node.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, active, description);
    }
}
