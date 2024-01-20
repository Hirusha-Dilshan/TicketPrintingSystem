package concurrentAssignment;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TicketMachine implements ServiceTicketMachine {

    private String ticketMachineName;
    private int paperLevel;
    private int tonerLevel;
    private int countTicketId;
    public static int replaceTonerCartridgeCount = 0;
    public static int refillPaperPacksCount = 0;

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition haveTonerLevel = lock.newCondition();
    private final Condition havePaperLevel = lock.newCondition();
    private final Condition noResource = lock.newCondition();

    public TicketMachine(String ticketMachineName, int paperLevel, int tonerLevel) {
        this.ticketMachineName = ticketMachineName;
        this.paperLevel = paperLevel;
        this.tonerLevel = tonerLevel;
        this.countTicketId = 0;
    }

    public Ticket getTicket(String passengerName, String phoneNumber, String emailAddress, String arrivalLocation,
                            String departureLocation) {
        Ticket ticket = buy(passengerName, phoneNumber, emailAddress, arrivalLocation, departureLocation);
        printTicket(ticket);
        System.out.println("Passenger " + ticket.getName() + " got " + ticket);
        return ticket;
    }

    @Override
    public Ticket buy(String passengerName, String phoneNumber, String emailAddress, String arrivalLocation,
                      String departureLocation) {
        lock.lock();
        try {
            Ticket ticket = new Ticket(countTicketId + 1, 1000,
                    passengerName, phoneNumber, emailAddress, arrivalLocation, departureLocation);
            countTicketId++;
            havePaperLevel.signalAll();
            haveTonerLevel.signalAll();
            return ticket;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void printTicket(Ticket ticket) {
        lock.lock();
        try {
            Thread.sleep(new Random().nextInt(1000) + 1000);
            while (!(paperLevel > 0) || !(tonerLevel > ServiceTicketMachine.MIN_TONER_LEVEL)) {
                System.out.println("Thread passenger: " + ticket.getName() + " waiting ");
                noResource.await();
            }
            paperLevel--;
            tonerLevel--;
            System.out.println("Ticket id: " + ticket.getticketNumber().toString() + " printed");
            havePaperLevel.signalAll();
            haveTonerLevel.signalAll();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void replaceTonerCartridge() {
        lock.lock();
        try {
            while (tonerLevel >= ServiceTicketMachine.MIN_TONER_LEVEL) {
                System.out.println("Already has a good toner level");
                haveTonerLevel.await();
            }
            tonerLevel = ServiceTicketMachine.MAX_TONER_LEVEL;
            replaceTonerCartridgeCount++;
            System.out.println("Filled tonner");
            noResource.signalAll();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void refillTicketPaper() {
        lock.lock();
        try {
            while ((paperLevel + ServiceTicketMachine.SHEETS_PER_PACK) > ServiceTicketMachine.FULL_PAPER_TRAY) {
                System.out.println("Already have a good paper level");
                havePaperLevel.await();
            }
            paperLevel += ServiceTicketMachine.SHEETS_PER_PACK;
            refillPaperPacksCount++;
            System.out.println("Filled ticket papers");
            noResource.signalAll();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int getPaperLevel() {
        return paperLevel;
    }

    @Override
    public int getTonerLevel() {
        return tonerLevel;
    }
}
