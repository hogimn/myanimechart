package com.hogimn.myanimechart.core.common.p6spy;

import com.github.vertical_blank.sqlformatter.SqlFormatter;
import com.p6spy.engine.common.PreparedStatementInformation;
import com.p6spy.engine.common.ResultSetInformation;
import com.p6spy.engine.event.JdbcEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class CustomJdbcEventListener extends JdbcEventListener {

    private static final Logger log = LoggerFactory.getLogger(CustomJdbcEventListener.class);
    private static final String BASE_PACKAGE = "com.hogimn.myanimechart";

    @Override
    public void onAfterExecuteQuery(PreparedStatementInformation statementInformation, long timeElapsedNanos, SQLException e) {
        QueryContext.reset();
        QueryContext.setElapsed(timeElapsedNanos);
    }

    @Override
    public void onAfterResultSetNext(ResultSetInformation resultSetInformation, long timeElapsedNanos, boolean hasNext, SQLException e) {
        if (hasNext) {
            QueryContext.inc();
        }
    }

    @Override
    public void onAfterResultSetClose(ResultSetInformation resultSetInformation, SQLException e) {
        int rows = QueryContext.get();
        long ms = QueryContext.getElapsed() / 1_000_000;

        log.info("""
                
                {}
                -- {} rows selected in {} ms
                
                {}
                """,
                getStackTrace(),
                rows,
                ms,
                SqlFormatter.format(resultSetInformation.getStatementInformation().getSql())
        );

        QueryContext.clear();
    }

    @Override
    public void onAfterExecuteUpdate(PreparedStatementInformation statementInformation, long timeElapsedNanos, int rowCount, SQLException e) {
        long ms = timeElapsedNanos / 1_000_000;

        log.info("""
                
                {}
                -- {} rows affected in {} ms
                
                {}
                """,
                getStackTrace(),
                rowCount,
                ms,
                SqlFormatter.format(statementInformation.getSql())
        );
    }

    private String getStackTrace() {
        return Arrays.stream(Thread.currentThread().getStackTrace())
                .filter(e -> e.getClassName().startsWith(BASE_PACKAGE))
                .filter(e -> !isProxyClass(e.getClassName()))
                .filter(e -> !e.getClassName().contains("p6spy"))
                .map(e -> "-- at " + e)
                .collect(Collectors.joining("\n"));
    }

    private boolean isProxyClass(String className) {
        return className.contains("$$EnhancerBySpringCGLIB$$")
                || className.contains("$$SpringCGLIB$$")
                || className.startsWith("org.springframework.cglib")
                || className.startsWith("org.springframework.aop")
                || className.startsWith("jdk.proxy");
    }
}