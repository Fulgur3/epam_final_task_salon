package com.pool;

import java.sql.SQLException;

public class PoolException extends SQLException {
    public PoolException(String reason, String SQLState, int vendorCode) {
        super(reason, SQLState, vendorCode);
    }

    public PoolException(String reason, String SQLState) {
        super(reason, SQLState);
    }

    public PoolException(String reason) {
        super(reason);
    }

    public PoolException() {
    }

    public PoolException(Throwable cause) {
        super(cause);
    }

    public PoolException(String reason, Throwable cause) {
        super(reason, cause);
    }

    public PoolException(String reason, String sqlState, Throwable cause) {
        super(reason, sqlState, cause);
    }

    public PoolException(String reason, String sqlState, int vendorCode, Throwable cause) {
        super(reason, sqlState, vendorCode, cause);
    }
}
