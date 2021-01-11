create table account
(
    id          serial primary key not null,
    login       varchar(2000),
    password    varchar(2000),
    employee_id int4 references employee (id)
);

create table employee
(
    id       serial primary key not null,
    first_name varchar(2000),
    last_name varchar(2000),
    inn      int8,
    hired    timestamp
);

insert into employee (first_name, last_name, inn) values ('denis', 'petrov',
                                                        123321123321123);

insert into account (login, password, employee_id)
values ('parsentev', '123', 1);
insert into account (login, password, employee_id)
values ('ban', '123', 1);

