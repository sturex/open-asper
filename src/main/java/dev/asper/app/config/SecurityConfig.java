package dev.asper.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    public static final String TRAINER_USER_NAME = "TRAINER";
    public static final String FRONTEND_USER_NAME = "FRONTEND";
    public static final String ROLE_TRAINER = "TRAINER";
    public static final String ROLE_FRONTEND = "FRONTEND";
    public final String trainerPassword = "hhshau32!ZZAWSt2yvd";
    public final String frontendPassword = "sahu7834zcnvgvghDSRes";
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(8);
    private final PasswordEncoder passwordEncoder = new PasswordEncoder() {
        @Override
        public String encode(CharSequence rawPassword) {
            return String.valueOf(rawPassword);
        }

        @Override
        public boolean matches(CharSequence rawPassword, String encodedPassword) {
            return rawPassword.equals(encodedPassword);
        }
    };

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/graphql", configuration);
        source.registerCorsConfiguration("/graphiql", configuration);
        return request -> configuration;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//                .authorizeHttpRequests(matcherRegistry -> matcherRegistry.anyRequest().hasAnyRole(ROLE_TRAINER, ROLE_FRONTEND))
                .authorizeHttpRequests(matcherRegistry -> matcherRegistry.anyRequest().permitAll())
                .httpBasic(withDefaults());
        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
                                                       PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(trainer(), frontend());
    }

    private UserDetails trainer() {
        UserDetails trainer = User.builder()
                .username(TRAINER_USER_NAME)
                .roles(ROLE_TRAINER)
                .password(passwordEncoder.encode(trainerPassword))
                .build();
        return User.withUserDetails(trainer).build();
    }

    private UserDetails frontend() {
        UserDetails trainer = User.builder()
                .username(FRONTEND_USER_NAME)
                .roles(ROLE_FRONTEND)
                .password(passwordEncoder.encode(frontendPassword))
                .build();
        return User.withUserDetails(trainer).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return passwordEncoder;
    }
}
