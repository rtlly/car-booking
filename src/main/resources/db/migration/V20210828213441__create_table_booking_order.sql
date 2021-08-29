create table if not exists booking_order(
    id                 varchar(50)                         not null comment 'ID'  primary key,
    customer_id        varchar(50)                         not null comment '用户id',
    returned_date      date                                null comment '还车日期',
    start_date      date                                   not null comment '起始日期',
    end_date      date                                     not null comment '结束日期',
    `state`            varchar(50)                         not null comment '订单状态',
    created_user       varchar(50)                         not null comment '数据创建人',
    created_dt         timestamp default CURRENT_TIMESTAMP not null comment '数据创建时间',
    last_modified_user varchar(50)                         not null comment '数据最后修改人',
    last_modified_dt   timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '数据最后修改时间'
)
ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE utf8mb4_bin
    comment '订单表';