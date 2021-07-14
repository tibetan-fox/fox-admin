
-- create database
CREATE DATABASE foxdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


-- create database user
CREATE USER 'foxuser'@'%' IDENTIFIED BY 'Foxpwd123' PASSWORD EXPIRE NEVER;

-- 更改加密方式为mysql_native_password，并刷新配置，否则会出现1251错误
ALTER USER 'foxuser'@'%' IDENTIFIED WITH mysql_native_password BY 'Foxpwd123';

-- 只授权DML
-- 需要用root或其它有权限的用户来建表
grant select, insert, update, delete on foxdb.* to 'foxuser'@'%';

FLUSH PRIVILEGES;

-- local database url
-- jdbc:mysql://localhost:3306/foxdb?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
