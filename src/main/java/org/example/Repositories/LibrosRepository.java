package org.example.Repositories;

import org.example.Connection.DatabaseConnection;
import org.example.Entities.LibrosEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class LibrosRepository implements I_Repository<LibrosEntity> {
    private static LibrosRepository instance;

    public LibrosRepository() {
    }

    public static LibrosRepository getInstanceOf() {
        if (instance == null) {
            instance = new LibrosRepository();
        }
        return instance;
    }

    @Override
    public void save(LibrosEntity libro) throws SQLException {
        String sql = "INSERT INTO libros (titulo, autor, anio_publicacion, unidades_disponibles) VALUES (?, ?, ?, ?);";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, libro.getTitulo());
            preparedStatement.setString(2, libro.getAutor());
            preparedStatement.setInt(3, libro.getAnio_publicacion());
            preparedStatement.setInt(4, libro.getUnidades_disponibles());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al insertar el libro.");
        }
    }

    @Override
    public void deleteById(Integer id) throws SQLException {
        String sql = "DELETE FROM libros WHERE id = ?;";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            int filas = preparedStatement.executeUpdate();
            System.out.printf("Se eliminaron " + filas + " libros.");
        } catch (SQLException e) {
            throw new SQLException("Error al eliminar el libro.");
        }
    }

    @Override
    public Optional<ArrayList<LibrosEntity>> findAll() throws SQLException {
        ArrayList<LibrosEntity> listaDeLibros = new ArrayList<>();
        String sql = "SELECT * FROM libros;";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    LibrosEntity libro = new LibrosEntity(
                            resultSet.getInt("id"),
                            resultSet.getString("titulo"),
                            resultSet.getString("autor"),
                            resultSet.getInt("anio_publicacion"),
                            resultSet.getInt("unidades_disponibles"));
                    listaDeLibros.add(libro);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error al listar los libros.");
        }
        return Optional.of(listaDeLibros);
    }

    @Override
    public Optional<LibrosEntity> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM libros WHERE id=?;";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1,id);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if (resultSet.next()){
                    return Optional.of(new LibrosEntity(
                            resultSet.getInt("id"),
                            resultSet.getString("titulo"),
                            resultSet.getString("autor"),
                            resultSet.getInt("anio_publicacion"),
                            resultSet.getInt("unidades_disponibles")
                    ));
                }
                else return Optional.empty();
            }
        }catch (SQLException e){
            throw new SQLException("Error al buscar el libro ingresado.");
        }
    }

    
}
