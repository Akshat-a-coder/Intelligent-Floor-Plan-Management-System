import java.util.*;


public class FloorPlan {
    private int floorPlanId;
    private String floorPlanName;
    private ArrayList<MeetingRoom> MeetingRooms;
    private Date createdDate;
    private FloorPlanManagement createdBy;
    private DataCache dataCache;
    private int priority;
    private Date lastModified;
    private int version;
    
    
    public FloorPlan(int floorPlanId, String floorPlanName, int version, Date lastModified,
                    ArrayList<MeetingRoom> MeetingRooms, Date createdDate, FloorPlanManagement createdBy , int priority) {
        this.floorPlanId = floorPlanId;
        this.floorPlanName = floorPlanName;
        this.version = version;
        this.lastModified = lastModified;
        this.MeetingRooms = new ArrayList<>(MeetingRooms); 
        this.createdDate = new Date(createdDate.getTime()); 
        this.createdBy = createdBy;
        this.priority = priority;
        this.dataCache= new DataCache();
    }

    public class DataCache {
        private Map<String, Object> cache;

        public DataCache() {
            this.cache = new HashMap<>();
        }

        public Object getFromCache(String key) {
            return cache.get(key);
        }

        public void addToCache(String key, Object data) {
            cache.put(key, data);
        }

        public void removeFromCache(String key) {
            cache.remove(key);
        }

        public void clearCache() {
            cache.clear();
        }
    }

    public int getFloorPlanId() {
        return floorPlanId;
    }

    public String getFloorPlanName() {
        return floorPlanName;
    }

    public int getVersion() {
        return version;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public ArrayList<MeetingRoom> getMeetingRooms() {
        return MeetingRooms;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public FloorPlanManagement getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(FloorPlanManagement createdBy) {
        this.createdBy = createdBy;
    }
    public void setPriority(final int priority){
        this.priority = priority;
    }

    public int getPriority(){
        return priority;
    }

    
    public void addMeetingRoom(MeetingRoom MeetingRoom) {
        MeetingRooms.add(MeetingRoom);
        dataCache.addToCache("MeetingRoom_" + MeetingRoom.getRoomNo(), MeetingRoom);
    }

    public void removeMeetingRoom(MeetingRoom MeetingRoom) {
        MeetingRooms.remove(MeetingRoom);
        dataCache.removeFromCache("MeetingRoom_" + MeetingRoom.getRoomNo());
    }

    public MeetingRoom getMeetingRoomById(final int MeetingRoomId) {
        final MeetingRoom cachedMeetingRoom = (MeetingRoom) dataCache.getFromCache("MeetingRoom_" + MeetingRoomId);
        if (cachedMeetingRoom != null) {
            System.out.println("MeetingRoom found in cache!");
            return cachedMeetingRoom;
        }
        else{
            for (MeetingRoom MeetingRoom : MeetingRooms) {
                if (MeetingRoom.getRoomNo() == MeetingRoomId) {
                    return MeetingRoom;
                }
            }
        }
        return null; // MeetingRoom not found
    }

   
    public void uploadPlan() {
        this.version++;
        this.lastModified = new Date();

        System.out.println("Floor plan uploaded. New version " + version);
    }
}


