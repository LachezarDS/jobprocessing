package co.sumup.jobprocessing.utility;

import co.sumup.jobprocessing.task.BashResponseConverter;
import co.sumup.jobprocessing.task.SimpleTask;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BashResponseConverterTest {

    private final BashResponseConverter responseConverter = new BashResponseConverter();

    @Test
    public void convert() {
        SimpleTask task1 = new SimpleTask("name1", "command1");
        SimpleTask task2 = new SimpleTask("name2", "command2");

        String result = responseConverter.convert(Arrays.asList(task1, task2));

        assertEquals("#!/usr/bin/env bash\n\ncommand1\ncommand2\n", result);
    }
}