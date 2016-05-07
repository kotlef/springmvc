-- ----------------------------------------------------------------
-- 用户, 用户登录, 用户登录日志: User, UserLogin, UserLogin_L
-- 建立: 邱克 20150416
-- 用户ID为自增变量, 采用8位编号, 从10000001开始
-- 员工和合作伙伴也可以是客户, 但当客户成为合作伙伴或员工时，则必须是员工或合作伙伴
-- 用户重要的日志变动放在Note字段里
-- ----------------------------------------------------------------
-- 用户表: User
DROP TABLE IF EXISTS User;
CREATE TABLE User
(
-- 关键信息
UID					INT AUTO_INCREMENT				COMMENT '用户内码(自增序号)',
User_Name			VARCHAR(30) NOT NULL			COMMENT '用户名称',
User_Type			CHAR(1) NOT NULL				COMMENT '用户类型(*, 0=员工, C=客户, P=合作伙伴, T=临时用户)',
ID_Type				CHAR(1) DEFAULT '0' NOT NULL	COMMENT '证件类型(*, 0=身份证, 1=护照, 2=军官证)',
ID_NO				VARCHAR(30) NOT NULL			COMMENT '证件号码(登录识别)',
CELL				VARCHAR(30) NOT NULL			COMMENT '联系手机(登录识别,仅能有一个号码)',
-- 其他信息
EMAIL				VARCHAR(100)					COMMENT '邮箱(登录识别)',
CELL2				VARCHAR(100) NOT NULL			COMMENT '备用手机(可录入多个)',
TEL					VARCHAR(100)					COMMENT '联系电话(可录入多个)',
FAX					VARCHAR(30)						COMMENT '传真',
QQ					VARCHAR(30)						COMMENT 'QQ号',
WeChat				VARCHAR(30)						COMMENT '微信号',
Nationality			INT								COMMENT '民族(*)',
Religion			INT								COMMENT '宗教(*)',
PolityFace			INT								COMMENT '政治面貌(*)',
Birthdate			DATE							COMMENT '生日',
BirthPlace			VARCHAR(30)						COMMENT '出生地/籍贯',
Marriage			CHAR(1) DEFAULT '0' NOT NULL	COMMENT '婚姻情况0=未知(*, 1=已婚, 2=未婚, 3=离异, 4=丧偶)',
Blood				CHAR(1) DEFAULT '0' NOT NULL	COMMENT '血型(*, 0=未知, 1=A型, 2=B型, 3=O型, 4=AB型)',
Gender				CHAR(1) DEFAULT '0' NOT NULL	COMMENT '性别(*, 0=未知, M=男性, F=女性, 外部员工可能未知)',
Height				DECIMAL(12,3)					COMMENT '身高(厘米)',
Weight				DECIMAL(12,3)					COMMENT '体重(公斤)',
Education			CHAR(1) DEFAULT '0' NOT NULL	COMMENT '学历(*, 0=未知, 1=专科及以下, 2=专科, 3=本科, 4=硕士, 5=博士)',
GradSchool			VARCHAR(100)					COMMENT '毕业学校',
Major				VARCHAR(100)					COMMENT '专业',
Graduation_Date		DATE							COMMENT '毕业时间',
Occupation			VARCHAR(30)						COMMENT '职业',
JobTitle			VARCHAR(100)					COMMENT '职称',
RegionID			INT								COMMENT '区域(与省, 市, 县都取自Region表)',
ProvinceID			INT								COMMENT '省(直辖市。)',
CityID				INT								COMMENT '市',
DistrictID			INT								COMMENT '区(县)',
TOWN				VARCHAR(100)					COMMENT '街道(镇, 手工填写)',
ADDR1				VARCHAR(100)					COMMENT '户籍地址',
ADDR2				VARCHAR(100)					COMMENT '当前居住地址',
ADDR3				VARCHAR(100)					COMMENT '通讯地址',
ZIP					VARCHAR(30)						COMMENT '通讯地址邮政编码',
URG_Linkman			VARCHAR(30)						COMMENT '紧急联系人',
URG_TEL				VARCHAR(30)						COMMENT '紧急联系人电话',
Sec_Question1		VARCHAR(100)					COMMENT '安全问题1',
Sec_Question2		VARCHAR(100)					COMMENT '安全问题2',
Sec_Question3		VARCHAR(100)					COMMENT '安全问题3',
Sec_Answer1			VARCHAR(100)					COMMENT '加密安全答案1',
Sec_Answer2			VARCHAR(100)					COMMENT '加密安全答案2',
Sec_Answer3			VARCHAR(100)					COMMENT '加密安全答案3',
Sec_Password		VARCHAR(30)						COMMENT '安全密码, 成为正式用户后必须填加',
Bind_Password		VARCHAR(30)						COMMENT '防挟持密码, 成为正式用户后必须填加',
-- 其他信息
Status				CHAR(1) DEFAULT '0' not null	COMMENT '状态(*, 0=正常, S=失效)',
Note				TEXT		 					COMMENT '备注',
Create_Time			DATETIME DEFAULT NOW()			COMMENT '记录建立时间',
Modify_Time			DATETIME DEFAULT NOW() ON UPDATE NOW()		COMMENT '最后修改时间',
Close_Date			DATE							COMMENT '停用日期',
CONSTRAINT PK_User PRIMARY KEY(UID)
);
ALTER TABLE User COMMENT= '用户表';
ALTER TABLE User AUTO_INCREMENT=10000000;
CREATE UNIQUE INDEX IDX_User_ID_NO ON User(ID_NO);
CREATE UNIQUE INDEX IDX_User_CELL ON User(CELL);

