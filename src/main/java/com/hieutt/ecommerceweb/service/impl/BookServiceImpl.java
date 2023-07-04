package com.hieutt.ecommerceweb.service.impl;

import com.hieutt.ecommerceweb.dto.BookDto;
import com.hieutt.ecommerceweb.entity.Book;
import com.hieutt.ecommerceweb.exception.ResourceNotFoundException;
import com.hieutt.ecommerceweb.repository.BookRepository;
import com.hieutt.ecommerceweb.service.BookService;
import com.hieutt.ecommerceweb.utils.ImageUpload;
import com.sun.mail.util.BASE64EncoderStream;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    private final Map<String, String> message;
    private final ImageUpload imageUpload;


    public BookServiceImpl(BookRepository bookRepository, ImageUpload imageUpload) {
        this.bookRepository = bookRepository;
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
    public List<BookDto> getAllBooks(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 5);
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
