package proj3;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ex1 {

    public static void main(String[] args) {
        BlockingQueue<String> queue = new ArrayBlockingQueue<String>(10);

        for(int i=0; i<30; i++){
            Task task = Task.create(queue);

            task.start();
        }
    }

    private static class Task extends Thread {

        private BlockingQueue<String> queue;

        @Override
        public void run() {
            while(true){
                try {
                    sleep((int)(Math.random() * 10000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(this.getName() + " try to execution.");
                execution();
                System.out.println("BlockingQueue " + this.getName() + " execution.");

                try {
                    sleep((int)(Math.random() * 20000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                terminate();
                System.out.println("BlockingQueue " + this.getName() + " terminate.");
            }
        }

        private void execution(){
            try {
                queue.put(this.getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void terminate(){
            try {
                queue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public static Task create(BlockingQueue<String> queue){
            Task task = new Task();
            task.setQueue(queue);

            return task;
        }

        public void setQueue(BlockingQueue<String> queue) {
            this.queue = queue;
        }
    }
}
