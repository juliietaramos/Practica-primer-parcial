package org.example.Services;


import org.example.Entities.PrestamosEntity;
import org.example.Entities.UsuariosEntity;
import org.example.Repositories.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PrestamosService {
    private UsuariosRepository usuariosRepository;
    private LibrosRepository librosRepository;
    private PrestamosRepository prestamosRepository;
    private static PrestamosService instance;
    private List<PrestamosEntity> listaDePrestamos;

    public PrestamosService() {
        this.usuariosRepository = UsuariosRepository.getInstanceOf();
        this.librosRepository = LibrosRepository.getInstanceOf();
        this.prestamosRepository = PrestamosRepository.getInstanceOf();
        listaDePrestamos = new ArrayList<>();
    }

    public static PrestamosService getInstanceOf() {
        if (instance == null) {
            instance = new PrestamosService();
        }
        return instance;
    }

    private void actualizarLista() {
        try {
            listaDePrestamos = prestamosRepository.findAll().get();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void agregarPrestamo(PrestamosEntity prestamo) {
        if (verificarDisponibilidad(prestamo.getId_libro())) {
            try {
                Double cantidad =recuperarCantidad(prestamo.getId_libro());
                prestamosRepository.save(prestamo);
                actualizarLista();
                System.out.println("Prestamo agregado con exito.");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }else System.out.println("No se encuentran las unidades necesarias.");
    }

    public void eliminarPrestamo(int id) {
        try {
            prestamosRepository.deleteById(id);
            actualizarLista();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<PrestamosEntity> listarPrestamos() {
        List<PrestamosEntity> lista = new ArrayList<>();
        try {
            lista = prestamosRepository.findAll().get();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return lista;
    }

    public PrestamosEntity mostrarPrestamo(int id) {
        try {
            return prestamosRepository.findById(id).get();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<PrestamosEntity> listarPrestamosActivos() {
        actualizarLista();
        return listaDePrestamos
                .stream()
                .filter(p -> p.getFecha_devolucion() == null)
                .toList();
    }

    public List<PrestamosEntity> listarPrestamosPorUsuario(int id) {
        actualizarLista();
        return listaDePrestamos
                .stream()
                .filter(p -> p.getUsuario_id() == id)
                .toList();
    }

    public void devolverLibro(int id) {
        try {
            if (verificarDevolucion(id)) {

                prestamosRepository.modifyDate(id);
            } else System.out.println("El prestamo ya fue devuelto anteriormente.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean verificarDevolucion(int id) {
        if (mostrarPrestamo(id).getFecha_devolucion() != null) { //si fue devuelto
            return false;
        } else return true;
    }

    private boolean verificarDisponibilidad(int id_libro) {
        boolean rta = true;
        try {
            if (librosRepository.findById(id_libro).get().getUnidades_disponibles() <= 0) { //si no hay unidades devulvo un false
                rta = false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return rta;
    }

    public Double recuperarCantidad(int id_libro){
        Double cantidad = 0.0;
        try{
            cantidad = librosRepository.findAll().get()
                    .stream()
                    .filter(c -> c.getId()== id_libro)
                    .mapToDouble(c -> c.getUnidades_disponibles())
                    .findFirst()
                    .orElse(0);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return cantidad;
    }
}
