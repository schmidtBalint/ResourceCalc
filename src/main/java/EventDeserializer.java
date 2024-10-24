import com.google.gson.*;

import java.lang.reflect.Type;

public class EventDeserializer implements JsonDeserializer<Event> {
    @Override
    public Event deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String customerId = jsonObject.get("customerId").getAsString();
        String eventType = jsonObject.get("eventType").getAsString();
        long timestamp = jsonObject.get("timestamp").getAsLong();
        return new Event(customerId, eventType, timestamp);
    }
}
