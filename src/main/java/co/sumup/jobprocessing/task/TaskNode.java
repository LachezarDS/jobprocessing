package co.sumup.jobprocessing.task;

import java.util.ArrayList;
import java.util.List;

/**
 * Node representation of a task with its relations.
 */
public class TaskNode {
    private SimpleTask task;
    private boolean ordered;
    private List<TaskNode> children;
    private List<TaskNode> predecessors;

    public TaskNode(SimpleTask task) {
        this.task = task;
        this.ordered = false;
        this.children = new ArrayList<>();
        this.predecessors = new ArrayList<>();
    }

    public SimpleTask getTask() {
        return task;
    }

    public void setTask(SimpleTask task) {
        this.task = task;
    }

    public boolean isOrdered() {
        return ordered;
    }

    public void setOrdered(boolean ordered) {
        this.ordered = ordered;
    }

    public List<TaskNode> getChildren() {
        return children;
    }

    public void setChildren(List<TaskNode> children) {
        this.children = children;
    }

    public List<TaskNode> getPredecessors() {
        return predecessors;
    }

    public void setPredecessors(List<TaskNode> predecessors) {
        this.predecessors = predecessors;
    }

    /**
     * Adds a node which depends on this node
     * @param child child node
     */
    public void addChild(TaskNode child) {
        this.children.add(child);
    }

    /**
     * Adds a node on which this node depends on
     * @param predecessor
     */
    public void addPredecessor(TaskNode predecessor) {
        this.predecessors.add(predecessor);
    }
}
