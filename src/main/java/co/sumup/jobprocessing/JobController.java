package co.sumup.jobprocessing;

import co.sumup.jobprocessing.task.DependentTask;
import co.sumup.jobprocessing.task.SimpleTask;
import co.sumup.jobprocessing.task.TaskList;
import co.sumup.jobprocessing.task.TaskSorter;
import co.sumup.jobprocessing.task.ResponseConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller with endpoints for operating with tasks
 */
@RestController
@RequestMapping("job")
public class JobController {

    private final TaskSorter taskSorter;

    private final ResponseConverter responseConverter;

    /**
     * Creates {@link JobController}
     * @param taskSorter taskSorter
     * @param responseConverter responseConverter
     */
    @Autowired
    public JobController(final TaskSorter taskSorter, final ResponseConverter responseConverter) {
        this.taskSorter = taskSorter;
        this.responseConverter = responseConverter;
    }

    /**
     * Endpoint for sorting a list of tasks
     *
     * @param taskList represents a list of tasts
     * @return response with sorted tasks
     */
    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE},
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> sortTasks(@RequestBody @Valid TaskList taskList,
                                            @RequestParam(required = false, defaultValue = "false") boolean bashScript) {
        List<DependentTask> tasks = taskList.getTasks();
        if (tasks == null || tasks.isEmpty()) {
            return generateCorrectResponse(bashScript, new ArrayList<>());
        }
        List<SimpleTask> orderedTasks = taskSorter.sort(tasks);

        return generateCorrectResponse(bashScript, orderedTasks);
    }

    private ResponseEntity<Object> generateCorrectResponse(boolean bashScript, List<SimpleTask> orderedTasks) {
        if (bashScript) {
            Resource resource = new ByteArrayResource(responseConverter.convert(orderedTasks).getBytes());
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
        }
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(orderedTasks);
    }

}
