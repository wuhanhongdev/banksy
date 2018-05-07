package com.power.banksy.config;

import com.github.pagehelper.PageHelper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author wuhanhong
 * @date 2018 - 05 - 07
 */
@Configuration
public class DatabaseConfig {
    @Value("config.database.url")
    private String url;
    @Value("config.database.driver")
    private String driver;
    @Value("config.database.username")
    private String username;
    @Value("config.database.password")
    private String password;
    @Value("config.database.connectionTimeout")
    private int connectionTimeout;
    @Value("config.database.idleTimeout")
    private int idleTimeout;
    @Value("config.database.maxLifetime")
    private int maxLifetime;
    @Value("config.database.maximumPoolSize")
    private int maximumPoolSize;
    @Value("config.database.minimumIdle")
    private int minimumIdle;

    @Bean
    public DataSource dataSource() {
        Properties props = new Properties();
        props.put("driverClassName", driver);
        props.put("jdbcUrl", url);
        props.put("username", username);
        props.put("password", password);
        props.put("connectionTimeout", connectionTimeout);
        props.put("idleTimeout", idleTimeout);
        props.put("maxLifetime", maxLifetime);
        props.put("maximumPoolSize", maximumPoolSize);
        props.put("minimumIdle", minimumIdle);
        HikariConfig config = new HikariConfig(props) {};
        HikariDataSource mysqlDS = new HikariDataSource(config);
        return mysqlDS;
    }

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean() {

        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setTypeAliasesPackage("com.power.assistant.model");

        bean.setDataSource(dataSource());

        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setLogImpl(StdOutImpl.class);

        bean.setConfiguration(configuration);
        //分页插件设置
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("reasonable", "true");
        properties.setProperty("supportMethodsArguments", "true");
        properties.setProperty("returnPageInfo", "check");
        properties.setProperty("params", "count=countSql");
        pageHelper.setProperties(properties);

        //添加分页插件
        bean.setPlugins(new Interceptor[]{pageHelper});


        try {
            Resource[] resources = resolver.getResources("classpath*:mybatis/mappers/*.xml");
            bean.setMapperLocations(resources);
            return bean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
