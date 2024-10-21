package com.acabouomony.user.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuração de Segurança para o User Service.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${aws.cognito.clientId}")
    private String clientId;

    @Value("${aws.cognito.userPoolId}")
    private String userPoolId;

    @Value("${aws.region}")
    private String region;

    /**
     * Define a cadeia de filtros de segurança.
     *
     * @param http objeto HttpSecurity para configuração.
     * @return SecurityFilterChain configurado.
     * @throws Exception em caso de erro de configuração.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Desativa CSRF para simplificação; em produção, configure adequadamente

                // Configuração de autorização usando o novo DSL
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(
                                "/auth/register",
                                "/auth/login",
                                "/css/**",
                                "/index"
                        ).permitAll()
                        .anyRequest().authenticated()
                )

                // Configuração de login OAuth2 com AWS Cognito
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login") // Página de login personalizada
                        .defaultSuccessUrl("/home", true) // Redireciona para /home após login bem-sucedido
                        .failureUrl("/login-error") // Redireciona para /login-error em falha de login
                        .permitAll()
                )

                // Configuração de logout
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }

    /**
     * Define o codificador de senhas.
     *
     * @return PasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
