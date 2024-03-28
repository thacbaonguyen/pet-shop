package com.petbackend.thbao.configurations;

import com.petbackend.thbao.models.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Value("${api.prefix}")
    private String apiPrefix;

    @Value("${jwt.secretKey}")
    private String secretKey;
    private final String[] PUBLIC_ENDPOINTS = {String.format("%s/users/register", apiPrefix), String.format("%s/users/login", apiPrefix)};
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(request ->
                    request.requestMatchers(HttpMethod.POST, String.format("%s/users/register", apiPrefix),
                                    String.format("%s/users/login", apiPrefix),
                                    String.format("%s/users/introspect", apiPrefix))
                .permitAll()
//                            //Category
//                            .requestMatchers(HttpMethod.GET, String.format("%s/categories/**", apiPrefix))
//                            .hasAnyRole(Role.ADMIN, Role.USER)
//                            .requestMatchers(HttpMethod.POST, String.format("%s/categories/**", apiPrefix))
//                            .hasRole(Role.ADMIN)
//                            .requestMatchers(HttpMethod.PUT, String.format("%s/categories/**", apiPrefix))
//                            .hasRole(Role.ADMIN)
//                            .requestMatchers(HttpMethod.DELETE, String.format("%s/categories/**", apiPrefix))
//                            .hasRole(Role.ADMIN)
//
//                            //Pet Missing
//                            .requestMatchers(HttpMethod.GET, String.format("%s/pets/**", apiPrefix))
//                            .hasAnyRole(Role.ADMIN, Role.USER)
//                            .requestMatchers(HttpMethod.POST, String.format("%s/pets/**", apiPrefix))
//                            .hasAnyRole(Role.ADMIN, Role.USER)
//                            .requestMatchers(HttpMethod.PUT, String.format("%s/pets/**", apiPrefix))
//                            .hasAnyRole(Role.ADMIN, Role.USER)
//                            .requestMatchers(HttpMethod.DELETE, String.format("%s/pets/**", apiPrefix))
//                            .hasAnyRole(Role.ADMIN, Role.USER)

                .anyRequest().authenticated());
        httpSecurity.oauth2ResourceServer(oAuth2 -> oAuth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder())
                .jwtAuthenticationConverter(jwtAuthenticationConverter())));
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
    @Bean
    public JwtDecoder jwtDecoder(){
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HS256");
        return NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS256).build();
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }
}
