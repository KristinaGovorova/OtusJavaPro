package otus.java.pro.Multithreading;

import java.util.LinkedList;
import java.util.List;

public class CustomThreadPool {

    private final List<WorkingThread> workingThreadList;
    private final LinkedList<Runnable> queue;
    private volatile boolean isShutdown;

    public CustomThreadPool(int capacity) {
        queue = new LinkedList<>();
        workingThreadList = new LinkedList<>();
        isShutdown = false;

        for (int i = 0; i < capacity; i++) {
            WorkingThread workingThread = new WorkingThread();
            workingThreadList.add(workingThread);
            workingThread.start();
        }
    }

    private class WorkingThread extends Thread {

        @Override
        public void run() {
            while (true) {
                Runnable task;
                synchronized (queue) {
                    while (queue.isEmpty() && !isShutdown) {
                        try {
                            queue.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    if (queue.isEmpty() && isShutdown) {
                        return;
                    }
                    task = queue.poll();
                }

                if (task != null) {
                    try {
                        task.run();
                    } catch (RuntimeException e) {
                        System.out.println("Task isn't completed" + e.getMessage());
                    }
                }
            }
        }
    }

    public synchronized void execute(Runnable r) {
        if (isShutdown) {
            throw new IllegalStateException("Thread pool started shutdown!");
        }
        synchronized (queue) {
            queue.add(r);
            System.out.println("New task in queue");
            queue.notifyAll();
        }
    }

    public void shutdown() {
        isShutdown = true;

        synchronized (queue) {
            queue.notifyAll();
            System.out.println("Thread pool started shutdown!");
        }
        for (WorkingThread workingThread : workingThreadList) {
            try {
                workingThread.join();
                System.out.println("Thread #" + workingThread.getName() + " completed work!");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
