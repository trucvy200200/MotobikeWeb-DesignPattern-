/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.MotorbikeStore.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;

/**
 *
 * @author ACER
 */
public class DBConnection {

    private static DBConnection instance = null;
    public static Connection connect;

    private DBConnection() {
        // private constructor to prevent direct instantiation
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://localhost/mydatabase", "root", "Zingzang2");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public ResultSet executeQuery(String sql, Object... parameters) throws SQLException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connect.prepareStatement(sql);
            setParameter(statement, parameters);
            resultSet = statement.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            return null;
        } finally {
            try {
                if (connect != null) {
                    connect.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                return null;
            }
        }
    }

    public void executeUpdate(String sql, Object... parameters) throws SQLException {
        PreparedStatement statement = null;
        try {
            connect.setAutoCommit(false);
            statement = connect.prepareStatement(sql);
            setParameter(statement, parameters);
            statement.executeUpdate();
            connect.commit();
        } catch (SQLException e) {
            if (connect != null) {
                try {
                    connect.rollback();
                } catch (SQLException e1) {

                    e1.printStackTrace();
                }
            }
        } finally {
            try {
                if (connect != null) {
                    connect.close();
                }
                if (statement != null) {
                    statement.close();
                }

            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
    }

    public ResultSet insert(String sql, Object... parameters) throws SQLException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connect.setAutoCommit(false);
            statement = connect.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            setParameter(statement, parameters);
            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            connect.commit();
            return resultSet;
        } catch (SQLException e) {
            if (connect != null) {
                try {
                    connect.rollback();
                    return null;
                } catch (SQLException e1) {

                    return null;
                }
            }
        } finally {
            try {
                if (connect != null) {
                    connect.close();
                }
                if (statement != null) {
                    statement.close();
                }

            } catch (SQLException e2) {
                  return null;
            }
        }
        return null;
    }

    private void setParameter(PreparedStatement statement, Object... parameters) {
        try {
            for (int i = 0; i < parameters.length; i++) {
                Object parameter = parameters[i];
                int index = i + 1;
                if (parameter instanceof Long) {
                    statement.setLong(index, (Long) parameter);
                } else if (parameter instanceof Integer) {
                    statement.setInt(index, (Integer) parameter);
                } else if (parameter instanceof String) {
                    statement.setString(index, (String) parameter);
                } else if (parameter instanceof java.sql.Date) {
                    statement.setDate(index, (java.sql.Date) parameter);
                } else if (parameter instanceof Float) {
                    statement.setFloat(index, (Float) parameter);
                } else if (parameter instanceof Timestamp) {
                    statement.setTimestamp(index, (Timestamp) parameter);
                } else if (parameter == null) {
                    statement.setNull(index, Types.NULL);
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
