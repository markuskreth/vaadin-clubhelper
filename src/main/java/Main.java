import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import com.mysql.cj.jdbc.MysqlDataSource;

public class Main {

	public static void main(String[] args) throws SQLException {
		MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setUrl(
				"jdbc:mysql://localhost:3306/clubhelper?useSSL=FALSE&useUnicode=yes&characterEncoding=utf8&serverTimezone=UTC");
		dataSource.setUser("root");
		dataSource.setPassword("07!73");
		try (Connection conn = dataSource.getConnection()) {
			DatabaseMetaData meta = conn.getMetaData();
			System.out.println(meta);
		}
	}

}
