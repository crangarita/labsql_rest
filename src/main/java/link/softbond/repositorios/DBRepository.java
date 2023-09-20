package link.softbond.repositorios;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import link.softbond.service.DBService;

@Repository
public class DBRepository {
    
    private final JdbcTemplate jdbcTemplate;
    
    @Autowired
    public DBService dBService;

    @Autowired
    public DBRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate.setDataSource(dataSource);
    }

    public List<Map<String, Object>> ejecutarConsulta(String database, String sql) {
    	
    	dBService.seleccionar(database);
    	this.jdbcTemplate.setDataSource(dBService.getDataSourceCurrent());
    	
        return this.jdbcTemplate.queryForList(sql);
    }
}
