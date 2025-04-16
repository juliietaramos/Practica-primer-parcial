package org.example.Services;

import lombok.*;
import org.example.Entities.UsuariosEntity;
import org.example.Repositories.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor

public class UsuariosService {
    private  UsuariosRepository usuariosRepository;
    private  LibrosRepository librosRepository;
    private  PrestamosRepository prestamosRepository;
    private static UsuariosService instance;
    private  List<UsuariosEntity> listaDeUsuarios;

    public UsuariosService() {
        this.usuariosRepository = UsuariosRepository.getInstanceOf();
        this.librosRepository = LibrosRepository.getInstanceOf();
        this.prestamosRepository = PrestamosRepository.getInstanceOf();
        listaDeUsuarios=new ArrayList<>();
    }

    public static UsuariosService getInstanceOf() {
        if (instance == null) {
            instance = new UsuariosService();
        }
        return instance;
    }

    private void actualizarLista() {
        try {
            listaDeUsuarios = usuariosRepository.findAll().get();
        } catch (SQLException e) {
            System.out.printf(e.getMessage());
        }
    }

    public void agregarUsuario (UsuariosEntity usuario){
        try{
            usuariosRepository.save(usuario);
            actualizarLista();
            System.out.println("Usuario agregado con exito.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void eliminarUsuario (int id){
        try{
            usuariosRepository.deleteById(id);
            actualizarLista();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<UsuariosEntity> listarUsuarios(){
        try{
            return listaDeUsuarios=usuariosRepository.findAll().get();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public UsuariosEntity mostrarUsuario (int id){
        try{
            return usuariosRepository.findById(id).get();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<UsuariosEntity> listarUsuariosEspecifico (List<Integer>listaId){
        actualizarLista();
        List<UsuariosEntity> usuariosFiltrados = listaDeUsuarios.stream()
                .filter(u -> listaId.contains(u.getId()))
                .toList();

        return usuariosFiltrados;
    }
}
