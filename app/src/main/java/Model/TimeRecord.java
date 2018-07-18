package Model;

/**
 * Created by ashwin on 2/24/2018.
 */

public class TimeRecord {
    private int id;
    private long timeMillis;
    private boolean isFirst;

    public TimeRecord() {
    }

    public TimeRecord(int id, long timeMillis, boolean isFirst) {
        this.id = id;
        this.timeMillis = timeMillis;
        this.isFirst = isFirst;
    }

    public TimeRecord(long timeMillis, boolean isFirst) {
        this.timeMillis = timeMillis;
        this.isFirst = isFirst;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimeMillis() {
        return timeMillis;
    }

    public void setTimeMillis(long timeMillis) {
        this.timeMillis = timeMillis;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }
}