-- 用户登录表: UserLogin, 同一个UID的加密密码是相同的
DROP TABLE IF EXISTS UserLogin;
CREATE TABLE UserLogin
(
LoginString			VARCHAR(100) NOT NULL			COMMENT '登录识别串, 由User表填加, 包括身份证, 电话, 邮箱等',
UID					INT								COMMENT '用户内码',
Password			VARCHAR(100) NOT NULL			COMMENT '加密的登录密码',
Salt				VARCHAR(100) NOT NULL			COMMENT '加密盐',
Status				CHAR(1) DEFAULT '0' NOT NULL	COMMENT '状态(冗余, *, 0=正常, S=失效)',
CONSTRAINT PK_UserLogin PRIMARY KEY(LoginString)
);
ALTER TABLE UserLogin COMMENT= '用户登录识别表';

-- 用户登录日志表: UserLogin_L
DROP TABLE IF EXISTS UserLogin_L;
CREATE TABLE UserLogin_L
(
LogID				INT AUTO_INCREMENT				COMMENT '日志内码(自增序号)',
UID					INT NOT NULL					COMMENT '用户内码',
LoginString			VARCHAR(100)					COMMENT '登录识别串, 登录行为时要记录',
LoginLog_Type		CHAR(1)							COMMENT '登录日志类型（L=登录, Q=退出, P=重要行为)',
Log_DESC			TEXT							COMMENT '日志描述',
Create_Time			DATETIME DEFAULT NOW()			COMMENT '记录建立时间',
CONSTRAINT PK_UserLogin_L PRIMARY KEY(LogID)
);
ALTER TABLE UserLogin_L COMMENT= '用户登录日志表';

