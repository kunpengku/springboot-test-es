package com.kunpengku.test1.testes.controller;

import com.alibaba.fastjson.JSON;
import com.kunpengku.test1.testes.es.StaffESDaoImpl;
import com.kunpengku.test1.testes.es.StaffEntity;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * DATE 2018/10/23.
 *
 * @author fupeng.
 */
@RestController
public class Test {

    @Autowired
    StaffESDaoImpl staffESDao;

    /**
     * 这个方法介绍 如何 插入数据到ES
     */
    @RequestMapping("/create")
    public void create() {
        System.out.println("start");
        String[] names = {"李明","李媛","张飞"};
        for (String name : names){
            StaffEntity s = new StaffEntity();
            s.setName(name);
            staffESDao.insert(s);
        }
    }

    /**
     * 这个方法介绍 如何 从ES中 搜索数据
     */
    @RequestMapping("/s")
    public List<StaffEntity> search(@RequestParam("q") String q) {
        StaffEntity s = new StaffEntity();
        s.setName(q);
        SearchResponse search = staffESDao.search(s);
        SearchHits searchHits = search.getHits();

        List<StaffEntity> resultList = new ArrayList<>();
        Iterator<SearchHit> iter = searchHits.iterator();
        while (iter.hasNext()) {
            SearchHit x = iter.next();
            resultList.add(JSON.parseObject(x.getSourceAsString(),StaffEntity.class));
        }
        return resultList;
    }
}
