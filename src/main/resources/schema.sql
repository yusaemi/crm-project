DROP TABLE IF EXISTS client;
DROP TABLE IF EXISTS company;
DROP TABLE IF EXISTS user;

CREATE TABLE company (
  id INT AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  address VARCHAR(255),
  created_by VARCHAR(255),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_by VARCHAR(255),
  updated_at TIMESTAMP,
  PRIMARY KEY (id)
);

CREATE TABLE client (
  id INT AUTO_INCREMENT,
  company_id INT,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(255),
  phone VARCHAR(255),
  created_by VARCHAR(255),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_by VARCHAR(255),
  updated_at TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (company_id) REFERENCES company(id)
);

CREATE TABLE user (
  username VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  role VARCHAR(255) NOT NULL,
  PRIMARY KEY (username)
);