create table board_posts
(
    board_board_id bigint not null,
    posts_post_id  bigint not null,
    primary key (board_board_id, posts_post_id),
    constraint UK8qgn4buo6mcwvhr6wy1185ewp
        unique (posts_post_id),
    constraint FKeopjo9uapbq6e1km7y5eqac12
        foreign key (board_board_id) references board (board_id),
    constraint FKh40h2s4fw6rc0d2ewrnm1uunw
        foreign key (posts_post_id) references post (post_id)
);

create table board
(
    board_id bigint auto_increment
        primary key,
    name     varchar(255) null
);

create table comment
(
    comment_id        bigint auto_increment
        primary key,
    created_at        datetime(6)  not null,
    updated_at        datetime(6)  not null,
    likes             bigint       not null,
    text_comment      varchar(255) null,
    creator_user_id   binary(16)   null,
    parent_comment_id bigint       null,
    constraint FKf6v5iovdm8x7ywvwao7w1s1i9
        foreign key (creator_user_id) references user (user_id),
    constraint FKhvh0e2ybgg16bpu229a5teje7
        foreign key (parent_comment_id) references comment (comment_id)
);
create table fcmtoken
(
    fcm_id     bigint auto_increment
        primary key,
    created_at datetime(6)  not null,
    updated_at datetime(6)  not null,
    token      varchar(255) null
);


create table feedback_recipients
(
    feedback_feedback_id     binary(16) not null,
    recipients_space_user_id binary(16) not null,
    constraint FK5hfknxf58qqi2a8ro4qoobpm3
        foreign key (feedback_feedback_id) references feedback (feedback_id),
    constraint FKgvjaqirwbdq9p6g415ah6qj7m
        foreign key (recipients_space_user_id) references space_user (space_user_id)
);

create table feedback
(
    feedback_id binary(16)          not null
        primary key,
    created_at  datetime(6)         not null,
    updated_at  datetime(6)         not null,
    message     varchar(255)        null,
    timeline    varchar(255)        null,
    type        enum ('AI', 'USER') null,
    creator_id  binary(16)          null,
    constraint FKctqubma5fvrobxkjblp0ki0gj
        foreign key (creator_id) references space_user (space_user_id)
);

create table history_feedbacks
(
    history_history_id    binary(16) not null,
    feedbacks_feedback_id binary(16) not null,
    primary key (history_history_id, feedbacks_feedback_id),
    constraint UKap8s6b154d94pw1x74vwl11mi
        unique (feedbacks_feedback_id),
    constraint FKm0soj87becp8bxwpv7go50n8h
        foreign key (feedbacks_feedback_id) references feedback (feedback_id),
    constraint FKst40rcmydrqhwa56cpejy5s3m
        foreign key (history_history_id) references history (history_id)
);

create table history
(
    history_id                   binary(16)   not null
        primary key,
    created_at                   datetime(6)  not null,
    updated_at                   datetime(6)  not null,
    name                         varchar(255) null,
    video_key_video_id           bigint       null,
    video_thumbnail_url_image_id bigint       null,
    constraint UKf6vrs0ismyxitgiog0cacrxug
        unique (video_key_video_id),
    constraint UKhsmvp8taeyd1u13qdlemp10d8
        unique (video_thumbnail_url_image_id),
    constraint FK18i8edaqojq5svbooag6vl35u
        foreign key (video_thumbnail_url_image_id) references image (image_id),
    constraint FKnqex6gk7fy8vkso46lvqpwdvx
        foreign key (video_key_video_id) references video (video_id)
);


create table image
(
    image_id         bigint auto_increment
        primary key,
    created_at       datetime(6)  not null,
    updated_at       datetime(6)  not null,
    image_key        varchar(255) null,
    uploader_user_id binary(16)   null,
    constraint FKlav1xb1h0gcutu18nqotpwoyh
        foreign key (uploader_user_id) references user (user_id)
);

