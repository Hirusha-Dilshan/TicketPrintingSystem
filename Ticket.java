package concurrentAssignment;

public class Ticket {

    private final Integer id;
    private final float price;
    private final String name;
    private String number;
    private String email;
    private final String start;
    private final String end;

    public Ticket(int id, float price, String name, String number, String email,
                  String start, String end) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.number = number;
        this.email = email;
        this.start = start;
        this.end = end;
    }

    public Integer getticketNumber() {
        return id;
    }

    public float getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id+
                '}';
    }
}
