package es.apryso.aprysobarcodeserver.app;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableJpaRepositories(basePackages = "es.apryso.aprysobarcodeserver.repository")
@PropertySource("classpath:dataBaseConfig.properties")
public class DataBaseConfig {
	
	@Value( "${dataSource.jndiName:#{null}}" )
	private String jndiName;
	
	@Value( "${dataSource.driverClassName:#{null}}" )
	private String driverClassName;
	
	@Value( "${dataSource.url:#{null}}" )
	private String url;
	
	@Value( "${dataSource.dialect:#{null}}" )
	private String dialect;
	
	@Value( "${dataSource.username:#{null}}" )
	private String username;
	
	@Value( "${dataSource.password:#{null}}" )
	private String password;
	
	@Value( "${dataSource.generateDDL:#{true}}" )
	private Boolean generateDDL;
	
	@Value( "${dataSource.showSQL:#{true}}" )
	private Boolean showSQL;
	
	
	@Bean
	public DataSource getDataSource() {
		
		if (jndiName != null) {
			JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
			dsLookup.setResourceRef(true);
			return dsLookup.getDataSource(jndiName);
		} else {
			
			HikariConfig config = new HikariConfig();
			config.setJdbcUrl(url);
			config.setUsername(username);
			config.setPassword(password);
			config.setDriverClassName(driverClassName);
			return new HikariDataSource(config);
		}
	}
	
	/*
	@Bean
	public JndiObjectFactoryBean getDataSourceJndi() {
		
		JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
		if (jndiName != null) {
			jndiObjectFactoryBean.setJndiName(jndiName);
			jndiObjectFactoryBean.setResourceRef(true);
			jndiObjectFactoryBean.setProxyInterface(javax.sql.DataSource.class);
		}
		return jndiObjectFactoryBean;
	}
	
	@Bean
	public DataSource getDataSourceLocal() {
		
		if (url != null) {
			HikariConfig config = new HikariConfig();
			config.setJdbcUrl(url);
			config.setUsername(username);
			config.setPassword(password);
			config.setDriverClassName(driverClassName);
	
			return new HikariDataSource(config);
		} else {
			return null;
		}
	}
	*/
	
	@Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setShowSql(showSQL);
        hibernateJpaVendorAdapter.setGenerateDdl(generateDDL);
        return hibernateJpaVendorAdapter;
    }
	
	@Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, JpaVendorAdapter jpaVendorAdapter) {
        LocalContainerEntityManagerFactoryBean lef = new LocalContainerEntityManagerFactoryBean();
        lef.setDataSource(dataSource);
        lef.setJpaVendorAdapter(jpaVendorAdapter);
        lef.setPackagesToScan("es.apryso.aprysobarcodeserver");
        return lef;
    }
	
	@Bean(name="transactionManager")
	public PlatformTransactionManager dbTransactionManager(LocalContainerEntityManagerFactoryBean lef) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(lef.getObject());
        return transactionManager;
    }

}
