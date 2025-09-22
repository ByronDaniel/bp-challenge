-- Tabla de personas
CREATE TABLE persons (
	id INT IDENTITY(1,1) NOT NULL PRIMARY KEY,		
	name VARCHAR(255) NOT NULL,
	gender VARCHAR(255) NOT NULL,
	age INT NOT NULL,
	identification VARCHAR(255) NOT NULL,
	address VARCHAR(255) NOT NULL,
	phone VARCHAR(255) NOT NULL
);

-- Tabla de clientes (1:1 con personas)
CREATE TABLE clients (
	id INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
  personId INT NOT NULL UNIQUE,
	password VARCHAR(20) NOT NULL,
	status BIT NOT NULL,
	FOREIGN KEY (personId) REFERENCES persons(id) ON DELETE CASCADE
);

-- Tabla de cuentas (1:N con clientes)
CREATE TABLE accounts (
    id INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    numberAccount VARCHAR(255) NOT NULL,
    accountType VARCHAR(255) NOT NULL,
    balance DECIMAL(19, 4) NOT NULL,
    status BIT NOT NULL,
    clientId INT NOT NULL,
	FOREIGN KEY (clientId) REFERENCES clients(id) ON DELETE CASCADE
);

-- Tabla de movimientos
CREATE TABLE movements (
    id INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    date DATETIME NOT NULL DEFAULT GETDATE(),
    movementType VARCHAR(255) NOT NULL,
    value DECIMAL(19, 4) NOT NULL,
    balance DECIMAL(19, 4) NOT NULL,
    accountId INT NOT NULL,
	FOREIGN KEY (accountId) REFERENCES accounts(id) ON DELETE CASCADE
);