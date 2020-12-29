package com.example.saul.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.example.saul.pojo.DruidDataSourceProperty;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: James
 * @Date: 2019/8/29 11:15
 * @Description:
 */
@MapperScan("com.example.saul.mapper")
@ConditionalOnClass(com.alibaba.druid.pool.DruidDataSource.class)
@ConditionalOnProperty(name = "spring.datasource.type", havingValue = "com.alibaba.druid.pool.DruidDataSource", matchIfMissing = true)
public class JdbcConfiguration implements TransactionManagementConfigurer {
    @Autowired
    private DruidDataSourceProperty druidDataSourceProperty;


    private Logger logger = LoggerFactory.getLogger(getClass());

    @Bean
    public DataSource dataSource() {
        DruidDataSource datasource = new DruidDataSource();

        datasource.setUrl(druidDataSourceProperty.getUrl());
        datasource.setUsername(druidDataSourceProperty.getUsername());
        datasource.setPassword(druidDataSourceProperty.getPassword());
        datasource.setDriverClassName(druidDataSourceProperty.getDriverClassName());

        datasource.setInitialSize(druidDataSourceProperty.getInitialSize());
        datasource.setMinIdle(druidDataSourceProperty.getMinIdle());
        datasource.setMaxActive(druidDataSourceProperty.getMaxActive());

        datasource.setMaxWait(druidDataSourceProperty.getMaxWait());

        datasource.setTimeBetweenEvictionRunsMillis(druidDataSourceProperty.getTimeBetweenEvictionRunsMillis());

        datasource.setMinEvictableIdleTimeMillis(druidDataSourceProperty.getMinEvictableIdleTimeMillis());
        datasource.setValidationQuery(druidDataSourceProperty.getValidationQuery());
        datasource.setTestWhileIdle(druidDataSourceProperty.isTestWhileIdle());
        datasource.setTestOnBorrow(druidDataSourceProperty.isTestOnBorrow());
        datasource.setTestOnReturn(druidDataSourceProperty.isTestOnReturn());
        datasource.setPoolPreparedStatements(druidDataSourceProperty.isPoolPreparedStatements());
        datasource.setMaxPoolPreparedStatementPerConnectionSize(druidDataSourceProperty.getMaxPoolPreparedStatementPerConnectionSize());
        datasource.setUseGlobalDataSourceStat(druidDataSourceProperty.isUseGlobalDataSourceStat());
        try {
            datasource.setFilters(druidDataSourceProperty.getFilters());
        } catch (SQLException e) {
            logger.error("druid configuration initialization filter", e);
        }
        datasource.setConnectionProperties(druidDataSourceProperty.getConnectionProperties());

        return datasource;
    }

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean(@Qualifier("dataSource") DataSource dataSource) {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        // 添加XML目录
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            bean.setMapperLocations(resolver.getResources("classpath:mapper/*.xml"));
            SqlSessionFactory sq = bean.getObject();
            return sq;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Bean(name = "sqlSessionTemplate")
    @Autowired
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        SqlSessionTemplate sql = new SqlSessionTemplate(sqlSessionFactory);
        return sql;
    }

    @Bean(name = "txManager")
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public TransactionTemplate transactionTemplate(@Qualifier("txManager") PlatformTransactionManager txManager) {
        return new TransactionTemplate(txManager);
    }

    @Bean
    public ServletRegistrationBean druidServlet() {
        logger.info("init Druid Servlet Configuration ");
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean();
        servletRegistrationBean.setServlet(new StatViewServlet());
        servletRegistrationBean.addUrlMappings("/druid/*");
        Map<String, String> initParameters = new HashMap<String, String>();
        initParameters.put("loginUsername", "admin");
        initParameters.put("loginPassword", "admin");
        initParameters.put("resetEnable", "false");
        initParameters.put("allow", "");
        initParameters.put("deny", "");
        servletRegistrationBean.setInitParameters(initParameters);
        return servletRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }
}
