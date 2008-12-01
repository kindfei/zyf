create table products (
    product_id varchar(10) primary key not null,
    product_name varchar(50),
    leverage decimal(5)
);
insert into products values('P01','USD/JPY',50);
insert into products values('P02','USD/JPY',100);
insert into products values('P03','USD/JPY',200);
insert into products values('P04','EUR/JPY',50);
insert into products values('P05','EUR/JPY',100);
insert into products values('P06','EUR/JPY',200);
insert into products values('P07','GBP/JPY',50);
insert into products values('P08','GBP/JPY',100);
insert into products values('P09','GBP/JPY',200);