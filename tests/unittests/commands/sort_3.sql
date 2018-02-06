--------------------------------------------------------------------------
-- .sort command test
--------------------------------------------------------------------------
.run ../common/postgresql_setup.sql

CREATE TABLE MyTable (a INTEGER, b BYTEA, c INT[]);

INSERT INTO MyTable VALUES (1, E'\\xdeadbeef', '{1,2,3}');
INSERT INTO MyTable VALUES (1, E'\\xdeadbeef', NULL);
INSERT INTO MyTable VALUES (1, E'\\xfacefeed', '{1,2,3}');
INSERT INTO MyTable VALUES (1, E'\\xface', '{2,3,4}');
INSERT INTO MyTable VALUES (2, E'\\xdeadbeef', '{1,2,3}');
INSERT INTO MyTable VALUES (2, NULL, '{1,2,3}');
INSERT INTO MyTable VALUES (2, NULL, '{1,2,3}');
FROM MyTable;

.sort -a 2 -a 1
SELECT * FROM MyTable ORDER BY a, b, c;

.sort -a 3
SELECT * FROM MyTable ORDER BY a, b, c;

DROP TABLE MyTable;

CREATE TABLE XmlTable (a INTEGER, b XML, c JSON);

INSERT INTO XmlTable VALUES (1, '<a>1234</a>', '[1,2,3]');
INSERT INTO XmlTable VALUES (1, '<a>1234</a>', '[2,3,4]');
INSERT INTO XmlTable VALUES (1, NULL, NULL);
INSERT INTO XmlTable VALUES (1, NULL, NULL);
INSERT INTO XmlTable VALUES (2, NULL, NULL);
INSERT INTO XmlTable VALUES (2, '<a>1234</a>', '[1,2,3]');
INSERT INTO XmlTable VALUES (2, '<a>1234</a>', '[2,3,4]');
INSERT INTO XmlTable VALUES (2, '<a>1234</a>', '[1,2,3]');
INSERT INTO XmlTable VALUES (2, '<a>1234</a>', '[2,3,4]');
INSERT INTO XmlTable VALUES (3, '<a>2222</a>', '[1,2,3]');
INSERT INTO XmlTable VALUES (3, '<a>3333</a>', '[2,3,4]');
INSERT INTO XmlTable VALUES (3, '<a>4444</a>', '[1,2,3]');
INSERT INTO XmlTable VALUES (3, '<a>5555</a>', '[2,3,4]');

.sort -a 2 -a 3
SELECT * FROM XmlTable ORDER BY a;

DROP TABLE XmlTable;

.close