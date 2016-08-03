package ee.jiss.commons.mongo

import com.mongodb.BasicDBList
import com.mongodb.BasicDBObject
import com.mongodb.DBObject

import java.util.regex.Pattern

import static java.lang.Double.parseDouble

class DBUtils {

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
}