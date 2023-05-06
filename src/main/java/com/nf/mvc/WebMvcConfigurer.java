package com.nf.mvc;

/**
 * 此接口是用来给mvc框架内部的一些核心组件提供配置使用的,<br/>
 * 所有mvc框架内需要进行配置的组件都可以在这里通过添加默认方法的形式实现
 *
 * <p>目前这个示例接口只提供了对RequestMappingHandlerMapping与Cors方面的设置</p>
 *
 * <p>此接口由用户实现，一般只需要提供一个实现类即可，提供多个此接口的实现类是不允许的，会抛出异常</p>
 *
 * <p>这里只写了对RequestMappingHandlerMapping与CorsConfiguration组件的配置能力
 * 你可以多提供一些方法来对其它组件进行配置，比如ArgumentResolver
 * </p>
 */
public interface WebMvcConfigurer {
    default void configureHandlerMapping(HandlerMapping handlerMapping) {

    }

    default void configureHandlerAdapter(HandlerAdapter adapter) {

    }

    default void configureArgumentResolver(ParameterProcessor argumentResolver) {

    }

    default void configureExceptionResolver(HandlerExceptionResolver exceptionResolver) {

    }

    default void configureCors(CorsConfiguration configuration) {
        configuration.applyDefaultConfiguration();
    }

}
