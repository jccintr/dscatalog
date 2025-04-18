package com.jcsoftware.DsCatalog.config;

//@Configuration
public class SecurityConfig {

	/*
	 @Bean
	    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// adicionado por jc
			http.exceptionHandling(exh -> exh.authenticationEntryPoint(
					(request,response,exception)->{
						response.sendError(HttpServletResponse.SC_UNAUTHORIZED,exception.getMessage());
					}
					));
			return http.build();
		}
	*/	
	
	/*
	@Component
    public class MyBeanPostProcessor implements BeanPostProcessor{

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            if (bean instanceof AuthorizationFilter authorizationFilter) {
                authorizationFilter.setFilterErrorDispatch(false);
            }
            return bean;
        }

    }
    */
}
