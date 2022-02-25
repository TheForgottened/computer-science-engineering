package pt.isec.metapd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pt.isec.metapd.repository.ServerRepository;
import pt.isec.metapd.restapi.security.AuthorizationFilter;
import pt.isec.metapd.restapi.token.TokenManager;

@ComponentScan(basePackages = {"pt.isec.metapd.restapi.controllers"})
@SpringBootApplication
public class RestApi {
	public static ServerRepository serverRepository;
	public static TokenManager tokenManager;

	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Invalid number of arguments! Usage: <dbms address>");
			return;
		}

		serverRepository = new ServerRepository(args[0]);
		tokenManager = new TokenManager();

		SpringApplication.run(RestApi.class, args);
	}

	@EnableWebSecurity
	@Configuration
	class WebSecurityConfig extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable()
					.addFilterAfter(new AuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
					.authorizeRequests()
					.antMatchers(HttpMethod.POST, "/session").permitAll()
					.anyRequest().authenticated().and()
					.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
					.exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
		}
	}
}
