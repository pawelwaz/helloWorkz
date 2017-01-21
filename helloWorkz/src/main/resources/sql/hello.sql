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

create table Message (
    id int unsigned not null auto_increment,
    content text,
    seen int,
    sendtime datetime default now(),
    sender int unsigned not null,
    receiver int unsigned not null,
    foreign key(sender) references HelloUser(id),
    foreign key(receiver) references HelloUser(id),
    primary key(id)
);

create table Conversation (
    id int unsigned not null auto_increment,
    owner int unsigned not null,
    person int unsigned not null,
    foreign key(owner) references HelloUser(id),
    foreign key(person) references HelloUser(id),
    primary key(id)
);

create table Contact (
    id int unsigned not null auto_increment,
    owner int unsigned not null,
    person int unsigned not null,
    foreign key(owner) references HelloUser(id),
    foreign key(person) references HelloUser(id),
    primary key(id)
);

create table workgroup (
    id int unsigned not null auto_increment,
    group_name text,
    description text,
    tasknumber int default 0,
    primary key(id)
);

create table membership (
    id int unsigned not null auto_increment,
    workgroup int unsigned not null,
    hellouser int unsigned not null,
    title text,
    description text,
    managment int default 0,
    users int default 0,
    active int default 1,
    tasks int default 0,
    discussions int default 0,
    foreign key(workgroup) references workgroup(id),
    foreign key(hellouser) references hellouser(id),
    primary key(id)
);

create table membershiprequest (
    id int unsigned not null auto_increment,
    workgroup int unsigned not null,
    hellouser int unsigned not null,
    foreign key(workgroup) references workgroup(id),
    foreign key(hellouser) references hellouser(id),
    primary key(id)
);

create table discussion (
    id int unsigned not null auto_increment,
    title text,
    lastpost datetime,
    hellouser int unsigned not null,
    workgroup int unsigned not null,
    posts int,
    foreign key(hellouser) references hellouser(id),
    foreign key(workgroup) references workgroup(id),
    primary key(id)
);

create table post (
    id int unsigned not null auto_increment,
    content text,
    created datetime,
    hellouser int unsigned not null,
    discussion int unsigned not null,
    foreign key(hellouser) references hellouser(id),
    foreign key(discussion) references discussion(id),
    primary key(id)
);

create table task (
    id int unsigned not null auto_increment,
    content text,
    creator int unsigned not null,
    workgroup int unsigned not null,
    status int,
    deadline text,
    deadlinehour text,
    created text,
    number int,
    closed text,
    annotation text,
    foreign key(creator) references hellouser(id),
    foreign key(workgroup) references workgroup(id),
    primary key(id)
);

create table taskuser (
    id int unsigned not null auto_increment,
    hellouser int unsigned not null,
    task int unsigned not null,
    not1 int default 0,
    not2 int default 0,
    not3 int default 0,
    foreign key(hellouser) references hellouser(id),
    foreign key(task) references task(id),
    primary key(id)
);

create table notification (
    id int unsigned not null auto_increment,
    content text,
    received text,
    hellouser int unsigned not null,
    foreign key(hellouser) references hellouser(id),
    primary key(id)
);

create table invitation (
    id int unsigned not null auto_increment,
    hellouser int unsigned not null,
    sender int unsigned not null,
    workgroup int unsigned not null,
    foreign key(hellouser) references hellouser(id),
    foreign key(sender) references hellouser(id),
    foreign key(workgroup) references workgroup(id),
    primary key(id)
);