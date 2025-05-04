CREATE TABLE continents (
                            id INT PRIMARY KEY AUTO_INCREMENT,
                            name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE countries (
                           id INT PRIMARY KEY AUTO_INCREMENT,
                           name VARCHAR(255) NOT NULL,
                           code VARCHAR(10) NOT NULL UNIQUE,
                           continent_id INT,
                           FOREIGN KEY (continent_id) REFERENCES continents(id)
);

SET REFERENTIAL_INTEGRITY FALSE;

TRUNCATE TABLE countries;
TRUNCATE TABLE continents;

SET REFERENTIAL_INTEGRITY TRUE;


