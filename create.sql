create sequence hibernate_sequence start 1 increment 1

    create table articles (
       article_id int4 not null,
        author varchar(255),
        clean_url varchar(255),
        country varchar(255),
        isbookmarked boolean,
        language varchar(255),
        url varchar(255),
        published_date varchar(255),
        rank varchar(255),
        rights varchar(255),
        summary varchar(255),
        title varchar(255),
        topic varchar(255),
        user_id int4,
        primary key (article_id)
    )

    create table registration_confirmation_token (
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
        is_enabled boolean,
        is_locked boolean,
        password varchar(255) not null,
        role varchar(255),
        username varchar(255) not null,
        primary key (user_id)
    )

    alter table registration_confirmation_token 
       add constraint FK4haiysw2wtq6rspqoqyjn55ql 
       foreign key (user_id) 
       references users
create sequence hibernate_sequence start 1 increment 1

    create table articles (
       article_id int4 not null,
        author varchar(255),
        clean_url varchar(255),
        country varchar(255),
        isbookmarked boolean,
        language varchar(255),
        url varchar(255),
        published_date varchar(255),
        rank varchar(255),
        rights varchar(255),
        summary varchar(255),
        title varchar(255),
        topic varchar(255),
        user_id int4,
        primary key (article_id)
    )

    create table registration_confirmation_token (
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
        is_enabled boolean,
        is_locked boolean,
        password varchar(255) not null,
        role varchar(255),
        username varchar(255) not null,
        primary key (user_id)
    )

    alter table registration_confirmation_token 
       add constraint FK4haiysw2wtq6rspqoqyjn55ql 
       foreign key (user_id) 
       references users
create sequence hibernate_sequence start 1 increment 1

    create table articles (
       article_id int4 not null,
        author varchar(255),
        clean_url varchar(255),
        country varchar(255),
        isbookmarked boolean,
        language varchar(255),
        url varchar(255),
        published_date varchar(255),
        rank varchar(255),
        rights varchar(255),
        summary varchar(255),
        title varchar(255),
        topic varchar(255),
        user_id int4,
        primary key (article_id)
    )

    create table registration_confirmation_token (
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
        is_enabled boolean,
        is_locked boolean,
        password varchar(255) not null,
        role varchar(255),
        username varchar(255) not null,
        primary key (user_id)
    )

    alter table registration_confirmation_token 
       add constraint FK4haiysw2wtq6rspqoqyjn55ql 
       foreign key (user_id) 
       references users
create sequence hibernate_sequence start 1 increment 1

    create table articles (
       article_id int4 not null,
        author varchar(255),
        clean_url varchar(255),
        country varchar(255),
        isbookmarked boolean,
        language varchar(255),
        url varchar(255),
        published_date varchar(255),
        rank varchar(255),
        rights varchar(255),
        summary varchar(255),
        title varchar(255),
        topic varchar(255),
        user_id int4,
        primary key (article_id)
    )

    create table registration_confirmation_token (
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
        is_enabled boolean,
        is_locked boolean,
        password varchar(255) not null,
        role varchar(255),
        username varchar(255) not null,
        primary key (user_id)
    )

    alter table registration_confirmation_token 
       add constraint FK4haiysw2wtq6rspqoqyjn55ql 
       foreign key (user_id) 
       references users
create sequence hibernate_sequence start 1 increment 1

    create table articles (
       article_id int4 not null,
        author varchar(255),
        clean_url varchar(255),
        country varchar(255),
        isbookmarked boolean,
        language varchar(255),
        url varchar(255),
        published_date varchar(255),
        rank varchar(255),
        rights varchar(255),
        summary varchar(255),
        title varchar(255),
        topic varchar(255),
        user_id int4,
        primary key (article_id)
    )

    create table registration_confirmation_token (
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
        is_enabled boolean,
        is_locked boolean,
        password varchar(255) not null,
        role varchar(255),
        username varchar(255) not null,
        primary key (user_id)
    )

    alter table registration_confirmation_token 
       add constraint FK4haiysw2wtq6rspqoqyjn55ql 
       foreign key (user_id) 
       references users
create sequence hibernate_sequence start 1 increment 1

    create table articles (
       article_id int4 not null,
        author varchar(255),
        clean_url varchar(255),
        country varchar(255),
        isbookmarked boolean,
        language varchar(255),
        url varchar(255),
        published_date varchar(255),
        rank varchar(255),
        rights varchar(255),
        summary varchar(255),
        title varchar(255),
        topic varchar(255),
        user_id int4,
        primary key (article_id)
    )

    create table registration_confirmation_token (
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
        is_enabled boolean,
        is_locked boolean,
        password varchar(255) not null,
        role varchar(255),
        username varchar(255) not null,
        primary key (user_id)
    )

    alter table registration_confirmation_token 
       add constraint FK4haiysw2wtq6rspqoqyjn55ql 
       foreign key (user_id) 
       references users
