import com.google.gson.*;

import java.lang.reflect.Type;

public class CustomConverter implements JsonDeserializer<Shelter> {

    public Shelter deserialize(JsonElement json, Type type,
                              JsonDeserializationContext context) throws JsonParseException {
        try {
            JsonObject object = json.getAsJsonObject();
            Shelter shelter = new Shelter();

            shelter.setX(object.get("position").getAsDouble());
            shelter.setName(object.get("name").getAsString());

            return shelter;
        } catch (NullPointerException e) {
            System.err.println("Enter is wrong.");
            return  null;
        }
    }
}



