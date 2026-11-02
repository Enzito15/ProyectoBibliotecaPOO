BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "cliente" (
	"idcliente"	INTEGER,
	"nombre"	TEXT,
	"apellidos"	TEXT,
	"dni"	TEXT,
	"email"	TEXT,
	PRIMARY KEY("idcliente")
);
CREATE TABLE IF NOT EXISTS "libros" (
	"idlibros"	INTEGER,
	"numero"	TEXT NOT NULL UNIQUE,
	"titulo"	TEXT,
	"volumen"	TEXT,
	"autor"	TEXT,
	"precio"	REAL NOT NULL,
	"estado"	TEXT DEFAULT 'Disponible',
	PRIMARY KEY("idlibros" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "reserva" (
	"idreserva"	INTEGER,
	"clienteId"	INTEGER NOT NULL,
	"libroId"	INTEGER NOT NULL,
	"fecha_reserva"	DATE NOT NULL,
	"fecha_ingreso"	DATE NOT NULL,
	"fecha_devolver"	DATE NOT NULL,
	"estado"	TEXT DEFAULT 'Activa',
	"total"	REAL NOT NULL,
	"usuarioId"	INTEGER,
	PRIMARY KEY("idreserva" AUTOINCREMENT),
	FOREIGN KEY("clienteId") REFERENCES "cliente"("idcliente"),
	FOREIGN KEY("libroId") REFERENCES "libros"("idlibros"),
	FOREIGN KEY("usuarioId") REFERENCES "usuario"("idusuario")
);
CREATE TABLE IF NOT EXISTS "usuario" (
	"idusuario"	INTEGER,
	"user"	TEXT,
	"password"	TEXT,
	"tipoUser"	TEXT,
	"nombre"	TEXT,
	"apellidos"	TEXT,
	"dni"	TEXT,
	PRIMARY KEY("idusuario")
);
COMMIT;
