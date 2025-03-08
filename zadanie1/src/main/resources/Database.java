import java.util.Map;
import java.util.HashMap;
import org.json.JSONObject;

public class Database {
    Map<String, String> map = new HashMap<>();
    JSONObject db;

    public Database() {
        map.put("name", "jon");
        map.put("surname", "doe");
        map.put("age", "22");
        map.put("city", "chicago");
        db = new JSONObject(map);
    }

    public String get(String key) {
        return db.getString(key);
    }

}