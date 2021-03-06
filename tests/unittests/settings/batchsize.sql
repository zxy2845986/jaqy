--------------------------------------------------------------------------
-- batchsize setting test
--------------------------------------------------------------------------
.set batchsize

.run ../common/derby_setup.sql

.open derby:memory:csvDB;create=true

-- VARCHAR type
CREATE TABLE MyTable(a INTEGER PRIMARY KEY, b INTEGER);

INSERT INTO MyTable VALUES (1, 1);
INSERT INTO MyTable VALUES (2, 2);
INSERT INTO MyTable VALUES (3, 3);
INSERT INTO MyTable VALUES (4, 4);
INSERT INTO MyTable
SELECT a + (SELECT max(a) FROM MyTable),
       a + (SELECT max(a) FROM MyTable)
FROM MyTable;

SELECT COUNT(*) FROM MyTable;

.export csv batch.csv
SELECT * FROM MyTable ORDER BY a;
DELETE FROM MyTable;

.set batchsize

.set batchsize 1
.import csv -h batch.csv
INSERT INTO MyTable VALUES (?, ?);
SELECT * FROM MyTable ORDER BY a;
DELETE FROM MyTable;

.set batchsize 2
.import csv -h batch.csv
INSERT INTO MyTable VALUES (?, ?);
SELECT * FROM MyTable ORDER BY a;
DELETE FROM MyTable;

.set batchsize 10000
.import csv -h batch.csv
INSERT INTO MyTable VALUES (?, ?);
SELECT * FROM MyTable ORDER BY a;
DELETE FROM MyTable;

.set batchsize dummy
.set batchsize -1
.set batchsize
.set batchsize 0
.set batchsize

DROP TABLE MyTable;
.close
.os rm -f batch.csv
