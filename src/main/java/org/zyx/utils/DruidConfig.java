package org.zyx.utils;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by SunShine on 2020/4/9.
 */
@Configuration
public class DruidConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druid(){
        DruidDataSource druidDataSource = new DruidDataSource();
        return druidDataSource;
    }
    //1.配置servlet
    @Bean
    public ServletRegistrationBean statViewServlet(){
        ServletRegistrationBean bean =
                new ServletRegistrationBean(new StatViewServlet(), "/druid/*");//这里是访问druid的地址
        HashMap<Object, Object> hashMap = new HashMap<>();
        //druid数据监控的用户和密码

//        http://127.0.0.1:8080/druid/index.html 访问路径
//        访问的账号和密码
        hashMap.put("loginUsername","admin");
        hashMap.put("loginPassword","123456");
        hashMap.put("allow","");//允许所有
        bean.setInitParameters(hashMap);
        return bean;
    }
    //配置过滤器，配置到达需要监控哪些，不需要监控哪些
    @Bean
    public FilterRegistrationBean webStatFilter(){
        FilterRegistrationBean bean = new FilterRegistrationBean(new WebStatFilter());
        HashMap<Object, Object> hashMap = new HashMap<>();
        //这里排除一些不需要监控的资源
        hashMap.put("exclusions","*.js,*.css,/druid/*");
        bean.setInitParameters(hashMap);
        bean.setUrlPatterns(Arrays.asList("/*"));
        return bean;
    }

}

