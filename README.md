# Golang_Notes
# 1. go tools
```
    go build xxx.go
    go run xxx.so xxx.txt
    go doc
    go fmt
    go get
    go test
```
# 2. currency
## 1. goroutine
    not visible to OS by default
    * logical processor\
        if only 1 logical processor -> goroutines run concurrently, no parallel\
        but we can change num of logical processors so that they can map to goroutines
    * interleaving -> state not deterministic\
        order of execution between concurrent task is not known
    * race conditions\
        outcome depends on non-deterministic ordering
    * create goroutine
    ```
    go foo()
    ```
    * early exit
    if main thread finish -> goroutines will early exit.
    ```
    func main(){
        go printsth()
        printsth() //maybe only this will be printed
    }
    ```
    * synchronization\
    sync package
        * wait groups\
        sync.WaitGroup, contains a internal counter:
        increment counter for each goroutine to wait for\
        it go through if counter is 0.
        ```
        wg.Add(x) -> called on waiting thread.
        wg.Done() -> called on waited goroutines
        wg.wait() -> blocking call
        ```
## 2. channel
Typed, c:=make(chan, int)
```
c <- 3
x := <- c
//an example
func prod(v1 int, v2 int, c chan int){
    c<- v1*v2
}
func main(){
    c:=make (chan,int)
    go prod(1,2,c)
    go prod(2,3,c)
    a:=<-c
    b:=<-c
    fmt.Printf(a*b)
}
```

* Unbuffered channel\
it cannot hold data -> "c<-3" will block until 3 is read by other thread with "x := <-c", and vice versa\
can be directly used for synchronization (wait() & notify()), "c<-3" & "<-c".

* buffered chan\
sending will only block when buffer is full\
receiving blocks when buffer is empty\
use of buffer: sender(producer) & receiver (consumer) do not need to operate at same speed.
```
c:=make(chan int, 10)
//iterate until sender called close(c)
for i := range c{
    fmt.Println(i)
}
```


## 3. select

```
select {
    case a = <-c1:
        fmt.Println(a)
    case b = <-c2:
        fmt.Println(b)    
}
```
Both Send and Receive can be selected
```
select {
    case a = <-chanin:

    case chanout <- b:
        //if nobody is receiving on outchan it will block
    default:
        //xxx
}
```
Abort can be used
```
for {
    select{
        case a <-c:

        case <- abort:
            return
    }
}
```
# 3. recommended workspace
```
    defined by GOPATH var
    src
    pkg
    bin
```
# 4. variable
```
    dec: var x,y int
    types: int, float, string
    type dec: type temperature float64
                type ID int
    init: var x int = 100 OR var x = 100 OR x:=100 (only inside function)
        uninit var has 0 or empty value.

    pointer: var x *int =&x OR new() returns a pointer -> x := new(int); *x=3

    blocks: {} -> sequence of declaration and statements

    int: int8,int16,...int64, uint8,..., uint 64

    float: float32, float64 -> 

    string: rune (code point is a Unicode character) -> x:="Hi There", each byte is a rune (UTF-8 code point)
        unicode package: IsSpace(r rune), IsLetter(r rune), ToUpper(r rune)
        string package: Compare(a,b), Contains(s,sub), HasPrefix(s,sub), Index(s,sub), Replace(s,old,new,n), TrimSpace(s)
        Strconv package: Atoi(s), FormatFloat() -> float to str, ParseFloat() -> str to float
    
    constants: 
        1. const ( x=4
            z="Hi")
        2. iota 
            type Grade int
            const(
                A Grade = iota //1
                B               //2
                C               //3
            )

    type conversion: 
```
# 5. Memory
    heap and stack (mainly inside function)
    compiler determines stack vs heap
    garbage collection at background

# 6. fmt
    6.1 Printf: fmt.Printf("a"+ x)
                fmt.Printf("a %s", x)
    6.2 Scan: 
        var x int;
        num,err:= fmt.Scan(&x)

# 7. flow control
```
    if cond { statement }
    for init;term;update { statement } OR for term{ stmts } OR for { stmts }
    SWITCH
        Normal switch:
        switch x { //auto break at end of each case
            case 1:
                stmts
            case 2:
                stmts}
        Tagless switch: case contains a boolean and the first True is executed.
        switch{
            case x>1:
            case x<-1:
            default:}
    BREAK & CONTINUE
```
# 8. Data structure
## 8.1 Array
        dec: var x [5]int; var x [5]int=[5]{1,2,3,4,5}; x:=[...]{1,2,3,4}; 
        for loop: for i,v range x{ stmts}; 
## 8.2 Slice: 
variable size; ptr to the start; length is num of ele; capacity is max num of eles (start of slice to the end of arr);
```
    s1:=x[1:3]; s1:=[]int{1,2,3}; len(); cap()
    access slice: change slice will change the underlying array and slices also refer to the same array.
    append: x=append(x,1)
```
## 8.3 Hash Table & Maps
        key/value pairs; len(x)
        normal dec: var x map[string][int]; x=make(map[string][int]);
        literal: x:= map[string][int] {"xxx":1 }
        delete: delete(x,"xxx")
        two-value: id,p=x["xxx"] -> p is presence of the key
        for loop: for k,v := range x { stmts }
