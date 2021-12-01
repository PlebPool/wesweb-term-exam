// This is just for my info.
DROP DATABASE IF EXISTS {db_name};
DROP USER IF EXISTS '{user}'@'localhost';
CREATE DATABASE {db_name};
CREATE USER '{user}'@'localhost' IDENTIFIED BY '{pass}';
GRANT ALL ON {db_name}.* TO '{user}'@'localhost';
CREATE TABLE {db_name}.post (post_id SERIAL PRIMARY KEY, alias VARCHAR(255), title VARCHAR(255), content LONGTEXT, likes BIGINT, date_of_post DATE, time_of_post TIME);
CREATE TABLE {db_name}.who_liked_what (PRIMARY KEY `uniqueness` (`ip`, `post_id`), ip INT UNSIGNED, post_id BIGINT UNSIGNED, liked BOOLEAN);
CREATE TABLE {db_name}.path_to_post(post_id SERIAL PRIMARY KEY, the_path TINYTEXT);

INSERT INTO who_liked_what VALUES(inet_aton("127.0.0.2"), 2, true) ON DUPLICATE KEY UPDATE liked = NOT`liked`;
UPDATE post SET likes = if((post_id, (SELECT liked FROM who_liked_what WHERE (ip, post_id) = (inet_aton("127.0.0.2"), 2))) = (2, true), likes+1, likes-1) WHERE post_id = 2;
SELECT * FROM who_liked_what, post WHERE who_liked_what.post_id = post.post_id;

DELETE FROM post;

SELECT * FROM post;