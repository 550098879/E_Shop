package org.zyx.utils;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

/**
 * MyBatis Plus 逆向工程
 *
 */
public class MyBatisPlusGenerator {

    public static void main(String[] args) {
        //1.创建generator(自动生成)对象
        AutoGenerator autoGenerator = new AutoGenerator();
        //2.数据源
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL);
        dataSourceConfig.setDriverName("com.mysql.cj.jdbc.Driver");
        dataSourceConfig.setUrl("jdbc:mysql://localhost:3306/eshop?serverTimezone=UTC");
        dataSourceConfig.setUsername("root");
        dataSourceConfig.setPassword("550098879");

        //3.装载数据源
        autoGenerator.setDataSource(dataSourceConfig);

        //4.全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setOutputDir(System.getProperty("user.dir")+"/src/main/java");//来到项目路径
        globalConfig.setOpen(false);//关闭自动打开创建的文件
        globalConfig.setAuthor("刈剑丶");//设置作者
        globalConfig.setServiceName("%sService");//去除service接口的 I 开头
        autoGenerator.setGlobalConfig(globalConfig);//添加全局配置

        //5.设置包信息
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent("org.zyx");//设置父包
        //packageConfig.setModuleName("generator");//在父包中添加一个新的包,将生成的文件添加到该包中,避免冲突
        //设置包名,有默认的包名配置,也可以自己修改
        packageConfig.setController("controller");
        packageConfig.setService("service");
        packageConfig.setServiceImpl("serviceImpl");
        packageConfig.setEntity("entity");
        packageConfig.setMapper("mapper");
        autoGenerator.setPackageInfo(packageConfig);//将包信息添加到自动生成中

        //6.配置生成策略
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setEntityLombokModel(true);//自动添加lombok注解
        strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);//下划线转驼峰
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);//下划线转驼峰
        //限制表(只生成自己需要的表),可变参数
        //strategyConfig.setInclude("table1","table2",....);

        autoGenerator.setStrategy(strategyConfig);//将生成策略添加到自动生成中

        //7.执行
        autoGenerator.execute();

        //8.生成后需要在 Application 启动了类中对mapper包进行扫描(MapperScan(...))
    }


}
