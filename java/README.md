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
    * implement interface methods (only 1 method missing implementation)
        ```
        public interface Runner{
            public void goRun(int time);
        }
        Runner runner = (time) ->{System.out.println("Runner run for: "+ time+ " seconds.");};
        runner.goRun(3);
        ```
        It is different to anonymous interface implementations(more code, more powerful), but similiar.
    * with multiple param
        it can also return value
        ```
        (p1,p2)->{return p1>p2;}
        //equals to the expression:
        (p1,p2)->p1>p2;
        ```
    * Method references
        * Static method reference\
        easy, use TheClass::StaticMethod
        * Parameter method reference\
        have to make sure parameter and return types match (same signiture).
        * Instance method reference
            ```
            ClassA instance = new ClassA();
            InterfaceB inter = ClassA::instanceMethod;
            ```
        * Constructor methods reference
            ```
            public interface Factory {
                public String create(char[] val);
            }
            Factory fac = String::new;
            ```

### 0.2 Stream
It can be used on any type of Collection( List, Set etc.)\
* Init a stream
    * Stream.of
    ```
    Stream<Integer> stream = Stream.of(1,2,3);
    ```
    * Stream builder
    ```
    Stream<String> streamBuilder =
        Stream.<String>builder().add("a").add("b").add("c").build();
    ```
    * stream.iterator
    ```
    Stream<Integer> streamIterated = Stream.iterate(0, n -> n + 2).limit(10);
    ```
    * from collection\
    eg. define a list and call .stream() method.

* pipeline
    * skip(int num) & limit(int num) & findFirst()
    * range(int i1, int i2) - used on primitives
        ```
        IntStream intStream = IntStream.range(1, 3);
        ```
    * map(lambda) takes a lambda function\
        map one object to another and return it, like a lookup operation.

    * filter(lambda) takes a lambda func\
        return a bool based on some test.
        ```
        Optional<String> stream = list.stream().filter(element -> {
            log.info("filter() was called");
            return element.contains("2");
        }).map(element -> {
            log.info("map() was called");
            return element.toUpperCase();
        }).findFirst();
        ```
    * collect
        collect the processed stream into data structure (Collection)
        ```
        List<String> collectorCollection = productList.stream().map(Product::getName).collect(Collectors.toList());
        ```

    * count
        count size, used at end
        ```
        long size = list.stream().skip(2).map(element -> {
            wasCalled();
            return element.substring(0, 3);
        }).count();
        ```
    * reduce\
    act as accumulator
    ```
    List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);
    int result = numbers
        .stream()
        .reduce(0, (subtotal, element) -> subtotal + element);
    //equavalent to
    int result = numbers.stream().reduce(0, Integer::sum);
    ```


### 0.3 Iterable
An interface:
    
    public interface Iterable<T> {
        Iterator<T> iterator();
        Spliterator<T> spliterator();
        void forEach(Consumer<? super T> action); //loop over the iterable and call the consumer func on it.
    }

* spliterator (in both iterable and stream)\
    mainly used to split source data and process source data.\
    common methods: TryAdvance(Consumer action), trySplit(), ForEachRemaining(Consumer action), estimateSize().


### 0.4 Access Modifier
| | public | protected | default | private |
| --- | --- | --- | --- | --- |
|same class| Y | Y | Y | Y |
|same package subclass| Y | Y | Y | N |
|same package non-subclass|Y | Y | Y | N |
|diff package subclass| Y | Y | N | N |
|diff package non-subclass| Y | N | N | N |

<br>

### 0.5 Collections
* String



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