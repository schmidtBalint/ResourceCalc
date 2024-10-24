public class Dataset {
    private String customerId;
    private String workloadId;
    private long timestamp;
    private String eventType;

    // Getters
    public String getCustomerId() {
        return customerId;
    }

    public String getWorkloadId() {
        return workloadId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getEventType() {
        return eventType;
    }

    // Setters
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setWorkloadId(String workloadId) {
        this.workloadId = workloadId;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
