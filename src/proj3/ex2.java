package proj3;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ex2 {

    private static StringBuilder text = new StringBuilder();

    public static void main(String[] args) {

        for(int i=0; i<5; i++){
            Task task = Task.create();

            task.start();
        }
    }

    private static class Task extends Thread {

        ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

        @Override
        public void run() {
            while(true){
                try {
                    sleep((int)(Math.random() * 10000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                write();

                try {
                    sleep((int)(Math.random() * 20000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                read();

            }
        }

        private void write(){
            readWriteLock.writeLock().lock();

            System.out.println("ReadWriteLock " + this.getName() + " write.");

            text.append("[");
            text.append(this.getName());
            text.append("]");

            readWriteLock.writeLock().unlock();
        }

        private void read(){

            readWriteLock.readLock().lock();

            System.out.println("ReadWriteLock " + this.getName() + " read.");

            System.out.println(text);

            readWriteLock.readLock().unlock();
        }

        public static Task create(){
            Task task = new Task();

            return task;
        }
    }

}
