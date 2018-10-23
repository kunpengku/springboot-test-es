package com.kunpengku.test1.testes.es;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * DATE 2018/10/23.
 *
 * @author fupeng.
 */
@Service
public class StaffESDaoImpl {
    @Autowired
    TransportClient client;

    public void insert(StaffEntity staffEntity){
        IndexRequestBuilder builder = null;
        builder = client.prepareIndex("testes", "testes");

        IndexResponse response = builder.setSource(
                JSON.toJSONString(staffEntity), XContentType.JSON).get();
    }


    public SearchResponse search(StaffEntity entity){
        SearchRequestBuilder builder = client.prepareSearch("testes").setTypes("testes");
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.filter(QueryBuilders.matchPhrasePrefixQuery(
                    "name", entity.getName()).maxExpansions(50));
        builder.setQuery(boolQueryBuilder);
        return builder.get();
    }

}
