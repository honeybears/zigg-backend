CREATE TABLE zigg.board
(
    board_id   BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime              NOT NULL,
    updated_at datetime              NOT NULL,
    board_name VARCHAR(255)          NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (board_id)
);

CREATE TABLE zigg.comment
(
    comment_id   BIGINT AUTO_INCREMENT NOT NULL,
    created_at   datetime              NOT NULL,
    updated_at   datetime              NOT NULL,
    is_deleted   BIT(1)                NULL,
    likes        INT                   NULL,
    text_comment VARCHAR(255)          NULL,
    creator      BLOB                  NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (comment_id)
);

CREATE TABLE zigg.comment_replies
(
    comment_comment_id BIGINT NOT NULL,
    replies_comment_id BIGINT NOT NULL
);

CREATE TABLE zigg.fcm_token
(
    fcm_id     BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime              NOT NULL,
    updated_at datetime              NOT NULL,
    token      VARCHAR(255)          NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (fcm_id)
);

CREATE TABLE zigg.feedback
(
    feedback_id BLOB         NOT NULL,
    created_at  datetime     NOT NULL,
    updated_at  datetime     NOT NULL,
    message     VARCHAR(255) NULL,
    timeline    VARCHAR(255) NULL,
    type        ENUM         NULL,
    creator_id  BLOB         NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (feedback_id)
);

CREATE TABLE zigg.feedback_recipients
(
    feedback_feedback_id     BLOB NOT NULL,
    recipients_space_user_id BLOB NOT NULL
);

CREATE TABLE zigg.`history`
(
    history_id   BLOB         NOT NULL,
    created_at   datetime     NOT NULL,
    updated_at   datetime     NOT NULL,
    history_name VARCHAR(255) NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (history_id)
);

CREATE TABLE zigg.history_feedbacks
(
    history_history_id    BLOB NOT NULL,
    feedbacks_feedback_id BLOB NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (history_history_id, feedbacks_feedback_id)
);

CREATE TABLE zigg.image
(
    image_id   BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime              NOT NULL,
    updated_at datetime              NOT NULL,
    image_key  VARCHAR(255)          NULL,
    uploader   BLOB                  NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (image_id)
);

CREATE TABLE zigg.invite
(
    invite_id     BLOB     NOT NULL,
    created_at    datetime NOT NULL,
    updated_at    datetime NOT NULL,
    is_expired    BIT(1)   NOT NULL,
    invite_status ENUM     NULL,
    invitee       BLOB     NULL,
    inviter       BLOB     NULL,
    space         BLOB     NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (invite_id)
);

CREATE TABLE zigg.post
(
    post_id                  BIGINT AUTO_INCREMENT NOT NULL,
    created_at               datetime              NOT NULL,
    updated_at               datetime              NOT NULL,
    like_cnt                 INT                   NULL,
    comment_cnt              INT                   NULL,
    text_content             VARCHAR(255)          NULL,
    title                    VARCHAR(255)          NULL,
    board_board_id           BIGINT                NULL,
    creator                  BLOB                  NULL,
    video_content_video_id   BIGINT                NULL,
    video_thumbnail_image_id BIGINT                NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (post_id)
);

CREATE TABLE zigg.post_comments
(
    post_post_id        BIGINT NOT NULL,
    comments_comment_id BIGINT NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (post_post_id, comments_comment_id)
);

CREATE TABLE zigg.post_image_contents
(
    post_post_id            BIGINT NOT NULL,
    image_contents_image_id BIGINT NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (post_post_id, image_contents_image_id)
);

CREATE TABLE zigg.post_like
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    created_at   datetime              NOT NULL,
    updated_at   datetime              NOT NULL,
    post_post_id BIGINT                NULL,
    user_user_id BLOB                  NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE zigg.post_scrap
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    created_at   datetime              NOT NULL,
    updated_at   datetime              NOT NULL,
    post_post_id BIGINT                NULL,
    user_user_id BLOB                  NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE zigg.space
(
    space_id            BLOB         NOT NULL,
    created_at          datetime     NOT NULL,
    updated_at          datetime     NOT NULL,
    name                VARCHAR(255) NULL,
    reference_video_key VARCHAR(255) NULL,
    image_key_image_id  BIGINT       NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (space_id)
);

CREATE TABLE zigg.space_histories
(
    space_space_id       BLOB NOT NULL,
    histories_history_id BLOB NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (space_space_id, histories_history_id)
);

