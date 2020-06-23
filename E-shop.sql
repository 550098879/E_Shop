-- create database eshop;
use eshop;

-- 网上商城项目
-- drop table if exists tableName;

-- 1.管理员表:users
create table users
(
    userId     int auto_increment
        primary key,
    login_name varchar(50)   not null,
    psw        varchar(30)   not null,
    status     int default 0 not null,
    user_role  int default 0 not null
);

-- drop table users

-- 2.商品类型表:goods_type
create table goods_type
(
    type_id   int auto_increment
        primary key,
    type_name varchar(50)   null,
    userId    int default 1 not null,
    super_id  int default 0 null
);

--  drop table goods_type

-- 3.商品表
create table goods
(
    good_id    int auto_increment
        primary key,
    goods_name varchar(50)    not null,
    type_id    int            null,
    super_id   int            not null,
    price      decimal(16, 2) not null comment '单价',
    stock      int            null comment '库存',
    status     int default 0  not null comment '0:未上架  1:已上架 ',
    remark     varchar(200)   null comment '备注',
    constraint goods_goods_name_uindex
        unique (goods_name)
);

--  drop table goods

--  4.商品图片表:goods_pic
create table goods_pic
(
    pic_id   int auto_increment
        primary key,
    pic_type int          null,
    pic_path varchar(100) null,
    goods_id int          null
);

--  买家表buyer
create table buyer
(
    buyer_id    int auto_increment
        primary key,
    buyer_name  varchar(50)                 not null,
    account     varchar(50)                 not null comment '登录账号',
    psw         varchar(50)                 not null,
    balance     decimal(16, 2) default 0.00 not null comment '余额',
    pay_psw     varchar(50)    default '0'  null comment '支付密码',
    face        varchar(100)                null comment '头像',
    create_time datetime                    not null,
    constraint buyer_account_uindex
        unique (account)
);

--  买家充值表store
create table store
(
    store_id   int auto_increment
        primary key,
    buyer_id   int            not null comment '买家主键',
    store_time datetime       not null comment '充值时间',
    amount     decimal(16, 2) not null comment '充值金额'
)
    comment '买家充值表';

-- 买家地址表address
create table address
(
    address_id int auto_increment
        primary key,
    buyer_id   int          not null,
    address    varchar(100) not null,
    phone      varchar(30)  not null,
    nicheng    varchar(30)  not null,
    status     int          null comment '状态:0:禁用  1:启用'
);


--  购物车表goods_car
create table good_car
(
    car_id   int auto_increment comment '购物车id'
        primary key,
    buyer_id int      not null,
    goods_id int      not null comment '商品id',
    num      int      not null comment '购买数量',
    add_time datetime not null
)
    comment '购物车';

--  订单表order_form
create table order_form
(
    order_id      int(20)        not null
        primary key,
    create_time   datetime       not null comment '创建日期',
    delivery_time datetime       null comment '发货时间',
    teceive_time  datetime       null comment '收货时间',
    address       varchar(100)   not null,
    phone         varchar(30)    not null,
    nicheng       varchar(30)    not null,
    buyer_id      int            not null comment '所属买家',
    total         decimal(16, 2) null comment '金额',
    status        int default 0  not null comment '状态(0:创建 1:发货  2:已收货)'
)
    comment '订单表';


--  订单明细表:order_detail
create table order_detail
(
    detail_id int auto_increment
        primary key,
    order_id  int(20) not null comment '所属订单',
    good_id   int     not null comment '商品id',
    num       int     not null comment '购买数量'
)
    comment '订单明细表';