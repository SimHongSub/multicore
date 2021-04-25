import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Implement multithreaded version of pc_serial.java
 *     using static load balancing.
 *
 */
public class pc_static {
    /**
     * NUM_END : Range of numbers on which to perform prime number check
     * NUM_THREAD : Number of threads
     * primeCountList : List to store each thread execution result (Number of prime)
     */
    private static final int NUM_END = 200000;
    private static final int NUM_THREAD = 32;
    private static List<Integer> primeCountList = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        /**
         * interval : Number of numbers to allocate to each thread
         */
        int i;
        int interval = (NUM_END/NUM_THREAD);

        /**
         * blockingQueue : Queue to manage multiple tasks in a thread pool
         * threadPoolExecutor : Thread pool to manage multiple threads at once
         *
         * If more tasks are created than the number of threads,
         * the task waits in the queue and a valid thread executes the task.
         */
        BlockingQueue<Runnable> blockingQueue = new LinkedBlockingDeque<>();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(NUM_THREAD, NUM_THREAD,
                1, TimeUnit.SECONDS, blockingQueue);

        long startTotalTime = System.currentTimeMillis();

        /**
         * Calculate the range of numbers to allocate to each thread.
         * Create tasks as many as the number of threads.
         */
        for (i=0;i<NUM_THREAD;i++) {
            int startNum = interval * i;
            int endNum;

            if(i == NUM_THREAD-1){
                endNum = NUM_END;
            }else{
                endNum = interval * (i+1);
            }

            Runnable task = Task.createTask(startNum, endNum);

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

            Integer totalPrimeCount = 0;

            // Counting the total number of prime
            for (Integer count : primeCountList) {
                totalPrimeCount += count;
            }

            System.out.println("1..."+(NUM_END-1)+" prime# counter=" + totalPrimeCount);
        }
    }

    /**
     * Implement tasks to be performed by each thread.
     *
     * implements Runnable class
     */
    private static class Task implements Runnable {
        /**
         * startNum, endNUm
         * : Range of numbers each thread will perform prime number check.
         *
         */
        private int startNum;
        private int endNum;

        @Override
        public void run() {
            int counter = 0;
            int i;

            long startTime = System.currentTimeMillis();

            /**
             * Determine whether the number from startNum to endNum-1 is prime.
             *
             * if it is a prime number, increase counter 1
             */
            for(i = this.startNum; i < this.endNum; i++){
                if(isPrime(i)) counter++;
            }

            long endTime = System.currentTimeMillis();
            long timeDiff = endTime - startTime;

            /**
             * Save counter to primeCountList
             */
            primeCountList.add(counter);

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
         * @param startNum
         * @param endNum
         * @return
         */
        public static Task createTask(int startNum, int endNum) {
            Task task =  new Task();

            task.setStartNum(startNum);
            task.setEndNum(endNum);

            return task;
        }

        /**
         * private variable set method
         *
         */
        public void setStartNum(int startNum) {
            this.startNum = startNum;
        }

        public void setEndNum(int endNum) {
            this.endNum = endNum;
        }
    }
}