INSERT INTO zigg.image (image_id, created_at, updated_at, image_key, uploader_user_id) VALUES (1, '2024-10-04 19:07:46.000000', '2024-10-04 19:07:47.000000', 'profile/default_profile1.png', null);
INSERT INTO zigg.image (image_id, created_at, updated_at, image_key, uploader_user_id) VALUES (2, '2024-10-04 19:07:56.000000', '2024-10-04 19:08:01.000000', 'profile/default_profile2.png', null);
INSERT INTO zigg.image (image_id, created_at, updated_at, image_key, uploader_user_id) VALUES (3, '2024-10-04 19:08:07.000000', '2024-10-04 19:08:09.000000', 'profile/default_profile3.png', null);
INSERT INTO zigg.image (image_id, created_at, updated_at, image_key, uploader_user_id) VALUES (4, '2024-10-04 19:08:16.000000', '2024-10-04 19:08:18.000000', 'profile/default_profile4.png', null);
INSERT INTO zigg.image (image_id, created_at, updated_at, image_key, uploader_user_id) VALUES (5, '2024-10-04 19:08:29.000000', '2024-10-04 19:08:25.000000', 'profile/default_profile5.png', null);
INSERT INTO zigg.image (image_id, created_at, updated_at, image_key, uploader_user_id) VALUES (6, '2024-10-04 19:08:36.000000', '2024-10-04 19:08:39.000000', 'profile/default_profile6.png', null);
INSERT INTO zigg.image (image_id, created_at, updated_at, image_key, uploader_user_id) VALUES (7, '2024-10-04 19:08:44.000000', '2024-10-04 19:08:59.000000', 'profile/default_profile7.png', null);
INSERT INTO zigg.image (image_id, created_at, updated_at, image_key, uploader_user_id) VALUES (8, '2024-10-04 19:09:02.000000', '2024-10-04 19:09:03.000000', 'profile/default_profile8.png', null);
INSERT INTO zigg.image (image_id, created_at, updated_at, image_key, uploader_user_id) VALUES (9, '2024-10-04 19:09:14.000000', '2024-10-04 19:09:15.000000', 'profile/default_profile9.png', null);
INSERT INTO zigg.image (image_id, created_at, updated_at, image_key, uploader_user_id) VALUES (10, '2024-10-04 19:09:32.000000', '2024-10-04 19:09:35.000000', 'profile/default_profile10.png', null);
INSERT INTO zigg.image (image_id, created_at, updated_at, image_key, uploader_user_id) VALUES (11, '2024-10-04 19:12:30.269783', '2024-10-04 19:12:30.269783', 'image/space/default_space.jpeg', null);


create table invite
(
    invite_id  binary(16)                             not null
        primary key,
    created_at datetime(6)                            not null,
    updated_at datetime(6)                            not null,
    is_expired bit                                    not null,
    status     enum ('ACCEPTED', 'DENIED', 'WAITING') null,
    invitee    binary(16)                             null,
    inviter    binary(16)                             null,
    space      binary(16)                             null,
    constraint FK17dr4j1ldf68ijh9jl2y4xcc4
        foreign key (inviter) references user (user_id),
    constraint FKlf3nx4p75gy5ffyapitjs57a3
        foreign key (space) references space (space_id),
    constraint FKoe0829o0jncoq8lgace7p3phb
        foreign key (invitee) references user (user_id)
);


create table post_comments
(
    post_post_id        bigint not null,
    comments_comment_id bigint not null,
    primary key (post_post_id, comments_comment_id),
    constraint UKp5mtl3wujn9knlxr3mv02pxym
        unique (comments_comment_id),
    constraint FK1jod8ebo19f650nperx6ahpyx
        foreign key (comments_comment_id) references comment (comment_id),
    constraint FKh3a98kwisr5vka488yw7uyxuk
        foreign key (post_post_id) references post (post_id)
);

create table post_image_contents
(
    post_post_id            bigint not null,
    image_contents_image_id bigint not null,
    primary key (post_post_id, image_contents_image_id),
    constraint UKr5c3urbdjh5kmg587c0r1fk5r
        unique (image_contents_image_id),
    constraint FK41csw925clg9jgs7d4punbc9y
        foreign key (post_post_id) references post (post_id),
    constraint FKm8wyejd5pl2fiy5j7itixwhe2
        foreign key (image_contents_image_id) references image (image_id)
);

