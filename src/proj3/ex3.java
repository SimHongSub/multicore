package proj3;

import java.util.concurrent.atomic.AtomicInteger;

public class ex3{
    private static AtomicInteger current = new AtomicInteger();

    public static void main(String[] args) {

        for(int i=0; i<5; i++){
            Task task = Task.create();

            task.start();
        }
    }

    private static class Task extends Thread {

        @Override
        public void run(){

            for(int i=0; i<5; i++){
                int num = current.incrementAndGet();
                System.out.println(this.getName() + " set num : " + num);
            }

        }

        public static Task create(){
            Task task = new Task();

            return task;
        }
    }

}

