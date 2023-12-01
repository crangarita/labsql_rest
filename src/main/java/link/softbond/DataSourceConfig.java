package link.softbond;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.SQLException;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    
    private String driverClassName = "com.mysql.cj.jdbc.Driver";
    private String url = "jdbc:mysql://roundhouse.proxy.rlwy.net:35887/railway?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private String username = "root";
    private String password = "1eec1dbBG4eD62eEEcGADC3aEAG1fCEH";
    
    private DataSource dataSource;

    
    @Bean
    public DataSource dataSource() {
        if (dataSource == null) {       	
            dataSource = createDataSource();
        }
        return dataSource;
    }
    
    public void setDataSourceProperties(String driverClassName, String url, String username, String password) {
        this.driverClassName = driverClassName;
        this.url = url;
        this.username = username;
        this.password = password;
    }
    
    
    private DataSource createDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }
    
    public void updateDataSource() {
        destroyDataSource();
        dataSource = createDataSource();
    }

    private void destroyDataSource() {
    	try {
			dataSource.getConnection().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	public DataSource getDataSource() {
		return dataSource;
	}
    
}