CREATE TABLE zigg.space_user
(
    space_user_id BLOB     NOT NULL,
    created_at    datetime NOT NULL,
    updated_at    datetime NOT NULL,
    space_role    ENUM     NULL,
    withdraw      BIT(1)   NOT NULL,
    space_id      BLOB     NULL,
    user_id       BLOB     NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (space_user_id)
);

CREATE TABLE zigg.user
(
    user_id                           BLOB         NOT NULL,
    created_at                        datetime     NOT NULL,
    updated_at                        datetime     NOT NULL,
    `description`                     VARCHAR(255) NULL,
    jwt_token                         VARCHAR(255) NULL,
    name                              VARCHAR(255) NULL,
    nickname                          VARCHAR(255) NULL,
    platform                          ENUM         NULL,
    provider_id                       VARCHAR(255) NULL,
    user_role                         ENUM         NULL,
    tags                              VARCHAR(255) NULL,
    profile_banner_image_key_image_id BIGINT       NULL,
    profile_image_key_image_id        BIGINT       NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (user_id)
);

CREATE TABLE zigg.user_device_tokens
(
    user_user_id         BLOB   NOT NULL,
    device_tokens_fcm_id BIGINT NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (user_user_id, device_tokens_fcm_id)
);

CREATE TABLE zigg.video
(
    video_id   BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime              NOT NULL,
    updated_at datetime              NOT NULL,
    duration   VARCHAR(255)          NULL,
    video_key  VARCHAR(255)          NULL,
    uploader   BLOB                  NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (video_id)
);

ALTER TABLE zigg.space_histories
    ADD CONSTRAINT UK5i18bfm07mg6wqy8anhy1t4yg UNIQUE (histories_history_id);

ALTER TABLE zigg.comment_replies
    ADD CONSTRAINT UK8iuxd4uf9ielc9w0vqqpma789 UNIQUE (replies_comment_id);

ALTER TABLE zigg.user_device_tokens
    ADD CONSTRAINT UKacv5hs3ejm1s9l84qw5ahepaj UNIQUE (device_tokens_fcm_id);

ALTER TABLE zigg.history_feedbacks
    ADD CONSTRAINT UKap8s6b154d94pw1x74vwl11mi UNIQUE (feedbacks_feedback_id);

ALTER TABLE zigg.post_comments
    ADD CONSTRAINT UKp5mtl3wujn9knlxr3mv02pxym UNIQUE (comments_comment_id);

ALTER TABLE zigg.post_image_contents
    ADD CONSTRAINT UKr5c3urbdjh5kmg587c0r1fk5r UNIQUE (image_contents_image_id);

ALTER TABLE zigg.invite
    ADD CONSTRAINT FK17dr4j1ldf68ijh9jl2y4xcc4 FOREIGN KEY (inviter) REFERENCES zigg.user (user_id) ON DELETE NO ACTION;

CREATE INDEX FK17dr4j1ldf68ijh9jl2y4xcc4 ON zigg.invite (inviter);

ALTER TABLE zigg.post_comments
    ADD CONSTRAINT FK1jod8ebo19f650nperx6ahpyx FOREIGN KEY (comments_comment_id) REFERENCES zigg.comment (comment_id) ON DELETE NO ACTION;

ALTER TABLE zigg.space_histories
    ADD CONSTRAINT FK37i3i95n4wxhbuy2qgduuemf2 FOREIGN KEY (histories_history_id) REFERENCES zigg.`history` (history_id) ON DELETE NO ACTION;

ALTER TABLE zigg.post
    ADD CONSTRAINT FK3orwkcnrtfi169j7oyxo856nx FOREIGN KEY (video_thumbnail_image_id) REFERENCES zigg.image (image_id) ON DELETE NO ACTION;

CREATE INDEX FK3orwkcnrtfi169j7oyxo856nx ON zigg.post (video_thumbnail_image_id);

ALTER TABLE zigg.post_image_contents
    ADD CONSTRAINT FK41csw925clg9jgs7d4punbc9y FOREIGN KEY (post_post_id) REFERENCES zigg.post (post_id) ON DELETE NO ACTION;

ALTER TABLE zigg.space_user
    ADD CONSTRAINT FK41uctqfllf093h11jpuul83fe FOREIGN KEY (user_id) REFERENCES zigg.user (user_id) ON DELETE NO ACTION;

CREATE INDEX FK41uctqfllf093h11jpuul83fe ON zigg.space_user (user_id);

ALTER TABLE zigg.space_histories
    ADD CONSTRAINT FK5fjgtmw83k3edefc2jb82vc4x FOREIGN KEY (space_space_id) REFERENCES zigg.space (space_id) ON DELETE NO ACTION;

