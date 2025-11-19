CREATE TABLE orders (
id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
customer varchar(300) NOT NULL,
pizza varchar(300) NOT NULL,
status varchar(20) NOT NULL
);
