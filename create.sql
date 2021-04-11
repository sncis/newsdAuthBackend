create sequence hibernate_sequence start 1 increment 1

    create table articles (
       article_id  serial not null,
        author varchar(255),
        content varchar(255),
        description varchar(255),
        isbookmarked boolean,
        publishedat varchar(255),
        source varchar(255),
        title varchar(255),
        url varchar(255),
        urltoimage varchar(255),
        user_id int4,
        primary key (article_id)
    )

    create table regsitration_confirmation_token (
       id int8 not null,
        date date,
        token varchar(255),
        user_id int4 not null,
        primary key (id)
    )

    create table roles (
       role_id int4 not null,
        role_description varchar(255),
        role_name varchar(255),
        primary key (role_id)
    )

    create table users (
       user_id int4 not null,
        active boolean,
        email varchar(255) not null,
        enabled boolean,
        locked boolean,
        password varchar(255),
        role int4,
        username varchar(255) not null,
        primary key (user_id)
    )

    alter table regsitration_confirmation_token 
       add constraint FKadfom3l3niqj8senxoac1d4v 
       foreign key (user_id) 
       references users
create sequence hibernate_sequence start 1 increment 1

    create table articles (
       article_id  serial not null,
        author varchar(255),
        content varchar(255),
        description varchar(255),
        isbookmarked boolean,
        publishedat varchar(255),
        source varchar(255),
        title varchar(255),
        url varchar(255),
        urltoimage varchar(255),
        user_id int4,
        primary key (article_id)
    )

    create table regsitration_confirmation_token (
       id int8 not null,
        date date,
        token varchar(255),
        user_id int4 not null,
        primary key (id)
    )

    create table roles (
       role_id int4 not null,
        role_description varchar(255),
        role_name varchar(255),
        primary key (role_id)
    )

    create table users (
       user_id int4 not null,
        active boolean,
        email varchar(255) not null,
        enabled boolean,
        locked boolean,
        password varchar(255),
        role int4,
        username varchar(255) not null,
        primary key (user_id)
    )

    alter table regsitration_confirmation_token 
       add constraint FKadfom3l3niqj8senxoac1d4v 
       foreign key (user_id) 
       references users
create sequence hibernate_sequence start 1 increment 1

    create table articles (
       article_id  serial not null,
        author varchar(255),
        content varchar(255),
        description varchar(255),
        isbookmarked boolean,
        publishedat varchar(255),
        source varchar(255),
        title varchar(255),
        url varchar(255),
        urltoimage varchar(255),
        user_id int4,
        primary key (article_id)
    )

    create table regsitration_confirmation_token (
       id int8 not null,
        date date,
        token varchar(255),
        user_id int4 not null,
        primary key (id)
    )

    create table roles (
       role_id int4 not null,
        role_description varchar(255),
        role_name varchar(255),
        primary key (role_id)
    )

    create table users (
       user_id int4 not null,
        active boolean,
        email varchar(255) not null,
        enabled boolean,
        locked boolean,
        password varchar(255),
        role int4,
        username varchar(255) not null,
        primary key (user_id)
    )

    alter table regsitration_confirmation_token 
       add constraint FKadfom3l3niqj8senxoac1d4v 
       foreign key (user_id) 
       references users
create sequence hibernate_sequence start 1 increment 1

    create table articles (
       article_id  serial not null,
        author varchar(255),
        content varchar(255),
        description varchar(255),
        isbookmarked boolean,
        publishedat varchar(255),
        source varchar(255),
        title varchar(255),
        url varchar(255),
        urltoimage varchar(255),
        user_id int4,
        primary key (article_id)
    )

    create table regsitration_confirmation_token (
       id int8 not null,
        date date,
        token varchar(255),
        user_id int4 not null,
        primary key (id)
    )

    create table roles (
       role_id int4 not null,
        role_description varchar(255),
        role_name varchar(255),
        primary key (role_id)
    )

    create table users (
       user_id int4 not null,
        active boolean,
        email varchar(255) not null,
        enabled boolean,
        locked boolean,
        password varchar(255),
        role int4,
        username varchar(255) not null,
        primary key (user_id)
    )

    alter table regsitration_confirmation_token 
       add constraint FKadfom3l3niqj8senxoac1d4v 
       foreign key (user_id) 
       references users
create sequence hibernate_sequence start 1 increment 1

    create table articles (
       article_id  serial not null,
        author varchar(255),
        content varchar(255),
        description varchar(255),
        isbookmarked boolean,
        publishedat varchar(255),
        source varchar(255),
        title varchar(255),
        url varchar(255),
        urltoimage varchar(255),
        user_id int4,
        primary key (article_id)
    )

    create table regsitration_confirmation_token (
       id int8 not null,
        date date,
        token varchar(255),
        user_id int4 not null,
        primary key (id)
    )

    create table roles (
       role_id int4 not null,
        role_description varchar(255),
        role_name varchar(255),
        primary key (role_id)
    )

    create table users (
       user_id int4 not null,
        active boolean,
        email varchar(255) not null,
        enabled boolean,
        locked boolean,
        password varchar(255),
        role int4,
        username varchar(255) not null,
        primary key (user_id)
    )

    alter table regsitration_confirmation_token 
       add constraint FKadfom3l3niqj8senxoac1d4v 
       foreign key (user_id) 
       references users
