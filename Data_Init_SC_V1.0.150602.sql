-- ------------------------------------------------------------
-- 版本 V1.0.150602
-- 1、初始化【机构表,组织表,角色表,资源表: ORGGroup,Org,Role,Res】
-- 2、初始化【主页表,菜单表,角色菜单表: Homepage,Menu, Role_Menu】
-- 3、初始化【权限表,角色权限表: Permission, Role_Permission】
-- 4、初始化【用户表,用户登录表,岗位表: User,Userlogin,Post】
-- ------------------------------------------------------------

-- ------------------------------------------------------------
-- 1、初始化【机构表,组织表,角色表,资源表: ORGGroup,Org,Role,Res】
-- ------------------------------------------------------------
DELETE FROM `D1`.`orggroup` WHERE `ORGGroupID`='1001';
INSERT INTO `D1`.`orggroup` (`ORGGroupID`, `ORGGroup_Name`, `ORGGroup_Type`, `ParentID`, `Tree_Level`, `State`, `Open_Date`, `Status`,`Note`) VALUES ('1001', '某公司', 'H', '0', '0', 'open', '2015-6-2', '0','用于系统测试');

DELETE FROM `D1`.`org` WHERE `ORGID`='1';
DELETE FROM `D1`.`org` WHERE `ORGID`='2';
INSERT INTO `D1`.`org` (`ORGID`,`ORG_Code`, `ORG_Name`, `ORG_Type`, `ParentID`, `Tree_Level`, `State`, `Station_Flag`, `ORGGroupID`, `Open_Date`, `Status`, `Note`) VALUES ('1','YWZX', '业务中心', 'O', '0', '0', 'closed', 'N', '1001', '2015-06-02', '0', '用于系统测试');
INSERT INTO `D1`.`org` (`ORGID`,`ORG_Code`, `ORG_Name`, `ORG_Type`, `ParentID`, `Tree_Level`, `State`, `Station_Flag`, `ORGGroupID`, `Open_Date`, `Status`, `Note`) VALUES ('2','YW1B', '业务一部', 'O', '1', '0', 'open', 'N', '1001', '2015-06-02', '0', '用于系统测试');

DELETE FROM `D1`.`role` WHERE `RoleID`='1';
DELETE FROM `D1`.`role` WHERE `RoleID`='2';
DELETE FROM `D1`.`role` WHERE `RoleID`='3';
INSERT INTO `D1`.`role` (`RoleID`, `Role_Code`, `Role_Name`, `Role_Type`,  `HomepageID`,  `Status`, `Note`) VALUES ('1','YWDU', '业务调度', 'E',  '3',  '0', '用于系统测试');
INSERT INTO `D1`.`role` (`RoleID`,`Role_Code`, `Role_Name`, `Role_Type`,   `HomepageID`, `Status`, `Note`) VALUES ('2','YWBL', '业务办理', 'E',  '1',  '0', '用于系统测试');
INSERT INTO `D1`.`role` (`RoleID`,`Role_Code`, `Role_Name`, `Role_Type`,  `HomepageID`, `Status`, `Note`) VALUES ('3','YWJJ', '业务接警', 'E',  '2',  '0', '用于系统测试');




-- ------------------------------------------------------------
-- 2、初始化【主页表,菜单表,角色菜单表: Homepage,Menu, Role_Menu】
-- ------------------------------------------------------------
DELETE FROM `D1`.`homepage` WHERE `HomepageID`='1';
DELETE FROM `D1`.`homepage` WHERE `HomepageID`='2';
DELETE FROM `D1`.`homepage` WHERE `HomepageID`='3';
INSERT INTO `D1`.`homepage` (`HomepageID`,`Homepage_Name`, `Homepage_Url`, `Note`) VALUES ('1','主页1', 'modules/index1.html', '用于系统测试');
INSERT INTO `D1`.`homepage` (`HomepageID`,`Homepage_Name`, `Homepage_Url`, `Note`) VALUES ('2','主页2', 'modules/index2.html', '用于系统测试');
INSERT INTO `D1`.`homepage` (`HomepageID`,`Homepage_Name`, `Homepage_Url`, `Note`) VALUES ('3','主页3', 'modules/index3.html', '用于系统测试');


-- ------------------------------------------------------------
-- 3、初始化【权限表,角色权限表: Permission, Role_Permission】
-- ------------------------------------------------------------



-- ------------------------------------------------------------
-- 4、初始化【用户表,用户登录表,岗位表: User,Userlogin,Post】初始化密码123
-- ------------------------------------------------------------
DELETE FROM `D1`.`user` WHERE `UID`='10000001';
DELETE FROM `D1`.`user` WHERE `UID`='10000002';
DELETE FROM `D1`.`user` WHERE `UID`='10000003';
DELETE FROM `D1`.`user` WHERE `UID`='10000004';
INSERT INTO `D1`.`user` (`UID`, `User_Name`, `User_Type`, `ID_Type`, `ID_NO`, `CELL`, `EMAIL`, `CELL2`) VALUES ('10000001', '测试用户', '0', '0', '1', '18812345678', '18812345678@qq.com', '18812345678');
INSERT INTO `D1`.`user` (`UID`, `User_Name`, `User_Type`, `ID_Type`, `ID_NO`, `CELL`, `EMAIL`, `CELL2`) VALUES ('10000002', '测试用户', '0', '0', '2', '18800001234', '18800001234@qq.com', '18800001234');
INSERT INTO `D1`.`user` (`UID`, `User_Name`, `User_Type`, `ID_Type`, `ID_NO`, `CELL`, `EMAIL`, `CELL2`) VALUES ('10000003', '测试用户', '0', '0', '3', '18800001234', '18800008888@qq.com', '18800008888');
INSERT INTO `D1`.`user` (`UID`, `User_Name`, `User_Type`, `ID_Type`, `ID_NO`, `CELL`, `EMAIL`, `CELL2`) VALUES ('10000004', '测试用户', '0', '0', '4', '18800001234', '18800006666@qq.com', '18800006666');

