package co.sumup.jobprocessing.task;

import co.sumup.jobprocessing.exception.CyclicDependencyException;
import co.sumup.jobprocessing.exception.DublicateKeyException;
import co.sumup.jobprocessing.exception.NoSuchTaskException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DefaultTaskSorterTest {

    private final DefaultTaskSorter taskSorter = new DefaultTaskSorter();

    @Test
    public void sortTest() {
        DependentTask task1 = new DependentTask("task1", "command1", Arrays.asList("task2", "task3"));
        DependentTask task2 = new DependentTask("task2", "command2", Collections.singletonList("task3"));
        DependentTask task3 = new DependentTask("task3", "command3", null);

        List<SimpleTask> sortedList = taskSorter.sort(Arrays.asList(task1, task2, task3));

        assertEquals(3, sortedList.size());
        assertEquals("task3", sortedList.get(0).getName());
        assertEquals("command3", sortedList.get(0).getCommand());
        assertEquals("task2", sortedList.get(1).getName());
        assertEquals("command2", sortedList.get(1).getCommand());
        assertEquals("task1", sortedList.get(2).getName());
        assertEquals("command1", sortedList.get(2).getCommand());
    }

    @Test
    public void sortNoIndependentTaskTest() {
        DependentTask task1 = new DependentTask("task1", "command1", Collections.singletonList("task2"));
        DependentTask task2 = new DependentTask("task2", "command2", Collections.singletonList("task1"));

        assertThrows(CyclicDependencyException.class, () -> taskSorter.sort(Arrays.asList(task1, task2)));
    }

    @Test
    public void sortCyclicDpendencyTest() {
        DependentTask task1 = new DependentTask("task1", "command1", Collections.singletonList("task1"));
        DependentTask task2 = new DependentTask("task2", "command2", null);

        assertThrows(CyclicDependencyException.class, () -> taskSorter.sort(Arrays.asList(task1, task2)));
    }

    @Test
    public void sortDublicateTaskNamesTest() {
        DependentTask task1 = new DependentTask("task1", "command1", Collections.singletonList("task2"));
        DependentTask task2 = new DependentTask("task2", "command2", null);
        DependentTask task3 = new DependentTask("task2", "command22", null);

        assertThrows(DublicateKeyException.class, () -> taskSorter.sort(Arrays.asList(task1, task2, task3)));
    }

    @Test
    public void sortUnknownTaskTest() {
        DependentTask task1 = new DependentTask("task1", "command1", Collections.singletonList("task3"));
        DependentTask task2 = new DependentTask("task2", "command2", null);

        assertThrows(NoSuchTaskException.class, () -> taskSorter.sort(Arrays.asList(task1, task2)));
    }
}