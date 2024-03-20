package security; // valida o token de pessoas autorizadas a mexer nas configurações do site 

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class BasicSecurityConfig {

	@Autowired
	private JwtAuthFilter authFilter;

	@Bean // serve para trazer outras classes tbm ( A anotação oferece suporte a atributos
			// como , , e para configurar o ciclo de vida e o comportamento do bean).
	UserDetailsService userDetailsService() {

		return new UserDetailsServiceImpl();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationProvider authenticationProvider() { // provedor, responsável para conferir a validação do usuario
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService());
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {// formulário
		http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.csrf(csrf -> csrf.disable()).cors(withDefaults())
				.authorizeHttpRequests((auth) -> auth.requestMatchers("/usuarios/logar").permitAll()
						.requestMatchers("/usuarios/cadastrar").permitAll().requestMatchers("/error/**").permitAll()
						.requestMatchers(HttpMethod.OPTIONS).permitAll().anyRequest().authenticated())
				.authenticationProvider(authenticationProvider())
				.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class).httpBasic(withDefaults());

		return http.build();

	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() { // tive que pedir ajuda para o gepeto, pq deu erro
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		((UrlBasedCorsConfigurationSource) source).registerCorsConfiguration("/**", configuration);
		return (CorsConfigurationSource) source;

	}

}
