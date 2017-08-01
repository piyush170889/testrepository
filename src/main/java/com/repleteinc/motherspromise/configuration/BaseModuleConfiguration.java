package com.repleteinc.motherspromise.configuration;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.X509Certificate;

import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.repleteinc.motherspromise.configuration.rest.UnauthorizedEntryPoint;
import com.repleteinc.motherspromise.crons.ScheduledTasks;
import com.repleteinc.motherspromise.dao.user.JpaUserDao;
import com.repleteinc.motherspromise.dao.user.PatientUserDaoImpl;

@Configuration
@EnableAspectJAutoProxy
@EnableWebMvc
@EnableTransactionManagement
@EnableScheduling
@ComponentScan(basePackages = "com.repleteinc.*")
@PropertySources({ @PropertySource("classpath:config.properties"), @PropertySource("classpath:db.properties") })
public class BaseModuleConfiguration extends WebMvcConfigurerAdapter implements SchedulingConfigurer {

	@Autowired
	private Environment environment;

	@Override
	public void configurePathMatch(PathMatchConfigurer matcher) {
		matcher.setUseRegisteredSuffixPatternMatch(false);
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {

		registry.addMapping("/**").allowCredentials(false)
			.allowedOrigins("*")
			.allowedMethods("PUT", "POST", "GET", "OPTIONS", "DELETE")
			.exposedHeaders("Authorization", "Content-Type");
	}

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.setScheduler(taskExecutor());
	}
	@Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/jsp/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }
	
	@Bean(destroyMethod = "shutdown")
	public Executor taskExecutor() {
		return Executors.newScheduledThreadPool(Integer.parseInt(environment.getProperty("threadpool.size")));
	}

	@Bean
	public ScheduledTasks startScheduledTask() {
		return new ScheduledTasks();
	}

	@Bean(name = "jdbcTemplate")
	public JdbcTemplate getJdbcTemplate() {

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource(), true);
		return jdbcTemplate;
	}

	@Bean(name = "sqlProperties")
	public Properties getSqlPropertiesFile() throws IOException {
		Resource resource = new ClassPathResource("sqlQueries.properties");
		return PropertiesLoaderUtils.loadProperties(resource);
	}

	@Bean(name = "responseMessageProperties")
	public Properties getResponseMessagePropertiesFile() throws IOException {
		Resource resource = new ClassPathResource("responseMessages.properties");
		return PropertiesLoaderUtils.loadProperties(resource);
	}

	@Bean(name = "configProperties")
	public Properties getConfigPropertiesFile() throws IOException {
		Resource resource = new ClassPathResource("config.properties");
		return PropertiesLoaderUtils.loadProperties(resource);
	}

	@Bean
	public EhCacheManagerFactoryBean ehCacheCacheManager() {
		EhCacheManagerFactoryBean factory = new EhCacheManagerFactoryBean();
		factory.setConfigLocation(new ClassPathResource(environment.getRequiredProperty("cachefile.path")));
		factory.setShared(true);
		return factory;
	}

	@Bean
	public CacheManager cacheManager() {
		EhCacheCacheManager cacheManager = new EhCacheCacheManager();
		cacheManager.setCacheManager(ehCacheCacheManager().getObject());
		return cacheManager;
	}

	@Bean
	public DriverManagerDataSource dataSource() {

		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		
		//TODO: Configure Different Environments
		dataSource.setDriverClassName(environment.getRequiredProperty("db.driver"));
		dataSource.setUrl(environment.getRequiredProperty("db.url"));
		dataSource.setUsername(environment.getRequiredProperty("db.userName"));
		dataSource.setPassword(environment.getRequiredProperty("db.password"));

		return dataSource;
	}

	@Bean
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(dataSource());
		sessionFactory.setPackagesToScan(new String[] { "com.knovaly.web.beans.entity" });
		sessionFactory.setHibernateProperties(hibernateProperties());
		return sessionFactory;
	}

	private Properties hibernateProperties() {
		Properties properties = new Properties();
		properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
		properties.put("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql"));
		properties.put("hibernate.format_sql", environment.getRequiredProperty("hibernate.format_sql"));
		properties.put("hibernate.jdbc.batch_versioned_data", true);
		return properties;
	}

	@Bean
	@Autowired
	public HibernateTransactionManager transactionManager(SessionFactory s) {
		HibernateTransactionManager txManager = new HibernateTransactionManager();
		txManager.setSessionFactory(s);
		return txManager;
	}

	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver multipartResolver() {

		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setMaxUploadSize(Integer.parseInt(environment.getRequiredProperty("max.uploadsize")));
		return multipartResolver;
	}

	@Bean
	public StandardPasswordEncoder passwordEncoder() {
		return new StandardPasswordEncoder(environment.getRequiredProperty("secret.key"));
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return new JpaUserDao();
	}
	
	@Bean
	public UserDetailsService ptntUserDetailsService(){
		return new PatientUserDaoImpl();
	}

	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return new UnauthorizedEntryPoint();
	}

	@Bean
	public AuthenticationManager authenticationManager(ObjectPostProcessor<Object> objectPostProcessor)
			throws Exception {
		return new AuthenticationManagerBuilder(objectPostProcessor).userDetailsService(userDetailsService())
				.passwordEncoder(passwordEncoder()).and().build();
	}
	
	@Bean
	public AuthenticationManager patientAuthenticationManager(ObjectPostProcessor<Object> objectPostProcessor)
			throws Exception {
		return new AuthenticationManagerBuilder(objectPostProcessor).userDetailsService(ptntUserDetailsService())
				.passwordEncoder(passwordEncoder()).and().build();
	}
	
	@Bean
	public RestTemplate doGetRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		final ClientHttpRequestFactory clientHttpRequestFactory = createRequestFactory(true, false);
		 restTemplate.setRequestFactory(clientHttpRequestFactory);
		 return restTemplate;
	}
	
	private ClientHttpRequestFactory createRequestFactory(boolean trustSelfSignedCerts, boolean disableRedirectHandling) {
		HttpClientBuilder httpClientBuilder = HttpClients.custom().useSystemProperties();

		if (trustSelfSignedCerts) {
			//TODO : Remove Deprecated Method Usage
			httpClientBuilder.setSslcontext(initSSLContext());
			httpClientBuilder.setHostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		}
		
		if (disableRedirectHandling) {
			httpClientBuilder.disableRedirectHandling();
		}

		HttpClient httpClient = httpClientBuilder.build();
		CookieStore cookieStore = new BasicCookieStore();
		HttpContext httpContext = new BasicHttpContext();
		httpContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
		// ...

		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

		return requestFactory;
	}
	
	private SSLContext initSSLContext() {
	    try {
	    	 TrustManager[] trustAllCerts = new TrustManager[] { new MyTrustManager() };

	 	    // Install the all-trusting trust manager
	 	    SSLContext sc = SSLContext.getInstance("SSL");
	 	    sc.init(null, trustAllCerts, new java.security.SecureRandom());
	 	    HostnameVerifier allHostsValid = new HostnameVerifier() {
	 	        public boolean verify(String hostname, SSLSession session) {
	 	            return true;
	 	        }
	 	    };
	 	    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	 	    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	        return sc;
	    } catch (final Exception ex) {
	        return null;
	    }
	}
	
	class MyTrustManager implements X509TrustManager 
	{
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
		   return null;
		}
		
		public void checkClientTrusted(X509Certificate[] certs, String authType) {
		}
		
		public void checkServerTrusted(X509Certificate[] certs, String authType) {
		}
		
		@Override
		 public void checkClientTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate, String paramString) {}
		
		@Override
		public void checkServerTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate, String paramString) {}
	}

}