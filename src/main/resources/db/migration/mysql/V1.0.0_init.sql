CREATE TABLE board
(
    board_id   BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime              NOT NULL,
    updated_at datetime              NOT NULL,
    board_name VARCHAR(255)          NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (board_id)
);

CREATE TABLE comment
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

CREATE TABLE comment_replies
(
    comment_comment_id BIGINT NOT NULL,
    replies_comment_id BIGINT NOT NULL
);

CREATE TABLE fcm_token
(
    fcm_id     BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime              NOT NULL,
    updated_at datetime              NOT NULL,
    token      VARCHAR(255)          NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (fcm_id)
);

CREATE TABLE feedback
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

CREATE TABLE feedback_recipients
(
    feedback_feedback_id     BLOB NOT NULL,
    recipients_space_user_id BLOB NOT NULL
);

CREATE TABLE flyway_schema_history
(
    installed_rank INT                                   NOT NULL,
    version        VARCHAR(50)                           NULL,
    `description`  VARCHAR(200)                          NOT NULL,
    type           VARCHAR(20)                           NOT NULL,
    script         VARCHAR(1000)                         NOT NULL,
    checksum       INT                                   NULL,
    installed_by   VARCHAR(100)                          NOT NULL,
    installed_on   timestamp DEFAULT 'CURRENT_TIMESTAMP' NOT NULL,
    execution_time INT                                   NOT NULL,
    success        TINYINT(1)                            NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (installed_rank)
);

CREATE TABLE `history`
(
    history_id   BLOB         NOT NULL,
    created_at   datetime     NOT NULL,
    updated_at   datetime     NOT NULL,
    history_name VARCHAR(255) NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (history_id)
);

CREATE TABLE history_feedbacks
(
    history_history_id    BLOB NOT NULL,
    feedbacks_feedback_id BLOB NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (history_history_id, feedbacks_feedback_id)
);

CREATE TABLE image
(
    image_id   BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime              NOT NULL,
    updated_at datetime              NOT NULL,
    image_key  VARCHAR(255)          NULL,
    uploader   BLOB                  NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (image_id)
);

CREATE TABLE invite
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

CREATE TABLE post
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

CREATE TABLE post_comments
(
    post_post_id        BIGINT NOT NULL,
    comments_comment_id BIGINT NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (post_post_id, comments_comment_id)
);

CREATE TABLE post_image_contents
(
    post_post_id            BIGINT NOT NULL,
    image_contents_image_id BIGINT NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (post_post_id, image_contents_image_id)
);

CREATE TABLE post_like
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    created_at   datetime              NOT NULL,
    updated_at   datetime              NOT NULL,
    post_post_id BIGINT                NULL,
    user_user_id BLOB                  NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE post_scrap
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    created_at   datetime              NOT NULL,
    updated_at   datetime              NOT NULL,
    post_post_id BIGINT                NULL,
    user_user_id BLOB                  NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE space
(
    space_id            BLOB         NOT NULL,
    created_at          datetime     NOT NULL,
    updated_at          datetime     NOT NULL,
    name                VARCHAR(255) NULL,
    reference_video_key VARCHAR(255) NULL,
    image_key_image_id  BIGINT       NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (space_id)
);

CREATE TABLE space_histories
(
    space_space_id       BLOB NOT NULL,
    histories_history_id BLOB NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (space_space_id, histories_history_id)
);

CREATE TABLE space_user
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

CREATE TABLE user
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

CREATE TABLE user_device_tokens
(
    user_user_id         BLOB   NOT NULL,
    device_tokens_fcm_id BIGINT NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (user_user_id, device_tokens_fcm_id)
);

CREATE TABLE video
(
    video_id   BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime              NOT NULL,
    updated_at datetime              NOT NULL,
    duration   VARCHAR(255)          NULL,
    video_key  VARCHAR(255)          NULL,
    uploader   BLOB                  NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (video_id)
);

ALTER TABLE space_histories
    ADD CONSTRAINT UK5i18bfm07mg6wqy8anhy1t4yg UNIQUE (histories_history_id);

ALTER TABLE comment_replies
    ADD CONSTRAINT UK8iuxd4uf9ielc9w0vqqpma789 UNIQUE (replies_comment_id);

ALTER TABLE history_feedbacks
    ADD CONSTRAINT UKap8s6b154d94pw1x74vwl11mi UNIQUE (feedbacks_feedback_id);

ALTER TABLE user_device_tokens
    ADD CONSTRAINT UKcauhg0ikqv9hwkkic3t99updb UNIQUE (device_tokens_fcm_id);

ALTER TABLE post_comments
    ADD CONSTRAINT UKp5mtl3wujn9knlxr3mv02pxym UNIQUE (comments_comment_id);

ALTER TABLE post_image_contents
    ADD CONSTRAINT UKr5c3urbdjh5kmg587c0r1fk5r UNIQUE (image_contents_image_id);

CREATE INDEX flyway_schema_history_s_idx ON flyway_schema_history (success);

