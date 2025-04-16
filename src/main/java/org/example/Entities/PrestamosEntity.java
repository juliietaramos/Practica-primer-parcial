package org.example.Entities;

import lombok.*;
import java.time.LocalDate;
import java.util.Objects;


@Builder
@AllArgsConstructor
@Getter
@Setter
public class PrestamosEntity {
    private int id;
    private int id_libro;
    private int usuario_id;
    private LocalDate fecha_prestamo;
    private LocalDate fecha_devolucion;

//    public PrestamosEntity(int id, int id_libro, int usuario_id, LocalDate fecha_prestamo, LocalDate fecha_devolucion) {
//        this.id = id;
//        this.id_libro = id_libro;
//        this.usuario_id = usuario_id;
//        this.fecha_prestamo = fecha_prestamo;
//        this.fecha_devolucion = fecha_devolucion;
//    }



    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PrestamosEntity that = (PrestamosEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PrestamosEntity{" +
                "id=" + id +
                ", id_libro=" + id_libro +
                ", usuario_id=" + usuario_id +
                ", fecha_prestamo=" + fecha_prestamo +
                ", fecha_devolucion=" + fecha_devolucion +
                '}';
    }
}
