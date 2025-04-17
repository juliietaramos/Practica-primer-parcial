package org.example;

import org.example.Entities.LibrosEntity;
import org.example.Entities.PrestamosEntity;
import org.example.Entities.UsuariosEntity;
import org.example.Services.LibrosService;
import org.example.Services.PrestamosService;
import org.example.Services.UsuariosService;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    static UsuariosService usuariosService = UsuariosService.getInstanceOf();
    static LibrosService librosService = LibrosService.getInstanceOf();
    static PrestamosService prestamosService = PrestamosService.getInstanceOf();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

//        RF01 - El sistema deberá permitirle al Bibliotecario realizar alta y baja de usuarios. FALTA DAR DE BAJA!!!
//        darDeAlta();
//        darDeBaja();

//        RF02 - El sistema deberá permitirle al Bibliotecario listar todos los usuarios
//        listarUsuarios();

//        RF03 - El sistema deberá permitirle al Bibliotecario listar todos los usuarios con préstamos activos.
//        System.out.println("Usuarios con prestamos activos:");
//        listarUsuariosPrestamosActivos().forEach(System.out::println);

//        RF04 - El sistema deberá permitirle al Bibliotecario generar un préstamo nuevo.
//       darAltaPrestamo();

//        RF05 - El sistema deberá permitirle al Bibliotecario marcar un préstamo como devuelto.
 //       devolverPrestamo();

//        RF06 - El sistema deberá permitirle al Bibliotecario visualizar todos los préstamos.
//        listarPrestamos().forEach(System.out::println);

//        RF07 - El sistema deberá permitirle al Bibliotecario visualizar todos los préstamos activos.
//        listarPrestamosActivos().forEach(System.out::println);

//        RF08 - El sistema deberá permitirle al Bibliotecario visualizar el libro más prestado.
//        System.out.println(libroMasPrestado());

//        RF09 - El sistema deberá permitirle al Bibliotecario visualizar el total de libros disponibles.
//        visualizarLibrosDisponibles();

//        RF10 - El sistema deberá permitirle al Bibliotecario visualizar todos los libros.
//        visualizarLibros();

//        RF11 - El sistema deberá permitirle al Bibliotecario visualizar el usuario con
//        mayor número de préstamos históricos en el sistema.
//        System.out.println(mostrarUserConMasPrestamos());

//        RF12 - El sistema deberá permitirle al Bibliotecario visualizar el promedio de
//        préstamos por usuario, pero solo teniendo en cuenta a los usuarios que tienen por lo menos un préstamo.
//        promedioPrestamos();
    }

    private static UsuariosEntity crearUsuario() {
        System.out.println("Ingrese el nombre del usuario: ");
        String nombre = scanner.nextLine();
        System.out.println("Ingrese el mail del usuario: ");
        String email = scanner.nextLine();
        return new UsuariosEntity(nombre, email);
    }

    private static void darDeAlta() {
        usuariosService.agregarUsuario(crearUsuario());
    }

    private static void darDeBaja() {
        System.out.println("Ingrese el id del usuario a dar de baja: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        usuariosService.eliminarUsuario(id);
    }

    private static void listarUsuarios() {
        usuariosService.listarUsuarios().forEach(System.out::println);
    }

    private static List<UsuariosEntity> listarUsuariosPrestamosActivos() {
        List<Integer> listaId = prestamosService.listarPrestamosActivos().stream().map(PrestamosEntity::getUsuario_id).distinct().toList();
        return usuariosService.listarUsuariosEspecifico(listaId);
    }

    private static void darAltaPrestamo() {
        listarUsuarios();
        System.out.println("Ingrese el id del usuario: ");
        int id_usuario = scanner.nextInt();
        scanner.nextLine();
        librosService.listarLibros().forEach(System.out::println);
        System.out.println("Ingrese el id del libro: ");
        int id_libro = scanner.nextInt();
        scanner.nextLine();
        prestamosService.agregarPrestamo(PrestamosEntity.builder()
                .id_libro(id_libro)
                .usuario_id(id_usuario)
                .fecha_devolucion(null)
                .build());
    }

    private static void mostrarPrestamosUsuario() {
        listarUsuarios();
        System.out.println("Ingrese el id del usuario: ");
        int id_usuario = scanner.nextInt();
        scanner.nextLine();
        prestamosService.listarPrestamosPorUsuario(id_usuario).forEach(System.out::println);
    }

    private static void devolverPrestamo() {
        mostrarPrestamosUsuario();
        System.out.println("Ingrese el id del prestamo a devolver: ");
        int id_prestamo = scanner.nextInt();
        scanner.nextLine();
        prestamosService.devolverLibro(id_prestamo);
    }

    private static List<PrestamosEntity> listarPrestamos() {
        return prestamosService.listarPrestamos();
    }

    private static List<PrestamosEntity> listarPrestamosActivos() {
        return listarPrestamos()
                .stream()
                .filter(p -> p.getFecha_devolucion() == null)
                .toList();
    }

    private static LibrosEntity libroMasPrestado() {
        Map<Integer, Long> prestamosPorLibro = prestamosService.prestamosPorLibro();
        // Encontrar el libro con el máximo número de préstamos
        Optional<Map.Entry<Integer, Long>> libroMasPrestado = prestamosPorLibro
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue());

        return librosService.mostrarLibros(libroMasPrestado.get().getKey());
    }

    private static void visualizarLibrosDisponibles (){
        librosService.listarLibrosDisponibles().forEach(System.out::println);
    }

    private static void visualizarLibros(){
        librosService.listarLibros().forEach(System.out::println);
    }

    private static UsuariosEntity mostrarUserConMasPrestamos(){
        return usuariosService.mostrarUsuario(prestamosService.obtenerIdUserMasPrestamos());
    }

    private static void promedioPrestamos(){
        double promedio = prestamosService.obtenerPromedioPrestamosPorUsuario();
        System.out.println("El promedio de prestamos por usuario es de " + promedio);
    }
}