package com.hieutt.ecommerceweb.service.impl;

import com.hieutt.ecommerceweb.dto.BookDto;
import com.hieutt.ecommerceweb.entity.Book;
import com.hieutt.ecommerceweb.entity.Category;
import com.hieutt.ecommerceweb.exception.ResourceNotFoundException;
import com.hieutt.ecommerceweb.repository.BookRepository;
import com.hieutt.ecommerceweb.repository.CategoryRepository;
import com.hieutt.ecommerceweb.service.BookService;
import com.hieutt.ecommerceweb.utils.Constants;
import com.hieutt.ecommerceweb.utils.ImageUpload;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    private final Map<String, String> message;
    private final ImageUpload imageUpload;


    public BookServiceImpl(BookRepository bookRepository, CategoryRepository categoryRepository, ImageUpload imageUpload) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
        this.imageUpload = imageUpload;
        this.message = new HashMap<>();
    }

    @Override
    public Map<String, String> createBook(BookDto bookDto, MultipartFile image) throws IOException {
        if (bookRepository.existsByTitleAndAuthor(bookDto.getTitle(), bookDto.getAuthor())) {
            message.put("type", "error");
            message.put("detail", "This book already exists! Please try again ⛔");
            return message;
        }
        Book book = mapToEntity(bookDto);
        if (!image.isEmpty()) {
            imageUpload.upload(image);
            System.out.println(Base64.getEncoder().encodeToString(image.getBytes()));
            book.setImage(Base64.getEncoder().encodeToString(image.getBytes()));
        }
        bookRepository.save(book);
        message.put("type", "success");
        message.put("detail", "Successfully created 🎉");
        return message;
    }

    @Override
    public List<BookDto> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(book -> mapToDto(book))
                .collect(Collectors.toList());
    }

    @Override
    public List<BookDto> getAllBooks(int pageNo, String sortBy, String sortDir) {
        if (Objects.equals(sortBy, "null")) {
            sortBy = Constants.DEFAULT_SORT_BY;
        }
        if (Objects.equals(sortDir, "null")) {
            sortDir = Constants.DEFAULT_SORT_DIRECTION;
        }
        // sorting
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, 3, sort);
        Page<Book> bookPage = bookRepository.findAll(pageable);
        List<Book> books = bookPage.getContent();
        return books.stream()
                .map(book -> mapToDto(book))
                .collect(Collectors.toList());
    }

    @Override
    public BookDto getBookById(Long id) {
        Book book = findBookById(id);
        return mapToDto(book);
    }

    @Override
    public Map<String, String> updateBook(Long id, BookDto bookDto, MultipartFile image) throws IOException {
        Book book = findBookById(id);
        if (bookRepository.existsByTitleAndAuthor(bookDto.getTitle(), bookDto.getAuthor())
                && !book.getTitle().equals(bookDto.getTitle())) {
            message.put("type", "error");
            message.put("detail", "This book already exists! Please try again ⛔");
            return message;
        }
        if (!image.isEmpty()) {
            imageUpload.upload(image);
            book.setImage(Base64.getEncoder().encodeToString(image.getBytes()));
        }
        book.setTitle(bookDto.getTitle());
        book.setDescription(bookDto.getDescription());
        book.setAuthor(bookDto.getAuthor());
        book.setPrice(bookDto.getPrice());
        book.setPublishedYear(bookDto.getPublishedYear());
        book.setQuantity(bookDto.getQuantity());
        book.setCategory(bookDto.getCategory());

        Book updatedBook = bookRepository.save(book);
        message.put("type", "success");
        message.put("detail", "Successfully updated 🎉");
        return new HashMap<>();
    }

    @Override
    public void deleteBook(Long bookId) {
        Book book = findBookById(bookId);
        book.setDeleted(true);
        bookRepository.save(book);
    }

    @Override
    public void restoreBook(Long bookId) {
        Book book = findBookById(bookId);
        book.setDeleted(false);
        bookRepository.save(book);
    }

    @Override
    public Page<BookDto> searchBooks(String keyword, int pageNo) {
        List<BookDto> books = bookRepository.searchBookByAuthorOrTitleOrPublishedYear(keyword)
                .stream()
                .map(book -> mapToDto(book))
                .toList();
        Pageable pageable = PageRequest.of(pageNo, 3);
        return toPage(books, pageable);
    }

    @Override
    public Page<BookDto> getBooksByCategory(Long categoryId, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 3);
        Category category = categoryRepository.findById(categoryId).get();
        List<BookDto> books = bookRepository.searchAllByCategory(category)
                .stream()
                .map(book -> mapToDto(book))
                .toList();
        return toPage(books, pageable);
    }

    private Page<BookDto> toPage(List<BookDto> books, Pageable pageable) {
        if (pageable.getOffset() >= books.size()) {
            return Page.empty();
        }
        int startIndex = (int) pageable.getOffset();
        int endIndex = ((pageable.getOffset() + pageable.getPageSize()) > books.size())
                ? books.size()
                : (int) (pageable.getOffset() + pageable.getPageSize());
        List<BookDto> subList = books.subList(startIndex, endIndex);
        return new PageImpl<>(subList, pageable, books.size());
    }

    private Book findBookById(Long id) {
        return bookRepository
                .findById(id).orElseThrow(()
                        -> new ResourceNotFoundException("Book", "id", id));
    }

    private BookDto mapToDto(Book book) {
        return modelMapper.map(book, BookDto.class);
    }

    private Book mapToEntity(BookDto bookDto) {
        return modelMapper.map(bookDto, Book.class);
    }
}