ALTER TABLE zigg.feedback_recipients
    ADD CONSTRAINT FK5hfknxf58qqi2a8ro4qoobpm3 FOREIGN KEY (feedback_feedback_id) REFERENCES zigg.feedback (feedback_id) ON DELETE NO ACTION;

CREATE INDEX FK5hfknxf58qqi2a8ro4qoobpm3 ON zigg.feedback_recipients (feedback_feedback_id);

ALTER TABLE zigg.post_like
    ADD CONSTRAINT FK89uoktlvquc87m52ox2x50hf5 FOREIGN KEY (post_post_id) REFERENCES zigg.post (post_id) ON DELETE NO ACTION;

CREATE INDEX FK89uoktlvquc87m52ox2x50hf5 ON zigg.post_like (post_post_id);

ALTER TABLE zigg.post_scrap
    ADD CONSTRAINT FK8m5nbiqqeaau0a7tfhkpar9oc FOREIGN KEY (post_post_id) REFERENCES zigg.post (post_id) ON DELETE NO ACTION;

CREATE INDEX FK8m5nbiqqeaau0a7tfhkpar9oc ON zigg.post_scrap (post_post_id);

ALTER TABLE zigg.comment
    ADD CONSTRAINT FKbajbn32p7kj4vv771c5o0g9m FOREIGN KEY (creator) REFERENCES zigg.user (user_id) ON DELETE NO ACTION;

CREATE INDEX FKbajbn32p7kj4vv771c5o0g9m ON zigg.comment (creator);

ALTER TABLE zigg.post
    ADD CONSTRAINT FKbq8cspws6cmwfjjj378uy3fdj FOREIGN KEY (video_content_video_id) REFERENCES zigg.video (video_id) ON DELETE NO ACTION;

CREATE INDEX FKbq8cspws6cmwfjjj378uy3fdj ON zigg.post (video_content_video_id);

ALTER TABLE zigg.post
    ADD CONSTRAINT FKbuaebop1n8uhx0uqlnfnye9u5 FOREIGN KEY (creator) REFERENCES zigg.user (user_id) ON DELETE NO ACTION;

CREATE INDEX FKbuaebop1n8uhx0uqlnfnye9u5 ON zigg.post (creator);

ALTER TABLE zigg.user_device_tokens
    ADD CONSTRAINT FKck5mpynop7rvxgy16ynn3bd8p FOREIGN KEY (device_tokens_fcm_id) REFERENCES zigg.fcm_token (fcm_id) ON DELETE NO ACTION;

ALTER TABLE zigg.feedback
    ADD CONSTRAINT FKctqubma5fvrobxkjblp0ki0gj FOREIGN KEY (creator_id) REFERENCES zigg.space_user (space_user_id) ON DELETE NO ACTION;

CREATE INDEX FKctqubma5fvrobxkjblp0ki0gj ON zigg.feedback (creator_id);

ALTER TABLE zigg.user_device_tokens
    ADD CONSTRAINT FKef6b50gm1tu0qkj943voj3o8x FOREIGN KEY (user_user_id) REFERENCES zigg.user (user_id) ON DELETE NO ACTION;

ALTER TABLE zigg.comment_replies
    ADD CONSTRAINT FKft2oospw1s0b2btelvdcl77vi FOREIGN KEY (replies_comment_id) REFERENCES zigg.comment (comment_id) ON DELETE NO ACTION;

ALTER TABLE zigg.space
    ADD CONSTRAINT FKfxv38hmy559tnmdue8j4wrq15 FOREIGN KEY (image_key_image_id) REFERENCES zigg.image (image_id) ON DELETE NO ACTION;

CREATE INDEX FKfxv38hmy559tnmdue8j4wrq15 ON zigg.space (image_key_image_id);

ALTER TABLE zigg.feedback_recipients
    ADD CONSTRAINT FKgvjaqirwbdq9p6g415ah6qj7m FOREIGN KEY (recipients_space_user_id) REFERENCES zigg.space_user (space_user_id) ON DELETE NO ACTION;

CREATE INDEX FKgvjaqirwbdq9p6g415ah6qj7m ON zigg.feedback_recipients (recipients_space_user_id);

ALTER TABLE zigg.post_comments
    ADD CONSTRAINT FKh3a98kwisr5vka488yw7uyxuk FOREIGN KEY (post_post_id) REFERENCES zigg.post (post_id) ON DELETE NO ACTION;

ALTER TABLE zigg.space_user
    ADD CONSTRAINT FKhmodbff0cni9kll8bqc2n8ygg FOREIGN KEY (space_id) REFERENCES zigg.space (space_id) ON DELETE NO ACTION;

