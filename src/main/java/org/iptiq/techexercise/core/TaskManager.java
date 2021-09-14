package org.iptiq.techexercise.core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

import org.iptiq.techexercise.config.Config;


public interface TaskManager {

    BlockingDeque<Process> processes = new LinkedBlockingDeque<>(Config.PROCESS_SIZE);
    
    void add(Process process);

    default List<Process> list() {
        return new ArrayList<>(processes);
    }

    default void kill(String pid)  {
        processes.stream().filter(process -> 
            process.getPid().equals(pid)).findFirst()
                .ifPresent(process ->  {
                    processes.remove(process);
                    process.kill();
                });
    }

    default void killAll() {
        processes.forEach(process ->  {
            processes.remove(process);
            process.kill();
        });
    }

    default void killAll(Process.TaskPriority taskPriority) {
        processes.stream().filter(process -> process.getTaskPriority() == taskPriority)
            .forEach(process ->  {
                processes.remove(process);
                process.kill();
            });
    }

    public class TaskManger implements TaskManager {
        @Override
        public synchronized void add(Process process) {
            if (processes.remainingCapacity() > 0) {
                processes.add(process);
                process.start();
            }
        }
    }

    public class FIFOTaskManger implements TaskManager {
        @Override
        public synchronized void add(Process process) {
            if (processes.remainingCapacity() == 0) {
                processes.removeFirst().kill();
            }
            processes.add(process);
            process.start(); 
        }
    }

    public class PriorityTaskManger implements TaskManager {
        @Override
        public synchronized void add(Process process) {
            if (processes.remainingCapacity() == 0) {
                processes.stream()
                    .sorted(Comparator.comparing(Process::getTaskPriority))
                        .findFirst().ifPresent(p ->  {
                            processes.remove(p);
                            p.kill();
                        });
            }
            processes.add(process);
            process.start();
        }

        @Override
        public List<Process> list() {
            return processes.stream()
                .sorted(Comparator.comparing(Process::getCreationTime))
                    .collect(Collectors.toList());
        }
    }
}


