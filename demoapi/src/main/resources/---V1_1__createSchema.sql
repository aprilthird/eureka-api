CREATE scheme IF NOT EXISTS demoeureka;

CREATE TABLE users (
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username varchar(255),
    email varchar(255),
    password varchar(255)
);

CREATE TABLE roles (
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name varchar(255)
);

CREATE TABLE user_roles (
    user_id int NOT NULL,
    role_id int NOT NULL
);

CREATE TABLE products (
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name varchar(255),
    description varchar(255),
    price decimal(18,2)
);