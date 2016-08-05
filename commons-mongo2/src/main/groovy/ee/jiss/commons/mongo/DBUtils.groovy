package ee.jiss.commons.mongo

import com.mongodb.BasicDBList
import com.mongodb.BasicDBObject
import com.mongodb.DBObject

import java.util.regex.Pattern

import static java.lang.Double.parseDouble
import static org.joda.time.format.DateTimeFormat.forPattern

class DBUtils {
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    
    static Pattern pattern(str) {
        Pattern.compile(str as String)
    }

    static Set translatedValue(String str, Map<String, String> values) {
        values.findAll({ k, String v -> v.contains(str) }).keySet()
    }

    static Double numberValue(String str) {
        str = str.replace(',', '.')
        if (! str.matches('\\+?-?\\d+(\\.\\d+)?')) return null;

        parseDouble(str)
    }

    static DBObject dbEmpty() {
        return new BasicDBObject()
    }

    static BasicDBObject dbMap(Map<String, ?>... map) {
        def result = new BasicDBObject()
        map.each { if (it) result.putAll(it) }
        return result
    }

    static DBObject dbList(List... list) {
        def result = new BasicDBList()
        list.each { if (it) result.addAll(it) }
        return result
    }

    static Pattern pattern(String str) {
        return Pattern.compile(str);
    }

    static Pattern dateValue(String str, String format) {
        try {
            return pattern(forPattern(format).parseDateTime(str).toString(DATE_PATTERN));
        } catch (Exception ignore) {
            return null;
        }
    }

    static DBObject dbList(DBObject... items) {
        BasicDBList result = new BasicDBList();
        Collections.addAll(result, items);
        return result;
    }

    static BasicDBObject and(DBObject... items) {
        final BasicDBObject result = new BasicDBObject();
        result.put('$and', dbList(items));
        return result;
    }

    static BasicDBObject or(DBObject... items) {
        final BasicDBObject result = new BasicDBObject();
        result.put('$or', dbList(items));
        return result;
    }

    static BasicDBObject 'in'(DBObject item) {
        final BasicDBObject result = new BasicDBObject();
        result.put('$in', item);
        return result;
    }

    static BasicDBObject gt(Long item) {
        final BasicDBObject result = new BasicDBObject();
        result.put('$gt', item);
        return result;
    }

    static BasicDBObject lt(Long item) {
        final BasicDBObject result = new BasicDBObject();
        result.put('$lt', item);
        return result;
    }

    static BasicDBObject regex(Pattern item) {
        final BasicDBObject result = new BasicDBObject();
        result.put('$regex', item);
        return result;
    }

    static <T extends Map<String, Object>> T addToAnd(T target, Map... items) {
        for (Map item : items) {
            //noinspection unchecked
            ((List) target.get('$and')).add(item);
        }

        return target;
    }
}