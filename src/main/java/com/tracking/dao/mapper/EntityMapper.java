package com.tracking.dao.mapper;

import java.sql.ResultSet;

public interface EntityMapper<T> {
    T mapRow(ResultSet rs);
}
