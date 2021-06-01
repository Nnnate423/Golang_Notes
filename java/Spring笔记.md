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
* init properities of bean
    - Normal way 
        - hard code in @Bean
        - hard code in XML
    - inject value using file
        - @PropertySource("properities file")
        - use Environment to load properities file
        - env.getProperty("prop name", default_value)
    - property placeholder
        - ${ ... }
    - SpEL
        - access values: #{ ... } pass in value-> @Value("#{systemProperties['something']}") String value
        - use classes: T(java.lang.Math) -> able to use the static methods/ vars.
        - allow multiple operators: #{S.score>100 ? "Winner" : "Loser"}
        - regular expression: #{admian.email matches '[a-z._%+-]+@[..]'}
        - use Collections: #{S.songs[random number]}
        - look up: .?[cond], .^[] first match, .$[] last match
            - eg. #{S.songs.?[art eq 'new era']}
## 4. AOP
designed for cross-cutting concerns like security, transaction.\
inheritance and delegation have their flaws: 
1. inheritance increase coupling and results in a fragile inheritance relations.
2. delegation increase complexity

For AOP, aspect is a special class -> can be declared without changing the class which needs this feature.
* Concepts
    * Advice
        - describe the work of AOP
        - set the time to run AOP service
            - Before, After, After-returning, After-throwing, Around
    * Join Point 连接点
        - 应用执行过程中，插入切面的一个点
    * Pointcut
        - 匹配一个或多个连接点 (where to use the aspect) -> use regex
    * Aspect
        - integration of Advice and Pointcut
    * Introduction 引入
        - add new methods & attributes to the existing class.
    * Weaving
        - 把切面应用到目标对象并创建新的代理的过程 \
        -> during runtime: AOP container will create a dynamic proxy for a instance.
* Types of AOP support
    1. proxy based classic Spring AOP -> do not use
    2. POJO aspects -> Spring AOP join point only apply to methods, while AspectJ support 字段/构造器\
    Spring AOP is based on proxy, it wraps the target object -> accept request for it, run aspect logic before its methods invocation.
    3. @AspectJ Annotation-driven
    4. 注入式@AspectJ切面

* Select Join point throuth Pointcut
    1. write pointcut
    execution: 匹配连接点的执行方法
    others: 限制匹配
    ```
    execution(* somepackage.Randomcls.do(..) and bean('beanid'))
    execution(* somepackage.Randomcls.do(..) && within(somepackage))
    ```

    2. Define Aspect with pointcut
    ```
    @Aspect
    public class XXX{
        //define pointcut for later convenient use
        @Pointcut("execution(.. pkg.Sclass.method(..))")
        public void point(){}

        //use point cut
        @Before("point()")
        public void beforelog(){System.out.println("sss")}

        @After("point()")
        public void afterlog(){System.out.println("sss")}
    }
    ```
    in configuration file
    ```
    @Configuration
    @EnableAspectJAutoProxy     //start aspectj proxy
    public class XXXConfig{
        @Bean                //declare bean
        public XXX xxx(){return new XXX();}
    }
    ```
    3. use of @Around\
        make sure to pass in ProceedingJoinPoint class as argument to the advice function.\
        wrap the jp.proceed() function with your log to achieve around log for a point cut.
    4. work with args
    ```
    @Aspect
    public class XXX{
        @Pointcut("execution(.. pkg.Sclass.method(int))" + "args(argName)")
        public void point(int argName){}

        @Before("point(argName)")
        public void somefunc(int argName){...}
    }
    ```

## 5. Spring MVC
0. basic process\
DispatcherServlet -> handler mapping -(find controller)-> controller\
-> get model data and view name -> view resolver (configured in dispatcher) -> view implemented
1. DispatcherServlet configuration
    * setup
    ```
    class initializer extends AbstractAnnotationConfigDispatcherServletInitializer{
        //override 3 methods
        //1. getServletmapping
        //2. getRootConfigClasses
        //3. getServletConfigClasses
    }
    ```
    first method specify the mapping path, like "/"\
    and it will create DispatcherServlet and ContextLoaderListener.\
    DispatcherServlet loads web related beans like resolvers, controllers; ContextLoaderListener loads other app related beans.
2. MVC config
    * setup
    ```
    @Configuration
    @EnableWebMvc
    @ComponentScan(...)
    class xxx extends WebMvcConfigerAdapter{
        //override defaulthandling
        //write @Bean for viewResolver
    }
    ```
    * Resolvers
        * ViewResolver
        * MultiPartResolver - handle file upload
3. Controller
    * RequestMapping(value = "/", method = GET)
    * gets input
        1. query param\
        eg. 
        ```
        @RequestMapping(...)
        public int mapfunc( @RequestParam( value = "xxx", defaultValue = 20 ) long max ){...}
        ```
        2. form param\
        eg. @NotNull, @Size(min=, max=), @Pattern
        3. path vars
        eg. 
        ```
        @RequestMapping("/{name}")
        public int mapfunc( @PathVariable("name") long max ){...}
        ```
4. exception handling
    * @ResponseStatue - change status code of a exception
    * @ExceptionHandler(xxxexception.class) - sepcific handler for a exception class, in one controller
    * @ControllerAdvice - @ExceptionHandler inside this advice class will be applied to all controller.
5. variable passing (for redirection)
    * by url
    * by session
    * by flash attribute (implemented by session)
    ```
    model.addFlashAttribute(name, obj)
    ```
