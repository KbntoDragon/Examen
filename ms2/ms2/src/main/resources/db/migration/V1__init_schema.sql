CREATE TABLE tipo_pagos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tipo VARCHAR(255) NOT NULL
);

CREATE TABLE boletas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    total DOUBLE NOT NULL,
    TipoPago_id INT,
    CONSTRAINT fk_boleta_tipopago FOREIGN KEY (TipoPago_id) REFERENCES tipo_pagos (id)
);

CREATE TABLE boletas_empleados (
    boleta_id INT NOT NULL,
    empleado_id INT,
    CONSTRAINT fk_be_boleta FOREIGN KEY (boleta_id) REFERENCES boletas (id)
);

CREATE TABLE boletas_productos (
    boleta_id INT NOT NULL,
    producto_id INT,
    CONSTRAINT fk_bp_boleta FOREIGN KEY (boleta_id) REFERENCES boletas (id)
);

CREATE TABLE boletas_repuestos (
    boleta_id INT NOT NULL,
    repuesto_id INT,
    CONSTRAINT fk_br_boleta FOREIGN KEY (boleta_id) REFERENCES boletas (id)
);

CREATE TABLE boletas_servicios (
    boleta_id INT NOT NULL,
    servicio_id INT,
    CONSTRAINT fk_bs_boleta FOREIGN KEY (boleta_id) REFERENCES boletas (id)
);
