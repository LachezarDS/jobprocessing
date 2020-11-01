package co.sumup.jobprocessing.task;

import co.sumup.jobprocessing.exception.CyclicDependencyException;
import co.sumup.jobprocessing.exception.DublicateKeyException;
import co.sumup.jobprocessing.exception.NoSuchTaskException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * {@inheritDoc}
 */
@Service
public class DefaultTaskSorter implements TaskSorter {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SimpleTask> sort(List<DependentTask> unorderedTasks) {
        List<SimpleTask> orderedTasks = new ArrayList<>();
        Queue<TaskNode> orderedTaskNodes = new LinkedList<>();
        convertTasksToLinkedNodes(unorderedTasks, orderedTasks, orderedTaskNodes);

        if (orderedTaskNodes.isEmpty()) {
            throw new CyclicDependencyException();
        }
        // here it goes through the already ordered task and checks whether their children are eligible to order and if yes
        // they are added to the final ordered list of tasks and the queue of nodes, until the queue is empty
        while (!orderedTaskNodes.isEmpty()) {
            TaskNode taskNode = orderedTaskNodes.poll();
            for (TaskNode child : taskNode.getChildren()) {
                if (child.isOrdered()) {
                    continue;
                }

                boolean eligibleForOrder = isEligibleForOrder(child);

                if (eligibleForOrder) {
                    orderedTasks.add(child.getTask());
                    orderedTaskNodes.add(child);
                    child.setOrdered(true);
                }
            }
        }

        // simple check whether all tasks are ordered
        if (orderedTasks.size() != unorderedTasks.size()) {
            throw new CyclicDependencyException();
        }
        return orderedTasks;
    }

    private boolean isEligibleForOrder(TaskNode child) {
        boolean eligibleForOrder = true;
        for (TaskNode predecessor : child.getPredecessors()) {
            if (!predecessor.isOrdered()) {
                eligibleForOrder = false;
                break;
            }
        }
        return eligibleForOrder;
    }

    private void convertTasksToLinkedNodes(List<DependentTask> unorderedTasks, List<SimpleTask> orderedTasks, Queue<TaskNode> orderedTaskNodes) {
        //create hashmap with the nodes for faster access while populating the node relations
        Map<String, TaskNode> nodes = unorderedTasks.stream()
                                                    .collect(Collectors.toMap(DependentTask::getName,
                                               (task) -> new TaskNode(new SimpleTask(task.getName(), task.getCommand())),
                                               (u, v) -> { throw new DublicateKeyException(
                                                           "There are multiple tasks with the same name."); },
                                               HashMap::new));
        //populate the nodes relations
        for (DependentTask dependentTask : unorderedTasks) {
            TaskNode taskNode = nodes.get(dependentTask.getName());
            List<String> requires = dependentTask.getRequires();
            if (requires == null || requires.isEmpty()) {
                orderedTasks.add(taskNode.getTask());
                orderedTaskNodes.add(taskNode);
                taskNode.setOrdered(true);
                continue;
            }
            for (String predecessor : requires) {
                TaskNode predecessorTaskNode = nodes.computeIfAbsent(predecessor, v -> {
                    throw new NoSuchTaskException(predecessor);
                });
                taskNode.addPredecessor(predecessorTaskNode);
                predecessorTaskNode.addChild(taskNode);
            }
        }
    }
}
