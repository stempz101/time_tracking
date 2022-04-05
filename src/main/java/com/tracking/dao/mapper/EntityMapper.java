package com.tracking.dao.mapper;

import java.sql.ResultSet;

/**
 * Mapper for setting entity from DB
 * @param <T>
 */
public interface EntityMapper<T> {
    /**
     * Mapping entity from DB
     * @param rs received data from DB
     * @return received entity
     */
    T mapRow(ResultSet rs);
}
