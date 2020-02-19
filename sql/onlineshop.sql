DROP DATABASE IF EXISTS onlineshop;
CREATE DATABASE onlineshop;
USE onlineshop;

CREATE TABLE person (
    id BIGINT NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    patronymic VARCHAR(50) DEFAULT 'none',
    login VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(50) DEFAULT 'none',
    phone VARCHAR(20) DEFAULT 'none',
    deposit INT DEFAULT 0,
    position VARCHAR(100) DEFAULT 'none',
    active BIT NOT NULL,
    account_non_expired BIT NOT NULL DEFAULT true,
    account_non_locked BIT NOT NULL DEFAULT true,
    credentials_non_expired BIT NOT NULL DEFAULT true,
    PRIMARY KEY (id),
    UNIQUE KEY uniq_login (login),
    CONSTRAINT min_deposit CHECK (deposit > 0)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE token (
    id BIGINT NOT NULL,
    token VARCHAR(50) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (id) REFERENCES person (id) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE address (
    id BIGINT NOT NULL,
    address VARCHAR(50) DEFAULT 'none',
    PRIMARY KEY (id),
    FOREIGN KEY (id) REFERENCES person (id) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

create table role (
    person_id BIGINT NOT NULL,
    roles VARCHAR(50),
    KEY role_key (roles),
    FOREIGN KEY (person_id) REFERENCES person (id) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE category (
    id BIGINT NOT NULL AUTO_INCREMENT,
    parent_id BIGINT,
    name VARCHAR(50) NOT NULL,
    UNIQUE KEY uniq_name (name),
    KEY parent_id_key (parent_id),
    PRIMARY KEY (id),
    FOREIGN KEY (parent_id) REFERENCES category (id) ON DELETE CASCADE
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
    person_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    KEY person_id_key (person_id),
    FOREIGN KEY (person_id) REFERENCES person (id) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE product_in_basket (
    id BIGINT NOT NULL AUTO_INCREMENT,
    basket_id BIGINT NOT NULL,
    product_id BIGINT,
    name VARCHAR(255) NOT NULL,
    price INT NOT NULL,
    count INT NOT NULL DEFAULT 1,
    KEY name_key (name),
    PRIMARY KEY (id),
    FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE SET NULL,
    FOREIGN KEY (basket_id) REFERENCES basket (id) ON DELETE CASCADE,
    CONSTRAINT min_quantity CHECK (count > 0),
    CONSTRAINT min_price CHECK (price > 0)
) ENGINE=INNODB DEFAULT CHARSET=utf8;