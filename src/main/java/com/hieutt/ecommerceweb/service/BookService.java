package com.hieutt.ecommerceweb.service;

import com.hieutt.ecommerceweb.dto.BookDto;
import com.hieutt.ecommerceweb.dto.CategoryDto;
import com.hieutt.ecommerceweb.entity.Book;
import com.hieutt.ecommerceweb.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface BookService {
    // ADMIN
    Map<String, String> createBook(BookDto bookDto, MultipartFile image) throws IOException;
    List<BookDto> getAllBooks();
    List<BookDto> getAllBooks(int pageNo, String sortBy, String sortDir);
    BookDto getBookById(Long id);
    Map<String, String> updateBook(Long id, BookDto bookDto, MultipartFile image) throws IOException;
    void deleteBook(Long bookId);
    void restoreBook(Long bookId);
    Page<BookDto> searchBooks(String keyword, int pageNo);
    Page<BookDto> getBooksByCategory(Long categoryId, int pageNo);

    // CUSTOMER
    List<BookDto> getAllAvailableBooks(String sortBy, String sortDir);
    List<BookDto> getAllAvailableRelatedBooks(Category category, BookDto bookDto);
    List<BookDto> getAllAvailableBooksByCategory(Long categoryId);

    List<BookDto> searchAvailableBooks(String keyword);
}
