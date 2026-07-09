CREATE TABLE productos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_producto VARCHAR(200) NOT NULL,
    precio DOUBLE NOT NULL,
    stock INT NOT NULL,
    codigo_barras VARCHAR(100) NOT NULL,
    boleta_id INT
);

CREATE TABLE repuestos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_repuesto VARCHAR(200) NOT NULL,
    precio DOUBLE NOT NULL,
    stock_repuesto INT NOT NULL,
    codigo_barras VARCHAR(100),
    boleta_id INT
);

CREATE TABLE servicios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_servicio VARCHAR(200) NOT NULL,
    desc_servicio VARCHAR(400) NOT NULL,
    valor_del_servicio DOUBLE NOT NULL,
    boleta_id INT
);
