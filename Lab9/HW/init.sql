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
CREATE TABLE cities (
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        name VARCHAR(255) NOT NULL,
                        country_id INT,
                        capital BOOLEAN,
                        latitude DOUBLE,
                        longitude DOUBLE,
                        FOREIGN KEY (country_id) REFERENCES countries(id)
);



SET REFERENTIAL_INTEGRITY FALSE;

TRUNCATE TABLE countries;
TRUNCATE TABLE continents;
TRUNCATE TABLE cities;

SET REFERENTIAL_INTEGRITY TRUE;


