package org.example.Repositories;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public interface I_Repository <T>{
    public void save(T t) throws SQLException;
    public void deleteById (Integer id) throws SQLException;
    public Optional<ArrayList<T>> findAll() throws SQLException;
    public Optional<T> findById (Integer id) throws SQLException;
}
