DROP DATABASE IF EXISTS onlineshop;
CREATE DATABASE `onlineshop`;
USE `onlineshop`;

CREATE TABLE token_rest (
    id BIGINT NOT NULL AUTO_INCREMENT,
    token VARCHAR(36) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY token (token)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE customer (
    id BIGINT NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    patronymic VARCHAR(50),
    login VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    active TINYINT NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uniq_login (login)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE customer_contacts (
    id BIGINT NOT NULL AUTO_INCREMENT,
    customer_id BIGINT NOT NULL,
    email VARCHAR(50) NOT NULL,
    address VARCHAR(50) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uniq_email (email),
    FOREIGN KEY (customer_id) REFERENCES customer (id) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE customer_deposit (
    customer_id BIGINT NOT NULL,
    deposit INT DEFAULT 0,
    KEY customer_id_key (customer_id),
    FOREIGN KEY (customer_id) REFERENCES customer (id) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE admin (
    id BIGINT NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    patronymic VARCHAR(50),
    position VARCHAR(100) NOT NULL,
    login VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uniq_login (login)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE category (
    id BIGINT NOT NULL AUTO_INCREMENT,
    parent_id BIGINT NOT NULL DEFAULT 0,
    name VARCHAR(50) NOT NULL,
    KEY parent_id_key (parent_id),
    PRIMARY KEY (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE product (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    price INT NOT NULL,
    count INT NOT NULL DEFAULT 0,
    KEY name_key (name),
    KEY price_key (price),
    PRIMARY KEY (id),
    CONSTRAINT min_price CHECK (price > 0)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE product_category (
    product_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    PRIMARY KEY (product_id, category_id),
    FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES category (id) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE basket (
    id BIGINT NOT NULL AUTO_INCREMENT,
    customer_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    KEY customer_id_key (customer_id),
    FOREIGN KEY (customer_id) REFERENCES customer (id) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE basket_item (
    basket_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    PRIMARY KEY (basket_id, product_id),
    FOREIGN KEY (product_id) REFERENCES product (id),
    FOREIGN KEY (basket_id) REFERENCES basket (id) ON DELETE CASCADE,
    CONSTRAINT min_quantity CHECK (quantity > 0)
) ENGINE=INNODB DEFAULT CHARSET=utf8;