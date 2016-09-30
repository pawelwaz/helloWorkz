create database if not exists hello CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
create user if not exists 'hello'@'%' identified by 'hello';
create user if not exists 'hello'@'localhost' identified by 'hello';
grant all privileges on hello.* to 'hello'@'%' identified by 'hello';
grant all privileges on hello.* to 'hello'@'localhost' identified by 'hello';
flush privileges;
use hello;

create table HelloUser (
    id int unsigned not null auto_increment,
    login varchar(16) not null,
    password varchar(128) not null,
    name varchar(32) default null,
    surname varchar(32) default null,
    email varchar(64) default null,
    phone varchar(64) default null,
    organisation varchar(128) default null,
    job varchar(128) default null,
    avatar blob,
    primary key(id)
);