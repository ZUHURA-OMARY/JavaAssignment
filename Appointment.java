public class Appointment {
    private String date;
    private String time;
    private String details;

    public Appointment(String date, String time, String details) {
        this.date = date;
        this.time = time;
        this.details = details;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDetails() {
        return details;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return date + " " + time + " : " + details;
    }

}
