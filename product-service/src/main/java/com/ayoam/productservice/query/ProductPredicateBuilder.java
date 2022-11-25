package com.ayoam.productservice.query;

import com.ayoam.productservice.model.QProduct;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ProductPredicateBuilder {
    public static Predicate productFilters(Map<String,String> filters){
        QProduct product = QProduct.product;
        BooleanBuilder where = new BooleanBuilder();

        //Price filters
        try{
            if (filters.get("minPrice") != null && filters.get("maxPrice") != null) {
                if(!filters.get("minPrice").isEmpty() && !filters.get("maxPrice").isEmpty()){
                    where.and(product.originalPrice.between(Integer.parseInt(filters.get("minPrice")),Integer.parseInt(filters.get("maxPrice"))));
                }
            }
        }
        catch (Exception e){
            throw new RuntimeException("price is not valid!");
        }


        //Brands filters
        List<String> brandsList = filters.get("brand")!=null ?
                (new ArrayList<String>(Arrays.asList(filters.get("brand").split(",")))).stream().map(item -> item.toLowerCase()).toList()
                :
                new ArrayList<>();

        if(brandsList.size()>0){
            where.and(product.brand.name.in(brandsList));
        }

        return where;
    }
}
