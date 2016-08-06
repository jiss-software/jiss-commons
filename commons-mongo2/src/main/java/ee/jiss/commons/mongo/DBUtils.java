package ee.jiss.commons.mongo;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class DBUtils {
    public static DBObject dbEmpty() {
        return new BasicDBObject();
    }

    public static BasicDBObject dbMap(Map<String, ?>... maps) {
        BasicDBObject result = new BasicDBObject();
        for (Map<String, ?> it : maps) if (it != null) result.putAll(it);
        return result;
    }

    public static DBObject dbList(List<?>... lists) {
        BasicDBList result = new BasicDBList();
        for (List<?> it : lists) if (it != null) result.addAll(it);
        return result;
    }

    public static DBObject dbList(DBObject... items) {
        BasicDBList result = new BasicDBList();
        Collections.addAll(result, items);
        return result;
    }

    public static BasicDBObject and(DBObject... items) {
        BasicDBObject result = new BasicDBObject();
        result.put("$and", dbList(items));
        return result;
    }

    public static BasicDBObject or(DBObject... items) {
        BasicDBObject result = new BasicDBObject();
        result.put("$or", dbList(items));
        return result;
    }

    public static BasicDBObject in(DBObject item) {
        BasicDBObject result = new BasicDBObject();
        result.put("$in", item);
        return result;
    }

    public static BasicDBObject gt(Long item) {
        BasicDBObject result = new BasicDBObject();
        result.put("$gt", item);
        return result;
    }

    public static BasicDBObject lt(Long item) {
        BasicDBObject result = new BasicDBObject();
        result.put("$lt", item);
        return result;
    }

    public static BasicDBObject regex(Pattern item) {
        BasicDBObject result = new BasicDBObject();
        result.put("$regex", item);
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Map<String, Object>> T addToAnd(T target, Map... items) {
        for (Map item : items) ((List) target.get("$and")).add(item);
        return target;
    }

    public static BasicDBObject keyValue(String key, Object value) {
        BasicDBObject result = new BasicDBObject();
        result.put(key, value);
        return result;
    }
}