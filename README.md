# webadmin

## 1 多语言支持
### 1.1 使用方式
* 前端请求方式  
请求时，Header中指定：Accept-Language
```text
curl --location 'http://localhost:8080/greeting/hello' \
--header 'Accept-Language: zh-CN'
```
* 后端使用

```java
import java.util.List;

public class GreetingController {
    public String hello() {
        String message = Localization.getLocalizedString("greeting.hello");
        message = Localization.getLocalizedString("greeting.hello", List.of("test"));
        return message;
    }
}
```
### 1.2 配置文件说明  
路径：resources/localization/messages  
规则：message_{locate}.properties  
yaml配置：
```yml
spring:
  messages:
    # 指定多语言配置文件路径
    basename: localization/messages
    encoding: UTF-8
    # 是否回退到系统默认语言环境，默认为true，LocaleConfig已配置默认语言，禁用此配置
    fallback-to-system-locale: false
    # 消息解析时是否始终使用 MessageFormat 格式化消息
    # MessageFormat.format("Hello, {0}! Today is {1,date}.", "Alice", new Date());
    always-use-message-format: true
```

### 1.3 技术实现
**Config中配置前端语言解析器和拦截器**
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多语言设置拦截器，也可以使用Filter
        registry.addInterceptor(new LanguageInterceptor(localeResolver()));
    }
    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver slr = new AcceptHeaderLocaleResolver();
        // 设置默认语言
        slr.setDefaultLocale(Locale.US);
        return slr;
    }
}

```
**Interceptor自动解析前端语言**  
通过LocaleResolver从HttpServletRequest获取语言(Header: Accept-Language)  
并将解析到的语言存储到LocaleContextHolder中方便全局获取  
```java
public class LanguageInterceptor implements HandlerInterceptor {
    private LocaleResolver localeResolver;

    public LanguageInterceptor(LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Locale locale = localeResolver.resolveLocale(request);
        LocaleContextHolder.setLocale(locale);
        response.setHeader("x-trace-id", System.currentTimeMillis()+"");
        return true;
    }
}
```
**配置多语言转换类**
```java
import org.springframework.context.MessageSource;
@Component
public class Localization {
    private static MessageSource messageSource;
    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        Localization.messageSource = messageSource;
    }
    public static String getLocalizedString(String key) {
        Locale locale = LocaleContextHolder.getLocale(); // 获取当前线程的 Locale
        return getLocalizedString(key, null, locale);
    }
}
```
**配置多语言文件路径**  
其中basename为指定的多语言文件存放路径即:  
resources/localization/messages
```yml
spring:
  # 多语言配置
  messages:
    basename: localization/messages
    encoding: UTF-8
    fallback-to-system-locale: false
    always-use-message-format: true
```
文件结构如下：  
messages.properties：为默认语言文件，当LocaleContextHolder没有获取到语言时，使用此配置  
messages_{lang}.properties：其他语言配置文件，{lang}要和Locale中的名字对应
```text
resources
├── application.yml
├── localization
│   ├── messages.properties
│   ├── messages_en_US.properties
│   └── messages_zh_CN.properties

```

## 2 SpringSecurity集成
### 2.1 使用方式
```yaml
spring:
  security:
    # 是否启用认证
    enabled: true
    # 认证模式：basic/OAuth2
    type: OAuth2
```


* 关闭认证  
配置方式：spring.security.enabled=false
启用此配置：com.hans.aggreation.webadmin.web.config.DisableSecurityConfig

* Basic认证  
配置方式：spring.security.type=basic  
启用此配置：com.hans.aggreation.webadmin.web.config.BasicSecurityConfig  
  
* OAuth2认证  
配置方式 (注意严格区分大小写)：spring.security.type=OAuth2  
启用此配置： com.hans.aggreation.webadmin.web.config.OAuth2SecurityConfig  


### 1.3 技术实现
用户模型
```java
com.hans.aggreation.webadmin.core.model.UserModel
public class UserModel {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column
    private String password;
}
```


Basic中配置登录认证
```java
public class BasicSecurityConfig {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.err.println("========= Enable BasicSecurityConfig.");
        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/login", "/css/**")
                        .permitAll().anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/authenticateTheUser") // 对应登录表单的 action，用于登录信息校验
                        .defaultSuccessUrl("/home", true)
                        .failureUrl("/login?error=true")
                        .permitAll())
                .logout(logout -> logout
                        .logoutSuccessUrl("/login")
                        .permitAll()
                ).authenticationProvider(authenticationProvider())
                // 开启Basic认证，支持通过在Header中添加 Authorization方式调用接口
                // Authorization Basic Base64Encode(username:password)
                .httpBasic(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable()); // 禁用 CSRF 保护，前端需要post时，通常禁用
        return http.build();
    }

    /**
     * 配置自定义的登录接口
     * @return
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * 密码加密方式
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```


OAuth2密码加密方式
```java
// Config中配置Bean
public class OAuth2SecurityConfig {
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
// 登录业务中使用
public class OAuth2LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;

    public RestResponse<String> login(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
        try {
            Authentication authenticate = authenticationManager.authenticate(authenticationToken);
            if (authenticate!=null && authenticate.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authenticate);
                CustomUserDetails user = (CustomUserDetails)authenticate.getPrincipal();
                String token = JwtUtils.generateToken(user.getUsername());
                return RestResponse.success(token);
            }
        } catch (Exception e) {
            log.debug("认证失败", e);
            return RestResponse.error(Localization.getLocalizedString("oauth2.login.failed", List.of(e.getMessage())));
        }
        return RestResponse.error(Localization.getLocalizedString("oauth2.login.error"));
    }

}
// 登录认证filter
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 登录接口
        if ("/oAuth2Login/login".equals(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = request.getHeader("access_token");
        if (StringUtils.isBlank(token)) {
            filterChain.doFilter(request, response);
            return;
        }
        String usernameFromToken;
        UserDetails userDetails;
        try {
            usernameFromToken = JwtUtils.getUsernameFromToken(token);
            userDetails = customUserDetailsService.loadUserByUsername(usernameFromToken);
            // 此处可以继续检查用户信息是否可用
        } catch (Exception e) {
            filterChain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null ,null);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
```