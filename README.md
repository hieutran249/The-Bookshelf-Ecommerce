# The-Bookshelf-Ecommerce
An e-commerce website made with Spring Boot and Thymeleaf

## Technologies
Spring Boot 3, ModelMapper, Lombok, Mail Sender, Thymeleaf, Bootstrap 5

## Features
### ADMIN: 
CRUD Category, Book (with image).

Pagination on Category, Book.

Sort books by id, title, author, published year, price, and quantity.

Search books by title, author, and published year.

Manage Order: accept/reject orders, change statuses of orders, view order details.

Manage customers.

### CUSTOMER:
View books by category, and view book details.

Sort books by newest, high to low/low to high price.

Search books by title, author, and published year.

Add books to the shopping cart.

Checkout and place orders.

View placed orders.

Update personal information and password.

### Authentication & Authorization:
Admins and customers are required to log in to do some specific tasks or they can register a new account.
Only the super admin can create a new admin account.
If they forgot the password, they can choose to reset the password via their registered email address.

Only admins(admin & super admin) can manage categories, books, orders and customers.

Only customers can add books to the shopping cart, checkout, and place orders.

Anonymous users can view books, view book details, sort books and search for books.

## Installation
When you clone this repo, please use the thebookshelf.sql file (src/main/java/com/hieutt/ecommerceweb/utils/thebookshelf.sql) 
to import some dummy data including database structure, a super admin account, and some categories and books.
After importing the data, you can run the program properly.