create table post
(
    post_id                  bigint auto_increment
        primary key,
    created_at               datetime(6)  not null,
    updated_at               datetime(6)  not null,
    likes                    bigint       not null,
    text_content             varchar(255) null,
    title                    varchar(255) null,
    board_board_id           bigint       null,
    creator_user_id          binary(16)   null,
    video_content_video_id   bigint       null,
    video_thumbnail_image_id bigint       null,
    constraint UKgyp2d4r1sidp9jmm0n4ct6mvo
        unique (video_content_video_id),
    constraint UKul65dx0ce7sw4mxv7egkv1ia
        unique (video_thumbnail_image_id),
    constraint FK3orwkcnrtfi169j7oyxo856nx
        foreign key (video_thumbnail_image_id) references image (image_id),
    constraint FK3ytjg3polootwmtxq6b7ceamr
        foreign key (creator_user_id) references user (user_id),
    constraint FKbq8cspws6cmwfjjj378uy3fdj
        foreign key (video_content_video_id) references video (video_id),
    constraint FKjdwd14rfby2rlu2ju5e0jepl3
        foreign key (board_board_id) references board (board_id)
);

create table space_histories
(
    space_space_id       binary(16) not null,
    histories_history_id binary(16) not null,
    primary key (space_space_id, histories_history_id),
    constraint UK5i18bfm07mg6wqy8anhy1t4yg
        unique (histories_history_id),
    constraint FK37i3i95n4wxhbuy2qgduuemf2
        foreign key (histories_history_id) references history (history_id),
    constraint FK5fjgtmw83k3edefc2jb82vc4x
        foreign key (space_space_id) references space (space_id)
);

create table space_user
(
    space_user_id binary(16)             not null
        primary key,
    created_at    datetime(6)            not null,
    updated_at    datetime(6)            not null,
    role          enum ('ADMIN', 'USER') null,
    withdraw      bit                    not null,
    space_id      binary(16)             null,
    user_id       binary(16)             null,
    constraint FK41uctqfllf093h11jpuul83fe
        foreign key (user_id) references user (user_id),
    constraint FKhmodbff0cni9kll8bqc2n8ygg
        foreign key (space_id) references space (space_id)
);

create table space
(
    space_id            binary(16)   not null
        primary key,
    created_at          datetime(6)  not null,
    updated_at          datetime(6)  not null,
    name                varchar(255) null,
    reference_video_key varchar(255) null,
    image_key_image_id  bigint       null,
    constraint UKedegj2562xkkkk1ik5qgbjk0w
        unique (image_key_image_id),
    constraint FKfxv38hmy559tnmdue8j4wrq15
        foreign key (image_key_image_id) references image (image_id)
);

create table user_device_tokens
(
    user_user_id         binary(16) not null,
    device_tokens_fcm_id bigint     not null,
    primary key (user_user_id, device_tokens_fcm_id),
    constraint UKacv5hs3ejm1s9l84qw5ahepaj
        unique (device_tokens_fcm_id),
    constraint FK9fycmgaa8x0o9gc71aka6wo9b
        foreign key (device_tokens_fcm_id) references fcmtoken (fcm_id),
    constraint FKef6b50gm1tu0qkj943voj3o8x
        foreign key (user_user_id) references user (user_id)
);

CREATE TABLE user (
    user_id                           BINARY(16)                                         NOT NULL PRIMARY KEY,
    created_at                        DATETIME(6)                                        NOT NULL,
    updated_at                        DATETIME(6)                                        NOT NULL,
    description                       VARCHAR(255)                                       NULL,
    jwt_token                         VARCHAR(255)                                       NULL,
    name                              VARCHAR(255)                                       NULL,
    nickname                          VARCHAR(255)                                       NULL,
    platform                          ENUM('APPLE', 'GOOGLE', 'GUEST', 'KAKAO', 'TEST') NULL,
                      provider_id                       VARCHAR(255)                                       NULL,
                      role                              ENUM('ADMIN', 'GUEST', 'USER')                    NULL,
                      tags                              VARCHAR(255)                                       NULL,
                      profile_banner_image_key_image_id BIGINT                                             NULL,
                      profile_image_key_image_id        BIGINT                                             NULL,
                      CONSTRAINT FKhprbh316ur7niobeo662xv89x
                          FOREIGN KEY (profile_banner_image_key_image_id) REFERENCES image(image_id),
                      CONSTRAINT FKllxa6yp80pm7wkv7c7omayddj
                          FOREIGN KEY (profile_image_key_image_id) REFERENCES image(image_id)
);


create table video
(
    video_id         bigint auto_increment
        primary key,
    created_at       datetime(6)  not null,
    updated_at       datetime(6)  not null,
    duration         varchar(255) null,
    video_key        varchar(255) null,
    uploader_user_id binary(16)   null,
    constraint FK5jvd8jx2g01ljlrmka730qxes
        foreign key (uploader_user_id) references user (user_id)
);

