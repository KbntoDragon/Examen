public class V1_init {
-- Esquema inicial del microservicio de bicicletas (db_bicicleta)

CREATE TABLE marcas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

CREATE TABLE colores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE modelos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_modelo VARCHAR(100) NOT NULL,
    tipo_suspension VARCHAR(50) NOT NULL,
    talla_cuadro VARCHAR(10) NOT NULL,
    marca_id INT,
    CONSTRAINT fk_modelo_marca FOREIGN KEY (marca_id) REFERENCES marcas (id)
);

CREATE TABLE bicicletas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    material VARCHAR(50) NOT NULL,
    cliente_id INT,
    modelo_id INT,
    CONSTRAINT fk_bicicleta_modelo FOREIGN KEY (modelo_id) REFERENCES modelos (id)
);

-- Relaciones ManyToMany
CREATE TABLE bicicleta_color (
    bicicleta_id INT NOT NULL,
    color_id INT NOT NULL,
    CONSTRAINT fk_bc_bicicleta FOREIGN KEY (bicicleta_id) REFERENCES bicicletas (id),
    CONSTRAINT fk_bc_color FOREIGN KEY (color_id) REFERENCES colores (id)
);

CREATE TABLE bicicleta_marca (
    bicicleta_id INT NOT NULL,
    marca_id INT NOT NULL,
    CONSTRAINT fk_bm_bicicleta FOREIGN KEY (bicicleta_id) REFERENCES bicicletas (id),
    CONSTRAINT fk_bm_marca FOREIGN KEY (marca_id) REFERENCES marcas (id)
);

}
