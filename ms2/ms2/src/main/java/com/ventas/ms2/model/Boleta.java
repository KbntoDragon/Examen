package com.ventas.ms2.model;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "boletas")
public class Boleta {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private Double total;

  @ManyToOne
  @JoinColumn(name = "TipoPago_id")
  private TipoPago tipoPago;

  @ElementCollection
  @CollectionTable(name = "boletas_empleados", joinColumns = @JoinColumn(name = "boleta_id"))
  @Column(name = "empleado_id")
  private List<Integer> empleadoIds;

  @ElementCollection
  @CollectionTable(name = "boletas_productos", joinColumns = @JoinColumn(name = "boleta_id"))
  @Column(name = "producto_id")
  private List<Integer> productoIds;

  @ElementCollection
  @CollectionTable(name = "boletas_repuestos", joinColumns = @JoinColumn(name = "boleta_id"))
  @Column(name = "repuesto_id")
  private List<Integer> repuestoIds;

  @ElementCollection
  @CollectionTable(name = "boletas_servicios", joinColumns = @JoinColumn(name = "boleta_id"))
  @Column(name = "servicio_id")
  private List<Integer> servicioIds;
  
}