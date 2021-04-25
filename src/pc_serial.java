public class pc_serial {
    private static final int NUM_END = 200000;

    public static void main(String[] args) {
        int counter = 0;
        int i;

        long startTime = System.currentTimeMillis();

        for(i=0;i<NUM_END;i++){
            if(isPrime(i)) counter++;
        }

        long endTime = System.currentTimeMillis();
        long timeDiff = endTime - startTime;

        System.out.println("Execution Time : " + timeDiff + "ms");
        System.out.println("1..."+(NUM_END-1)+" prime# counter=" + counter + "\n");
    }

    private static boolean isPrime(int x) {
        int i;

        if(x<=1) return false;

        for(i=2;i<x;i++) {
            if(x % i == 0) return false;
        }

        return true;
    }
}
