import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * ParkingGarage class
 */
class ParkingGarage {
  private BlockingQueue places;

  public ParkingGarage(BlockingQueue queue) {
    this.places = queue;
  }

  public void enter(String name) { // enter parking garage
    try {
      places.put(name);
    } catch (InterruptedException e) {}
  }

  public void leave() { // leave parking garage
    try {
      places.take();
    } catch (InterruptedException e) {}
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
public class ParkingGarageOperation {
  public static void main(String[] args){
    BlockingQueue queue = new ArrayBlockingQueue<String>(10);
    ParkingGarage parkingGarage = new ParkingGarage(queue);
    for (int i=1; i<= 40; i++) {
      Car c = new Car("Car "+i, parkingGarage);
    }
  }
}

