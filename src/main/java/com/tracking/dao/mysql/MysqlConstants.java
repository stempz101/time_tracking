package com.tracking.dao.mysql;

public abstract class MysqlConstants {
    private MysqlConstants() {

    }

    // REPLACE VALUES
    public static final String ORDER_BY = "set_order_by";
    public static final String IN = "set_in";

    // FIELDS
    public static final String COL_ID = "id";
    public static final String COL_ACTIVITY_ID = "activity_id";
    public static final String COL_CATEGORY_ID = "category_id";
    public static final String COL_USER_ID = "user_id";
    public static final String COL_CREATOR_ID = "creator_id";

    public static final String COL_NAME = "name";
    public static final String COL_NAME_EN = "name_en";
    public static final String COL_NAME_UA = "name_ua";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_IMAGE = "image";
    public static final String COL_TEXT = "text";
    public static final String COL_LAST_NAME = "last_name";
    public static final String COL_FIRST_NAME = "first_name";
    public static final String COL_EMAIL = "email";
    public static final String COL_PASSWORD = "password";
    public static final String COL_IS_ADMIN = "is_admin";
    public static final String COL_IS_BLOCKED = "is_blocked";
    public static final String COL_COUNT = "count";
    public static final String COL_START_TIME = "start_time";
    public static final String COL_STOP_TIME = "stop_time";
    public static final String COL_SPENT_TIME = "spent_time";
    public static final String COL_STATUS = "status";
    public static final String COL_PEOPLE_COUNT = "people_count";
    public static final String COL_ACTIVITY_COUNT = "activity_count";
    public static final String COL_CREATE_TIME = "create_time";

    // QUERIES
    public static final String INSERT_USER = "INSERT INTO users (last_name, first_name, email, password) " +
            "VALUES (?, ?, ?, ?)";
    public static final String INSERT_ACTIVITY = "INSERT INTO activities (name, description, image, creator_id, status) " +
            "VALUES (?, ?, ?, ?, ?)";
    public static final String INSERT_CATEGORY = "INSERT INTO categories (name_en, name_ua) " +
            "VALUES (?, ?)";
    public static final String INSERT_ACTIVITY_CATEGORIES = "INSERT INTO activities_categories " +
            "VALUES (?, ?)";

    public static final String SELECT_USER = "SELECT * FROM users WHERE id = ?";
    public static final String SELECT_USER_BY_EMAIL = "SELECT * FROM users WHERE email = ?";
    public static final String SELECT_ALL_USERS = "SELECT id, last_name, first_name, image, " +
            "activity_count, spent_time, is_admin, is_blocked FROM users " +
            "ORDER BY is_admin DESC, last_name " +
            "LIMIT ?, ?";
    public static final String SELECT_ALL_USERS_ORDER = "SELECT id, last_name, first_name, image, " +
            "activity_count, spent_time, is_admin, is_blocked FROM users " +
            "ORDER BY " + ORDER_BY + " " +
            "LIMIT ?, ?";
    public static final String SELECT_ALL_USERS_WHERE_NAME = "SELECT id, last_name, first_name, image, " +
            "activity_count, spent_time, is_admin, is_blocked FROM users " +
            "WHERE last_name LIKE ? AND first_name LIKE ? " +
            "ORDER BY is_admin DESC, last_name " +
            "LIMIT ?, ?";
    public static final String SELECT_ALL_USERS_WHERE_NAME_ORDER = "SELECT id, last_name, first_name, image, " +
            "activity_count, spent_time, is_admin, is_blocked FROM users " +
            "WHERE last_name LIKE ? AND first_name LIKE ? " +
            "ORDER BY " + ORDER_BY + " " +
            "LIMIT ?, ?";
    public static final String GET_USER_COUNT = "SELECT COUNT(*) count FROM users";
    public static final String GET_USER_COUNT_WHERE_NAME = "SELECT COUNT(*) count FROM users " +
            "WHERE last_name LIKE ? AND first_name LIKE ?";
    public static final String SET_USER_ADMIN = "UPDATE users SET is_admin = ? WHERE id = ?";
    public static final String SET_USER_BLOCK = "UPDATE users SET is_blocked = ? WHERE id = ?";
    public static final String UPDATE_USER_ACTIVITY_COUNT = "UPDATE users SET activity_count = (SELECT COUNT(*) " +
            "FROM users_activities WHERE user_id = ?)" +
            "WHERE id = ?";
    public static final String UPDATE_USER_SPENT_TIME = "UPDATE users SET spent_time = spent_time + (" +
            "SELECT TIMESTAMPDIFF(SECOND, start_time, ?) FROM users_activities " +
            "WHERE activity_id = ? AND user_id = ?) " +
            "WHERE id = ?";

