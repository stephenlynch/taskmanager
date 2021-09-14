package org.iptiq.techexercise;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import org.iptiq.techexercise.core.Process;
import org.iptiq.techexercise.core.TaskManager;

public class TaskManagerRunner {
    
    public static void main(String[] args) {
        testTaskManager();
        testFIFOTaskManager();
        testPriorityTaskManager();
    }

    static void testTaskManager() {

        TaskManager taskManager = new TaskManager.TaskManger();
        taskManager.add(new Process("1", Process.TaskPriority.LOW ));
        taskManager.add(new Process("2", Process.TaskPriority.HIGH ));
        taskManager.add(new Process("3", Process.TaskPriority.MEDIUM ));
        taskManager.add(new Process("4", Process.TaskPriority.HIGH ));

        System.out.printf("%n%nadd 4 processes, size should be 3 is: %n%s%n%n", taskManager.list().size());

        System.out.printf("list running processes: %n%s%n%n", taskManager.list().stream().map(process -> 
            new StringBuilder().append(process.getPid()).append(" ").append(process.getTaskPriority()))
                .collect(Collectors.joining("\n")));

        taskManager.kill("3");
        System.out.printf("kill process 3: %n%s%n%n", taskManager.list().stream().map(process -> 
        new StringBuilder().append(process.getPid()).append(" ").append(process.getTaskPriority()))
            .collect(Collectors.joining("\n")));

        taskManager.killAll();
        System.out.printf("killAll should be 0 is: %n%s%n%n", taskManager.list().size());

        taskManager = new TaskManager.TaskManger();
        taskManager.add(new Process("1", Process.TaskPriority.LOW ));
        taskManager.add(new Process("2", Process.TaskPriority.HIGH ));
        taskManager.add(new Process("3", Process.TaskPriority.MEDIUM ));
        taskManager.add(new Process("4", Process.TaskPriority.HIGH ));

        taskManager.killAll(Process.TaskPriority.HIGH);
        System.out.printf("killAll by Priority HIGH: %n%s%n%n", taskManager.list().stream().map(process -> 
        new StringBuilder().append(process.getPid()).append(" ").append(process.getTaskPriority()))
            .collect(Collectors.joining("\n")));

        taskManager.killAll();
    }

    static void testFIFOTaskManager() {

        TaskManager taskManager = new TaskManager.FIFOTaskManger();
       
        taskManager.add(new Process("2", Process.TaskPriority.HIGH ));
        taskManager.add(new Process("1", Process.TaskPriority.LOW ));
        taskManager.add(new Process("3", Process.TaskPriority.MEDIUM ));
        taskManager.add(new Process("4", Process.TaskPriority.MEDIUM ));

        System.out.printf("FIFO should be LOW, MEDIUM, MEDIUM: %n%s%n%n", taskManager.list().stream()
            .map(process -> new StringBuilder().append(process.getPid())
                .append(" ").append(process.getTaskPriority()))
                .collect(Collectors.joining("\n")));

        taskManager.killAll();
    }

    static void testPriorityTaskManager() {

        TaskManager taskManager = new TaskManager.PriorityTaskManger();
        taskManager.add(new Process("4", Process.TaskPriority.HIGH ));
        taskManager.add(new Process("2", Process.TaskPriority.MEDIUM ));
        taskManager.add(new Process("1", Process.TaskPriority.LOW ));
        taskManager.add(new Process("13", Process.TaskPriority.MEDIUM ));

        System.out.printf("PRIORITY should be HIGH, MEDIUM, MEDIUM: %n%s%n%n", taskManager.list().stream().map(process -> 
        new StringBuilder().append(process.getPid()).append(" ").append(process.getTaskPriority()))
            .collect(Collectors.joining("\n")));

        System.out.printf("should be ordered by creation time: %n%s%n", taskManager.list().stream().map(process -> 
         new StringBuilder().append(process.getPid()).append(" ")
         .append(process.getTaskPriority()).append(" ")
         .append(DateTimeFormatter.ISO_INSTANT.format(process.getCreationTime()
            .truncatedTo(ChronoUnit.MICROS))).toString()
        ).collect(Collectors.joining("\n")));

        taskManager.killAll();
    }

    
}
