import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    private final static String DB_NAME = "jdbc:postgresql://localhost:5432/postgres";
    private final static String USER = "postgres";
    private final static String PASS = "postgres";

    public static void main(String[] args) throws SQLException {
        Pool dataSource = initializePooledDataSource();
        long start = System.nanoTime();
        double total = 0.0;
        for (int i = 1; i < 20; i++) {
            try (Connection connection = dataSource.getConnection()) {
                connection.setAutoCommit(false);
                try (Statement statement = connection.createStatement()) {
                    ResultSet rs = statement.executeQuery("select random() from products");
                    rs.next();
                    total += rs.getDouble(1);
                }
                connection.rollback();
            }
        }
        System.out.println((System.nanoTime() - start) / 1000000 + " ms");
    }


    private static DataSource initializeDataSource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL(DB_NAME);
        dataSource.setUser(PASS);
        dataSource.setPassword(USER);
        return dataSource;
    }

    private static Pool initializePooledDataSource() throws SQLException {
        return new Pool(DB_NAME, PASS, USER);
    }
}
