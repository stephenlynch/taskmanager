package org.iptiq.techexercise.core;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicBoolean;

final public class Process extends Thread {

    public enum TaskPriority {LOW, MEDIUM, HIGH};

    private AtomicBoolean running = new AtomicBoolean(true);
    final private String pid;
    final private TaskPriority taskPriority;
    private final Instant creationTime = Instant.now();

    public Process(String pid, TaskPriority taskPriority){
        this.pid = pid;
        this.taskPriority = taskPriority;
    }

    public Instant getCreationTime() {
        return this.creationTime;
    }

    public String getPid() {
        return this.pid;
    }

    public TaskPriority getTaskPriority() {
        return this.taskPriority;
    }

    public void start() {
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (running.get()) {
            try {
                System.out.printf(" task running is pid %s priority %s%n" ,getPid(), getTaskPriority());
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public synchronized void kill() {
        running.set(false);
    }
}
