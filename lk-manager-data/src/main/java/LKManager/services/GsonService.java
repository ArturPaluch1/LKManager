package LKManager.services;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class GsonService {

   // private final Gson gson = new Gson();

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class,new LocalDateAdapter())
            .create();



    public <T> T jsonToObject(String jsonString, Class<T> objectClass)
    {

             return   gson.fromJson(jsonString,    objectClass);


    }

    public String objectToJson(Object object)
    {
      return  gson.toJson(object);
    }
    public <T> List<T> jsonToList1(String jsonString, Class<T> listObjectClass)
    {
        Type listType = TypeToken.getParameterized(List.class, listObjectClass).getType();

        return   gson.fromJson(jsonString,    listType);


    }
    public <T> List<T> jsonToList(List<String> jsonListString, Class<T> listObjectClass)
    {
        Type listType = TypeToken.getParameterized(List.class, listObjectClass).getType();
        String jsonString = "[" + String.join(",", jsonListString) + "]";

        return   gson.fromJson(jsonString,    listType);


    }
    public <T>List<String> listToJson(List<T> list)
    {
        if (list != null && !list.isEmpty())
        {
    //        Type listType= TypeToken.getParameterized(List.class, list.get(0).getClass()).getType();


            List<String> jsonUsers = new ArrayList<>();


            for (T user : list) {
                String jsonUser = gson.toJson(user);
                jsonUsers.add(jsonUser);
            }
            return  jsonUsers;
        }
        else
            return null;
    }


    public class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {


        @Override
        public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return LocalDate.parse(json.getAsString() );
        }

        @Override
        public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }
    }
}
