package by.mkwt.webquiz.config.security;

import by.mkwt.webquiz.service.user.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AnonymousAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

import java.security.Principal;

@Configuration
@EnableWebSecurity
public class ApplicationSecurity extends WebSecurityConfigurerAdapter {

    private final RESTAuthenticationEntryPoint authenticationEntryPoint;
    private final RESTAuthenticationFailureHandler authenticationFailureHandler;
    private final RESTAuthenticationSuccessHandler authenticationSuccessHandler;
    private final UserDetailServiceImpl userDetailService;

    @Autowired
    public ApplicationSecurity(RESTAuthenticationEntryPoint authenticationEntryPoint, RESTAuthenticationFailureHandler authenticationFailureHandler, RESTAuthenticationSuccessHandler authenticationSuccessHandler, UserDetailServiceImpl userDetailService) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.userDetailService = userDetailService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
//        builder.inMemoryAuthentication().withUser("user").password("user").roles("USER").and().withUser("admin")
//                .password("admin").roles("ADMIN");
        builder.userDetailsService(userDetailService).passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.anonymous().authenticationFilter(anonymousAuthenticationFilter()).authenticationProvider(anonymousAuthenticationProvider());
        http.authorizeRequests().antMatchers("/nims/**").authenticated();
        http.authorizeRequests().antMatchers("/room/**").authenticated();
        http.authorizeRequests().antMatchers("/personal/**").authenticated();
        http.authorizeRequests().antMatchers("/secured/**").authenticated();
        http.authorizeRequests().antMatchers("/games/**").authenticated();
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
        http.formLogin().successHandler(authenticationSuccessHandler);
        http.formLogin().failureHandler(authenticationFailureHandler);
        http.logout().logoutSuccessUrl("/");

        // CSRF tokens handling
        http.csrf().disable();
    }

    private AnonymousAuthenticationFilter anonymousAuthenticationFilter() {
        Principal p = () -> "guest";
        return new AnonymousAuthenticationFilter("foobar", p, AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
    }

    private AnonymousAuthenticationProvider anonymousAuthenticationProvider() {
        return new AnonymousAuthenticationProvider("foobar");
    }

}
