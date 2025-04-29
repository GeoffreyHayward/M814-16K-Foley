-- Change price column type from DECIMAL/NUMERIC to FLOAT
ALTER TABLE workshops ALTER COLUMN price TYPE FLOAT USING price::FLOAT;