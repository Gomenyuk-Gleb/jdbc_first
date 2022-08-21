import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Pool extends PGSimpleDataSource {
    private Queue<Connection> pool;
    private int poolSizeDef = 15;

    public Pool() {
    }

    public Pool(int poolSize) {
        this.poolSizeDef = poolSize;
    }

    public Pool(String url, String user, String pass) throws SQLException {
        pool = new ConcurrentLinkedDeque<>();
        this.setUrl(url);
        this.setUser(user);
        this.setPassword(pass);
        for (int i = 0; i < poolSizeDef; i++) {
            pool.add(new ConnectionTest(super.getConnection(), this));
        }
    }

    @Override
    public Connection getConnection() {
        return pool.poll();
    }

    public Queue<Connection> getPool() {
        return pool;
    }

    public void setPool(Queue<Connection> pool) {
        this.pool = pool;
    }

    public int getPoolSizeDef() {
        return poolSizeDef;
    }

    public void setPoolSizeDef(int poolSizeDef) {
        this.poolSizeDef = poolSizeDef;
    }
}
