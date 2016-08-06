package ee.jiss.commons.mongo;

import java.util.Map;

public class Selection {
    public Integer skip = 0;
    public Integer limit;

    public String query;

    public Map<String, Object> filter;
    public OrderCondition order;
    public Map<String, Object> projection;
}


