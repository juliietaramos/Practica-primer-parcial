package org.example.Services;


import org.example.Entities.PrestamosEntity;
import org.example.Entities.UsuariosEntity;
import org.example.Repositories.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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
                int cantidad = recuperarCantidad(prestamo.getId_libro());
                prestamosRepository.save(prestamo);
                librosRepository.actualizarUnidades(prestamo.getId_libro(), cantidad - 1);
                actualizarLista();
                System.out.println("Prestamo agregado con exito.");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else System.out.println("No se encuentran las unidades necesarias.");
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
        } catch (NoSuchElementException e) {
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
                PrestamosEntity prestamo = mostrarPrestamo(id);
                int cantidad = recuperarCantidad(prestamo.getId_libro());
                librosRepository.actualizarUnidades(prestamo.getId_libro(), cantidad + 1);
                System.out.println("Libro devuelto con exito.");
            } else if (!verificarDevolucion(id)){
                System.out.println("El prestamo ya fue devuelto anteriormente.");
            } else  if (verificarDevolucion(id)==null)System.out.println("Ha ocurrido un error.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Map<Integer, Long> prestamosPorLibro (){
        actualizarLista();
        return listaDePrestamos
                .stream()
                .collect(Collectors.groupingBy(PrestamosEntity::getId_libro, Collectors.counting()));
    }

    public int obtenerIdUserMasPrestamos(){
        actualizarLista();
        return listaDePrestamos
                .stream()
                .collect(Collectors.groupingBy(PrestamosEntity::getUsuario_id, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(0);
    }

    private Boolean verificarDevolucion(int id) {
        try {
            if (mostrarPrestamo(id).getFecha_devolucion() != null) { //si fue devuelto
                return false;
            } else return true;
        } catch (Exception e) {
            System.out.println("Error al ingresar el id del usuario." + e.getMessage());
        }
        return null;
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

    public int recuperarCantidad(int id_libro) {
        int cantidad = 0;
        try {
            cantidad = librosRepository.findAll().get()
                    .stream()
                    .filter(c -> c.getId() == id_libro)
                    .mapToInt(c -> c.getUnidades_disponibles())
                    .findFirst()
                    .orElse(0);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return cantidad;
    }

    public double obtenerPromedioPrestamosPorUsuario() {
        actualizarLista();

        // Agrupar por usuario y contar préstamos
        Map<Integer, Long> prestamosPorUsuario = listaDePrestamos.stream()
                .collect(Collectors.groupingBy(PrestamosEntity::getUsuario_id, Collectors.counting()));

        // Calcular el promedio: suma total / cantidad de usuarios con préstamos
        return prestamosPorUsuario.values().stream()
                .mapToLong(Long::longValue)
                .average()
                .orElse(0.0); // Por si no hay datos
    }




}
