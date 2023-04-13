package com.github.protocolfuzzing.protocolstatefuzzer.utils;

import java.util.LinkedList;
import java.util.List;

/**
 * Collection of tasks which are executed on termination, an alternative to shutdown hook.
 */
public class CleanupTasks {
    /**
     * List of cleanup tasks to be run.
     */
    protected List<Runnable> tasks;

    /**
     * Class constructor, which creates a new {@link #tasks} list.
     */
    public CleanupTasks() {
        tasks = new LinkedList<>();
    }

    /**
     * Adds a new runnable task to the {@link #tasks}.
     *
     * @param runnable  task to be run during {@link #execute()}
     */
    public void submit(Runnable runnable) {
        tasks.add(runnable);
    }

    /**
     * Executes the {@link #tasks} consecutively.
     */
    public void execute() {
        for (Runnable task : tasks) {
            task.run();
        }
    }
}
