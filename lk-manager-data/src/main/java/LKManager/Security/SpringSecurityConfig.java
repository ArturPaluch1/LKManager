package LKManager.Security;

import LKManager.services.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity

public class SpringSecurityConfig   {
private final UserService customUserDetailsService;
    @Value("${spring.security.rememberme.key}")
    private  String rememberMeKey;
private final  CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler= new CustomAuthenticationSuccessHandler();

    public SpringSecurityConfig(UserService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }
/*
    @Bean
    public PasswordEncoder getBcryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
*/

/*    @Bean
    public InMemoryUserDetailsManager get() {
        UserDetails user = User.withUsername("jan")
                .password(getBcryptPasswordEncoder().encode("jan123"))
                .roles("USER")
                .build();
        UserDetails admin = User.withUsername("admin")
                .password(getBcryptPasswordEncoder().encode("admin123"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(Arrays.asList(user, admin));
    }*/

/*    @Bean
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(new CustomUserDetailsService())
                .passwordEncoder(passwordEncoder());
    }*/


    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .sessionManagement()
               // .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .authorizeRequests()
               // .antMatchers("/**").permitAll()
                    .antMatchers("/js/**", "/css/**","/webjars/**","/fonts/**","/images/**").permitAll()
                    .antMatchers("/","/index", "/public/**","/confirmEmail","/passwordChangeConfirmEmail").permitAll()


                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
                .and()
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())//.ignoringAntMatchers("/api/**")
               .and()
          //  .csrf().disable()
          //      .and()

               .formLogin().permitAll()

             //   .loginPage("/").permitAll()
                .loginPage("/public/logIn").permitAll()
                    .successHandler(customAuthenticationSuccessHandler)
             //   .defaultSuccessUrl("/public/LK/schedule/schedule", true)
             //   .defaultSuccessUrl("/admin/LK/schedule/schedule", true)

                    .failureUrl("/public/logIn?error=true")
                .and()
                .logout()
                    .logoutUrl("/logout") // URL do wylogowania
                    .logoutSuccessUrl("/public/logIn?logout=true") // przekierowanie po wylogowaniu
                    .invalidateHttpSession(true) // unieważnia sesję
                    .deleteCookies("JSESSIONID") // usuwa cookie sesji
                    .permitAll()
                .and()
                .rememberMe()
                    .userDetailsService(customUserDetailsService)
                    .rememberMeParameter("remember-me")
                    .key(rememberMeKey)
                    .tokenValiditySeconds(1296000)// 2 weeks and one day

                .and()
                .userDetailsService(customUserDetailsService);



        return http.build();
    }



    class  CustomAuthenticationSuccessHandler  implements AuthenticationSuccessHandler{


        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            String targetUrl = determineTargetUrl(authentication);

            if (response.isCommitted()) {
                return;
            }

            response.sendRedirect(targetUrl);



        }
        protected String determineTargetUrl(Authentication authentication) {


            GrantedAuthority authority= authentication.getAuthorities().stream().findFirst().orElse(null);

            switch(authority.getAuthority())
            {
                case "ROLE_ADMIN" :{
                    return "/admin/LK/schedule/schedule";
                }
                case "ROLE_DEACTIVATED_USER" :{
                    return "todo";
                }
                case "ROLE_CAPITAN" :{
                    return "todo";
                }
                default: //zwykły user
                    return "/public/LK/results";

            }




        }



    }

}





