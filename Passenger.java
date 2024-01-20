package concurrentAssignment;

import java.util.Random;

public class Passenger implements Runnable {

    private TicketMachine machine;
    private String name;
    private String number;
    private String email;
    private final String start;
    private final String end;



    public Passenger(TicketMachine machine, String name, String number, String email,
                     String start, String departureLocation) {
        this.machine = machine;
        this.name = name;
        this.number = number;
        this.email = email;
        this.start = start;
        this.end = departureLocation;
    }

    @Override
    public void run() {
        Random r = new Random();
        machine.getTicket(this.name, this.number, this.email, this.start,
                this.end);
        try {
            Thread.sleep(r.nextInt(1000) + 1000); //using the random sleep interval
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
