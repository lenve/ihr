-- ============================================================
-- iHR 业务表初始化脚本（02_business.sql）
-- 在 01_init.sql 之后执行；覆盖 36 个菜单页面的全部业务表。
-- 设计参考：eHR 通用模块划分 + 绩效闭环（目标→自评→上级评价→面谈→签署→申诉）
-- ============================================================

-- ---------------- 1. 员工档案（个人信息/人事报表数据源） ----------------
CREATE TABLE IF NOT EXISTS emp_employee (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) DEFAULT NULL COMMENT '关联登录账号',
  emp_no VARCHAR(32) NOT NULL COMMENT '工号',
  real_name VARCHAR(50) NOT NULL COMMENT '姓名',
  gender VARCHAR(10) DEFAULT '男',
  birth_date DATE DEFAULT NULL,
  id_card VARCHAR(32) DEFAULT NULL,
  phone VARCHAR(20) DEFAULT NULL,
  email VARCHAR(100) DEFAULT NULL,
  dept_name VARCHAR(50) DEFAULT NULL COMMENT '部门',
  position VARCHAR(50) DEFAULT NULL COMMENT '岗位',
  job_level VARCHAR(30) DEFAULT NULL COMMENT '职级',
  education VARCHAR(20) DEFAULT NULL COMMENT '学历',
  school VARCHAR(100) DEFAULT NULL,
  major VARCHAR(100) DEFAULT NULL,
  entry_date DATE DEFAULT NULL COMMENT '入职日期',
  employment_status INT DEFAULT 1 COMMENT '1在职 2试用 3离职',
  work_place VARCHAR(50) DEFAULT NULL,
  remark VARCHAR(500) DEFAULT NULL,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_username (username), KEY idx_dept (dept_name)
) COMMENT '员工档案';

