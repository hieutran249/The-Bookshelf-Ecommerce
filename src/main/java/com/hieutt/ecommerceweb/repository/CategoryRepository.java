package com.hieutt.ecommerceweb.repository;

import com.hieutt.ecommerceweb.entity.Book;
import com.hieutt.ecommerceweb.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);

    @Query("SELECT c FROM Category c WHERE c.deleted = false")
    List<Category> findAllAvailable();
}
