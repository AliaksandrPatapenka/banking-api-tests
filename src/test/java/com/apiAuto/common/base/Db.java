package com.apiAuto.common.base;

import com.apiAuto.common.base.config.DbConfig;

import java.sql.*;

/** Помощник для проверок данных в БД. */
public class Db {

    /** Проверить, что строка есть: Db.exists("SELECT 1 FROM users WHERE login = ?", login) */
    public static boolean exists(String sql, Object value) {
        try (var con = connect(); var ps = con.prepareStatement(sql)) {
            ps.setObject(1, value);
            return ps.executeQuery().next(); // true — строка найдена
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка запроса: " + sql, e);
        }
    }

    /** Удалить строку: Db.delete("DELETE FROM users WHERE login = ?", login) */
    public static int delete(String sql, Object value) {
        try (var con = connect(); var ps = con.prepareStatement(sql)) {
            ps.setObject(1, value);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка запроса: " + sql, e);
        }
    }

    /** Получить одно значение из первой строки: Db.getValue("SELECT name FROM users WHERE login = ?", login) */
    public static Object getValue(String sql, Object value) {
        try (var con = connect(); var ps = con.prepareStatement(sql)) {
            ps.setObject(1, value);
            var rs = ps.executeQuery();
            return rs.next() ? rs.getObject(1) : null;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка запроса: " + sql, e);
        }
    }

    private static Connection connect() {
        try {
            return DriverManager.getConnection(DbConfig.DB_URL, DbConfig.DB_USER, DbConfig.PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось подключиться к БД", e);
        }
    }
}