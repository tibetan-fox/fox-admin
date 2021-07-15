-- clean menus
-- 删除官网链接
delete from sys_menu where menu_name = '若依官网';

-- 删除定时任务（quartz），后续考虑用xxl-job实现定时任务
delete from sys_menu where menu_name = '定时任务';

-- 删除数据库监控（druid monitor)
delete from sys_menu where menu_name = '数据监控';

-- 删除服务监控，后续由专业监控软件监控
delete from sys_menu where menu_name = '服务监控';

-- 删除缓存监控，后续由专业监控软件监控
delete from sys_menu where menu_name = '缓存监控';

-- 删除代码生成，用IDE plugin来生成代码
delete from sys_menu where menu_name = '代码生成';

-- 删除表单构建，暂时用不到这个功能，微服务才是重点
delete from sys_menu where menu_name = '表单构建';