ALTER TABLE post_comments
    ADD CONSTRAINT FK1jod8ebo19f650nperx6ahpyx FOREIGN KEY (comments_comment_id) REFERENCES comment (comment_id) ON DELETE NO ACTION;

ALTER TABLE video
    ADD CONSTRAINT FK28cx58hgy0l9kwlv5w03lgsnt FOREIGN KEY (uploader) REFERENCES user (user_id) ON DELETE NO ACTION;

CREATE INDEX FK28cx58hgy0l9kwlv5w03lgsnt ON video (uploader);

ALTER TABLE space_histories
    ADD CONSTRAINT FK37i3i95n4wxhbuy2qgduuemf2 FOREIGN KEY (histories_history_id) REFERENCES `history` (history_id) ON DELETE NO ACTION;

ALTER TABLE post
    ADD CONSTRAINT FK3orwkcnrtfi169j7oyxo856nx FOREIGN KEY (video_thumbnail_image_id) REFERENCES image (image_id) ON DELETE NO ACTION;

CREATE INDEX FK3orwkcnrtfi169j7oyxo856nx ON post (video_thumbnail_image_id);

ALTER TABLE post_image_contents
    ADD CONSTRAINT FK41csw925clg9jgs7d4punbc9y FOREIGN KEY (post_post_id) REFERENCES post (post_id) ON DELETE NO ACTION;

ALTER TABLE space_histories
    ADD CONSTRAINT FK5fjgtmw83k3edefc2jb82vc4x FOREIGN KEY (space_space_id) REFERENCES space (space_id) ON DELETE NO ACTION;

ALTER TABLE feedback_recipients
    ADD CONSTRAINT FK5hfknxf58qqi2a8ro4qoobpm3 FOREIGN KEY (feedback_feedback_id) REFERENCES feedback (feedback_id) ON DELETE NO ACTION;

CREATE INDEX FK5hfknxf58qqi2a8ro4qoobpm3 ON feedback_recipients (feedback_feedback_id);

ALTER TABLE invite
    ADD CONSTRAINT FK60wkqtxja6o0hgou6cme5rs8e FOREIGN KEY (invitee) REFERENCES user (user_id) ON DELETE NO ACTION;

CREATE INDEX FK60wkqtxja6o0hgou6cme5rs8e ON invite (invitee);

ALTER TABLE user
    ADD CONSTRAINT FK72sgc830otp5n8nscyukrx9ja FOREIGN KEY (profile_image_key_image_id) REFERENCES image (image_id) ON DELETE NO ACTION;

CREATE INDEX FK72sgc830otp5n8nscyukrx9ja ON user (profile_image_key_image_id);

ALTER TABLE post_like
    ADD CONSTRAINT FK89uoktlvquc87m52ox2x50hf5 FOREIGN KEY (post_post_id) REFERENCES post (post_id) ON DELETE NO ACTION;

CREATE INDEX FK89uoktlvquc87m52ox2x50hf5 ON post_like (post_post_id);

ALTER TABLE post_scrap
    ADD CONSTRAINT FK8m5nbiqqeaau0a7tfhkpar9oc FOREIGN KEY (post_post_id) REFERENCES post (post_id) ON DELETE NO ACTION;

CREATE INDEX FK8m5nbiqqeaau0a7tfhkpar9oc ON post_scrap (post_post_id);

ALTER TABLE user_device_tokens
    ADD CONSTRAINT FK988a1na0t00tw7i0s99m39d1m FOREIGN KEY (device_tokens_fcm_id) REFERENCES fcm_token (fcm_id) ON DELETE NO ACTION;

ALTER TABLE post
    ADD CONSTRAINT FKaa8l9ff159t8jldvrdr8ryc9d FOREIGN KEY (creator) REFERENCES user (user_id) ON DELETE NO ACTION;

CREATE INDEX FKaa8l9ff159t8jldvrdr8ryc9d ON post (creator);

ALTER TABLE post_like
    ADD CONSTRAINT FKb4p2017s194os2nijkp0qmgbp FOREIGN KEY (user_user_id) REFERENCES user (user_id) ON DELETE NO ACTION;

CREATE INDEX FKb4p2017s194os2nijkp0qmgbp ON post_like (user_user_id);

ALTER TABLE user_device_tokens
    ADD CONSTRAINT FKboohyt5eqhixumagdrpiotn7g FOREIGN KEY (user_user_id) REFERENCES user (user_id) ON DELETE NO ACTION;

ALTER TABLE post
    ADD CONSTRAINT FKbq8cspws6cmwfjjj378uy3fdj FOREIGN KEY (video_content_video_id) REFERENCES video (video_id) ON DELETE NO ACTION;

CREATE INDEX FKbq8cspws6cmwfjjj378uy3fdj ON post (video_content_video_id);

ALTER TABLE feedback
    ADD CONSTRAINT FKctqubma5fvrobxkjblp0ki0gj FOREIGN KEY (creator_id) REFERENCES space_user (space_user_id) ON DELETE NO ACTION;

CREATE INDEX FKctqubma5fvrobxkjblp0ki0gj ON feedback (creator_id);

