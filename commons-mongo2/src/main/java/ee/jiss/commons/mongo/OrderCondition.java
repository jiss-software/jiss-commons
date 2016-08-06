package ee.jiss.commons.mongo;

import com.mongodb.BasicDBObject;

import java.math.BigInteger;

public class OrderCondition extends BasicDBObject {
    @Override
    public Object put(String key, Object val) {
        return super.put(key, val != null && BigInteger.class.isInstance(val) ? ((BigInteger) val).intValue() : val);
    }
}