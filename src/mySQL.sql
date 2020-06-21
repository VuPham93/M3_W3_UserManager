create database usermanager;
use usermanager;

create table users (
                       id  int(3) NOT NULL AUTO_INCREMENT,
                       name varchar(120) NOT NULL,
                       email varchar(220) NOT NULL,
                       country varchar(120),
                       PRIMARY KEY (id)
);

insert into users(name, email, country) values('An','an@codegym.vn','Viet Nam');
insert into users(name, email, country) values('Minh','minh@codegym.vn','Viet Nam');
insert into users(name, email, country) values('Kante','kante@che.uk','Kenia');
insert into users(name, email, country) values('Tuan','tuan@codegym.vn','Viet Nam');
insert into users(name, email, country) values('John','john@codegym.vn','USA');
insert into users(name, email, country) values('Mike','mike@codegym.vn','USA');
insert into users(name, email, country) values('Jame','jame@codegym.vn','USA');
insert into users(name, email, country) values('Han','han@codegym.vn','Korea');

delimiter //
create procedure get_user_by_id(in user_id int)
begin
    select users.name, users.email, users.country
    from users
    where users.id = user_id;
end //

delimiter //
create procedure insert_user(
    in user_name varchar(50),
    in user_email varchar(50),
    in user_country varchar(50)
)
begin
    insert into users(name, email, country) values (user_name, user_email, user_country);
end //

create table Permission(
                           id int(11) primary key auto_increment,
                           name varchar(50)
);

create table User_Permission(
                                permission_id int(11),
                                user_id int(11)
);

insert into Permission(name) values('add');

insert into Permission(name) values('edit');

insert into Permission(name) values('delete');

insert into Permission(name) values('view');

delimiter //
create procedure select_all_user()
begin
    select * from users;
end //

delimiter //
create procedure update_user(
    in user_name varchar(50),
    in user_email varchar(50),
    in user_country varchar(50),
    in user_id int
)
begin
    update users
    set
        name = user_name,
        email = user_email,
        country = user_country
    where id = user_id;
end //

delimiter //
create procedure delete_user(in user_id int)
begin
    delete from users where id = user_id;
end //