create table if not exists car_rental_info (
    id                 varchar(50)                         not null comment 'ID'  primary key,
    car_id            varchar(50)                         not null comment '车的唯一标识',
    rental_date        date                                not null comment '租用日期',
    order_id           varchar(50)                         not null comment '订单id',
    created_user       varchar(50)                         not null comment '数据创建人',
    created_dt         timestamp default CURRENT_TIMESTAMP not null comment '数据创建时间',
    last_modified_user varchar(50)                         not null comment '数据最后修改人',
    last_modified_dt   timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '数据最后修改时间'
)
ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE utf8mb4_bin
    comment '租车信息表';

create unique index idx_car_id_and_date on car_rental_info(car_id, rental_date);