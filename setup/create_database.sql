-- Database: urss

DROP DATABASE urss;

CREATE DATABASE urss 
	WITH OWNER = postgres
    TEMPLATE = template0
    ENCODING = 'UTF8';
	
COMMENT ON DATABASE urss
	IS 'Database for URSS WebApp Service';