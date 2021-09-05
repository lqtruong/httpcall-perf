use `sample`;

create table if not exists `polls_batch`(
    `id`            bigint primary key auto_increment,
    `batch`         bigint not null,
    `tenant_id`     varchar(20) not null,
    `table_name`    varchar(250) not null,
    `created_at`    datetime not null default current_timestamp,
    `modified_at`   datetime not null default current_timestamp
);

create index idx_polls_created_at on `polls_batch`(created_at);
