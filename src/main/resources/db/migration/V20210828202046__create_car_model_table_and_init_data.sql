create table if not exists car_model
(
    id                 varchar(50)                         not null comment 'ID' primary key,
    car_number         varchar(50)                         not null comment '车牌',
    `type`             varchar(50)                         not null comment '车的类型',
    rent_price         decimal(38, 4)                      not null comment '租车价格',
    created_user       varchar(50)                         not null comment '数据创建人',
    created_dt         timestamp default CURRENT_TIMESTAMP not null comment '数据创建时间',
    last_modified_user varchar(50)                         not null comment '数据最后修改人',
    last_modified_dt   timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '数据最后修改时间'
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE utf8mb4_bin
    comment '车库存表';

insert into car_model(id, car_number,`type`, rent_price,created_user, last_modified_user) values
(uuid(), 'carnumber01', 'TOYOTA_CAMRY', 200, 'system', 'system'),
(uuid(), 'carnumber02', 'TOYOTA_CAMRY', 200, 'system', 'system'),
(uuid(), 'carnumber03', 'BMW_650', 100, 'system', 'system'),
(uuid(), 'carnumber04', 'BMW_650', 100, 'system', 'system');