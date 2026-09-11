package com.apiAuto.common.helpers;

import com.apiAuto.common.base.config.DbConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Хелпер для проверок данных в БД через JDBC. */
public class DbUtils {

    /** Есть ли строка. */
    public static boolean exists(String sql, Object value) {
        try (var con = connect(); var ps = con.prepareStatement(sql)) {
            ps.setObject(1, value);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка запроса: " + sql, e);
        }
    }

    /** Одно значение из первой строки (COUNT, id, MAX). null, если строк нет. */
    public static Object getValue(String sql, Object value) {
        try (var con = connect(); var ps = con.prepareStatement(sql)) {
            ps.setObject(1, value);
            var rs = ps.executeQuery();
            return rs.next() ? rs.getObject(1) : null;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка запроса: " + sql, e);
        }
    }

    /**
     * DELETE/UPDATE/INSERT. Возвращает число затронутых строк.
     */
    public static void delete(String sql, Object value) {
        try (var con = connect(); var ps = con.prepareStatement(sql)) {
            ps.setObject(1, value);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка запроса: " + sql, e);
        }
    }

    /** Одна строка как Map<колонка, значение>. null, если строк нет. */
    public static Map<String, Object> getRow(String sql, Object value) {
        try (var con = connect(); var ps = con.prepareStatement(sql)) {
            ps.setObject(1, value);
            try (var rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                var meta = rs.getMetaData();
                int cols = meta.getColumnCount();
                var row = new LinkedHashMap<String, Object>();
                for (int i = 1; i <= cols; i++) {
                    row.put(meta.getColumnLabel(i), rs.getObject(i));
                }
                return row;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка запроса: " + sql, e);
        }
    }

    /** Все строки как List<Map<колонка, значение>>. Пустой список, если строк нет. */
    public static List<Map<String, Object>> getRows(String sql, Object value) {
        try (var con = connect(); var ps = con.prepareStatement(sql)) {
            ps.setObject(1, value);
            try (var rs = ps.executeQuery()) {
                var meta = rs.getMetaData();
                int cols = meta.getColumnCount();
                var result = new ArrayList<Map<String, Object>>();
                while (rs.next()) {
                    var row = new LinkedHashMap<String, Object>();
                    for (int i = 1; i <= cols; i++) {
                        row.put(meta.getColumnLabel(i), rs.getObject(i));
                    }
                    result.add(row);
                }
                return result;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка запроса: " + sql, e);
        }
    }

    /** Подключение к БД. */
    private static Connection connect() {
        try {
            return DriverManager.getConnection(DbConfig.DB_URL, DbConfig.DB_USER, DbConfig.PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось подключиться к БД", e);
        }
    }
}