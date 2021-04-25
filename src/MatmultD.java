import java.util.*;
import java.lang.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Implement parallel matrix multiplication that uses multi-threads.
 * Use a static load balancing approach.
 */
public class MatmultD {
    /**
     * c = a x b : The result of multiplying two matrices a, b
     */
    private static Scanner sc = new Scanner(System.in);
    private static int[][] c;

    public static void main(String [] args) throws InterruptedException {
        int i;
        int thread_no = 1;

        if (args.length==1) thread_no = Integer.valueOf(args[0]);

        /**
         * blockingQueue : Queue to manage multiple tasks in a thread pool
         * threadPoolExecutor : Thread pool to manage multiple threads at once
         *
         * If more tasks are created than the number of threads,
         * the task waits in the queue and a valid thread executes the task.
         */
        BlockingQueue<Runnable> blockingQueue = new LinkedBlockingDeque<>();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(thread_no, thread_no,
                10, TimeUnit.SECONDS, blockingQueue);

        /**
         * read matrix from input file
         */
        int a[][]=readMatrix();
        int b[][]=readMatrix();

        /**
         * Determine if two matrices can be multiplied
         */
        if (a.length != b[0].length) {
            System.out.println("invalid matrix");

            threadPoolExecutor.shutdown();
            System.exit(1);
        }

        /**
         * Allocate space to store c multiplied by two matrices
         * interval : The range to allocate to each thread
         */
        c = new int[a.length][b[0].length];
        int interval = a.length / thread_no;

        long startTotalTime = System.currentTimeMillis();

        /**
         * Create a task and copy a part of the matrix a (deep copy method) and pass it over
         */
        for(i=0;i<thread_no;i++){
            Runnable task;
            int start = interval * i;

            if (i != thread_no-1){
                task = Task.createTask(start, deepCopy(start, a, interval), b);
            }else{
                task = Task.createTask(start, deepCopy(start, a, (a.length-(interval*i))), b);
            }

            threadPoolExecutor.execute(task);
        }

        threadPoolExecutor.shutdown();

        boolean isTerminated = threadPoolExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);

        long endTotalTime = System.currentTimeMillis();
        long totalTimeDiff = endTotalTime - startTotalTime;

        if(isTerminated){
            printMatrixSum(c);
            System.out.println("Execution Time using all thread : " + totalTimeDiff + "ms");
        }
    }

    /**
     * Method to read matrix from input file
     *
     * @return
     */
    public static int[][] readMatrix() {
        int rows = sc.nextInt();
        int cols = sc.nextInt();
        int[][] result = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = sc.nextInt();
            }
        }
        return result;
    }

    /**
     * Method to print matrix sum
     *
     * @param mat
     */
    public static void printMatrixSum(int[][] mat) {
        int rows = mat.length;
        int columns = mat[0].length;
        int sum = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                sum+=mat[i][j];
            }
        }
        System.out.println();
        System.out.println("Matrix Sum = " + sum + "\n");
    }

    /**
     * Method to copy part of matrix
     *
     * @param start
     * @param original
     * @param size
     * @return
     */
    private static int[][] deepCopy(int start, int[][] original, int size) {
        if(original == null) return null;
        int[][] result = new int[size][original[0].length];

        for(int i=0; i<size; i++){
            System.arraycopy(original[start+i], 0, result[i], 0, original[0].length);
        }

        return result;
    }

    /**
     * Implement tasks to be performed by each thread.
     *
     * implements Runnable class
     */
    private static class Task implements Runnable {

        /**
         * start : Variable to find the location of c where the calculation result will be saved
         * a, b : Two matrices to be multiplied in each thread.
         */
        private int start;
        private int a[][];
        private int b[][];

        @Override
        public void run() {
            long startTime = System.currentTimeMillis();

            multiMatrix();

            long endTime = System.currentTimeMillis();
            long timeDiff = endTime - startTime;

            System.out.println(Thread.currentThread().getName() + " Execution Time : " + timeDiff + "ms");
        }

        /**
         * Store the result of a x b in c
         */
        private void multiMatrix(){
            int n = a[0].length;
            int m = a.length;
            int p = b[0].length;

            for(int i = 0;i < m;i++){
                for(int j = 0;j < p;j++){
                    for(int k = 0;k < n;k++){
                        c[start + i][j] += a[i][k] * b[k][j];
                    }
                }
            }
        }

        /**
         * Task creation method
         *
         * @param start
         * @param a
         * @param b
         * @return
         */
        public static Task createTask(int start, int a[][], int b[][]) {
            Task task = new Task();

            task.setStart(start);
            task.setA(a);
            task.setB(b);

            return task;
        }

        /**
         * private variable set method
         *
         */
        public void setStart(int start) {
            this.start = start;
        }

        public void setA(int[][] a) {
            this.a = a;
        }

        public void setB(int[][] b) {
            this.b = b;
        }
    }
}