    public static final String SELECT_USER_WHERE_ACTIVITY = "SELECT * FROM users " +
            "JOIN users_activities ON id = user_id " +
            "WHERE user_id = ? AND activity_id = ?";
    public static final String SELECT_USERS_WHERE_ACTIVITY = "SELECT * FROM users " +
            "JOIN users_activities ON id = user_id " +
            "WHERE activity_id = ? AND is_blocked IS NULL " +
            "ORDER BY is_admin DESC, status, last_name " +
            "LIMIT ?, ?";
    public static final String SELECT_USERS_WHERE_NOT_IN_ACTIVITY = "SELECT id, last_name, first_name, is_admin FROM users " +
            "WHERE is_blocked IS NULL AND id NOT IN (SELECT id FROM users " +
            "JOIN users_activities ON id = user_id " +
            "WHERE activity_id = ?) " +
            "ORDER BY is_admin DESC, last_name";

    public static final String START_TIME = "UPDATE users_activities SET start_time = ?, status = 'STARTED' " +
            "WHERE activity_id = ? AND user_id = ?";
    public static final String STOP_TIME = "UPDATE users_activities SET stop_time = ?, " +
            "spent_time = spent_time + TIMESTAMPDIFF(SECOND, start_time, ?), status = 'STOPPED' " +
            "WHERE activity_id = ? AND user_id = ?";

