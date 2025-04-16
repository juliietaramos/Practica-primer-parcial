
package org.example.Repositories;

import org.example.Connection.DatabaseConnection;
import org.example.Entities.PrestamosEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

public class PrestamosRepository implements I_Repository<PrestamosEntity> {
    private static PrestamosRepository instance;

    public PrestamosRepository() {
    }

    public static PrestamosRepository getInstanceOf() {
        if (instance == null) {
            instance = new PrestamosRepository();
        }
        return instance;
    }

    private Optional<PrestamosEntity> resultSetToPrestamo(ResultSet resultSet) throws SQLException {
        LocalDate localDate = null;
        String fechaDevolucion = resultSet.getString("fecha_devolucion");
        if (fechaDevolucion != null) localDate = LocalDate.parse(fechaDevolucion);
        return Optional.of(PrestamosEntity.builder()
                .id(resultSet.getInt("id"))
                .id_libro(resultSet.getInt("libro_id"))
                .usuario_id(resultSet.getInt("usuario_id"))
                .fecha_prestamo(LocalDate.parse(resultSet.getString("fecha_prestamo")))
                .fecha_devolucion(localDate)
                .build());
    }

    @Override
    public void save(PrestamosEntity prestamos) throws SQLException {
        String sql = "INSERT INTO prestamos (libro_id, usuario_id) VALUES (?, ?);";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, prestamos.getId_libro());
            preparedStatement.setInt(2, prestamos.getUsuario_id());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al insertar el prestamo." + e.getMessage());
        }
    }

    @Override
    public void deleteById(Integer id) throws SQLException {
        String sql = "DELETE FROM prestamos WHERE id = ?;";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            int filas = preparedStatement.executeUpdate();
            System.out.printf("Se eliminaron " + filas + " prestamos.");
        } catch (SQLException e) {
            throw new SQLException("Error al eliminar el prestamo."+ e.getMessage());
        }
    }

    @Override
    public Optional<ArrayList<PrestamosEntity>> findAll() throws SQLException, NullPointerException {
        ArrayList<PrestamosEntity> listaDePrestamos = new ArrayList<>();
        String sql = "SELECT * FROM prestamos;";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Optional<PrestamosEntity> prestamo = resultSetToPrestamo(resultSet);
                    prestamo.ifPresent(listaDePrestamos::add);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error al listar los prestamos. " + e.getMessage());
        }catch (NullPointerException e){
            throw new NullPointerException("Error " + e.getMessage());
        }
        return Optional.of(listaDePrestamos);
    }

    public void modifyDate (int id_prestamo) throws SQLException{
        String sql = "UPDATE prestamos SET fecha_devolucion = DATE('now') WHERE id = ?;";
        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1,id_prestamo);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al devolver el libro. " + e.getMessage());
        }
    }

    @Override
    public Optional<PrestamosEntity> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM prestamos WHERE id=?;";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSetToPrestamo(resultSet);
                } else return Optional.empty();
            }
        } catch (SQLException e) {
            throw new SQLException("Error al buscar el libro ingresado."+ e.getMessage());
        }
    }

}
