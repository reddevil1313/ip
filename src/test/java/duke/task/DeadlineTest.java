package duke.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class DeadlineTest {
    @Test
    public void createNewDeadlineTest() {
        Deadline deadline = new Deadline("Submission", LocalDate.parse("2021-02-11"));
        assertEquals("[D][ ] Submission (by: Feb 11 2021)", deadline.toString());
    }

    @Test
    public void markDeadlineAsDoneTest() {
        Deadline deadline = new Deadline("Submission", LocalDate.parse("2021-02-11"));
        deadline.completeTask();
        assertEquals("[D][x] Submission (by: Feb 11 2021)", deadline.toString());
    }
}