CREATE INDEX FKhmodbff0cni9kll8bqc2n8ygg ON zigg.space_user (space_id);

ALTER TABLE zigg.user
    ADD CONSTRAINT FKhprbh316ur7niobeo662xv89x FOREIGN KEY (profile_banner_image_key_image_id) REFERENCES zigg.image (image_id) ON DELETE NO ACTION;

CREATE INDEX FKhprbh316ur7niobeo662xv89x ON zigg.user (profile_banner_image_key_image_id);

ALTER TABLE zigg.comment_replies
    ADD CONSTRAINT FKj01hm5s17rd8q8jc98ens5m90 FOREIGN KEY (comment_comment_id) REFERENCES zigg.comment (comment_id) ON DELETE NO ACTION;

CREATE INDEX FKj01hm5s17rd8q8jc98ens5m90 ON zigg.comment_replies (comment_comment_id);

ALTER TABLE zigg.post
    ADD CONSTRAINT FKjdwd14rfby2rlu2ju5e0jepl3 FOREIGN KEY (board_board_id) REFERENCES zigg.board (board_id) ON DELETE NO ACTION;

CREATE INDEX FKjdwd14rfby2rlu2ju5e0jepl3 ON zigg.post (board_board_id);

ALTER TABLE zigg.video
    ADD CONSTRAINT FKlcwqgj3aipnh8at6trsm8sqmw FOREIGN KEY (uploader) REFERENCES zigg.user (user_id) ON DELETE NO ACTION;

CREATE INDEX FKlcwqgj3aipnh8at6trsm8sqmw ON zigg.video (uploader);

ALTER TABLE zigg.invite
    ADD CONSTRAINT FKlf3nx4p75gy5ffyapitjs57a3 FOREIGN KEY (space) REFERENCES zigg.space (space_id) ON DELETE NO ACTION;

CREATE INDEX FKlf3nx4p75gy5ffyapitjs57a3 ON zigg.invite (space);

ALTER TABLE zigg.user
    ADD CONSTRAINT FKllxa6yp80pm7wkv7c7omayddj FOREIGN KEY (profile_image_key_image_id) REFERENCES zigg.image (image_id) ON DELETE NO ACTION;

CREATE INDEX FKllxa6yp80pm7wkv7c7omayddj ON zigg.user (profile_image_key_image_id);

ALTER TABLE zigg.history_feedbacks
    ADD CONSTRAINT FKm0soj87becp8bxwpv7go50n8h FOREIGN KEY (feedbacks_feedback_id) REFERENCES zigg.feedback (feedback_id) ON DELETE NO ACTION;

ALTER TABLE zigg.post_like
    ADD CONSTRAINT FKm15h83bh2ocahuoir27bwe27a FOREIGN KEY (user_user_id) REFERENCES zigg.user (user_id) ON DELETE NO ACTION;

CREATE INDEX FKm15h83bh2ocahuoir27bwe27a ON zigg.post_like (user_user_id);

ALTER TABLE zigg.post_image_contents
    ADD CONSTRAINT FKm8wyejd5pl2fiy5j7itixwhe2 FOREIGN KEY (image_contents_image_id) REFERENCES zigg.image (image_id) ON DELETE NO ACTION;

ALTER TABLE zigg.post_scrap
    ADD CONSTRAINT FKn2kuv0drafpdy4q5ms1px0u6p FOREIGN KEY (user_user_id) REFERENCES zigg.user (user_id) ON DELETE NO ACTION;

CREATE INDEX FKn2kuv0drafpdy4q5ms1px0u6p ON zigg.post_scrap (user_user_id);

ALTER TABLE zigg.invite
    ADD CONSTRAINT FKoe0829o0jncoq8lgace7p3phb FOREIGN KEY (invitee) REFERENCES zigg.user (user_id) ON DELETE NO ACTION;

CREATE INDEX FKoe0829o0jncoq8lgace7p3phb ON zigg.invite (invitee);

ALTER TABLE zigg.image
    ADD CONSTRAINT FKsh58fujjtnyx8hdqc8p0xuufg FOREIGN KEY (uploader) REFERENCES zigg.user (user_id) ON DELETE NO ACTION;

CREATE INDEX FKsh58fujjtnyx8hdqc8p0xuufg ON zigg.image (uploader);

ALTER TABLE zigg.history_feedbacks
    ADD CONSTRAINT FKst40rcmydrqhwa56cpejy5s3m FOREIGN KEY (history_history_id) REFERENCES zigg.`history` (history_id) ON DELETE NO ACTION;

DROP TABLE zigg.flyway_schema_history;