-- ----------------------------------------------------------------
-- 机构表, 组织表: ORGGroup, ORG
-- 建立: 邱克 20150415
-- 机构类型: H=公司总部, R=区域管理中心, C=小区服务点, P=外部组织
-- 组织类型: M=管理部门, O=市场业务及运营部门, T=技术部门, S=服务部门, P=外部组织
-- 机构和组织记录只能停用不能删除
-- ORGGroupID从1000开始手工编码, 1001是公司总部
-- ORGID从100001号开始编码, 自动增长, 100001是公司最高管理部门
-- ----------------------------------------------------------------
-- 机构表: ORGGroup
DROP TABLE IF EXISTS ORGGroup;
CREATE TABLE ORGGroup
(
ORGGroupID			INT NOT NULL					COMMENT '机构内码(人工编码)',
ORGGroup_Name		VARCHAR(100) NOT NULL			COMMENT '机构名称',
ORGGroup_Type		CHAR(1) NOT NULL				COMMENT '机构类型(*, 见表头定义)',
ParentID			INT NOT NULL					COMMENT '父记录ID',
Tree_Level			INT NOT NULL					COMMENT '机构级别(0=缺省的最高, 记录从第1级开始, 1=1级)',
State				VARCHAR(30) NOT NULL			COMMENT '是否有子节点的标志(*, open=无子节点, closed=有子节点)',
Open_Date			DATE							COMMENT '启用日期(临时组织必填)',
Close_Date			DATE							COMMENT '停用日期(临时组织必填)',
Status				CHAR(1) DEFAULT '0' NOT NULL	COMMENT '状态(*, 0=正常, S=失效)',
Note				TEXT							COMMENT '备注',
Create_Time			DATETIME DEFAULT NOW()			COMMENT '记录建立时间',
Modify_Time			DATETIME DEFAULT NOW() ON UPDATE NOW()		COMMENT '最后修改时间',
CONSTRAINT PK_ORGGroup PRIMARY KEY(ORGGroupID)
);
ALTER TABLE ORGGroup COMMENT= '机构表';

-- 组织表: ORG
DROP TABLE IF EXISTS ORG;
CREATE TABLE ORG
(
ORGID				INT AUTO_INCREMENT				COMMENT '组织内码(自增序列)',
ORG_Code			VARCHAR(30) NOT NULL			COMMENT '组织编码(人工编写)',
ORG_Name			VARCHAR(100) NOT NULL			COMMENT '组织名称',
ORG_Type			CHAR(1) NOT NULL				COMMENT '组织类型(*, 见表头定义)',
ParentID			INT NOT NULL					COMMENT '父记录ID',
Tree_Level			INT NOT NULL					COMMENT '组织级别(0=缺省的最高, 记录从第1级开始, 1=1级)',
State				VARCHAR(30) NOT NULL			COMMENT '是否有子节点的标志(*, open=无子节点, closed=有子节点)',
Station_Flag		CHAR(1) DEFAULT 'N' NOT NULL	COMMENT '是否服务站(*, Y=服务站, N=非服务站)',
ORGGroupID			INT NOT NULL					COMMENT '所属机构内码',
Open_Date			DATE							COMMENT '启用日期(临时组织必填)',
Close_Date			DATE							COMMENT '停用日期(临时组织必填)',
Status				CHAR(1) DEFAULT '0' NOT NULL	COMMENT '状态(*, 0=正常, S=失效)',
Note				TEXT							COMMENT '备注',
Create_Time			DATETIME DEFAULT NOW()			COMMENT '记录建立时间',
Modify_Time			DATETIME DEFAULT NOW() ON UPDATE NOW()		COMMENT '最后修改时间',
CONSTRAINT PK_ORG PRIMARY KEY(ORGID)
);
ALTER TABLE ORG COMMENT= '组织表';
ALTER TABLE ORG AUTO_INCREMENT=100000;

