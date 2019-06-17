CREATE TABLE IF NOT EXISTS user(
  id           INT            PRIMARY KEY AUTO_INCREMENT,
  uid          INT            NOT NULL UNIQUE,
  name         VARCHAR(255)   NOT NULL UNIQUE,
  password     VARCHAR(255)   NOT NULL,
  icon_url     VARCHAR(255),
  phone        VARCHAR(255)   NOT NULL UNIQUE,
  email        VARCHAR(255)   NOT NULL UNIQUE,
  spare_cash   DOUBLE         NOT NULL DEFAULT 0,
  is_active    TINYINT(1)     NOT NULL DEFAULT 0,
  identity     INT            NOT NULL,
  verify_code  VARCHAR(255)   NOT NULL,
  grade        INT,
  gender       TINYINT(1),
  age          INT,
  major        VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS questionnaire(
  id           INT            PRIMARY KEY AUTO_INCREMENT,
  questions    LONGTEXT,
  answers      LONGTEXT
);

CREATE TABLE IF NOT EXISTS task(
  id           INT            PRIMARY KEY AUTO_INCREMENT,
  pub_id       INT            NOT NULL,
  title        VARCHAR(255)   NOT NULL,
  description  VARCHAR(255)   NOT NULL,
  payment      DOUBLE         NOT NULL,
  type         INT            NOT NULL,
  is_available TINYINT(1)     NOT NULL DEFAULT 0,
  cur_count    INT            NOT NULL,
  max_count    INT            NOT NULL,
  grade_limit  VARCHAR(255),
  gender_limit TINYINT(1),
  age_limit    VARCHAR(255),
  major_limit  VARCHAR(255),
  ques_id      INT,
  CONSTRAINT FOREIGN KEY(pub_id) REFERENCES user(id),
  CONSTRAINT FOREIGN KEY(ques_id) REFERENCES questionnaire(id)
);

CREATE TABLE IF NOT EXISTS user_acc_task(
  id           INT            PRIMARY KEY AUTO_INCREMENT,
  acc_id       INT            NOT NULL,
  task_id      INT            NOT NULL,
  is_done      TINYINT(1)     NOT NULL DEFAULT 0,
  CONSTRAINT FOREIGN KEY(acc_id) REFERENCES user(id),
  CONSTRAINT FOREIGN KEY(task_id) REFERENCES task(id)
)