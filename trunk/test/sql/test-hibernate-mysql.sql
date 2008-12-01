drop table if exists customers;
create table customers (
    customer_id varchar(10) primary key,
    customer_name varchar(50),
    address varchar(50),
    group_id varchar(10)
);
insert into customers values('C01','zhangyifei','China','G01');


drop table if exists emails;
create table emails (
    email_id varchar(10) primary key,
    customer_id varchar(10),
    email varchar(50)
);
insert into emails values('E01','C01','iameinstein@tom.com');
insert into emails values('E02','C01','iameinstein@sina.com');
insert into emails values('E03','C01','iameinstein@163.com');


drop table if exists products;
create table products (
    product_id varchar(10) primary key,
    product_name varchar(50),
    leverage int
) ENGINE = InnoDB;
insert into products values('P01','USD/JPY',50);
insert into products values('P02','USD/JPY',100);
insert into products values('P03','USD/JPY',200);
insert into products values('P04','EUR/JPY',50);
insert into products values('P05','EUR/JPY',100);
insert into products values('P06','EUR/JPY',200);
insert into products values('P07','GBP/JPY',50);
insert into products values('P08','GBP/JPY',100);
insert into products values('P09','GBP/JPY',200);


drop table if exists customer_product;
create table customer_product (
    id int auto_increment primary key,
    customer_id varchar(10),
    product_id varchar(10)
);
insert into customer_product values(null,'C01','P01');
insert into customer_product values(null,'C01','P02');
insert into customer_product values(null,'C01','P03');


drop table if exists groups;
create table groups (
    group_id varchar(10) primary key,
    rebate int
);
insert into groups values('G01',10);
insert into groups values('G02',20);
insert into groups values('G03',30);
