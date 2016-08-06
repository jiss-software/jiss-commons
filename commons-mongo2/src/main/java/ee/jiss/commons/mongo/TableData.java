package ee.jiss.commons.mongo;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import ee.jiss.commons.function.ThrowsConsumer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static ee.jiss.commons.lang.CheckUtils.*;
import static ee.jiss.commons.lang.ExceptionUtils.wrap;
import static ee.jiss.commons.mongo.DBUtils.*;
import static org.slf4j.LoggerFactory.getLogger;

public class TableData {
    public Long collectionSize;
    public Long selectionSize;

    public String error;

    public Integer skip = 0;
    public Integer limit;

    public BasicDBObject baseFilter = new BasicDBObject();

    public String query;
    public BasicDBList queryFilter = new BasicDBList();
    public BasicDBObject filter = new BasicDBObject();
    public BasicDBObject realFilter = new BasicDBObject();

    public OrderCondition sortOrder = new OrderCondition();
    public BasicDBObject projection = new BasicDBObject();

    public List<DBObject> data;

    public Map<String, Object> options = new HashMap<>();
    public Meta meta = new Meta();

    private transient List<ThrowsConsumer> postProcessors = new LinkedList<>();

    public List<DBObject> requestData(DBCollection collection) {
        return this.request(collection, false).data;
    }

    public TableData request(DBCollection collection) {
        return this.request(collection, true);
    }

    private TableData request(DBCollection collection, boolean stat) {
        realFilter = and();

        addToAnd(realFilter, baseFilter);
        if (isNotEmptyMap(filter)) addToAnd(realFilter, filter);
        if (isNotEmptyCollection(queryFilter)) addToAnd(realFilter, or(queryFilter));

        try {
            if (stat) collectionSize = collection.count(baseFilter);
            if (stat) selectionSize = collection.count(realFilter);

            data = collection.find(realFilter, projection).sort(sortOrder).skip(skip).limit(limit).toArray();
        } catch (Exception exp) {
            getLogger(collection.getClass()).error(exp.getMessage() + ":\n" + this);
            error = "Unable to fetch data";

            selectionSize = 0L;
            data = new LinkedList<>();
        }

        return this;
    }

    public TableData describeMeta(ThrowsConsumer<Meta> description) {
        wrap(() -> description.accept(meta));
        return this;
    }

    public TableData parseQuery(ThrowsConsumer<BasicDBList> parse) {
        if (isNotEmptyString(query)) wrap(() -> parse.accept(this.queryFilter));
        return this;
    }

    public TableData collectOptions(ThrowsConsumer<Map<String, Object>> collect) {
        wrap(() -> collect.accept(options));
        return this;
    }
}