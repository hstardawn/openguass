-- 01_create_database.sql
-- 数据库名：HuangxxMIS11
-- 在 openGauss / gsql 中执行：
--   gsql -d postgres -f sql/01_create_database.sql

DROP DATABASE IF EXISTS huangxx_mis11;
DROP DATABASE IF EXISTS HuangxxMIS11;
CREATE DATABASE huangxx_mis11
    WITH ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.utf8'
    LC_CTYPE = 'en_US.utf8';

-- 后续脚本请连接到 huangxx_mis11 后按顺序执行。
-- 课程报告中仍按个性化要求说明数据库名为 HuangxxMIS11；
-- openGauss 未加引号的数据库名会转小写，项目运行时使用稳定的小写连接名 huangxx_mis11。
