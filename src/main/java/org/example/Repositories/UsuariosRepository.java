package org.example.Repositories;

import org.example.Connection.DatabaseConnection;
import org.example.Entities.UsuariosEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

public class UsuariosRepository implements I_Repository<UsuariosEntity> {
    private static UsuariosRepository instance;

    public UsuariosRepository() {
    }

    public static UsuariosRepository getInstanceOf() {
        if (instance == null) {
            instance = new UsuariosRepository();
        }
        return instance;
    }

    @Override
    public void save(UsuariosEntity usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (nombre, email) VALUES (?, ?);";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, usuario.getNombre());
            preparedStatement.setString(2, usuario.getEmail());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al insertar el usuario." + e.getMessage());
        }
    }

    @Override
    public void deleteById(Integer id) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id = ?;";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            int filas = preparedStatement.executeUpdate();
            System.out.printf("Se eliminaron " + filas + " usuarios.");
        } catch (SQLException e) {
            throw new SQLException("Error al eliminar el usuario."+ e.getMessage());
        }
    }

    @Override
    public Optional<ArrayList<UsuariosEntity>> findAll() throws SQLException {
        ArrayList<UsuariosEntity> listaDeUsuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios;";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    UsuariosEntity usuario = new UsuariosEntity(
                            resultSet.getInt("id"),
                            resultSet.getString("nombre"),
                            resultSet.getString("email"));
                    listaDeUsuarios.add(usuario);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error al listar los usuarios."+ e.getMessage());
        }
        return Optional.of(listaDeUsuarios);
    }

    @Override
    public Optional<UsuariosEntity> findById(Integer id) throws SQLException, NoSuchElementException {
        String sql = "SELECT * FROM usuarios WHERE id=?;";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(new UsuariosEntity(
                            resultSet.getInt("id"),
                            resultSet.getString("nombre"),
                            resultSet.getString("email")));
                } else return Optional.empty();
            }
        } catch (SQLException e) {
            throw new SQLException("Error al buscar el libro ingresado.");
        }catch (NoSuchElementException e){
            throw new NoSuchElementException("No se encontro el id ingresado. " + e.getMessage());
        }
    }
}
