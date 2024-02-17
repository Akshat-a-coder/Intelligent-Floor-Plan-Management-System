import java.util.List;

public class RoomBooking {

    private int bookingId;
    private List<MeetingRoom> meetingRooms;
    private String startTime;
    private String endTime;
    private int participants;
    private FloorPlanManagement createdBy;

    public RoomBooking(int bookingId, List<MeetingRoom> meetingRooms, String startTime, String endTime,
                       int participants, FloorPlanManagement createdBy) {
        this.bookingId = bookingId;
        this.meetingRooms = meetingRooms;
        this.startTime = startTime;
        this.endTime = endTime;
        this.participants = participants;
        this.createdBy = createdBy;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public int getBookingId() {
        return bookingId;
    }

    public int getParticipants() {
        return participants;
    }

    public FloorPlanManagement getCreatedBy() {
        return createdBy;
    }

    public void bookRoom() throws RoomBookingException {
        boolean roomBooked = false;
        for (MeetingRoom meetingRoom : meetingRooms) {
            if (meetingRoom.isAvailable(startTime, endTime) && meetingRoom.hasCapacity(participants)) {
                System.out.println("Room booked: " + meetingRoom.getRoomNo());
                roomBooked = true;
                break; 
            }
        }
        if (!roomBooked) {
            System.out.println("Booking failed. Room not available or capacity exceeded.");
            throw new RoomBookingException("Booking failed. Room not available or capacity exceeded.");
        }
    }
    
}


