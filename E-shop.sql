-- create database eshop;
use eshop;

-- 网上商城项目
-- drop table if exists tableName;

-- 1.管理员表:users
create table users(
  userId int primary key not null auto_increment, #管理员id
  login_name varchar(50) not null, #登录账号
  psw varchar(30) not null ,       #登录密码
  status int not null, #状态(1启动,2禁用)
  user_role int not null #角色(1老板,2员工)
);

-- drop table users

-- 2.商品类型表:goods_type
create table goods_type(
  type_id int primary key not null auto_increment, #商品类型id
  type_name varchar(50),#类型名
  userId int not null , #商品类型创建人id(管理员id)
  super_id int          #上级类型id(二级菜单)
);

--  drop table goods_type

-- 3.商品表
create table goods(

  good_id int primary key not null auto_increment, #商品id
  goods_name varchar(50) not null ,
  type_id int,                  #所属小分类
  super_id int not null,        #所属大分类
  price decimal not null check(price>=0),       #单价
  stock int check(stock>=0) ,   #商品库存
  status int not null ,         #是否上架(0:未上架,1:上架)
  remark varchar(200)           #商品备注
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
    buyer_name  varchar(50)       not null,
    account     varchar(50)       not null comment '登录账号',
    psw         varchar(50)       not null,
    balance     decimal default 0 not null comment '余额',
    pay_psw     varchar(50)       not null comment '支付密码',
    face        varchar(100)      null comment '头像',
    create_time datetime          not null
);

--  买家充值表store
create table store
(
    store_id   int auto_increment
        primary key,
    buyer_id   int      not null comment '买家主键',
    store_time datetime not null comment '充值时间',
    amount     decimal  not null comment '充值金额'
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
    status     int          null comment '状态'
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
    order_id      int auto_increment
        primary key,
    create_time   datetime      not null comment '创建日期',
    delivery_time datetime      null comment '发货时间',
    teceive_time  datetime      null comment '收货时间',
    address       varchar(100)  not null,
    phone         varchar(30)   not null,
    nicheng       varchar(30)   not null,
    buyer_id      int           not null comment '所属买家',
    total         decimal       null comment '金额',
    status        int default 0 not null comment '状态(0:创建 1:发货  2:已收货)'
)
    comment '订单表';


# 订单明细表:order_detail
create table order_detail
(
    detail_id int auto_increment
        primary key,
    order_id  int not null comment '所属订单',
    good_id   int not null comment '商品id',
    num       int not null comment '购买数量'
)
    comment '订单明细表';