package duke;


import java.time.LocalDate;
import java.util.ArrayList;

import duke.task.Deadline;
import duke.task.Event;
import duke.task.Task;
import duke.task.ToDo;


/**
 * TaskList represents all the tasks contained in a Duke Chatbot.
 *
 * @author Samay Sagar
 * @version CS2103 AY21/22 Sem 1
 */
//Solution below adapted from https://github.com/jovyntls/ip
public class TaskList {
    private ArrayList<Task> tasks;
    /**
     * A constructor for a duke.TaskList which contains Tasks.
     */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /**
     * Converts a taskList to a text format that can be saved in a txt file.
     * @return a String that represents the savable data of a duke.TaskList.
     */
    public String toSaveData() {
        String data = "";
        for (Task task : tasks) {
            data += task.toSaveData();
        }
        return data;
    }
    /**
     * Adds an existing task to the list of tasks.
     * @param task The task to be added to the list of tasks.
     */
    public void addTask(Task task) {
        tasks.add(task);
    }
    /**
     * Given a string, creates a To-do from that string and adds it to the list of task.
     * @param taskTitle a String of the title of the To-do to be added.
     * @return the newly created To-do
     */
    public String addNewTodo(String taskTitle) {
        ToDo task = new ToDo(taskTitle);
        tasks.add(task);
        return "Got it. I've added this task:\n\t" + task.toString() + countTasks();
    }
    /**
     * Given a string, creates a Deadline from that string and adds it to the list of task.
     * @param taskTitle a String of the title of the Deadline to be added.
     * @return the newly created Deadline.
     */
    public String addNewDeadline(String taskTitle) throws DukeException {
        if (taskTitle.equals("d")) {
            throw new DukeException("You need to specify which event you want to add!\n");
        }

        final int eventStartId = 2;
        String eventDetails = taskTitle.substring(eventStartId);
        String[] commandSplit = splitCommand(eventDetails, "/by"); // "taskName /at datetime"
        String task = getTask(commandSplit);

        int delimiter = taskTitle.indexOf("/by ");
        LocalDate due = LocalDate.parse(taskTitle.substring(delimiter + 4));
        Deadline deadline = new Deadline(taskTitle.substring(0, delimiter), due);
        tasks.add(deadline);
        return "Got it. I've added this task:\n\t" + deadline.toString() + countTasks();
    }

    /**
     * Given a string, creates a Deadline from that string and adds it to the list of task.
     * @param taskTitle a String of the title of the Deadline to be added.
     * @return the newly created Deadline.
     */
    public String addNewEvent(String taskTitle) throws DukeException {
        if (taskTitle.equals("e")) {
            throw new DukeException("You need to specify which event you want to add!\n");
        }

        final int eventStartId = 2;
        String eventDetails = taskTitle.substring(eventStartId);
        String[] commandSplit = splitCommand(eventDetails, "/at"); // "taskName /at datetime"
        String task = getTask(commandSplit);

        int delimiter = taskTitle.indexOf("/at ");
        LocalDate due = LocalDate.parse(taskTitle.substring(delimiter + 4));
        Event event = new Event(taskTitle.substring(0, delimiter), due);
        tasks.add(event);
        return "Got it. I've added this task:\n\t" + event.toString() + countTasks();
    }

    /**
     * Splits the command into task and datetime.
     *
     * @param command User input to extract task and dateTime
     * @param by      The string to split the command by
     * @return The task and dateTime in a String array
     * @throws DukeException if Duke specific error
     */
    private static String[] splitCommand(String command, String by) throws DukeException {
        String[] commandSplit = command.split(by);

        // If cannot split the command
        if (commandSplit.length <= 1) {
            throw new DukeException("You need to provide a date/time! Format: YYYY-MM-DD" + "\n");
        }

        return commandSplit;
    }

    /**
     * Gets the task from the split original command.
     *
     * @param commandSplit the String array of the split command
     * @throws DukeException if task not specified
     */
    private static String getTask(String[] commandSplit) throws DukeException {
        String task = commandSplit[0].trim(); // Trim the first part of the original command

        if (task.isEmpty()) {
            throw new DukeException("You need to provide a task!" + "\n");
        }

        return task;
    }

    /**
     * Gets the datetime from the split original command.
     *
     * @param commandSplit the original command split into 2 parts
     * @return the datetime in String format
     */
    private static String getDateTime(String[] commandSplit) {
        assert commandSplit.length > 1;
        return commandSplit[1].trim(); // Get the 2nd part of the command
    }

    /**
     * Given the index number of a task, marks that task as completed.
     *
     * @param taskNumber an int representing the index of the task
     * @return the String representation of the completed task
     */
    public String completeTask (int taskNumber) throws DukeException {
        int taskIndex = taskNumber - 1;
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            throw new DukeException("The task you are trying to mark done does not exist. :(");
        }
        Task task = tasks.get(taskIndex);
        task.completeTask();
        return "Nice! I've marked this task as done:\n\t" + task.toString();
    }
    /**
     * Tells the user how many tasks there are in the list.
     *
     * @return A string that contains the number of tasks in the list.
     */
    public String countTasks() {
        return String.format("\nNow you have %d tasks in the list.", tasks.size());
    }

    /**
     * Returns the list of tasks that match a given keyword or phrase.
     * @param keyword a String that must be contained by tasks.
     * @return a filtered list of tasks that contain the keyword.
     */
    public String findTasks(String keyword) {
        TaskList filteredList = new TaskList();
        for (Task task : tasks) {
            if (task.titleContains(keyword)) {
                filteredList.addTask(task);
            }
        }
        return String.format("I've filtered tasks containing '%s'.\n", keyword)
                + filteredList.toString();
    }

    /**
     * Deletes a task when given its index number.
     *
     * @param taskNumber an int representing the index of the task
     * @return the String representation of the deleted task
     */
    public String deleteTask(int taskNumber) throws DukeException {
        int taskIndex = taskNumber - 1;
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            throw new DukeException("The task you are trying to delete does not exist. :(");
        }
        return "Noted. I've removed this task:\n\t"
                + tasks.remove(taskIndex).toString()
                + countTasks();
    }
    /**
     * Returns a string representing the list of tasks.
     *
     * @return A string representing the task list
     */
    @Override
    public String toString() {
        String output = "Here are the tasks in your list:";
        for (int i = 0; i < tasks.size(); i++) {
            int index = i + 1;
            output += "\n" + index + "." + tasks.get(i).toString();
        }
        return output;
    }
}
