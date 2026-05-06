CREATE SCHEMA ecommerce;
USE ecommerce;

CREATE TABLE Category (
                          CategoryId INT AUTO_INCREMENT PRIMARY KEY,
                          CategoryName VARCHAR(20) NOT NULL
);

CREATE TABLE Users (
                       UserId INT AUTO_INCREMENT PRIMARY KEY,
                       Name VARCHAR(50) NOT NULL,
                       Email VARCHAR(30) NOT NULL UNIQUE,
                       Password VARCHAR(20) NOT NULL,
                       PaymentDetails VARCHAR(30),
                       Role VARCHAR(30) CHECK (Role IN ('Admin', 'Customer'))
);

CREATE TABLE Products (
                          ProductId INT AUTO_INCREMENT PRIMARY KEY,
                          ProductName VARCHAR(50) NOT NULL,
                          Description VARCHAR(200),
                          Price DOUBLE NOT NULL,
                          CategoryId INT,
                          ImageURL VARCHAR(500),
                          CONSTRAINT fk_category_to_product
                              FOREIGN KEY (CategoryId) REFERENCES Category(CategoryId)
);

CREATE TABLE Review (
                        ReviewId INT AUTO_INCREMENT PRIMARY KEY,
                        UserId INT,
                        ProductId INT,
                        Rating INT CHECK (Rating BETWEEN 1 AND 5),
                        ReviewDescription VARCHAR(200),
                        FOREIGN KEY (UserId) REFERENCES Users(UserId),
                        FOREIGN KEY (ProductId) REFERENCES Products(ProductId)
);

CREATE TABLE ShoppingCart (
                              ShoppingCartId INT AUTO_INCREMENT PRIMARY KEY,
                              UserId INT,
                              IsActive BOOLEAN DEFAULT TRUE,
                              FOREIGN KEY (UserId) REFERENCES Users(UserId)
);

CREATE TABLE CartItems (
                           CartItemId INT AUTO_INCREMENT PRIMARY KEY,
                           ShoppingCartId INT,
                           ProductId INT,
                           Quantity INT NOT NULL,
                           FOREIGN KEY (ShoppingCartId) REFERENCES ShoppingCart(ShoppingCartId),
                           FOREIGN KEY (ProductId) REFERENCES Products(ProductId)
);

CREATE TABLE Address (
                         AddressId INT AUTO_INCREMENT PRIMARY KEY,
                         UserId INT,
                         HouseNo VARCHAR(20),
                         Area VARCHAR(20),
                         City VARCHAR(20),
                         State VARCHAR(20),
                         Country VARCHAR(20),
                         Pincode INT CHECK (Pincode BETWEEN 100000 AND 999999),
                         FOREIGN KEY (UserId) REFERENCES Users(UserId)
);

CREATE TABLE Orders (
                        OrderId INT AUTO_INCREMENT PRIMARY KEY,
                        UserId INT,
                        TotalPrice DOUBLE NOT NULL,
                        ShippingAddress INT,
                        OrderStatus VARCHAR(15) CHECK (OrderStatus IN ('Pending', 'Shipped', 'Delivered')),
                        PaymentStatus VARCHAR(15),
                        ShoppingCartId INT,
                        FOREIGN KEY (UserId) REFERENCES Users(UserId),
                        FOREIGN KEY (ShippingAddress) REFERENCES Address(AddressId),
                        FOREIGN KEY (ShoppingCartId) REFERENCES ShoppingCart(ShoppingCartId)
);