-- ----------------------------------------------------------------
-- 服务中心, 服务中心关系: Station, Station_R
-- 建立: 邱克 20150423
-- 每个服务中心都可和多个第三方服务中心建立关系
-- ----------------------------------------------------------------
DROP TABLE IF EXISTS Station;
CREATE TABLE Station
(
ORGID				INT NOT NULL					COMMENT '组织内码',
Station_No			VARCHAR(30) NOT NULL			COMMENT '服务站编号',
Station_Name		VARCHAR(100) NOT NULL			COMMENT '服务站名称',
Station_Type		CHAR(1) DEFAULT '0' NOT NULL	COMMENT '服务站类型(*, 0=内部服务站, 1=110警察局, 2=120救援, 9=119火警, R=零售商家, L=物流商家, F=餐饮商家)',
Station_DESC		TEXT							COMMENT '服务站描述',
Station_Add			VARCHAR(100) NOT NULL			COMMENT '服务站地址',
Station_Tel			VARCHAR(30) NOT NULL			COMMENT '服务站电话',
Station_Leader		VARCHAR(30) NOT NULL			COMMENT '服务站负责人',
Leader_Cell			VARCHAR(30) NOT NULL			COMMENT '服务站负责人手机',
Station_Level		INT	DEFAULT 0 NOT NULL			COMMENT '服务站级别',
Station_Area		INT DEFAULT 0 NOT NULL			COMMENT '服务站面积',
Station_P_Count		INT DEFAULT 0 NOT NULL			COMMENT '服务站人员数量',
Station_S_Count		INT DEFAULT 0 NOT NULL			COMMENT '服务站座席数量',
Longitude			DECIMAL(8,5)					COMMENT '经度',
Latitude			DECIMAL(8,5)					COMMENT '纬度',
Status				CHAR(1) DEFAULT '0' NOT NULL	COMMENT '状态(*, 0=正常, S=失效)',
Note				TEXT							COMMENT '备注',
Create_Time			DATETIME DEFAULT NOW()			COMMENT '记录建立时间',
Modify_Time			DATETIME DEFAULT NOW() ON UPDATE NOW()		COMMENT '最后修改时间',
CONSTRAINT PK_Station PRIMARY KEY(ORGID)
);
ALTER TABLE Station COMMENT= '服务中心表';

DROP TABLE IF EXISTS Station_R;
CREATE TABLE Station_R
(
ORGID				INT NOT NULL					COMMENT '组织内码',
R_ORGID				INT NOT NULL					COMMENT '关联组织内码',
Station_Type		CHAR(1) DEFAULT '0' NOT NULL	COMMENT '服务站类型(*, 1=110警察局, 2=120救援, 9=119火警, R=零售商家, L=物流商家, F=餐饮商家)',
Create_Time			DATETIME DEFAULT NOW()			COMMENT '记录建立时间',
CONSTRAINT PK_Station_R PRIMARY KEY(ORGID, R_ORGID)
);
ALTER TABLE Station_R COMMENT= '服务中心关系表';

-- ----------------------------------------------------------------
-- 角色, 组织角色: Role, ORGRole
-- 建立: 邱克 20150416
-- 组织角色表表示该组织设计的角色, 在给员工填加岗位时, 只能选择某部门的某角色
-- ----------------------------------------------------------------
DROP TABLE IF EXISTS Role;
CREATE TABLE Role
(
RoleID				INT AUTO_INCREMENT				COMMENT '角色编码',
Role_Code			VARCHAR(30) NOT NULL			COMMENT '角色编码(人工编写)',
Role_Name			VARCHAR(100) NOT NULL			COMMENT '角色名称',
Role_Type			CHAR(1) NOT NULL				COMMENT '角色类型(*, M=经理, E=员工, P=外部员工)',
HomepageID			INT NOT NULL					COMMENT '主页ID',
Status				CHAR(1) DEFAULT '0' NOT NULL	COMMENT '状态(*, 0=正常, S=失效)',
Note				TEXT							COMMENT '备注',
Create_Time			DATETIME DEFAULT NOW()			COMMENT '记录建立时间',
Modify_Time			DATETIME DEFAULT NOW() ON UPDATE NOW()		COMMENT '最后修改时间',
CONSTRAINT PK_Role PRIMARY KEY(RoleID)
);
ALTER TABLE Role COMMENT= '角色表';
CREATE UNIQUE INDEX IDX_Role ON Role(Role_Code);

DROP TABLE IF EXISTS ORGRole;
CREATE TABLE ORGRole
(
ORGID				INT NOT NULL					COMMENT '组织内码, 所属部门',
RoleID				INT NOT NULL					COMMENT '角色编码, 部门职务',
Status				CHAR(1) DEFAULT '0' NOT NULL	COMMENT '状态: 0=正常, S=失效',
Note				TEXT							COMMENT '备注',
Create_Time			DATETIME DEFAULT NOW()			COMMENT '记录建立时间',
Modify_Time			DATETIME DEFAULT NOW() ON UPDATE NOW()		COMMENT '最后修改时间',
CONSTRAINT PK_Post PRIMARY KEY(ORGID,RoleID)
);
ALTER TABLE ORGRole COMMENT= '组织角色表';

