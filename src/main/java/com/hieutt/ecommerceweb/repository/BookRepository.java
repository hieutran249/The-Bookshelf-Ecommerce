package com.hieutt.ecommerceweb.repository;

import com.hieutt.ecommerceweb.dto.BookDto;
import com.hieutt.ecommerceweb.entity.Book;
import com.hieutt.ecommerceweb.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    // ADMIN
    boolean existsByTitleAndAuthor(String title, String author);
    @Query("SELECT b FROM Book b WHERE b.author like %?1% or b.title like %?1% or b.publishedYear like %?1%")
    List<Book> searchBookByAuthorOrTitleOrPublishedYear(String keyword);
    List<Book> searchAllByCategory(Category category);


    // CUSTOMER
    @Query("SELECT b FROM Book b WHERE b.deleted = false")
    List<Book> findAllAvailable();
    @Query("SELECT b FROM Book b WHERE b.deleted = false ORDER BY b.price ASC")
    List<Book> findByPriceAsc();
    @Query("SELECT b FROM Book b WHERE b.deleted = false ORDER BY b.price DESC ")
    List<Book> findByPriceDesc();

}
