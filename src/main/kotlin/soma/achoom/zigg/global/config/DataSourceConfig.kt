//package soma.achoom.zigg.global.config
//
//import com.zaxxer.hikari.HikariDataSource
//import org.springframework.boot.context.properties.ConfigurationProperties
//import org.springframework.boot.jdbc.DataSourceBuilder
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.context.annotation.DependsOn
//import org.springframework.context.annotation.Primary
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories
//import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
//import soma.achoom.zigg.global.DataSourceRouter
//import javax.sql.DataSource
//
//@Configuration
//@EnableJpaRepositories(basePackages = ["soma.achoom.zigg.**.repository"])
//class DataSourceConfig{
//    @Bean
//    @ConfigurationProperties(prefix = "spring.datasource.write")
//    fun writeDataSource(): DataSource {
//        return DataSourceBuilder.create().type(HikariDataSource::class.java).build()
//    }
//    @Bean
//    @ConfigurationProperties(prefix = "spring.datasource.read")
//    fun readDataSource(): DataSource {
//        return DataSourceBuilder.create().type(HikariDataSource::class.java).build()
//    }
//
//    @Bean
//    @DependsOn("writeDataSource", "readDataSource")
//    fun routeDataSource(): DataSource {
//        val dataSourceRouter = DataSourceRouter()
//        dataSourceRouter.setTargetDataSources(
//            mapOf(
//                "write" to writeDataSource(),
//                "read" to readDataSource()
//            )
//        )
//        dataSourceRouter.setDefaultTargetDataSource(writeDataSource())
//        return dataSourceRouter
//    }
//
//    @Bean
//    @Primary
//    @DependsOn("routeDataSource")
//    fun dataSource(): DataSource {
//        return LazyConnectionDataSourceProxy(routeDataSource())
//    }
//}