package com.github.code.zxs.core.util;






@Component
public class SpringContextUtils implements ApplicationContextAware {
  
    // Spring应用上下文环境  
    private static ApplicationContext applicationContext;
  
    /** 
     * 实现ApplicationContextAware接口的回调方法。设置上下文环境 
     *  
     * @param applicationContext 
     */  
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextUtils.applicationContext = applicationContext;
    }  
  
    /** 
     * @return ApplicationContext 
     */  
    public static ApplicationContext getApplicationContext() {
        return applicationContext;  
    }  
  
    /** 
     * 获取对象 
     *  
     * @param name 
     * @return Object
     * @throws BeansException
     */  
    public static Object getBean(String name) throws BeansException {
        return applicationContext.getBean(name);
    }

    /**
     * 获取对象
     * @param type
     * @return Object
     * @throws BeansException
     */
    public static <T> T getBean(Class<T> type) throws BeansException {
        return applicationContext.getBean(type);
    }

    /**
     * 获取对象
     * @param name
     * @param type
     * @return Object
     * @throws BeansException
     */
    public static <T> T getBean(String name,Class<T> type) throws BeansException {
        return applicationContext.getBean(name,type);
    }
}