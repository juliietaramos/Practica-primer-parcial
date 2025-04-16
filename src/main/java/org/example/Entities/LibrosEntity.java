package org.example.Entities;

import java.util.Objects;

public class LibrosEntity {
    private int id;
    private String titulo;
    private String autor;
    private int anio_publicacion;
    private int unidades_disponibles;

    public LibrosEntity(int id, String titulo, String autor, int anio_publicacion, int unidades_disponibles) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.anio_publicacion = anio_publicacion;
        this.unidades_disponibles = unidades_disponibles;
    }

    public LibrosEntity(String titulo, String autor, int anio_publicacion, int unidades_disponibles) {
        this.titulo = titulo;
        this.autor = autor;
        this.anio_publicacion = anio_publicacion;
        this.unidades_disponibles = unidades_disponibles;
    }

    public LibrosEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getAnio_publicacion() {
        return anio_publicacion;
    }

    public void setAnio_publicacion(int anio_publicacion) {
        this.anio_publicacion = anio_publicacion;
    }

    public int getUnidades_disponibles() {
        return unidades_disponibles;
    }

    public void setUnidades_disponibles(int unidades_disponibles) {
        this.unidades_disponibles = unidades_disponibles;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LibrosEntity that = (LibrosEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "LibrosEntity{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", autor='" + autor + '\'' +
                ", anio_publicacion=" + anio_publicacion +
                ", unidades_disponibles=" + unidades_disponibles +
                '}';
    }
}