DELETE FROM `D1`.`userlogin` WHERE `LoginString`='18812345678';
DELETE FROM `D1`.`userlogin` WHERE `LoginString`='18800001234';
DELETE FROM `D1`.`userlogin` WHERE `LoginString`='18800008888';
DELETE FROM `D1`.`userlogin` WHERE `LoginString`='18800006666';
INSERT INTO `D1`.`userlogin` (`LoginString`, `UID`, `Password`, `Salt`, `Status`) VALUES ('18812345678', '10000001', '6394754e641abd41088487eb21d95594', '9ab6caa34e07097524760e04d758e63c', '0');
INSERT INTO `D1`.`userlogin` (`LoginString`, `UID`, `Password`, `Salt`, `Status`) VALUES ('18800001234', '10000002', 'fdae5b0c8e6cdc2d9b8a582fd3d5736c', '9ab6caa34e07097524760e04d758e63c', '0');
INSERT INTO `D1`.`userlogin` (`LoginString`, `UID`, `Password`, `Salt`, `Status`) VALUES ('18800008888', '10000002', '97f8a4501dfc5e776e12bf9a1db16e67', '9ab6caa34e07097524760e04d758e63c', '0');
INSERT INTO `D1`.`userlogin` (`LoginString`, `UID`, `Password`, `Salt`, `Status`) VALUES ('18800006666', '10000002', 'fdae5b0c8e6cdc2d9b8a582fd3d5736c', 'c7684d212296adbbea380835060e0ff2', '0');


DELETE FROM `D1`.`post` WHERE `UID`='10000001' and`ORGID`='2' and`RoleID`='1';
DELETE FROM `D1`.`post` WHERE `UID`='10000001' and`ORGID`='2' and`RoleID`='2';
DELETE FROM `D1`.`post` WHERE `UID`='10000001' and`ORGID`='2' and`RoleID`='3';

DELETE FROM `D1`.`post` WHERE `UID`='10000002' and`ORGID`='2' and`RoleID`='1';
DELETE FROM `D1`.`post` WHERE `UID`='10000002' and`ORGID`='2' and`RoleID`='2';
DELETE FROM `D1`.`post` WHERE `UID`='10000002' and`ORGID`='2' and`RoleID`='3';

DELETE FROM `D1`.`post` WHERE `UID`='10000003' and`ORGID`='2' and`RoleID`='1';
DELETE FROM `D1`.`post` WHERE `UID`='10000003' and`ORGID`='2' and`RoleID`='2';
DELETE FROM `D1`.`post` WHERE `UID`='10000003' and`ORGID`='2' and`RoleID`='3';

DELETE FROM `D1`.`post` WHERE `UID`='10000004' and`ORGID`='2' and`RoleID`='1';
DELETE FROM `D1`.`post` WHERE `UID`='10000004' and`ORGID`='2' and`RoleID`='2';
DELETE FROM `D1`.`post` WHERE `UID`='10000004' and`ORGID`='2' and`RoleID`='3';

INSERT INTO `D1`.`post` (`UID`, `ORGID`, `RoleID`, `Status`, `Note`) VALUES ('10000001', '2', '1', '0', '用于系统测试');
INSERT INTO `D1`.`post` (`UID`, `ORGID`, `RoleID`, `Status`, `Note`) VALUES ('10000001', '2', '2', '0', '用于系统测试');
INSERT INTO `D1`.`post` (`UID`, `ORGID`, `RoleID`, `Status`, `Note`) VALUES ('10000001', '2', '3', '0', '用于系统测试');

INSERT INTO `D1`.`post` (`UID`, `ORGID`, `RoleID`, `Status`, `Note`) VALUES ('10000002', '2', '1', '0', '用于系统测试');
INSERT INTO `D1`.`post` (`UID`, `ORGID`, `RoleID`, `Status`, `Note`) VALUES ('10000002', '2', '2', '0', '用于系统测试');
INSERT INTO `D1`.`post` (`UID`, `ORGID`, `RoleID`, `Status`, `Note`) VALUES ('10000002', '2', '3', '0', '用于系统测试');


INSERT INTO `D1`.`post` (`UID`, `ORGID`, `RoleID`, `Status`, `Note`) VALUES ('10000003', '2', '1', '0', '用于系统测试');
INSERT INTO `D1`.`post` (`UID`, `ORGID`, `RoleID`, `Status`, `Note`) VALUES ('10000003', '2', '2', '0', '用于系统测试');
INSERT INTO `D1`.`post` (`UID`, `ORGID`, `RoleID`, `Status`, `Note`) VALUES ('10000003', '2', '3', '0', '用于系统测试');


INSERT INTO `D1`.`post` (`UID`, `ORGID`, `RoleID`, `Status`, `Note`) VALUES ('10000004', '2', '1', '0', '用于系统测试');
INSERT INTO `D1`.`post` (`UID`, `ORGID`, `RoleID`, `Status`, `Note`) VALUES ('10000004', '2', '2', '0', '用于系统测试');
INSERT INTO `D1`.`post` (`UID`, `ORGID`, `RoleID`, `Status`, `Note`) VALUES ('10000004', '2', '3', '0', '用于系统测试');


