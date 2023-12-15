CREATE TABLE Categories (
                            categoryId SERIAL PRIMARY KEY,
                            categoryName VARCHAR(255) UNIQUE NOT NULL
);

INSERT INTO Categories (categoryName) SELECT 'Restaurant' WHERE NOT EXISTS (SELECT 1 FROM Categories WHERE categoryName = 'Restaurant');
INSERT INTO Categories (categoryName) SELECT 'Salaire' WHERE NOT EXISTS (SELECT 1 FROM Categories WHERE categoryName = 'Salaire');
INSERT INTO Categories (categoryName) SELECT 'Telephone' WHERE NOT EXISTS (SELECT 1 FROM Categories WHERE categoryName = 'Telephone');
INSERT INTO Categories (categoryName) SELECT 'Multimedia' WHERE NOT EXISTS (SELECT 1 FROM Categories WHERE categoryName = 'Multimedia');


