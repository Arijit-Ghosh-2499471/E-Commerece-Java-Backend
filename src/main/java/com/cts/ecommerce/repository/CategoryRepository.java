package com.cts.ecommerce.repository;

import com.cts.ecommerce.entity.Category;

import java.util.List;

public interface CategoryRepository {

    int save(Category category);

    int update(Category category);

    int deleteById(int categoryId);

    Category findById(int categoryId);

    List<Category> findAll();

    boolean existsById(int categoryId);
}


//package com.cts.ecommerce.repository;
//
//import com.cts.ecommerce.entity.Category;
//
//import java.util.List;
//import java.util.Optional;
//
///**
// * DAO contract for the Category table.
// */
//public interface CategoryRepository {
//
//    int save(Category category);
//
//    int update(Category category);
//
//    int deleteById(Integer categoryId);
//
//    Optional<Category> findById(Integer categoryId);
//
//    List<Category> findAll();
//
//    boolean existsById(Integer categoryId);
//}
