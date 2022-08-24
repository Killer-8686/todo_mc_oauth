package ru.javabegin.micro.planner.todo.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import ru.javabegin.micro.planner.utils.converter.KCRoleConverter;

@Configuration
@EnableWebSecurity // включаем механизм защиты адресов, которые настраиваем в SecurityFilterChain
@EnableGlobalMethodSecurity(prePostEnabled = true)// включение механизма защиты методов по ролям
public class SpringSecurityConfig {

    // создается спец. бин, который отвечает за настройки запросов по http (метод вызывается автоматически) Spring контейнером
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        // Комвертер для настройки spring security

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        // Подключаем конветер ролей
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KCRoleConverter());

        // Сетевые настройки
        http.authorizeRequests()
                .antMatchers("/login").permitAll()// анонимный пользователь сможет выполнять запросы только по этим URI
                .anyRequest().authenticated()// остальной API будет доступен только аутентифицированным пользователям

                .and()// добавляем новые настройки, не связанные с предыдущими


                .oauth2ResourceServer()// добавляем конвертер ролей из JWT в Authority (Role)
                .jwt()
                .jwtAuthenticationConverter(jwtAuthenticationConverter);

        return http.build();
    }
}