    public static final String SELECT_ACTIVITY = "SELECT * FROM activities WHERE id = ?";
    public static final String SELECT_ALL_ACTIVITIES = "SELECT * FROM activities WHERE people_count >= ? AND people_count <= ? " +
            "ORDER BY people_count DESC, create_time DESC, name LIMIT ?, ?";
    public static final String SELECT_ALL_ACTIVITIES_ORDER = "SELECT * FROM activities WHERE people_count >= ? AND people_count <= ? " +
            "ORDER BY " + ORDER_BY + " LIMIT ?, ?";
    public static final String SELECT_ACTIVITIES_LIKE = "SELECT * FROM activities WHERE name LIKE ? " +
            "AND people_count >= ? AND people_count <= ? " +
            "ORDER BY people_count DESC, create_time DESC, name LIMIT ?, ?";
    public static final String SELECT_ACTIVITIES_LIKE_ORDER = "SELECT * FROM activities WHERE name LIKE ? " +
            "AND people_count >= ? AND people_count <= ? " +
            "ORDER BY " + ORDER_BY + " LIMIT ?, ?";
    public static final String SELECT_ACTIVITY_CATEGORIES = "SELECT * FROM activities_categories WHERE activity_id = ?";
    public static final String SELECT_ACTIVITIES_WHERE_CATEGORY = "SELECT * FROM activities " +
            "JOIN activities_categories ON id = activity_id " +
            "WHERE category_id IN (" + IN + ") AND people_count >= ? AND people_count <= ? " +
            "GROUP BY id " +
            "HAVING COUNT(*) = ? " +
            "ORDER BY people_count DESC, create_time DESC, name " +
            "LIMIT ?, ?";
    public static final String SELECT_ACTIVITIES_WHERE_CATEGORY_ORDER = "SELECT * FROM activities " +
            "JOIN activities_categories ON id = activity_id " +
            "WHERE category_id IN (" + IN + ") AND people_count >= ? AND people_count <= ? " +
            "GROUP BY id " +
            "HAVING COUNT(*) = ? " +
            "ORDER BY " + ORDER_BY + " " +
            "LIMIT ?, ?";
    public static final String SELECT_ACTIVITIES_WHERE_CATEGORY_IS_NULL = "SELECT * FROM activities " +
            "LEFT JOIN activities_categories ON id = activity_id " +
            "WHERE category_id IS NULL AND people_count >= ? AND people_count <= ? " +
            "GROUP BY id " +
            "ORDER BY people_count DESC, create_time DESC, name " +
            "LIMIT ?, ?";
    public static final String SELECT_ACTIVITIES_WHERE_CATEGORY_IS_NULL_ORDER = "SELECT * FROM activities " +
            "LEFT JOIN activities_categories ON id = activity_id " +
            "WHERE category_id IS NULL AND people_count >= ? AND people_count <= ? " +
            "GROUP BY id " +
            "ORDER BY " + ORDER_BY + " " +
            "LIMIT ?, ?";
    public static final String SELECT_ACTIVITIES_LIKE_AND_WHERE_CATEGORY = "SELECT * FROM activities " +
            "JOIN activities_categories ON id = activity_id " +
            "WHERE name LIKE ? AND category_id IN (" + IN + ") AND people_count >= ? AND people_count <= ? " +
            "GROUP BY id " +
            "HAVING COUNT(*) = ? " +
            "ORDER BY people_count DESC, create_time DESC, name " +
            "LIMIT ?, ?";
    public static final String SELECT_ACTIVITIES_LIKE_AND_WHERE_CATEGORY_ORDER = "SELECT * FROM activities " +
            "JOIN activities_categories ON id = activity_id " +
            "WHERE name LIKE ? AND category_id IN (" + IN + ") AND people_count >= ? AND people_count <= ? " +
            "GROUP BY id " +
            "HAVING COUNT(*) = ? " +
            "ORDER BY " + ORDER_BY + " " +
            "LIMIT ?, ?";
    public static final String SELECT_ACTIVITIES_LIKE_AND_WHERE_CATEGORY_IS_NULL = "SELECT * FROM activities " +
            "LEFT JOIN activities_categories ON id = activity_id " +
            "WHERE name LIKE ? AND category_id IS NULL AND people_count >= ? AND people_count <= ? " +
            "GROUP BY id " +
            "HAVING COUNT(*) = 1 " +
            "ORDER BY people_count DESC, create_time DESC, name " +
            "LIMIT ?, ?";
    public static final String SELECT_ACTIVITIES_LIKE_AND_WHERE_CATEGORY_IS_NULL_ORDER = "SELECT * FROM activities " +
            "LEFT JOIN activities_categories ON id = activity_id " +
            "WHERE name LIKE ? AND category_id IS NULL AND people_count >= ? AND people_count <= ? " +
            "GROUP BY id " +
            "HAVING COUNT(*) = 1 " +
            "ORDER BY " + ORDER_BY + " " +
            "LIMIT ?, ?";
    public static final String GET_ACTIVITY_COUNT = "SELECT COUNT(*) count FROM activities WHERE people_count >= ? AND people_count <= ?";
    public static final String GET_ACTIVITIES_LIKE_COUNT = "SELECT COUNT(*) count FROM activities " +
            "WHERE name LIKE ? AND people_count >= ? AND people_count <= ?";
    public static final String GET_ACTIVITIES_WHERE_CATEGORY_COUNT = "SELECT COUNT(*) count FROM " +
            "(SELECT COUNT(*) FROM activities " +
            "JOIN activities_categories ON id = activity_id " +
            "WHERE category_id IN (" + IN + ") AND people_count >= ? AND people_count <= ? " +
            "GROUP BY id " +
            "HAVING COUNT(*) = ?) a";
    public static final String GET_ACTIVITIES_WHERE_CATEGORY_IS_NULL_COUNT = "SELECT COUNT(*) count FROM " +
            "(SELECT COUNT(*) FROM activities " +
            "LEFT JOIN activities_categories ON id = activity_id " +
            "WHERE category_id IS NULL AND people_count >= ? AND people_count <= ? " +
            "GROUP BY id) a";
    public static final String GET_ACTIVITIES_LIKE_AND_WHERE_CATEGORY_COUNT = "SELECT COUNT(*) count FROM " +
            "(SELECT * FROM activities " +
            "JOIN activities_categories ON id = activity_id " +
            "WHERE name LIKE ? AND category_id IN (" + IN + ") AND people_count >= ? AND people_count <= ? " +
            "GROUP BY id " +
            "HAVING COUNT(*) = ?) a";
    public static final String GET_ACTIVITIES_LIKE_AND_WHERE_CATEGORY_IS_NULL_COUNT = "SELECT COUNT(*) count FROM " +
            "(SELECT * FROM activities " +
            "LEFT JOIN activities_categories ON id = activity_id " +
            "WHERE name LIKE ? AND category_id IS NULL AND people_count >= ? AND people_count <= ? " +
            "GROUP BY id " +
            "HAVING COUNT(*) = 1) a";
    public static final String GET_ACTIVITY_PEOPLE_COUNT = "SELECT people_count FROM activities WHERE id = ?";
    public static final String GET_ACTIVITIES_MAX_PEOPLE_COUNT = "SELECT MAX(people_count) people_count FROM activities";
    public static final String GET_ACTIVITY_CREATOR = "SELECT users.id id, users.last_name, users.first_name, " +
            "users.image, users.is_admin, users.is_blocked FROM activities " +
            "JOIN users ON activities.creator_id = users.id " +
            "WHERE activities.id = ?";
    public static final String UPDATE_ACTIVITY = "UPDATE activities SET name = ?, description = ?, image = ? WHERE id = ?";
    public static final String UPDATE_ACTIVITY_PEOPLE_COUNT = "UPDATE activities " +
            "SET people_count = (SELECT COUNT(*) " +
            "FROM users_activities " +
            "WHERE activity_id = ?) " +
            "WHERE id = ?";
    public static final String DELETE_ACTIVITY = "DELETE FROM activities WHERE id = ?";
    public static final String DELETE_ACTIVITY_CATEGORIES = "DELETE FROM activities_categories WHERE activity_id = ?";

