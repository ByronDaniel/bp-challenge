IF EXISTS (SELECT name FROM sys.databases WHERE name = N'challenge')
BEGIN
    DROP DATABASE challenge;
END
GO

CREATE DATABASE challenge;
GO

USE challenge;
GO

CREATE TABLE persons (
    person_id INT IDENTITY(1,1) NOT NULL PRIMARY KEY,		
    name VARCHAR(70) NOT NULL,
    gender VARCHAR(9) NOT NULL,
    age INT NOT NULL,
    identification VARCHAR(10) NOT NULL,
    address VARCHAR(100) NOT NULL,
    phone VARCHAR(10) NOT NULL
);

CREATE TABLE clients (
    client_id INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    person_id INT NOT NULL UNIQUE,
    password VARCHAR(20) NOT NULL,
    status BIT NOT NULL,
    FOREIGN KEY (person_id) REFERENCES persons(person_id) ON DELETE CASCADE
);

CREATE TABLE accounts (
    account_id INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    client_id INT NOT NULL,
    number VARCHAR(10) NOT NULL,
    type VARCHAR(50) NOT NULL,
    balance DECIMAL(19, 4) NOT NULL,
    status BIT NOT NULL,
    FOREIGN KEY (client_id) REFERENCES clients(client_id) ON DELETE CASCADE
);

CREATE TABLE movements (
    movement_id INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    account_id INT NOT NULL,
    date DATETIME NOT NULL DEFAULT GETDATE(),
    type VARCHAR(7) NOT NULL,
    value DECIMAL(19, 4) NOT NULL,
    balance DECIMAL(19, 4) NOT NULL,
    FOREIGN KEY (account_id) REFERENCES accounts(account_id) ON DELETE CASCADE
);