package proj3;

import java.util.concurrent.Semaphore;

/**
 * ParkingGarage class
 */
class ParkingGarage {
    private Semaphore semaphore;

    public ParkingGarage(Semaphore semaphore) {
        this.semaphore = semaphore;
    }

    public void enter(String name) { // enter parking garage
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void leave() { // leave parking garage
        semaphore.release();
    }
}

/**
 * Car class
 */
class Car extends Thread {
    private ParkingGarage parkingGarage;

    public Car(String name, ParkingGarage p) {
        super(name);
        this.parkingGarage = p;
        start();
    }

    public void run() {
        while (true) {
            try {
                sleep((int)(Math.random() * 10000)); // drive before parking
            } catch (InterruptedException e) {}

            System.out.println(this.getName()+": trying to enter");
            parkingGarage.enter(this.getName());
            System.out.println(this.getName()+": entered");

            try {
                sleep((int)(Math.random() * 20000)); // stay within the parking garage
            } catch (InterruptedException e) {}

            parkingGarage.leave();
            System.out.println(this.getName()+": left");
        }
    }
}

/**
 * ParkingGarageOperation class
 */
public class ParkingSemaphore {
    public static void main(String[] args){
        Semaphore semaphore = new Semaphore(10);
        ParkingGarage parkingGarage = new ParkingGarage(semaphore);

        for (int i=1; i<= 40; i++) {
            Car c = new Car("Car "+i, parkingGarage);
        }
    }
}

