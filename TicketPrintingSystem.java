package concurrentAssignment;

import concurrentAssignment.Technician.TicketPaperTechnician;
import concurrentAssignment.Technician.TicketTonerTechnician;

public class TicketPrintingSystem {

    public static void main(String[] args) {

        TicketMachine ticketMachine = new TicketMachine(" TicketMachine1", 1, 2);

        Passenger Hirusha = new Passenger(
                ticketMachine,
                "Hirusha",
                "123",
                "passenger1@iit.com",
                "Matara",
                "Kandy");
        Passenger sachin = new Passenger(
                ticketMachine,
                "Sachin",
                "23232",
                "sachin@iit.com",
                "Matara",
                "Kandy");
        Passenger rahul = new Passenger(
                ticketMachine,
                "Rahul",
                "2323",
                "rahul@iit.com",
                "Galle",
                "kandy");
        Passenger ashen = new Passenger(
                ticketMachine,
                "Ashen",
                "3232",
                "ashen@iit.com",
                "kandy",
                "Galle");

        TicketTonerTechnician tonerTechnician = new TicketTonerTechnician("Toner", ticketMachine);
        TicketPaperTechnician paperTechnician = new TicketPaperTechnician("Paper", ticketMachine);

        ThreadGroup passengers = new ThreadGroup("Passengers");
        ThreadGroup technitians = new ThreadGroup("Technicians");

        Thread hirushaThread = new Thread(passengers, Hirusha, "hirusha");
        Thread sachinT = new Thread(passengers, sachin, "sachin");
        Thread rahulT = new Thread(passengers, rahul, "rahul");
        Thread ashenT = new Thread(passengers, ashen, "ashen");

        Thread tonerTechnicianThread = new Thread(technitians, tonerTechnician, "tonner tec");
        Thread paperTechnicianThread = new Thread(technitians, paperTechnician, "paper tec");

        hirushaThread.start();
        sachinT.start();
        rahulT.start();
        ashenT.start();

        tonerTechnicianThread.start();
        paperTechnicianThread.start();

        technitians.setMaxPriority(Thread.MAX_PRIORITY);

        while (technitians.activeCount() > 0 || passengers.activeCount() > 0) {
            if (passengers.activeCount() == 0) {
                technitians.interrupt();
                break;
            }
        }

        System.out.println("-------------------------------");

        System.out.println(
                "Toner Level:"+ticketMachine.getTonerLevel()); //printing the tonerLevel

        System.out.println(
                "Paper Level:"+ticketMachine.getPaperLevel()); //printing the paperLevel

    }
}
