CREATE TABLE sale(
	saleid INT PRIMARY KEY,
	userid VARCHAR(10) NOT NULL,
	saledate DATETIME
);

CREATE TABLE saleitem(
	saleid INT,
	seq INT,
	itemid INT NOT NULL,
	quantity INT,
	PRIMARY KEY(saleid, seq)
);