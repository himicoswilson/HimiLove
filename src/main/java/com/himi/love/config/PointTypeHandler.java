package com.himi.love.config;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.WKBReader;
import org.locationtech.jts.io.WKBWriter;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(Point.class)
public class PointTypeHandler extends BaseTypeHandler<Point> {
    private final WKBReader reader = new WKBReader();
    private final WKBWriter writer = new WKBWriter();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Point parameter, JdbcType jdbcType) throws SQLException {
        try {
            ps.setBytes(i, writer.write(parameter));
        } catch (Exception e) {
            throw new SQLException("Error converting Point to WKB", e);
        }
    }

    @Override
    public Point getNullableResult(ResultSet rs, String columnName) throws SQLException {
        byte[] bytes = rs.getBytes(columnName);
        if (bytes == null) return null;
        try {
            return (Point) reader.read(bytes);
        } catch (Exception e) {
            throw new SQLException("Error parsing Point WKB", e);
        }
    }

    @Override
    public Point getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        byte[] bytes = rs.getBytes(columnIndex);
        if (bytes == null) return null;
        try {
            return (Point) reader.read(bytes);
        } catch (Exception e) {
            throw new SQLException("Error parsing Point WKB", e);
        }
    }

    @Override
    public Point getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        byte[] bytes = cs.getBytes(columnIndex);
        if (bytes == null) return null;
        try {
            return (Point) reader.read(bytes);
        } catch (Exception e) {
            throw new SQLException("Error parsing Point WKB", e);
        }
    }
} 