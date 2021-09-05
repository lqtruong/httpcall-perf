use `sample`;

create table if not exists `polls`(
    `id`            bigint primary key auto_increment,
    `poll`          varchar(100) not null,
    `tenant_id`     varchar(20) not null,
    `poll_status`   varchar(250) null,
    `reason`        varchar(500) null,
    `created_at`    datetime not null default current_timestamp,
    `modified_at`   datetime not null default current_timestamp
);

create index idx_polls_poll_status on `polls`(poll_status);
