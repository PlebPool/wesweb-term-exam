// This is just for my info.
DROP DATABASE IF EXISTS {db_name};
DROP USER IF EXISTS '{user}'@'localhost';
CREATE DATABASE {db_name};
CREATE USER '{user}'@'localhost' IDENTIFIED BY '{pass}';
GRANT ALL ON {db_name}.* TO '{user}'@'localhost';
CREATE TABLE {db_name}.post (id SERIAL PRIMARY KEY, alias VARCHAR(255), title VARCHAR(255), content LONGTEXT, likes BIGINT, date_of_post DATE, time_of_post TIME);

DELETE FROM post;

SELECT * FROM post;