package com.ayoam.productservice.query;

import com.ayoam.productservice.model.QProduct;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import java.util.Map;

public class ProductPredicateBuilder {
    public static Predicate studentFilters(Map<String,String> filters){
        QProduct student = QProduct.product;
        BooleanBuilder where = new BooleanBuilder();
//        if (filters.get("name") != null) {
//            if(!filters.get("name").isEmpty()){
//                where.and(student.name.contains(filters.get("name")));
//            }
//        }
        return where;
    }
}
