# Spring in Action v4 笔记

## 3.核心装配
* Profile
    - why use @Profile
    - how to use : apply on class/methods
    - how to activate: DispatcherServlet 初始化参数；web上下文参数； @ActiveProfiles
    - how is it implemented? -> using @Conditional(ProfileCondition.class)
* Conditioanl Beans
    - why use @Conditional -> flexible & clear
    - how to use: 
        - @Conditional(SomeCondition.class)
        - implement matches method of Condition interface in SomeCondition class.
        - know about ConditionContext & AnnotatedTypeMetadata interfaces.
* resolve 歧义
    - why -> multiple bean implements same interface -> do not know which to inject
    - how to solve:
        - @Primary
        - @Qualifier(beanid)
        - self-defined Qualifier -> can apply multiple qualifier now as they can be different name annotations.
* bean的作用域 @Scope
    - why need scope -> stateful beans -> avoid contamination.
    - how to apply: @Scope([choose from below])
        - nothing: singleton
        - ConfigurableBeanFactory.SCOPE_PROTOTYPE: prototype
        - value = WebApplicationContext.SCOPE_SESSION, ProxyMode = ScopedProxyMode.INTERFACES
            - see page 86, 3.4.1
            - a proxy is injected instead of a bean -> it will decide which bean is injected during runtime.
            


## 4.