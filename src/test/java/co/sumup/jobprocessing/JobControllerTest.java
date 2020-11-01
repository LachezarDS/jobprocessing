package co.sumup.jobprocessing;

import co.sumup.jobprocessing.task.DependentTask;
import co.sumup.jobprocessing.task.SimpleTask;
import co.sumup.jobprocessing.task.TaskList;
import co.sumup.jobprocessing.task.TaskSorter;
import co.sumup.jobprocessing.task.ResponseConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JobControllerTest {

    @Mock
    private TaskSorter taskSorter;

    @Mock
    private ResponseConverter responseConverter;

    private JobController jobController;

    @BeforeEach
    public void setUp() {
        this.jobController = new JobController(taskSorter, responseConverter);
    }

    @Test
    public void sortTasksJsonTest() {
        List<SimpleTask> sortedTasks = createSimpleTasks();

        when(taskSorter.sort(any())).thenReturn(sortedTasks);

        TaskList taskList = createTaskList();

        ResponseEntity<Object> response = jobController.sortTasks(taskList, false);

        verify(taskSorter, times(1)).sort(taskList.getTasks());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
    }

    @Test
    public void sortTasksOctetStreamTest() {
        List<SimpleTask> sortedTasks = createSimpleTasks();

        when(taskSorter.sort(any())).thenReturn(sortedTasks);
        when(responseConverter.convert(any())).thenReturn("test response");

        TaskList taskList = createTaskList();

        ResponseEntity<Object> response = jobController.sortTasks(taskList, true);

        verify(taskSorter, times(1)).sort(taskList.getTasks());
        verify(responseConverter, times(1)).convert(sortedTasks);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_OCTET_STREAM, response.getHeaders().getContentType());
    }

    @Test
    public void sortTasksBashScriptFalseEmptyTaskList() {
        TaskList taskList = new TaskList();

        ResponseEntity<Object> response = jobController.sortTasks(taskList, false);

        verify(taskSorter, never()).sort(anyList());
        verify(responseConverter, never()).convert(any());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals(Collections.EMPTY_LIST, response.getBody());
    }

    @Test
    public void sortTasksBashScriptTrueEmptyTaskList() {
        TaskList taskList = new TaskList();

        when(responseConverter.convert(any())).thenReturn("");

        ResponseEntity<Object> response = jobController.sortTasks(taskList, true);

        verify(taskSorter, never()).sort(anyList());
        verify(responseConverter, times(1)).convert(any());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_OCTET_STREAM, response.getHeaders().getContentType());
    }

    private List<SimpleTask> createSimpleTasks() {
        return Arrays.asList(new SimpleTask("test2", "command2"),
                new SimpleTask("test1", "command1"));
    }

    private TaskList createTaskList() {
        TaskList taskList = new TaskList();
        DependentTask dependentTask1 = new DependentTask("name1", "command1", Collections.singletonList("name2"));
        DependentTask dependentTask2 = new DependentTask("name2", "command2", null);

        taskList.setTasks(Arrays.asList(dependentTask1, dependentTask2));
        return taskList;
    }
}