    public static final String INSERT_USER_ACTIVITY = "INSERT INTO users_activities (user_id, activity_id) " +
            "VALUES (?, ?)";
    public static final String DELETE_USER_ACTIVITY = "DELETE FROM users_activities WHERE user_id = ? AND activity_id = ?";

    public static final String SELECT_ALL_CATEGORIES_EN = "SELECT * FROM categories ORDER BY name_en";
    public static final String SELECT_ALL_CATEGORIES_UA = "SELECT * FROM categories ORDER BY name_ua";
    public static final String SELECT_ALL_CATEGORIES_EN_LIMIT = "SELECT * FROM categories ORDER BY name_en LIMIT ?, ?";
    public static final String SELECT_ALL_CATEGORIES_UA_LIMIT = "SELECT * FROM categories ORDER BY name_ua LIMIT ?, ?";
    public static final String SELECT_ALL_CATEGORIES_BY_ID_EN = "SELECT * FROM categories WHERE id IN (" + IN + ") ORDER BY name_en";
    public static final String SELECT_ALL_CATEGORIES_BY_ID_UA = "SELECT * FROM categories WHERE id IN (" + IN + ") ORDER BY name_ua";
    public static final String SELECT_CATEGORY = "SELECT * FROM categories WHERE id = ?";
    public static final String SELECT_CATEGORY_BY_NAME_EN = "SELECT * FROM categories WHERE name_en = ?";
    public static final String SELECT_CATEGORY_BY_NAME_UA = "SELECT * FROM categories WHERE name_ua = ?";
    public static final String SELECT_CATEGORY_BY_NAME_EN_OTHER = "SELECT * FROM categories WHERE name_en = ? AND id <> ?";
    public static final String SELECT_CATEGORY_BY_NAME_UA_OTHER = "SELECT * FROM categories WHERE name_ua = ? AND id <> ?";
    public static final String GET_CATEGORIES_COUNT = "SELECT COUNT(*) count FROM categories";
    public static final String UPDATE_CATEGORY = "UPDATE categories SET name_en = ?, name_ua = ? WHERE id = ?";
    public static final String DELETE_CATEGORY = "DELETE FROM categories WHERE id = ?";
}
