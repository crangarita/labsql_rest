package link.softbond.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import link.softbond.DataSourceConfig;

@Service
public class DBService {
    
    private final DataSourceConfig dataSourceConfig;
    @Value("${db.url}")
    private  String host;
    @Value("${db.username}")
    private  String username;
    @Value("${db.password}")
    private  String password;

    @Autowired
    public DBService(DataSourceConfig dataSourceConfig) {
        this.dataSourceConfig = dataSourceConfig;
    }

    public void cambiar(String driverClassName, String url, String username, String password) {
        dataSourceConfig.setDataSourceProperties(driverClassName, url, username, password);
    }
    
    public void seleccionar(String database) {
    	String driverClassName = "com.mysql.cj.jdbc.Driver";
    	//
    	//jdbc:mysql://localhost:3306/
    	String url = host+database+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    	//String username = "root";
    	//String password = "Lab2020.";
        dataSourceConfig.setDataSourceProperties(driverClassName, url, username, password);
        dataSourceConfig.updateDataSource();
    }
    
    public DataSource getDataSourceCurrent() {
    	return dataSourceConfig.getDataSource();
    }
    
    /**
     * Restaura una base de datos desde un archivo SQL proporcionado como MultipartFile y crea la base de datos con el nombre dado.
     * @param file Archivo SQL.
     * @param dbName Nombre de la nueva base de datos.
     * @throws SQLException Si ocurre un error en la ejecución SQL.
     * @throws IOException Si ocurre un error al leer el archivo.
     */
    public void restoreDatabaseFromBackup(MultipartFile file, String dbName) throws SQLException, IOException {
        DataSource dataSource = getDataSourceCurrent();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

            StringBuilder sql = new StringBuilder();
            String line;

            // Crear la base de datos con el nombre proporcionado
            statement.execute("CREATE DATABASE IF NOT EXISTS " + dbName);
            statement.execute("USE " + dbName);

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                // Ignorar las declaraciones CREATE DATABASE o USE en el archivo y usar el nombre proporcionado
                if (line.toUpperCase().startsWith("CREATE DATABASE") || line.toUpperCase().startsWith("USE")) {
                    continue; // Saltar estas líneas
                }

                if (!line.isEmpty() && !line.startsWith("--")) { // Ignorar líneas vacías o comentarios
                    sql.append(line);
                    if (line.endsWith(";")) { // Ejecutar cuando se complete una sentencia
                        statement.execute(sql.toString());
                        sql.setLength(0); // Resetear el StringBuilder
                    }
                }
            }
        }
    }
    
    
}
