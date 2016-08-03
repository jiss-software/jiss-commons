package ee.jiss.commons.mongo

import com.mongodb.DBCollection

import static ee.jiss.commons.mongo.DBUtils.dbMap

class TableData {

    Long collectionSize
    Long selectionSize

    String error

    Integer skip
    Integer limit

    String query
    List queryFilter = []
    Map filter = [:]
    Map realFilter

    Map sortOrder
    Map projection = [:]

    List data

    Map options = [:]
    Map meta = [:]

    TableData request(DBCollection collection) {
        realFilter = dbMap([ '$and': [[ 'state': 'ACTIVE' ]] ])

        if (filter) realFilter.$and.add(filter)
        if (queryFilter) realFilter.$and.add([ '$or': queryFilter ])

        collectionSize = collection.count dbMap([ 'state': 'ACTIVE' ])

        try {
            selectionSize = collection.count realFilter

            data = collection.find(realFilter, dbMap(projection))
                    .sort(dbMap(sortOrder)).skip(skip).limit(limit).toArray() as List
        } catch (Exception exp) {
            getLogger(collection.class).error(exp.message + ":\n" + this)
            error = exp.message

            selectionSize = 0
            data = []
        }

        this
    }

    TableData request(DBCollection collection, Closure after) {
        after(request(collection))
        this
    }

    TableData describeMeta(Closure description) {
        description(meta)
        this
    }

    TableData parseQuery(Closure parse) {
        if (query) parse { k, v -> if (v) queryFilter.add( [ "$k" : v instanceof List ? [ '$in': v ] : v ]) }
        this
    }

    TableData collectOptions(Closure collect) {
        collect(options)
        this
    }
}