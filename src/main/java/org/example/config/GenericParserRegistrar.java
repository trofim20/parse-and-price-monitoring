package org.example.config;

import lombok.extern.slf4j.Slf4j;
import org.example.base.JsoupBaseParser;
import org.example.base.SeleniumBaseParser;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
@Configuration
public class GenericParserRegistrar implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {
    private Environment environment;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        String url = environment.getRequiredProperty("spring.datasource.url");
        String user = environment.getRequiredProperty("spring.datasource.username");
        String password = environment.getRequiredProperty("spring.datasource.password");

        try(Connection connection = DriverManager.getConnection(url, user, password)){
            ResultSet resultSet = connection.createStatement().executeQuery(
                    "SELECT site_name, technology FROM site_config " +
                            "WHERE use_generic = TRUE AND enabled = TRUE"
            );

            while(resultSet.next()){
                String siteName = resultSet.getString("site_name");
                String technology = resultSet.getString("technology");

                Class<?> parserClass;

                if  ("jsoup".equalsIgnoreCase(technology)){
                    parserClass = JsoupBaseParser.class;
                }else if ("selenium".equalsIgnoreCase(technology)){
                    parserClass = SeleniumBaseParser.class;
                }else {
                    throw new IllegalArgumentException("неизвестная технология парсинга");
                }


                AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder
                        .rootBeanDefinition(parserClass)
                        .addConstructorArgValue(siteName)
                        .setAutowireMode(AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR)
                        .getBeanDefinition();

                String beanName = "parser_" + siteName;
                registry.registerBeanDefinition(beanName, beanDefinition);
                log.info("Зарегистрирован универсальный парсер: {} ({})", siteName, technology);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}

