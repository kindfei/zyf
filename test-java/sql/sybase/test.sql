
----------------------------------------------------------
-- Create Table
----------------------------------------------------------
if exists (select * from sysobjects where name = 'test' and type = 'U')
DROP TABLE test;

CREATE TABLE test ( 
	id int NOT NULL,
	version	int NULL,
	type varchar(2),
	content varchar(50) NULL,
	date_time datetime NULL
);

CREATE UNIQUE INDEX id_ind ON test(id, version);

----------------------------------------------------------
-- Create Stored Procedure
----------------------------------------------------------
if exists (select * from sysobjects where name = 'insertTest' and type = 'P')
drop proc insertTest;

create procedure insertTest
@id int out,
@version int out,
@content varchar(50),
@date_time datetime = null,
@transactional int = 0,
@rollback int = 0
as
begin

if (@transactional > 0) begin tran

if @id = null
begin
	select @id = max(id) + 1 from test
	if @id = null select @id = 0
end

if @version = null
begin
	select @version = max(version) + 1 from test where id = @id
	if @version = null select @version = 0
end

if @date_time = null select @date_time = getdate()

insert test (id, version, content, date_time) values (@id, @version, @content, @date_time)

select * from test where id = @id

if (@transactional > 0)
begin
	if (@rollback > 0) rollback tran
	else commit tran
end

end
return @id;

sp_procxmode insertTest, 'anymode';
sp_procxmode insertTest, 'Unchained';

----------------------------------------------------------
-- Test
----------------------------------------------------------
exec testInsTest null, null, 'test';

select * from test;