ALTER TABLE comment
    ADD CONSTRAINT FKfqymi934nige0v1gba0k0cx3g FOREIGN KEY (creator) REFERENCES user (user_id) ON DELETE NO ACTION;

CREATE INDEX FKfqymi934nige0v1gba0k0cx3g ON comment (creator);

ALTER TABLE comment_replies
    ADD CONSTRAINT FKft2oospw1s0b2btelvdcl77vi FOREIGN KEY (replies_comment_id) REFERENCES comment (comment_id) ON DELETE NO ACTION;

ALTER TABLE space
    ADD CONSTRAINT FKfxv38hmy559tnmdue8j4wrq15 FOREIGN KEY (image_key_image_id) REFERENCES image (image_id) ON DELETE NO ACTION;

CREATE INDEX FKfxv38hmy559tnmdue8j4wrq15 ON space (image_key_image_id);

ALTER TABLE feedback_recipients
    ADD CONSTRAINT FKgvjaqirwbdq9p6g415ah6qj7m FOREIGN KEY (recipients_space_user_id) REFERENCES space_user (space_user_id) ON DELETE NO ACTION;

CREATE INDEX FKgvjaqirwbdq9p6g415ah6qj7m ON feedback_recipients (recipients_space_user_id);

ALTER TABLE post_comments
    ADD CONSTRAINT FKh3a98kwisr5vka488yw7uyxuk FOREIGN KEY (post_post_id) REFERENCES post (post_id) ON DELETE NO ACTION;

ALTER TABLE image
    ADD CONSTRAINT FKhj9w2eemvk9oq6ypj5uk6m5j7 FOREIGN KEY (uploader) REFERENCES user (user_id) ON DELETE NO ACTION;

CREATE INDEX FKhj9w2eemvk9oq6ypj5uk6m5j7 ON image (uploader);

ALTER TABLE post_scrap
    ADD CONSTRAINT FKhk7swe1we1d40iepf8mtb5a8f FOREIGN KEY (user_user_id) REFERENCES user (user_id) ON DELETE NO ACTION;

CREATE INDEX FKhk7swe1we1d40iepf8mtb5a8f ON post_scrap (user_user_id);

ALTER TABLE space_user
    ADD CONSTRAINT FKhmodbff0cni9kll8bqc2n8ygg FOREIGN KEY (space_id) REFERENCES space (space_id) ON DELETE NO ACTION;

CREATE INDEX FKhmodbff0cni9kll8bqc2n8ygg ON space_user (space_id);

ALTER TABLE comment_replies
    ADD CONSTRAINT FKj01hm5s17rd8q8jc98ens5m90 FOREIGN KEY (comment_comment_id) REFERENCES comment (comment_id) ON DELETE NO ACTION;

CREATE INDEX FKj01hm5s17rd8q8jc98ens5m90 ON comment_replies (comment_comment_id);

ALTER TABLE post
    ADD CONSTRAINT FKjdwd14rfby2rlu2ju5e0jepl3 FOREIGN KEY (board_board_id) REFERENCES board (board_id) ON DELETE NO ACTION;

CREATE INDEX FKjdwd14rfby2rlu2ju5e0jepl3 ON post (board_board_id);

ALTER TABLE invite
    ADD CONSTRAINT FKlf3nx4p75gy5ffyapitjs57a3 FOREIGN KEY (space) REFERENCES space (space_id) ON DELETE NO ACTION;

CREATE INDEX FKlf3nx4p75gy5ffyapitjs57a3 ON invite (space);

ALTER TABLE history_feedbacks
    ADD CONSTRAINT FKm0soj87becp8bxwpv7go50n8h FOREIGN KEY (feedbacks_feedback_id) REFERENCES feedback (feedback_id) ON DELETE NO ACTION;

ALTER TABLE post_image_contents
    ADD CONSTRAINT FKm8wyejd5pl2fiy5j7itixwhe2 FOREIGN KEY (image_contents_image_id) REFERENCES image (image_id) ON DELETE NO ACTION;

ALTER TABLE user
    ADD CONSTRAINT FKoes0y38697beqgx8ntnppcbif FOREIGN KEY (profile_banner_image_key_image_id) REFERENCES image (image_id) ON DELETE NO ACTION;

CREATE INDEX FKoes0y38697beqgx8ntnppcbif ON user (profile_banner_image_key_image_id);

ALTER TABLE space_user
    ADD CONSTRAINT FKpwh215o0wuou6ak3fox00udgq FOREIGN KEY (user_id) REFERENCES user (user_id) ON DELETE NO ACTION;

CREATE INDEX FKpwh215o0wuou6ak3fox00udgq ON space_user (user_id);

ALTER TABLE invite
    ADD CONSTRAINT FKrej5tkou5ortka3ra8qe78vpt FOREIGN KEY (inviter) REFERENCES user (user_id) ON DELETE NO ACTION;

CREATE INDEX FKrej5tkou5ortka3ra8qe78vpt ON invite (inviter);

ALTER TABLE history_feedbacks
    ADD CONSTRAINT FKst40rcmydrqhwa56cpejy5s3m FOREIGN KEY (history_history_id) REFERENCES `history` (history_id) ON DELETE NO ACTION;