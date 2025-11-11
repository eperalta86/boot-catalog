package cl.eperalta86.boot.catalog.controller;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RestController
public class DatabaseController {


    @Autowired
    private DataSource dataSource;

  @GetMapping("/db/test")
    public String testConnection() {
        try (Connection conn = dataSource.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                return "Conexión exitosa a " + conn.getMetaData().getURL();
            } else {
                return "Conexión fallida: conn null o cerrada";
            }
        } catch (SQLException e) {
            return "Error conectando a la base de datos: " + e.getMessage();
        }
    }
}
