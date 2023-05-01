package com.blogapplication.blogapplication.config;


import java.util.Arrays;

import com.blogapplication.blogapplication.common.security.JwtAuthenticationEntryPoint;
import com.blogapplication.blogapplication.common.security.JwtAuthenticationFilter;
import com.blogapplication.blogapplication.common.security.JwtUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;



//import com.myschoolAuthService.security.JwtAuthenticationEntryPoint;
//import com.myschoolAuthService.security.JwtAuthenticationFilter;
//import com.myschoolAuthService.security.JwtUserDetailService;

@Configuration
@EnableWebSecurity
@PropertySource("classpath:constant/constant.properties")
@PropertySource("classpath:constant/constantEnv.properties")
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private JwtUserDetailService jwtUserDetailService;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Value("${settings.cors.origin}")
    private String corsOrigin ;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // TODO Auto-generated method stub
        super.configure(auth);
        auth.userDetailsService(jwtUserDetailService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // TODO Auto-generated method stub
        http.cors();
        http
                .csrf().disable()
                .authorizeRequests()


                .antMatchers("/swagger*/**",
                        "/v2/api-docs", "/configuration/**","/demo/**",
                        "/webjars/**","/authentication/login","/user/create-user").permitAll()

                .anyRequest().authenticated()
                .and().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // Add a filter to validate the tokens with every request
		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    }

    @Bean
    public AuthenticationManager getAuthenticationManager() throws Exception
    {
        return authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource()
    {

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern(corsOrigin);
//       configuration.addAllowedOrigin(corsOrigin);
        configuration.addAllowedHeader("Content-Type");
        configuration.addAllowedHeader("Authorization");
        configuration.addAllowedHeader("X-Requested-With");
        configuration.addAllowedHeader("authorization");
        configuration.addAllowedHeader("multipart/form-data");
        configuration.setAllowCredentials(true);
        configuration.setAllowedMethods(Arrays.asList("GET","POST"));
        configuration.setMaxAge((long) 86400);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }



}