-- ---------------- 2. 个人工作台 ----------------
CREATE TABLE IF NOT EXISTS todo_item (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(100) NOT NULL,
  content VARCHAR(500) DEFAULT NULL,
  biz_type VARCHAR(30) DEFAULT NULL COMMENT '业务类型：考核/审批/公告等',
  handler_name VARCHAR(50) DEFAULT NULL COMMENT '处理人',
  status INT DEFAULT 0 COMMENT '0待办 1已完成',
  due_time DATETIME DEFAULT NULL COMMENT '截止时间',
  finish_time DATETIME DEFAULT NULL,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '待办通知';

CREATE TABLE IF NOT EXISTS work_task (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(100) NOT NULL,
  description VARCHAR(500) DEFAULT NULL,
  assigner_name VARCHAR(50) DEFAULT NULL COMMENT '派发人',
  assignee_name VARCHAR(50) DEFAULT NULL COMMENT '负责人',
  priority INT DEFAULT 2 COMMENT '1高 2中 3低',
  status INT DEFAULT 0 COMMENT '0进行中 1已完成 2已取消',
  progress INT DEFAULT 0 COMMENT '进度 0-100',
  deadline DATE DEFAULT NULL,
  finish_time DATETIME DEFAULT NULL,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '工作任务';

CREATE TABLE IF NOT EXISTS alert_notice (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  level INT DEFAULT 1 COMMENT '1提示 2警告 3严重',
  title VARCHAR(100) NOT NULL,
  content VARCHAR(500) DEFAULT NULL,
  source VARCHAR(50) DEFAULT NULL COMMENT '来源系统/模块',
  status INT DEFAULT 0 COMMENT '0未处理 1已处理',
  handle_remark VARCHAR(300) DEFAULT NULL,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '预警通知';

-- ---------------- 3. 招聘与人才 ----------------
CREATE TABLE IF NOT EXISTS recruit_position (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(100) NOT NULL COMMENT '职位名称',
  dept_name VARCHAR(50) DEFAULT NULL,
  headcount INT DEFAULT 1 COMMENT '招聘人数',
  salary_min INT DEFAULT NULL,
  salary_max INT DEFAULT NULL,
  education VARCHAR(20) DEFAULT NULL,
  experience VARCHAR(50) DEFAULT NULL COMMENT '经验要求',
  job_desc TEXT COMMENT '职位描述',
  requirement TEXT COMMENT '任职要求',
  status INT DEFAULT 0 COMMENT '0招聘中 1暂停 2关闭',
  publisher VARCHAR(50) DEFAULT NULL,
  publish_time DATETIME DEFAULT NULL,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '招聘职位';

CREATE TABLE IF NOT EXISTS talent_profile (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  gender VARCHAR(10) DEFAULT '男',
  phone VARCHAR(20) DEFAULT NULL,
  email VARCHAR(100) DEFAULT NULL,
  source VARCHAR(50) DEFAULT NULL COMMENT '来源渠道',
  apply_position VARCHAR(50) DEFAULT NULL COMMENT '应聘职位',
  education VARCHAR(20) DEFAULT NULL,
  work_years INT DEFAULT NULL,
  skill VARCHAR(300) DEFAULT NULL,
  evaluation VARCHAR(10) DEFAULT 'B' COMMENT '人才评级 S/A/B/C',
  status INT DEFAULT 0 COMMENT '0待联系 1面试中 2已录用 3已淘汰',
  resume_url VARCHAR(300) DEFAULT NULL,
  remark VARCHAR(300) DEFAULT NULL,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '人才库';

CREATE TABLE IF NOT EXISTS talent_declaration (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  declarer_name VARCHAR(50) DEFAULT NULL COMMENT '申报人',
  declare_type VARCHAR(30) DEFAULT NULL COMMENT '申报类型：内部推荐/高端人才/团队引进',
  person_name VARCHAR(50) NOT NULL COMMENT '被申报人',
  reason VARCHAR(500) DEFAULT NULL COMMENT '申报理由',
  material_url VARCHAR(300) DEFAULT NULL,
  status INT DEFAULT 0 COMMENT '0待审核 1通过 2驳回',
  audit_comment VARCHAR(300) DEFAULT NULL,
  auditor VARCHAR(50) DEFAULT NULL,
  audit_time DATETIME DEFAULT NULL,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '人才申报';

CREATE TABLE IF NOT EXISTS hr_pool (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(50) NOT NULL COMMENT '人员姓名',
  dept_name VARCHAR(50) DEFAULT NULL,
  position VARCHAR(50) DEFAULT NULL,
  skill VARCHAR(300) DEFAULT NULL COMMENT '技能标签',
  pool_type INT DEFAULT 1 COMMENT '1内部 2外部',
  availability VARCHAR(50) DEFAULT NULL COMMENT '可投入度说明',
  status INT DEFAULT 1 COMMENT '1可用 2已占用',
  remark VARCHAR(300) DEFAULT NULL,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '人力资源池';

-- ---------------- 4. 绩效考核 ----------------
CREATE TABLE IF NOT EXISTS perf_relation (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  period VARCHAR(20) DEFAULT NULL COMMENT '考核周期 如 2026-H2',
  emp_name VARCHAR(50) NOT NULL,
  dept_name VARCHAR(50) DEFAULT NULL,
  superior_name VARCHAR(50) DEFAULT NULL COMMENT '考核人',
  relation_type INT DEFAULT 1 COMMENT '1上级 2同级 3下级 4HRBP',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '考核关系';

CREATE TABLE IF NOT EXISTS perf_appraisal (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  period VARCHAR(20) NOT NULL COMMENT '考核周期',
  emp_name VARCHAR(50) NOT NULL COMMENT '被考核人',
  dept_name VARCHAR(50) DEFAULT NULL,
  superior_name VARCHAR(50) DEFAULT NULL COMMENT '考核人',
  status INT DEFAULT 0 COMMENT '0目标制定 1目标确认 2自评中 3上级评价 4待面谈 5面谈审核 6待签署 7已完成 8申诉中',
  self_score DECIMAL(5,2) DEFAULT NULL COMMENT '自评总分',
  superior_score DECIMAL(5,2) DEFAULT NULL COMMENT '上级评分',
  total_score DECIMAL(5,2) DEFAULT NULL COMMENT '综合得分=自评40%+上级60%',
  grade VARCHAR(2) DEFAULT NULL COMMENT '等级 S/A/B/C/D',
  sign_status INT DEFAULT 0 COMMENT '0未签署 1已签署',
  remark VARCHAR(300) DEFAULT NULL,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_period (period), KEY idx_emp (emp_name)
) COMMENT '绩效考核主表';

CREATE TABLE IF NOT EXISTS perf_goal (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  appraisal_id BIGINT NOT NULL,
  goal_name VARCHAR(100) NOT NULL COMMENT '目标名称',
  dimension VARCHAR(20) DEFAULT '业绩' COMMENT '维度：业绩/行为/协作',
  weight INT DEFAULT 20 COMMENT '权重%',
  target_value VARCHAR(100) DEFAULT NULL COMMENT '目标值',
  standard VARCHAR(300) DEFAULT NULL COMMENT '评分标准',
  self_score DECIMAL(5,2) DEFAULT NULL COMMENT '自评得分',
  self_comment VARCHAR(300) DEFAULT NULL,
  superior_score DECIMAL(5,2) DEFAULT NULL COMMENT '上级评分',
  superior_comment VARCHAR(300) DEFAULT NULL,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_appraisal (appraisal_id)
) COMMENT '绩效目标（含自评/上级评分）';

CREATE TABLE IF NOT EXISTS perf_interview (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  appraisal_id BIGINT NOT NULL,
  emp_name VARCHAR(50) DEFAULT NULL,
  interviewer VARCHAR(50) DEFAULT NULL COMMENT '面谈人',
  interview_time DATETIME DEFAULT NULL,
  advantage VARCHAR(500) DEFAULT NULL COMMENT '优势肯定',
  shortage VARCHAR(500) DEFAULT NULL COMMENT '不足指出',
  improve_plan VARCHAR(500) DEFAULT NULL COMMENT '改进计划',
  fill_status INT DEFAULT 0 COMMENT '0未填写 1已填写',
  audit_status INT DEFAULT 0 COMMENT '0待审核 1通过 2驳回',
  audit_comment VARCHAR(300) DEFAULT NULL,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_appraisal (appraisal_id)
) COMMENT '绩效面谈';

CREATE TABLE IF NOT EXISTS perf_appeal (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  appraisal_id BIGINT NOT NULL,
  emp_name VARCHAR(50) DEFAULT NULL,
  reason VARCHAR(500) NOT NULL COMMENT '申诉理由',
  expect VARCHAR(300) DEFAULT NULL COMMENT '期望结果',
  status INT DEFAULT 0 COMMENT '0申诉中 1已受理 2已驳回 3已调整',
  audit_comment VARCHAR(300) DEFAULT NULL,
  audit_time DATETIME DEFAULT NULL,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '绩效申诉';

CREATE TABLE IF NOT EXISTS perf_achievement (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  achieve_type INT DEFAULT 1 COMMENT '1个人业绩 2团队业绩',
  period VARCHAR(20) DEFAULT NULL,
  emp_name VARCHAR(50) DEFAULT NULL COMMENT '申请人',
  dept_name VARCHAR(50) DEFAULT NULL,
  title VARCHAR(100) NOT NULL COMMENT '业绩名称',
  description VARCHAR(500) DEFAULT NULL,
  amount DECIMAL(12,2) DEFAULT NULL COMMENT '业绩金额/量化值',
  status INT DEFAULT 0 COMMENT '0草稿 1已提交 2已确认 3已驳回',
  audit_comment VARCHAR(300) DEFAULT NULL,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '业绩记录与团队业绩申请';

-- ---------------- 5. 人事服务 ----------------
CREATE TABLE IF NOT EXISTS sys_notice (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(100) NOT NULL,
  notice_type INT DEFAULT 1 COMMENT '1通知 2公告 3制度',
  content LONGTEXT,
  publisher VARCHAR(50) DEFAULT NULL,
  publish_time DATETIME DEFAULT NULL,
  status INT DEFAULT 1 COMMENT '0草稿 1已发布 2已下线',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '通知公告';

CREATE TABLE IF NOT EXISTS cms_article (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  category VARCHAR(20) NOT NULL COMMENT 'policy政策解读/guide办事指南/exit_entry出入境指南',
  title VARCHAR(100) NOT NULL,
  summary VARCHAR(300) DEFAULT NULL,
  content LONGTEXT,
  author VARCHAR(50) DEFAULT NULL,
  view_count INT DEFAULT 0,
  publish_time DATETIME DEFAULT NULL,
  status INT DEFAULT 1 COMMENT '0下线 1上线',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_category (category)
) COMMENT '内容文章（政策/指南）';

CREATE TABLE IF NOT EXISTS salary_record (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  emp_name VARCHAR(50) NOT NULL,
  dept_name VARCHAR(50) DEFAULT NULL,
  salary_month VARCHAR(10) NOT NULL COMMENT '工资月份 2026-10',
  base_salary DECIMAL(10,2) DEFAULT 0 COMMENT '基本工资',
  post_salary DECIMAL(10,2) DEFAULT 0 COMMENT '岗位工资',
  bonus DECIMAL(10,2) DEFAULT 0 COMMENT '奖金',
  deduction DECIMAL(10,2) DEFAULT 0 COMMENT '扣款',
  insurance DECIMAL(10,2) DEFAULT 0 COMMENT '社保公积金',
  tax DECIMAL(10,2) DEFAULT 0 COMMENT '个税',
  net_salary DECIMAL(10,2) DEFAULT 0 COMMENT '实发',
  status INT DEFAULT 0 COMMENT '0未发放 1已发放',
  pay_time DATETIME DEFAULT NULL,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_emp_month (emp_name, salary_month)
) COMMENT '工资记录';

CREATE TABLE IF NOT EXISTS training_record (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  emp_name VARCHAR(50) NOT NULL,
  dept_name VARCHAR(50) DEFAULT NULL,
  course_name VARCHAR(100) NOT NULL,
  category VARCHAR(30) DEFAULT NULL COMMENT '培训分类',
  trainer VARCHAR(50) DEFAULT NULL,
  start_date DATE DEFAULT NULL,
  end_date DATE DEFAULT NULL,
  hours INT DEFAULT NULL COMMENT '学时',
  score DECIMAL(5,2) DEFAULT NULL COMMENT '考核成绩',
  certificate VARCHAR(100) DEFAULT NULL COMMENT '证书',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '培训档案';

CREATE TABLE IF NOT EXISTS promotion_publicity (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  emp_name VARCHAR(50) NOT NULL,
  dept_name VARCHAR(50) DEFAULT NULL,
  from_position VARCHAR(50) DEFAULT NULL,
  to_position VARCHAR(50) DEFAULT NULL,
  apply_reason VARCHAR(500) DEFAULT NULL,
  publicity_start DATE DEFAULT NULL,
  publicity_end DATE DEFAULT NULL,
  status INT DEFAULT 0 COMMENT '0公示中 1已通过 2有异议',
  remark VARCHAR(300) DEFAULT NULL,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '晋升提拔公示';

CREATE TABLE IF NOT EXISTS vehicle_violation (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  plate_no VARCHAR(20) NOT NULL,
  driver_name VARCHAR(50) DEFAULT NULL,
  violate_time DATETIME DEFAULT NULL,
  violate_type VARCHAR(50) DEFAULT NULL,
  location VARCHAR(100) DEFAULT NULL,
  fine DECIMAL(8,2) DEFAULT 0,
  points INT DEFAULT 0 COMMENT '扣分',
  deduct_status INT DEFAULT 0 COMMENT '0未处理 1已处理',
  handle_remark VARCHAR(300) DEFAULT NULL,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '车辆违规';

-- ---------------- 6. 申报与审批 ----------------
CREATE TABLE IF NOT EXISTS visa_application (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  applicant_name VARCHAR(50) NOT NULL,
  dept_name VARCHAR(50) DEFAULT NULL,
  country VARCHAR(50) DEFAULT NULL,
  visa_type VARCHAR(30) DEFAULT NULL COMMENT '签证类型',
  entry_count VARCHAR(20) DEFAULT NULL COMMENT '单次/多次',
  plan_departure DATE DEFAULT NULL,
  plan_return DATE DEFAULT NULL,
  reason VARCHAR(500) DEFAULT NULL,
  status INT DEFAULT 0 COMMENT '0待审批 1已通过 2已驳回 3已办理',
  audit_comment VARCHAR(300) DEFAULT NULL,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '签证申请';

CREATE TABLE IF NOT EXISTS title_declaration (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  applicant_name VARCHAR(50) NOT NULL,
  dept_name VARCHAR(50) DEFAULT NULL,
  declare_title VARCHAR(50) DEFAULT NULL COMMENT '申报职称',
  declare_level VARCHAR(20) DEFAULT NULL COMMENT '初级/中级/副高/正高',
  major VARCHAR(50) DEFAULT NULL COMMENT '专业',
  obtain_date DATE DEFAULT NULL COMMENT '取得时间',
  material_url VARCHAR(300) DEFAULT NULL,
  status INT DEFAULT 0 COMMENT '0待审核 1通过 2驳回',
  audit_comment VARCHAR(300) DEFAULT NULL,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '职称申报';

CREATE TABLE IF NOT EXISTS title_record (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  emp_name VARCHAR(50) NOT NULL,
  title_name VARCHAR(50) DEFAULT NULL,
  title_level VARCHAR(20) DEFAULT NULL,
  certificate_no VARCHAR(50) DEFAULT NULL,
  obtained_date DATE DEFAULT NULL,
  validity_end DATE DEFAULT NULL,
  status INT DEFAULT 1 COMMENT '1有效 0失效',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '职称记录（我的职称）';

-- ---------------- 7. 信息收发平台 ----------------
CREATE TABLE IF NOT EXISTS sys_message (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  sender_name VARCHAR(50) NOT NULL COMMENT '发件人',
  receiver_name VARCHAR(50) NOT NULL COMMENT '收件人',
  title VARCHAR(100) NOT NULL,
  content TEXT,
  read_flag INT DEFAULT 0 COMMENT '0未读 1已读',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间'
) COMMENT '站内消息';

-- ============================================================
-- 初始化演示数据
-- ============================================================

INSERT INTO emp_employee (username, emp_no, real_name, gender, phone, email, dept_name, position, job_level, education, school, major, entry_date, employment_status, work_place) VALUES
('admin',   'IHR001', '系统管理员', '男', '13800000000', 'admin@ihr.com',   '人事部', '人事经理', 'M2', '本科', '华中科技大学', '人力资源管理', '2020-01-06', 1, '总部'),
('zhangsan','IHR002', '张三',      '男', '13800000001', 'zhangsan@ihr.com','技术部', '高级工程师', 'P6', '硕士', '浙江大学', '计算机科学与技术', '2021-03-15', 1, '总部'),
('lisi',    'IHR003', '李四',      '女', '13800000002', 'lisi@ihr.com',    '技术部', '工程师', 'P5', '本科', '武汉大学', '软件工程', '2022-07-01', 1, '总部'),
(NULL,      'IHR004', '王五',      '男', '13800000003', 'wangwu@ihr.com',  '技术部', '工程师', 'P5', '本科', '中山大学', '软件工程', '2022-09-01', 1, '总部'),
(NULL,      'IHR005', '赵六',      '女', '13800000004', 'zhaoliu@ihr.com', '市场部', '市场专员', 'P4', '本科', '四川大学', '市场营销', '2023-02-13', 1, '总部'),
(NULL,      'IHR006', '孙七',      '男', '13800000005', 'sunqi@ihr.com',   '市场部', '市场经理', 'M1', '硕士', '复旦大学', '工商管理', '2021-08-23', 1, '总部'),
(NULL,      'IHR007', '周八',      '男', '13800000006', 'zhouba@ihr.com',  '财务部', '会计', 'P5', '本科', '厦门大学', '会计学', '2020-05-11', 1, '总部'),
(NULL,      'IHR008', '吴九',      '女', '13800000007', 'wujiu@ihr.com',   '人事部', '人事专员', 'P4', '本科', '湖南大学', '行政管理', '2023-06-26', 2, '总部'),
(NULL,      'IHR009', '郑十',      '男', '13800000008', 'zhengshi@ihr.com','技术部', '架构师', 'P7', '博士', '清华大学', '计算机体系结构', '2019-04-08', 1, '总部'),
(NULL,      'IHR010', '钱一',      '女', '13800000009', 'qianyi@ihr.com',  '市场部', '实习生', 'P2', '本科', '华南理工', '广告学', '2026-09-01', 2, '总部');

INSERT INTO todo_item (title, content, biz_type, handler_name, status, due_time) VALUES
('填写 2026 下半年绩效自评', '请在 11-10 前完成自评提交，逾期将影响考核流程', '绩效考核', '张三', 0, '2026-11-10 18:00:00'),
('确认下属绩效目标', '张三需确认李四、王五的 2026-H2 绩效目标', '绩效考核', '张三', 0, '2026-11-05 18:00:00'),
('提交 10 月差旅报销', '出差深圳的差旅费用报销，附发票', '财务审批', '李四', 0, '2026-11-08 18:00:00'),
('完成新员工入职培训', '消防安全与信息安全必修课', '培训', '吴九', 1, '2026-10-20 18:00:00'),
('更新通讯录信息', '核对本部门联系方式', '人事服务', '赵六', 0, '2026-11-15 18:00:00');

INSERT INTO work_task (title, description, assigner_name, assignee_name, priority, status, progress, deadline) VALUES
('iHR 系统绩效模块开发', '完成绩效考核模块前后端联调', '系统管理员', '张三', 1, 0, 60, '2026-11-20'),
('招聘职位需求收集', '收集 2027 年校招各部门需求', '系统管理员', '吴九', 2, 0, 30, '2026-11-30'),
('Q4 市场活动执行', '双旦线上活动物料与投放', '孙七', '赵六', 2, 0, 45, '2026-12-05'),
('年度审计配合', '提供 2026 年度人事数据', '系统管理员', '周八', 1, 1, 100, '2026-10-31'),
('新人导师带教', '带教实习生钱一熟悉业务', '孙七', '赵六', 3, 0, 20, '2026-12-31');

INSERT INTO alert_notice (level, title, content, source, status, handle_remark) VALUES
(3, '合同到期预警', '王五劳动合同将于 2026-11-30 到期，请及时办理续签', '人事合同', 0, NULL),
(2, '试用期考核预警', '吴九试用期将于 2026-12-26 到期，请提前安排转正评估', '人事档案', 0, NULL),
(2, '绩效自评逾期提醒', '2026-H2 绩效自评截止 11-10，目前完成率仅 35%', '绩效考核', 0, NULL),
(1, '体检安排通知', '2026 年度员工体检预约开放，截止 11-30', '人事服务', 1, '已通知全员');

INSERT INTO recruit_position (title, dept_name, headcount, salary_min, salary_max, education, experience, job_desc, requirement, status, publisher, publish_time) VALUES
('高级 Java 工程师', '技术部', 2, 20000, 35000, '本科', '3-5年', '负责核心业务系统的设计与开发', '熟悉 Spring Boot/MySQL/Redis，有高并发经验', 0, '系统管理员', '2026-10-15 09:00:00'),
('人事专员', '人事部', 1, 6000, 10000, '本科', '1-3年', '负责招聘、入离职手续办理', '熟悉劳动法规，沟通能力强', 0, '系统管理员', '2026-10-20 09:00:00'),
('市场经理', '市场部', 1, 15000, 25000, '本科', '5年以上', '负责品牌市场活动策划与执行', '有 B 端市场经验优先', 1, '系统管理员', '2026-09-01 09:00:00'),
('财务会计', '财务部', 1, 8000, 14000, '本科', '3年以上', '负责总账与月度报表', '中级职称优先', 2, '系统管理员', '2026-08-01 09:00:00');

INSERT INTO talent_profile (name, gender, phone, email, source, apply_position, education, work_years, skill, evaluation, status, remark) VALUES
('刘一', '男', '13900000001', 'liuyi@mail.com', 'BOSS直聘', '高级 Java 工程师', '本科', 5, 'Java/Spring Cloud/K8s', 'A', 1, '技术面已过，待约 HR 面'),
('陈二', '女', '13900000002', 'chener@mail.com', '内推', '人事专员', '本科', 2, '招聘全流程/员工关系', 'B', 2, '已发 offer'),
('刘三', '男', '13900000003', 'liusans@mail.com', '猎头', '市场经理', '硕士', 6, '品牌策划/数字营销', 'S', 0, '猎头推荐，待联系'),
('李四', '女', '13900000004', 'lisi2@mail.com', '官网', '财务会计', '本科', 4, '总账/税务', 'C', 3, '经验不符');

INSERT INTO talent_declaration (declarer_name, declare_type, person_name, reason, status, audit_comment) VALUES
('张三', '内部推荐', '刘一', '前同事，技术扎实，推荐高级 Java 岗位', 1, '已安排面试流程'),
('孙七', '高端人才', '刘三', '行业头部市场负责人，可带团队', 0, NULL),
('系统管理员', '团队引进', '郑十', '资深架构师，拟引进负责平台架构', 1, '已入职技术部');

INSERT INTO hr_pool (name, dept_name, position, skill, pool_type, availability, status, remark) VALUES
('郑十', '技术部', '架构师', '架构设计/技术攻关', 1, '可投入 20%', 1, '重大项目可申请支援'),
('王五', '技术部', '工程师', '前端开发', 1, '可投入 40%', 2, '当前支援招聘模块'),
('刘一', NULL, 'Java 高工', 'Java/微服务', 2, '面试中', 1, '外部候选人'),
('钱一', '市场部', '实习生', '新媒体运营', 1, '可投入 100%', 1, '实习期至 12-31');

INSERT INTO perf_relation (period, emp_name, dept_name, superior_name, relation_type) VALUES
('2026-H2', '李四', '技术部', '张三', 1),
('2026-H2', '王五', '技术部', '张三', 1),
('2026-H2', '张三', '技术部', '系统管理员', 1),
('2026-H2', '赵六', '市场部', '孙七', 1),
('2026-H2', '孙七', '市场部', '系统管理员', 1),
('2026-H2', '吴九', '人事部', '系统管理员', 1),
('2026-H2', '张三', '技术部', '郑十', 4),
('2026-H2', '张三', '技术部', '孙七', 2);

INSERT INTO perf_appraisal (period, emp_name, dept_name, superior_name, status, self_score, superior_score, total_score, grade, sign_status) VALUES
('2026-H2', '张三', '技术部', '系统管理员', 2, NULL, NULL, NULL, NULL, 0),
('2026-H2', '李四', '技术部', '张三', 3, 88.00, NULL, NULL, NULL, 0),
('2026-H2', '王五', '技术部', '张三', 6, 85.00, 82.00, 83.20, 'B', 0),
('2026-H1', '李四', '技术部', '张三', 7, 90.00, 86.00, 87.60, 'A', 1),
('2026-H1', '张三', '技术部', '系统管理员', 7, 92.00, 95.00, 93.80, 'S', 1),
('2026-H1', '赵六', '市场部', '孙七', 7, 78.00, 75.00, 76.20, 'C', 1),
('2026-H1', '王五', '技术部', '张三', 8, 80.00, 70.00, 74.00, 'C', 0);

INSERT INTO perf_goal (appraisal_id, goal_name, dimension, weight, target_value, standard, self_score, self_comment, superior_score, superior_comment) VALUES
(1, 'iHR 系统核心模块交付', '业绩', 40, '11 月底上线', '按期交付满分，延期一周扣 10 分', NULL, NULL, NULL, NULL),
(1, '代码质量与线上事故', '行为', 30, 'P0 事故 0 起', '每起 P0 扣 15 分', NULL, NULL, NULL, NULL),
(1, '跨团队协作', '协作', 30, '满意度≥90%', '季度协作方评分', NULL, NULL, NULL, NULL),
(2, '招聘模块开发', '业绩', 50, '10-31 交付', '按期交付满分', 90.00, '按期完成，质量良好', NULL, NULL),
(2, '技术方案评审参与', '协作', 30, '参与≥6 次', '每次 15 分封顶', 85.00, '参与 8 次', NULL, NULL),
(2, '文档规范', '行为', 20, '文档完整率 100%', '抽检缺一份扣 5 分', 88.00, '已补齐', NULL, NULL),
(3, '绩效模块开发', '业绩', 50, '11-20 交付', '按期交付满分', 85.00, '开发完成 90%', 82.00, '基本按期'),
(3, '代码评审参与', '协作', 20, '每周≥2 次', '按参与率给分', 80.00, '达标', 82.00, '达标'),
(3, '线上问题响应', '行为', 30, '30 分钟内响应', '超时一次扣 5 分', 90.00, '全部及时响应', 82.00, '有两次超时');

INSERT INTO perf_interview (appraisal_id, emp_name, interviewer, interview_time, advantage, shortage, improve_plan, fill_status, audit_status) VALUES
(3, '王五', '张三', '2026-10-28 14:00:00', '开发速度快，交付稳定', '代码评审参与度不足，线上问题响应有超时', 'Q1 参加代码规范培训，建立响应值班机制', 1, 0),
(4, '李四', '张三', '2026-07-05 10:00:00', '执行力强，协作积极', '方案设计能力待提升', '安排架构设计导师辅导', 1, 1),
(5, '张三', '系统管理员', '2026-07-08 15:00:00', '技术全面，带团队负责', '可增加跨部门沟通', '参与公司级技术委员会', 1, 1),
(6, '赵六', '孙七', '2026-07-10 11:00:00', '活动执行到位', '数据复盘不够深入', '参加数据分析培训', 1, 2);

INSERT INTO perf_appeal (appraisal_id, emp_name, reason, expect, status, audit_comment) VALUES
(7, '王五', '上半年线上事故已复盘处理，扣分偏重', '申请调整至 B 等级', 0, NULL),
(4, '李四', '协作评分与实际参与记录不符', '申请复核协作项得分', 3, '复核后协作分上调 3 分，调整为 90.6');

INSERT INTO perf_achievement (achieve_type, period, emp_name, dept_name, title, description, amount, status) VALUES
(1, '2026-Q3', '张三', '技术部', '招聘平台重构', '完成架构升级，交付效率提升 30%', 300000, 2),
(1, '2026-Q3', '赵六', '市场部', 'Q3 线上获客', '渠道获客 1200 条线索', 120000, 2),
(1, '2026-Q4', '李四', '技术部', '绩效模块开发', '绩效考核模块按期交付', NULL, 1),
(2, '2026-Q3', '孙七', '市场部', '双 11 大促活动', '团队 GMV 同比增长 45%', 5000000, 2),
(2, '2026-Q4', '张三', '技术部', '稳定性专项', '团队负责故障率下降 50%', NULL, 0);

INSERT INTO sys_notice (title, notice_type, content, publisher, publish_time, status) VALUES
('2026 年度体检安排通知', 1, '全体员工：\n2026 年度员工体检将于 11 月 15 日至 11 月 30 日进行，请登录内网预约系统选择体检时间与套餐。\n体检前一天请清淡饮食，当日空腹。', '系统管理员', '2026-11-01 09:00:00', 1),
('关于 2026 下半年绩效考核启动的公告', 2, '2026-H2 绩效考核于 11 月 1 日启动，流程如下：\n1. 11-01 ~ 11-05 目标制定与确认\n2. 11-06 ~ 11-10 员工自评\n3. 11-11 ~ 11-15 上级评价\n4. 11-16 ~ 11-25 绩效面谈\n5. 11-26 ~ 11-30 结果签署\n对结果有异议可在签署后 3 个工作日内发起申诉。', '系统管理员', '2026-11-01 10:00:00', 1),
('公司考勤制度（2026 修订版）', 3, '弹性工作时间为 9:00-10:00 上班，对应 18:00-19:00 下班；每月迟到 3 次以内不扣款，超过按制度处理。详细条款见附件。', '人事部', '2026-09-01 09:00:00', 1),
('元旦放假通知', 1, '2027 年元旦 1 月 1 日（周五）放假 1 天，与周末连休。', '系统管理员', '2026-10-28 09:00:00', 0);

INSERT INTO cms_article (category, title, summary, content, author, view_count, publish_time, status) VALUES
('policy', '《2026 年度个税专项附加扣除政策解读》', '子女教育、赡养老人等 7 项专项附加扣除标准与申报要点', '自 2026 年 1 月 1 日起，3 岁以下婴幼儿照护、子女教育专项附加扣除标准为每孩每月 2000 元；赡养老人标准为每月 3000 元。请在 12 月底前完成次年专项附加扣除信息确认，避免影响 1 月工资计税。', '人事部', 156, '2026-10-10 09:00:00', 1),
('policy', '《职称评审新政策解读》', '2026 年职称评审放宽学历限制、强化业绩导向', '本次改革核心变化：1）副高及以下不再将论文作为唯一评价依据；2）项目业绩、专利、技术报告可作为代表性成果；3）工程系列增设大数据、人工智能专业方向。', '人事部', 98, '2026-09-20 09:00:00', 1),
('policy', '《员工绩效申诉制度说明》', '绩效结果申诉的受理范围、时限与流程', '申诉受理范围：评分计算错误、流程违规、明显不公。申诉时限：结果签署后 3 个工作日内。流程：提交申诉 → HRBP 受理 → 上一级考核人复核 → 5 个工作日内反馈。', '人事部', 45, '2026-10-25 09:00:00', 1),
('guide', '《入职办理指南》', '新员工入职材料清单与办理流程', '一、材料清单：身份证原件及复印件、学历学位证书、体检报告、银行卡、离职证明（社招）、一寸照片 2 张。\n二、流程：HR 发送入职通知 → 携带材料到人事部报到 → 签订劳动合同 → 领取工卡与办公设备 → 参加入职培训。\n三、注意事项：体检报告需为入职前 1 个月内三甲医院出具。', '人事部', 210, '2026-08-15 09:00:00', 1),
('guide', '《离职办理指南》', '离职申请、交接与结算流程', '一、提出申请：提前 30 天（试用期 3 天）在系统提交离职申请；\n二、工作交接：与直属上级确认交接清单，归还设备与资料；\n三、结算：人事部核算考勤与剩余假期，财务部结算工资；\n四、社保公积金：次月办理封存或转移，可申请公积金提取。', '人事部', 87, '2026-08-15 09:00:00', 1),
('guide', '《报销操作指南》', '差旅与日常费用报销流程', '一、差旅报销：出差前提交出差申请 → 保留行程单与发票 → 返回后 7 日内在系统提交报销单；\n二、标准：一线城市住宿 500 元/晚，其他城市 400 元/晚；\n三、审批流：直属上级 → 部门负责人 → 财务审核；\n四、到账：审核通过后 5 个工作日内打款。', '财务部', 132, '2026-09-05 09:00:00', 1),
('exit_entry', '《因私护照办理指引》', '护照首次办理与换发流程', '一、材料：身份证原件、出入境照片回执；\n二、流程：网上预约（国家移民管理局 APP）→ 现场办理 → 7 个工作日领取；\n三、费用：首次办理 120 元，换发 140 元。', '人事部', 56, '2026-07-20 09:00:00', 1),
('exit_entry', '《商务签证办理指引（以美国 B1 为例）》', '商务签证申请材料与面签流程', '一、基础材料：护照（有效期 6 个月以上）、DS-160 确认页、预约确认页、签证费收据；\n二、支持材料：在职证明、邀请函、行程单；\n三、流程：填写 DS-160 → 缴费预约 → 面签 → 等待出签（通常 3-5 个工作日）；\n四、系统申报：因公出国需提前 15 个工作日在本系统提交签证申请。', '人事部', 34, '2026-06-18 09:00:00', 1),
('exit_entry', '《港澳通行证及签注办理指引》', '港澳通行证申办与签注续签', '一、首次申办需本人到出入境管理大厅办理；\n二、旅游签注可通过自助机续签，即办即取；\n三、商务签注需提供公司营业执照与派遣函。', '人事部', 41, '2026-05-12 09:00:00', 1);

INSERT INTO salary_record (emp_name, dept_name, salary_month, base_salary, post_salary, bonus, deduction, insurance, tax, net_salary, status, pay_time) VALUES
('张三', '技术部', '2026-10', 15000, 8000, 6000, 0, 3200, 2450, 23350, 1, '2026-11-01 10:00:00'),
('李四', '技术部', '2026-10', 10000, 5000, 3000, 200, 2100, 980, 14720, 1, '2026-11-01 10:00:00'),
('王五', '技术部', '2026-10', 10000, 5000, 2000, 0, 2100, 860, 14040, 1, '2026-11-01 10:00:00'),
('赵六', '市场部', '2026-10', 8000, 4000, 4500, 0, 1700, 760, 14040, 1, '2026-11-01 10:00:00'),
('孙七', '市场部', '2026-10', 14000, 7000, 5000, 0, 3000, 2100, 20900, 1, '2026-11-01 10:00:00'),
('周八', '财务部', '2026-10', 9000, 4500, 1500, 0, 1900, 620, 12480, 1, '2026-11-01 10:00:00'),
('吴九', '人事部', '2026-10', 7000, 3000, 500, 0, 1400, 180, 8920, 1, '2026-11-01 10:00:00'),
('郑十', '技术部', '2026-10', 22000, 12000, 10000, 0, 4800, 5200, 34000, 1, '2026-11-01 10:00:00'),
('张三', '技术部', '2026-09', 15000, 8000, 5000, 0, 3200, 2250, 22550, 1, '2026-10-01 10:00:00'),
('李四', '技术部', '2026-09', 10000, 5000, 2000, 0, 2100, 820, 14080, 1, '2026-10-01 10:00:00');

INSERT INTO training_record (emp_name, dept_name, course_name, category, trainer, start_date, end_date, hours, score, certificate) VALUES
('张三', '技术部', '架构设计高级研修班', '技术', '外聘讲师', '2026-09-10', '2026-09-12', 24, 92, '架构研修结业证书'),
('李四', '技术部', 'Spring Boot 3 实战', '技术', '郑十', '2026-08-05', '2026-08-07', 16, 88, NULL),
('王五', '技术部', '信息安全意识培训', '合规', '人事部', '2026-07-20', '2026-07-20', 4, 95, '信息安全培训合格证'),
('赵六', '市场部', '数据分析基础', '通用', '外聘讲师', '2026-09-15', '2026-09-16', 12, 78, NULL),
('吴九', '人事部', '新员工入职培训', '通用', '人事部', '2026-06-27', '2026-06-28', 8, 90, NULL),
('孙七', '市场部', '管理者领导力培训', '管理', '外聘讲师', '2026-10-12', '2026-10-14', 20, 85, '领导力培训证书');

INSERT INTO promotion_publicity (emp_name, dept_name, from_position, to_position, apply_reason, publicity_start, publicity_end, status, remark) VALUES
('李四', '技术部', '工程师(P5)', '高级工程师(P6)', '上半年绩效 S 级，招聘模块交付突出', '2026-10-25', '2026-11-01', 0, NULL),
('赵六', '市场部', '市场专员(P4)', '市场主管(P5)', '大促活动执行出色', '2026-10-20', '2026-10-27', 1, '公示期满无异议'),
('周八', '财务部', '会计(P5)', '高级会计(P6)', '连续两年绩效 A 级', '2026-09-10', '2026-09-17', 2, '公示期收到异议，暂缓晋升');

INSERT INTO vehicle_violation (plate_no, driver_name, violate_time, violate_type, location, fine, points, deduct_status, handle_remark) VALUES
('粤B12345', '张三', '2026-10-12 08:30:00', '超速 10% 以下', '南山区滨海大道', 200, 0, 1, '已缴款'),
('粤B12345', '张三', '2026-09-03 14:20:00', '违规停车', '福田区新闻路', 200, 0, 1, '已缴款'),
('粤B67890', '孙七', '2026-10-20 17:45:00', '闯红灯', '宝安区创业二路', 500, 6, 0, NULL),
('粤A11111', '周八', '2026-08-15 09:10:00', '不按导向车道行驶', '天河路体育东路口', 200, 2, 1, '公司车统一处理');

INSERT INTO visa_application (applicant_name, dept_name, country, visa_type, entry_count, plan_departure, plan_return, reason, status, audit_comment) VALUES
('张三', '技术部', '美国', 'B1 商务', '多次', '2027-01-10', '2027-01-20', '参加 CES 技术大会并拜访合作方', 1, '同意，注意行程安全'),
('赵六', '市场部', '新加坡', '旅游', '单次', '2026-12-24', '2026-12-28', '年假出境游', 0, NULL),
('孙七', '市场部', '德国', 'B1 商务', '多次', '2027-03-01', '2027-03-10', '汉诺威工业展', 0, NULL),
('周八', '财务部', '日本', 'B1 商务', '单次', '2026-10-08', '2026-10-15', '子公司财务审计', 3, '已完成办理并归档');

INSERT INTO title_declaration (applicant_name, dept_name, declare_title, declare_level, major, obtain_date, material_url, status, audit_comment) VALUES
('李四', '技术部', '工程师', '中级', '计算机技术', '2026-09-01', '/materials/lisi-zhongji.pdf', 1, '材料齐全，通过评审'),
('周八', '财务部', '高级会计师', '副高', '会计学', NULL, '/materials/zhouba-fugao.pdf', 0, NULL),
('张三', '技术部', '高级工程师', '副高', '计算机技术', NULL, '/materials/zhangsan-fugao.pdf', 2, '论文代表作不足，建议补充项目业绩材料后重新申报');

INSERT INTO title_record (emp_name, title_name, title_level, certificate_no, obtained_date, validity_end, status) VALUES
('张三', '工程师', '中级', 'ZJ201900123', '2019-12-01', NULL, 1),
('郑十', '高级工程师', '副高', 'FG201600456', '2016-11-01', NULL, 1),
('周八', '会计师', '中级', 'ZJ202000789', '2020-10-01', NULL, 1),
('李四', '工程师', '中级', 'ZJ202600321', '2026-10-15', NULL, 1),
('王五', '助理工程师', '初级', 'CJ202301234', '2023-06-01', NULL, 1);

INSERT INTO sys_message (sender_name, receiver_name, title, content, read_flag, create_time) VALUES
('系统管理员', '张三', '绩效自评提醒', '请于 11-10 前完成 2026-H2 绩效自评。', 0, '2026-11-01 09:30:00'),
('系统管理员', '全员', '体检预约开放', '2026 年度体检预约已开放，截止 11-30。', 0, '2026-11-01 09:00:00'),
('张三', '李四', '代码评审邀请', '招聘模块 PR #128 需要你评审。', 1, '2026-10-30 16:20:00'),
('孙七', '赵六', 'Q4 活动物料确认', '请今天下班前确认双旦活动物料清单。', 0, '2026-10-31 11:00:00'),
('李四', '张三', '自评已提交', '我已完成绩效自评，请安排评价。', 1, '2026-10-31 18:00:00');
