import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Implement multithreaded version of pc_serial.java
 *     using dynamic load balancing.
 *
 */
public class pc_dynamic {
    /**
     * NUM_END : Range of numbers on which to perform prime number check
     * NUM_THREAD : Number of threads
     * current : The number to be checked now
     * counter : Total number of prime numbers
     *
     * current, counter is atomic variable, so solve concurrency
     */
    private static final int NUM_END = 200000;
    private static final int NUM_THREAD = 1;
    private static AtomicInteger current = new AtomicInteger();
    private static AtomicInteger counter = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {
        int i;

        /**
         * blockingQueue : Queue to manage multiple tasks in a thread pool
         * threadPoolExecutor : Thread pool to manage multiple threads at once
         *
         * If more tasks are created than the number of threads,
         * the task waits in the queue and a valid thread executes the task.
         */
        BlockingQueue<Runnable> blockingQueue = new LinkedBlockingDeque<>();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(NUM_THREAD, NUM_THREAD,
                10, TimeUnit.SECONDS, blockingQueue);

        long startTotalTime = System.currentTimeMillis();

        /**
         * Create tasks as many as the number of threads.
         */
        for (i=0;i<NUM_THREAD;i++) {

            Runnable task = Task.createTask();

            threadPoolExecutor.execute(task);
        }

        /**
         * Make sure all threads have finished their work
         * Wait if there is still a running thread
         */
        threadPoolExecutor.shutdown();

        boolean isTerminated = threadPoolExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);

        long endTotalTime = System.currentTimeMillis();
        long totalTimeDiff = endTotalTime - startTotalTime;

        if(isTerminated){
            System.out.println("\nExecution Time using all thread : " + totalTimeDiff + "ms");
            System.out.println("1..."+(NUM_END-1)+" prime# counter=" + counter);
        }
    }

    /**
     * Implement tasks to be performed by each thread.
     *
     * implements Runnable class
     */
    private static class Task implements Runnable {

        @Override
        public void run() {
            long startTime = System.currentTimeMillis();

            /**
             * Each thread iterates over to get the atomic variable current value and increments it by 1.
             *
             * If the imported value exceeds the NUM_END value, the run ends.
             *
             * If the imported value is a prime number, increment the counter variable by 1.
             */
            while (true) {
                int num = current.incrementAndGet();

                if(num >= NUM_END){
                    break;
                }

                if(isPrime(num)){
                    counter.incrementAndGet();
                }
            }

            long endTime = System.currentTimeMillis();
            long timeDiff = endTime - startTime;

            System.out.println(Thread.currentThread().getName() + " Execution Time : " + timeDiff + "ms");
        }

        /**
         * Prime number determination method
         *
         * @param x
         * @return
         */
        private static boolean isPrime(int x) {
            int i;

            if(x<=1) return false;

            for(i=2;i<x;i++) {
                if(x % i == 0) return false;
            }

            return true;
        }

        /**
         * Task creation method
         *
         * @return
         */
        public static Task createTask() {
            Task task =  new Task();

            return task;
        }
    }
}
