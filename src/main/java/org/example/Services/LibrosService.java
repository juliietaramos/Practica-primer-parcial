package org.example.Services;

import org.example.Entities.LibrosEntity;
import org.example.Entities.UsuariosEntity;
import org.example.Repositories.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LibrosService {
    private UsuariosRepository usuariosRepository;
    private LibrosRepository librosRepository;
    private PrestamosRepository prestamosRepository;
    private static LibrosService instance;
    private List<LibrosEntity> listaDeLibros;

    public LibrosService() {
        this.usuariosRepository = UsuariosRepository.getInstanceOf();
        this.librosRepository = LibrosRepository.getInstanceOf();
        this.prestamosRepository = PrestamosRepository.getInstanceOf();
        listaDeLibros=new ArrayList<>();
    }

    public  List<LibrosEntity>listarLibrosDisponibles(){
        actualizarLista();
        return listaDeLibros
                .stream()
                .filter(l -> l.getUnidades_disponibles()>0)
                .toList();
    }

    public static LibrosService getInstanceOf() {
        if (instance == null) {
            instance = new LibrosService();
        }
        return instance;
    }

    private void actualizarLista() {
        try {
            listaDeLibros = librosRepository.findAll().get();
        } catch (SQLException e) {
            System.out.printf(e.getMessage());
        }
    }

    public void agregarLibros (LibrosEntity libro){
        try{
            librosRepository.save(libro);
            actualizarLista();
            System.out.println("Libro agregado con exito.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void eliminarLibro (int id){
        try{
            librosRepository.deleteById(id);
            actualizarLista();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<LibrosEntity> listarLibros(){
        try{
            return listaDeLibros=librosRepository.findAll().get();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public LibrosEntity mostrarLibros (int id){
        try{
            return librosRepository.findById(id).get();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
