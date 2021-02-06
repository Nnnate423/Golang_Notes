# Java Notes
## 0. Native Java
### 0.1. Lambda & Consumer, Supplier Interface
consumer only 1 input, no return;\
supplier 1 return, no input;\
function 1 input param and 1 ouput param.
* Consumer\
    By Def: A Consumer is a functional interface that accepts a single input and returns no output.\
    In layman’s language, as the name suggests the implementation of this interface consumes the input supplied to it.\
    Two methods:
    ```
    void accept(T t); //accept
    default Consumer<T> andThen(Consumer<? super T> after); //composition a new consumer with two consumers.
    ```
    Example
    ```
    import java.util.function.Consumer;
    Consumer<String> show_str = str -> {System.out.println(str);};
    Consumer<Integer> Increment_num = num -> {num+=1;};
    strs.forEach(show_str);
    show_str.andThen(show_str2).accept("andThen and accept test.");
    ```
    usually be passed into forEach(): perform this function on every element.

* Supplier\
    a lambda Function generates data using the get() method
    ```
    Supplier<Integer> intSupplier = () -> r.nextInt();
    ```

* Function\
    Generic Interface.
    ```
    Function<String, Integer> samplefunc= (a) -> {return a.length();};
    ```
    can be used in stream map() as it can return some new objects.
* Lambda
    * implement interface methods
    ```
    public interface
    ```
    * with multiple param

    * Method references as lambda


### 0.2 Stream

### 0.3 Iterable

## 1. Spring
### 1.1 annotation & basics
* @SpringBootApplication
    @SpringBootApplication = @SpringBootConfiguration + @EnableAutoConfiguration + @ComponentScan
* @WebMvcTest
    for mock spring mvc test, create mockMvc and do rest calls to controller.
* @Automired
    can be used on:
    * constructor
    * property
    * setter
* validation
    * @NotNull: a constrained CharSequence, Collection, Map, or Array is valid as long as it's not null, but it can be empty
    * @NotEmpty: a constrained CharSequence, Collection, Map, or Array is valid as long as it's not null and its size/length is greater than zero
    * @NotBlank: a constrained String is valid as long as it's not null and the trimmed length is greater than zero  

* mapping
    * @RequestMapping General-purpose request handling 
    * @GetMapping Handles HTTP GET requests
    * @PostMapping Handles HTTP POST requests 
    * @PutMapping Handles HTTP PUT requests
    * @DeleteMapping Handles HTTP DELETE requests
    * @PatchMapping Handles HTTP PATCH requests
* Spring Web dependency
    it brings spring MVC and embeded tomcat.



### 1.2. JDBC
* JDBC template

* rowmapper
use class or method reference by like: this::method
```
@Override
public Ingredient findOne(String id) {
  return jdbc.queryForObject(
      "select id, name, type from Ingredient where id=?",
      new RowMapper<Ingredient>() {
}
  public Ingredient mapRow(ResultSet rs, int rowNum)
      throws SQLException {
    return new Ingredient(
        rs.getString("id"),
        rs.getString("name"),
        Ingredient.Type.valueOf(rs.getString("type")));
};
}, id);
```

* save data 
    1. Directly, using the update() method
    2. Using the SimpleJdbcInsert wrapper class


## 2. Tomcat


## 3. Spring boot


## 4. Concurrency