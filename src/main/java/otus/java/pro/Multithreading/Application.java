package otus.java.pro.Multithreading;

public class Application {
    public static void main(String[] args) {
        CustomThreadPool customThreadPool = new CustomThreadPool(10);

        for (int i = 0; i < 100; i++) {
            int taskId = i;
            customThreadPool.execute(() -> {
                System.out.println("Task #" + taskId + " running in thread " + Thread.currentThread().getName());
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        customThreadPool.shutdown();
    }
}
