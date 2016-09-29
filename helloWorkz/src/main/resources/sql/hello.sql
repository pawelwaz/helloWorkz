create database if not exists hello;
create user if not exists 'hello'@'%' identified by 'hello';
create user if not exists 'hello'@'localhost' identified by 'hello';
grant all privileges on hello.* to 'hello'@'%' identified by 'hello';
grant all privileges on hello.* to 'hello'@'localhost' identified by 'hello';
flush privileges;
use hello;

create table HelloUser (
	id int unsigned not null auto_increment,
    login varchar(128) not null,
    password varchar(128) not null,
    primary key(id)
);