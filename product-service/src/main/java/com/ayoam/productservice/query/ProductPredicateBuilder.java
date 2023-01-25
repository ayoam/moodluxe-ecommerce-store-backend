package com.ayoam.productservice.query;

import com.ayoam.productservice.model.QCategory;
import com.ayoam.productservice.model.QProduct;
import com.ayoam.productservice.repository.CategoryRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductPredicateBuilder {
    public static Predicate productFilters(Map<String,String> filters){

        QProduct product = QProduct.product;
        QCategory category = QCategory.category;

        BooleanBuilder where = new BooleanBuilder();

        //Price filters
        try{
            if (filters.get("minPrice") != null && filters.get("maxPrice") != null) {
                if(!filters.get("minPrice").isEmpty() && !filters.get("maxPrice").isEmpty()){
                    where.and(product.price.between(Integer.parseInt(filters.get("minPrice")),Integer.parseInt(filters.get("maxPrice"))));
                }
            }
        }
        catch (Exception e){
            throw new RuntimeException("price is not valid!");
        }


        //Brands filters
        List<String> brandsList = null;

        if(filters.get("brand")!=null){
            if(!filters.get("brand").isEmpty()){
                brandsList = (new ArrayList<String>(Arrays.asList(filters.get("brand").split(",")))).stream().map(item -> item.toLowerCase()).toList();
            }
        }

        if(brandsList!=null){
            where.and(product.brand.name.in(brandsList));
        }

        //category filter
        if(filters.get("category")!=null){
            if(!filters.get("category").isEmpty()) {
                where.and(product.categoryList.any().name.eq(filters.get("category").replace('-', ' ')));
            }
        }

        //query search
        if(filters.get("q")!=null){
            if(!filters.get("q").isEmpty()) {
                where.and(product.libelle.likeIgnoreCase("%"+filters.get("q")+"%"));
            }
        }

        //status filter
        if(filters.get("status")!=null){
            if(!filters.get("status").isEmpty()) {
                if(Objects.equals(filters.get("status"), "draft")) {
                    where.and(product.active.eq(false));
                }
                else{
                    where.and(product.active.eq(true));
                }
            }
        }

        return where;
    }
}