-- ----------------------------------------------------------------
-- 菜单,角色菜单,权限,角色权限,资源,组织资源:Menu,Role_Menu,Permission,Role_Permission, Res,ORG_Res
-- 建立: 原勇  20150504
-- 菜单和角色是多对多关系,模块是角色拥有的菜单
-- 访问权限是访问授权表，权限表将在访问过程中进行权限检测，防止恶意用户拼接地址，保证访问安全
-- 配置规则，用户配置员工组织、角色，然后角色配置菜单，角色配置访问权限
-- Permission表Permission_Url，Permission_value采用人工对应匹配规则，鉴于采用shiro第三方结构，使用其推荐规则模型
-- Permission_Url: 人工填写(规则是系统中支持的所有访问路径）
-- Permission_value: perms:[user:edit:123],roles[role1,role2,...]
-- perms:[user:add:123]： perms:[资源:操作:实例]
-- Permission_value字段值得组装规则是通过界面上的资源
-- (取自wordbook)、操作(取自wordbook)，实例手动填写，角色遍历角色表，然后按照访问规则进行save组装)
-- 资源表：专门针对数据权限设计，主要作用是数据资源能够快速的查询、统计
-- 组织资源表: 用户根据岗位表查询到组织，然后快速过滤筛选其访问的数据资源
-- ----------------------------------------------------------------
-- 修改日期 20150509 修改人 原勇  
-- 新增 角色权限表
-- ----------------------------------------------------------------
DROP TABLE IF EXISTS Menu;
CREATE TABLE Menu
(
MenuID              INT AUTO_INCREMENT              COMMENT '菜单内码',
Menu_name           VARCHAR(30) NOT NULL            COMMENT '菜单名称',
Menu_url            TEXT                            COMMENT '菜单导航地址',
Menu_level			CHAR(1) DEFAULT '0' NOT NULL	COMMENT '菜单级别根节点为0，',
ParentID            INT NOT NULL	                COMMENT '父菜单ID, 直接上一级的MenuID',
State			    VARCHAR(30)  NOT NULL  	        COMMENT '是否最低级别的标志(*, closed=有子节点, open=没有子节点)',
Note                TEXT                            COMMENT '备注',
Create_Time			DATETIME DEFAULT NOW()			COMMENT '记录建立时间',
CONSTRAINT PK_Menu PRIMARY KEY(MenuID)
);
ALTER TABLE Menu COMMENT= '菜单表';

DROP TABLE IF EXISTS Role_Menu;
CREATE TABLE Role_Menu
(
RoleID				INT NOT NULL					COMMENT '角色编码(取自 Role表）',
MenuID              INT NOT NULL                    COMMENT '菜单内码(取自 Role_Menu表）',
Note				TEXT							COMMENT '备注',
Create_Time			DATETIME DEFAULT NOW()			COMMENT '记录建立时间',
CONSTRAINT PK_Role_Menu PRIMARY KEY(RoleID,MenuID)
);
ALTER TABLE Role_Menu COMMENT= '角色菜单表';

DROP TABLE IF EXISTS Permission;
CREATE TABLE Permission
(
PermissionID		INT AUTO_INCREMENT				COMMENT '权限内码',
Permission_Url      TEXT							COMMENT '访问地址(*,取自wordbook，按照访问对象地址类型)',
Permission_value    VARCHAR(100) NOT NULL			COMMENT '访问地址操作权限(*,资源、操作取自wordbook，实例手动填写，按照访问规则进行save组装)',
Note				TEXT							COMMENT '备注',
Create_Time			DATETIME DEFAULT NOW()			COMMENT '记录建立时间',
CONSTRAINT PK_Permission PRIMARY KEY(PermissionID)
);
ALTER TABLE Permission COMMENT= '权限表';


DROP TABLE IF EXISTS Role_Permission;
CREATE TABLE Role_Permission
(
RoleID         		INT NOT NULL					COMMENT '角色内码(取自 Role表)',
PermissionID        INT NOT NULL				    COMMENT '权限内码(取自 Permission表)',
Note				TEXT							COMMENT '备注',
Create_Time			DATETIME DEFAULT NOW()			COMMENT '记录建立时间',
CONSTRAINT PK_Role_Permission PRIMARY KEY(RoleID,PermissionID)
);
ALTER TABLE Role_Permission COMMENT= '角色权限表';


