--------------------------------------------------------------------------
-- .handler command test
--------------------------------------------------------------------------
.help handler
.handler

.run ../common/sqlite_setup.sql
.open sqlite::memory:

.handler prompt
.handler prompt display.fill ("-- javascript prompt: " + session.id + ": " + interpreter.sqlCount + "/" + interpreter.commandCount + " ") + "\n"
.handler prompt
.handler prompt default
.handler prompt

.handler title
.handler title "-- title --"
.handler title
.handler title default
.handler title

.handler success
.handler success "-- javascript success --"
SELECT 1234 AS Test;
.handler success
.handler success default
SELECT 1234 AS Test;
.handler success

.handler update
.handler update "-- javascript update: " + interpreter.activityCount
CREATE TABLE MyTable (a INTEGER, b INTEGER);
INSERT INTO MyTable VALUES (1, 1);
UPDATE MyTable SET b = 2;
.handler update
.handler update default
SELECT 1234 AS Test;
.handler update

.handler error
.handler error "-- javascript error: " + ((error == null) ? message : (sqlex == null ? error.message : ("SQL Error: " + sqlex.message)))
SELECT asdf;
.handler error
.handler error default
SELECT asdf;
.handler error

.handler activity
.handler activity "-- javascript activity: " + interpreter.activityCount
SELECT 1234 AS Test;
.handler activity
.handler activity default
SELECT 1234 AS Test;
.handler activity

.close
