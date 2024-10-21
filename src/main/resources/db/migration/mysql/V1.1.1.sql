ALTER TABLE zigg.comment
    DROP FOREIGN KEY FKhvh0e2ybgg16bpu229a5teje7;

CREATE TABLE zigg.comment_replies
(
    comment_comment_id BIGINT NOT NULL,
    replies_comment_id BIGINT NOT NULL
);

ALTER TABLE zigg.comment_replies
    ADD CONSTRAINT UK8iuxd4uf9ielc9w0vqqpma789 UNIQUE (replies_comment_id);

ALTER TABLE zigg.comment_replies
    ADD CONSTRAINT FKft2oospw1s0b2btelvdcl77vi FOREIGN KEY (replies_comment_id) REFERENCES zigg.comment (comment_id) ON DELETE NO ACTION;

ALTER TABLE zigg.comment_replies
    ADD CONSTRAINT FKj01hm5s17rd8q8jc98ens5m90 FOREIGN KEY (comment_comment_id) REFERENCES zigg.comment (comment_id) ON DELETE NO ACTION;

CREATE INDEX FKj01hm5s17rd8q8jc98ens5m90 ON zigg.comment_replies (comment_comment_id);

ALTER TABLE zigg.comment
    DROP COLUMN parent_comment_id;

CREATE INDEX FK3orwkcnrtfi169j7oyxo856nx ON zigg.post (video_thumbnail_image_id);

CREATE INDEX FKbq8cspws6cmwfjjj378uy3fdj ON zigg.post (video_content_video_id);