DROP TABLE IF EXISTS Res;
CREATE TABLE Res
(
ResID               INT AUTO_INCREMENT              COMMENT '资源内码(自增序号)',
Res_Type            CHAR(1) NOT NULL                COMMENT '资源类型(*,S=服务站资源)',
ItemID              INT NOT NULL                    COMMENT '资源ID(取自 资源类型对应实体的ID，例如Sation的ORGID',
Note                TEXT                            COMMENT '备注',
Create_Time			DATETIME DEFAULT NOW()			COMMENT '记录建立时间',
CONSTRAINT PK_Res PRIMARY KEY(ResID)
);
ALTER TABLE Res COMMENT= '资源表';

DROP TABLE IF EXISTS ORG_Res;
CREATE TABLE ORG_Res
(
ORGID               INT NOT NULL                    COMMENT '组织内码(取自 ORGID表)',
ResID		        INT NOT NULL					COMMENT '资源内码(取自 Res表)',
Note                TEXT                            COMMENT '备注',
Create_Time			DATETIME DEFAULT NOW()			COMMENT '记录建立时间',
CONSTRAINT PK_ORG_Res PRIMARY KEY(ORGID,ResID)
);
ALTER TABLE ORG_Res COMMENT= '组织资源表';

-- ----------------------------------------------------------------
-- 员工, 岗位: EMP, Post
-- 建立: 邱克 20150420
-- 一个员工可以在多个部门兼任多个职务, 这样一个员工可能在POST里有多条记录
-- ----------------------------------------------------------------
DROP TABLE IF EXISTS EMP;
CREATE TABLE EMP
(
UID					INT NOT NULL					COMMENT '用户内码(取自 USER表)',
EMP_Code			VARCHAR(30) NOT NULL			COMMENT '员工编码(标准工号)',
EMP_Password        VARCHAR(30) DEFAULT '0' NOT NULL	COMMENT '工号密码',
EMP_Type			CHAR(1) DEFAULT '0' NOT NULL	COMMENT '员工类型(*, 0=员工, P=合作伙伴)',
Entry_DATE			DATE							COMMENT '入职时间',
Depart_DATE			DATE							COMMENT '离职时间',
POST_Rank			CHAR(1) DEFAULT '0' NOT NULL	COMMENT '岗级(*, 0=未知, A=一般员工, B=一般管理, C=高层管理)',
Resume				TEXT							COMMENT '个人简历',
ParentID			INT NOT NULL					COMMENT '父记录ID, 直接上级的UID',
Status				CHAR(1) DEFAULT '0' NOT NULL	COMMENT '状态(*, 0=正常, N=未转正, S=失效)',
Note				TEXT							COMMENT '备注',
Create_Time			DATETIME DEFAULT NOW()			COMMENT '记录建立时间',
Modify_Time			DATETIME DEFAULT NOW() ON UPDATE NOW()		COMMENT '最后修改时间',
CONSTRAINT PK_EMP PRIMARY KEY(UID)
);
ALTER TABLE EMP COMMENT= '员工表';

DROP TABLE IF EXISTS POST;
CREATE TABLE POST
(
UID					INT NOT NULL					COMMENT '用户内码(取自 USER表)',
ORGID				INT NOT NULL					COMMENT '组织内码, 所属部门',
RoleID				INT NOT NULL					COMMENT '角色编码, 部门职务',
Status				CHAR(1) DEFAULT '0' NOT NULL	COMMENT '状态(*, 0=正常, S=失效)',
Note				TEXT							COMMENT '备注',
Create_Time			DATETIME DEFAULT NOW()			COMMENT '记录建立时间',
Modify_Time			DATETIME DEFAULT NOW() ON UPDATE NOW()		COMMENT '最后修改时间',
CONSTRAINT PK_Post PRIMARY KEY(UID,ORGID,RoleID)
);
ALTER TABLE Post COMMENT= '岗位表';