## 8.4 Struct
        type Person struct{
            id int
            name string
        } p1,p2
        dec: p3:=new(Person) OR p3:=Person(id:1,name="xxx")
        p1.name="xxx"
        make
        slice: make([]int, 10); make([]int,10,20);

# 9. Communication
## 9.1 RFC request for comment

* protocol packages:\
    "net/http": http.get(url)\
    "net": TCP/IP and socket programming\
            
        net.Dial("tcp","url:80")
* JSON: RFC 7159
    ```
    attri-val pair -> struct or map
    barr,err=json.Marshal(xx) return a byte[]
    var p Person; err:= json.Unmarshal(barr,&p);
    ```
* io/ioutil:
    ```
    dat,err:=ioutil.ReadFile("xxx.txt"); dat is []byte
    err:= ioutil.WriteFile("xxx.txt", dat, 0777)
    ```
    * os:
    ```
    os.Create()
    os.Open() -> f,err:=os.Open("xx.txt")
    os.Close()
    os.Read() read from file and read into []byte -> f.Read(barr) (make barr first)
    os.Write()
    ```

# 10. function
## 10.1 main(): 
```
func main(){fmt.Printf("...")}
```
## 10.2 init: 
```
func foo(x int) (int,int) {return x,x+1}
```
## 10.3 call by value/ reference: 
        call by value: normal-> not affect outside variable
        call by reference: pass a pointer -> will affect.
            eg. func foo(y* int) { *y=*y+1}
                foo(&x)
            pass array:
            eg2. func foo(arr *[3]int) int{ (*x)[0]=(*x)[0]+1 }
                foo(&x)
            pass slice:
            eg3. func foo(sli int) int{sli[0]=sli[0]+1}
                a:=[]int{1,2,3}
                foo(a)
## 10.4 function can be treated as other variables: 
        eg. can be pass as args, can be created dynamically etc.
        1. dec a var is function:
            var funcvar func(int) int
            func Intinc(x int) int{ return x+1}
            funcvar=Intinc; funcvar(1)
        2. pass as args
            func apply(funcvar func(int) int,x int){return funcvar(x)}
        3. anonymous func
            v:=apply(func (x int) int {return x+1}, 2)
        4. return a function
            func makeeg(x,y int) func(int) int{
                fn:=func (z,q int) int {
                    return x+y+z+q
                }
                return fn
            }
        5. Closure
            function + environment
            when function is passed -> its environment goes with it
            above eg, the x and y goes with fn.
        6. variable args number
            ...int means take any number of integer.
            func getmax(vals ...int){
                for _,v := range vals{
                    ...
                }
            }
        7. variadic slice argument
            getmax(1,2,4,6)
            vslice:=[]int{1,2,4,6}
            getmax(vslice...)
        8. defer func call
        only call is defered.
        func main(){
            defer fmt.Println("xxx")
        }

# 11. Class
    not conventional object-oriented
## 11.1 Associating Methods
1. with data
```
type myint int
func (m myint) foo(int) int{}
v.foo()
```
2. with struct
```
type Point struct{
    x int
    y int
} 
```
3. encapsulation
```
package data
type Point struct{x,y int}
func (p Point) init(x,y int){}
func (p Point) Double(v float64){...}
...
package main
var p data.Point
p.init()
...
```
## 11.2 Receiver, referencing & dereferencing
```
func (p *Point) Modify(x int){
    p.x=p.x+x
}
now this function is able to change p as p is passed by reference.
And there is no need to deference nor reference, like *(p).x.
p:=Point(3,4)
p.Modify(1) -> no need to reference
```
## 11.3 OOP Features
1. Polymorphism\
    -> override function with same signature

2. Interface\
signature of the methods
```
type Shape interface{
    Area() float64
    Perimeter() float64
}
# no explicit indication needed
type Triangle {}
func (T Triangle) Area() float64 {...}
func (T Triangke) Perimeter() float64 {...}
```
empty interface: interface{}
```
func Printme(var interface{}){
    fmt.Println(var)
}
```
3. concrete types and interface
```
func (d *Dog) Speak(){
    if d==nil {
        fmt.Println("noise ")
    }else{
        fmt.Println(d.name)
    }
}
var s1 Animal
var d1 Dog{"Bob"}
s1=d1
s1.Speak()
---nil dynamic value with valid dynamic type is allowed---
var s1 Animal
var d1 *Dog
s1=d1
s1.Speak()
---nil dynamic type nor value-> cannot call the method---
var s1 Animal
s1.Speak() -> error
``` 
4. type assertions

type assertions
```
func xxx() bool{
rect,ok:=s.(Rectangle)
if ok{
    do something
}
Tri,ok:=s.(Triangle)
if ok {
    do something
}}
```
type switch
```
switch :=sh:=s.(type){
    case Rectangle:
        ...
    case Triangle:
        ...
}
```
5. error interfance
```
type error() interface{
    Error() string
}
```
if operation correct: err==nil

eg. 
```
f,err:=os.Open("xxx")
if err!=nil {
    ...
}
```