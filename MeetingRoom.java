import java.util.ArrayList;
import java.util.List;

public class MeetingRoom {
    private final int roomNo;
    private final int capacity;
    private final int floor;
    private final List<RoomBooking> bookings;

    public MeetingRoom(int roomNo, int capacity, Integer floor) {
        this.roomNo = roomNo;
        this.capacity = capacity;
        this.floor = floor;
        this.bookings = new ArrayList<>();
    }

    public boolean isAvailable(String startTime, String endTime) {
        // Check if the room is available during the specified time
        for (RoomBooking booking : bookings) {
            if (isTimeConflict(startTime, endTime, booking.getStartTime(), booking.getEndTime())) {
                return false;
            }
        }
        return true;
    }

    public boolean hasCapacity(int participants) {
        // Check if the room has sufficient capacity
        return capacity >= participants;
    }


    private boolean isTimeConflict(String start1, String end1, String start2, String end2) {
        //Here we can check conflict based on data types 
        return !(end1.compareTo(start2) <= 0 || start1.compareTo(end2) >= 0);
    }

    public int getRoomNo(){
        return roomNo;
    }

    public void addBooking(RoomBooking booking) {
        bookings.add(booking);
    }
}
