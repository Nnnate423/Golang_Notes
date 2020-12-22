# interview notes
# 1. network
## 1.1 TCP & UDP
* TCP 3-way & 4-way handshake
    * start: syn+seq
    * end: fin
* TCP & UDP comparasion
    * connection or conntectionless
    * stream-orient or datagram-orient
    * __reliable or not__
    * heading length difference, UDP 8 bytes, TCP 20 bytes?
    * TCP has __flow control__ and __congestion control__
* TCP reliable techniques
    * ack (number) -> deal with segment corrupted
    * sequence number -> deal with reply corrupted
    * sender timer & retransmission -> deal with req/reply lost
* well-known ports
    * 20/21 - ftp
    * 22 - SSH
    * 23 - telnet
    * 53 - DNS
* sockets
    * a port -> corresponds to program/ process
* TCP pipelining
    * selective repeat
    * go-back-N
* TCP flow-control & congestion control
    * flow-control 
        Windows based -> unacked message cannot be more tham the window size
    * congestion control
        * delay based:
            BBR \
            -> control rate to be around optimum point
        * loss based:
            reno,Cubic \
            -> only start to control when loss happens \
            -> the queue can already be very long \
            -> long response time
        * AIMD\
        additive increase: enhance fairness\
        multiply decrease: speed up converge\

## 1.2 HTTP & HTTPS
* persistent & non-persistent
* response time (rely on TCP)-> two RTT
* HTTP method (REST API)
    * GET
    * PUT
    * DELETE
    * POST
    * HEAD -> GET with no response body.
* response status code
    * 200 OK
    * 204 no content (put)
    * 301 Moved Permanently, location, 
    * 400 Bad Request
    * 404 Not Found
    * 405 method not allowed (put delete)
    * 409 conflict (post)
    * 505 HTTP Version Not Supported
* cookie & session
    * cookie
        * A cookie is used to create a user session layer on the top of stateless HTTP .
        * can be used to identify user and track activity.
    * session
        * TODO
* web caching
    * A Web cache or a proxy server is a network entity that satisfies HTTP requests on behalf of an origin Web server
    * improve performance -> reduce response time
    * reduce traffic load
* HTTPS protocol
    * TODO

## 1.3 DNS
* a large scale distributed system -> multi-level
* naming system
* how it works \
brower URL -> look up in local DNS -> if not found -> look up level DNS -> return IP -> establish TCP/IP connection -> send HTTP request to the server

# 2. operating system
System software, collection of system modules.
## 2.1 OS types
* batch OS - 批处理, SPOOLING
* Timesharing OS
* Real time OS
* Multi-tasking OS - 多道程序?

## 2.2 Characters
1. Concurrency & Parallel
2. Sharing
3. Virtual -> every process thinks it owns its virtual address space
4. Random -> don't know how fast a program executed & respond to random incidents

## 2.3 Functions
* Provide abstraction of hardware
* Provide services to users
* Resource management
    * CPU management -> scheduling
    * Storage management
    * memory management
    * I/O
    * devices
    * interfaces

## 2.4 Interrupt & Traps
* interrupt
    * support parallel operations of CPU & devices
    * external -> from I/O & others
    * asynchronized
    * signals (around 30)
        * -9 sigkill - cannot ignore

* trap
    * planed
    * synchronzied
* Fault & execption
    * internal -> from program
    * synchronized
* interrupt vector table\
    __handle interrupt__: \
    hardware send interrupt signal -> \
    CPU preparation: user mode to kernal mode and push PC& PSW into stack -> \
    look up interrupt vector table -> \
    interrupt_handler starts working and save states into stack -> \
    detect return instruction -> \
    program states recovered from stacks and continue

## 2.5 System call


## 2.6 Process & thread
* Process
    * __Process control block__ (PCB) - task_struct in linuc
        * includes PID, name, userID, relations, states, priority, code entry, queue pointer, virtual memory space, \
        file lists, CPU information like PSW,PC, stack pointer & pages pointers
    * unit of resource allocation:
        * memory space
        * files
        * address space (??)
    * process states
        * Running
        * Ready
        * waiting/blocked
        * suspended
        * new & terminated
    * creation of process
        * fock() in linux 
        ```
        pid=fork()
        if (pid<0){//err}
        if (pid==0){//new process}
        else {//old process}
        ```
       1. Allocate PID,PCB
       2. allocation location/space
       3. ini PCB (state=new)
       4. set queue pointer -> start queueing 
* Thread
    * unit of scheduling
    * System level -> windows
        * use System calls to create
    * User level
        * cannot take advantages of multi-processing by definition as it cannot rely on kernel
        * not visible to OS
        * if one thread hang -> all threads hang (correct?)
        * eg. java threads are user-level threads but they mapped to kernel-level threads so that they can still use multi-processing.
    * LWP thread
    * advantages over processes
        * ease of communication -> shared heap memory, no need kernel
        * lightweight to create and terminate
        * less consumption while threads switching\
            no need to change memory mapping\
            only need to store its dedicated stack
* IPC
    * Socket\
    use port number to contact a process.
    * RPC & RMI \
    details: 
    
    * pipe
    * Message queue
    * process shared memory

## 2.7 CPU and scheduling
* registers
    1. visible to users
    2. control & states: PC, PSW (program status word, indicate kernal/ user mode), Stack ptr
* scheduling

## 2.8 Synchronization
### 2.8.1 Mutual exclusion
competing for critical resource (critical section)
* Dekker\
busy waiting, pturn, qturn, turn
* Peterson
```
#pricess i
enter_region(i)
#critical section
leave_region(i)
```
### 2.8.2 Locks
* Spinlock -> usually used on multi-core computer \
-> reduce process switching cost (usually for short-term, fast critical region locking)
* Semaphore -> P, V operation -> long_term locking as scheduling cost is big
```
struc semaphore
{
    int count;
    queueType queue;
}

P(s)
{   s.count--;
    if (s.count<0){ 
        BLOCK process; 
        insert into queue; 
        re-schedule another process;
    }
}
 
V(s)
{   s.count++;
    // if there are still process waiting
    if (s.count<=0){
        wake() one process from s.queue;
        change its status to READY;
        insert into READY queue;
    }
}
```
* Mutex\
semaphore with count=1
* futex
    * mix of user& kernel level lock
    * avoid unnecessary trap into kernel
### 2.8.3 Monitor
1. entering of monitor is mutual exclusion
2. synchronization: can set condition -> have wait()/wake() operation
3. consists of condition queue, 
* HOARE\
when var c= condition
    * wait(c)
    * signal(c)\
    signal the first process in queue(c), and put current process into urgent queue \
    -> after it done, switch back to current process
* MESA
    two times less of switching than HOARE
    * signal(c)
    * notify(c) -> need while loop, timer and broadcast

## 2.9 Memory & virtual memory

## 2.10 file system

## 2.11 Deadlocks & Classic problems

## 2.12 Linux Commands
* Performance tools
    * top -u username\
        press k -> enter pid to kill\
        Virtual memory(virt)=Physical mem(res) + swap
    * vmstat\
    check system status:
    mem, io,swap, cpu
    * iostat\
    check cpu & i/o status
    * free -m, ds -ah\
    check disk space
    * lsof\
    list of open files
* Network
    * tcpdump -i interface src/dst ip_addr
        * -D check avail interfaces
    * netstat 
        * -a    all tcp udp connections
        * -at/au    only tcp/udp
        * -tp   pid
* process
    * ps -aux\
    checka all processes
    * jobs
    * fg
    * bg -> resume last suspanded bg work (Ctrl-z)

# 3. database
## 3.1 Transaction ACID 
support of transaction -> innoDB?
* Atomic
* Consistency
* Isolation
    * read uncommitted -> dirty read
    * read committed -> repeat select in a transaction may give different val
    * repeatable read -> will snapshot of init select and save for future
    * serializable -> performance loss 
* Durability

## 3.2 SQL

## 3.3 NoSQL DB
* redis
* MangoDB

# 4. languages
## 4.0 OOP
characters:
* Abstraction -> interfaces
* Encapsulation -> hide details or variables
* Inheritance -> inherit methods and variables
* Polymorphism\
Eg.
```
Animal a= Dog()
Animal b= Cat()
a.say()
b.say() 
```
## 4.1 java

## 4.2 python
### multi-threading
as GIL exists, python threads can only progress concurrently, not parallel\
therefore it does not provide additional computing power\
-> I/O intense tasks are prefered.
## 4.3 Golang

## 4.4 shell scripting
### 4.4.1 shell

### 4.4.2 sed

### 4.4.3 awk

# 5. docker & VM
## 5.1 comparasion
* docker is lightweight as it built on the OS kernel & they share its libs
* VM OS level isolations & docker is process level iso
* docker: same OS kernel, VM is different kernel.

# 6. Data Struture


# 7. experience 
## 1. C# & C++ dev
* liveness detection
* edit the C++ code and load it as a .dll file into C# wrapper
* multi-threading -> worker thread sends request
* C# web clients & restful API
* captured image converted to Base64
## 2. Python Kafka dev
* set up kafka message queues
* accept messages and process -> put into a message queue
* otherside, use multi-threading to reading from same topic -> consume message and send it using api
* get result and append to Redis
* Goal: solve concurrency problem as accept messages faster than the api processing the request. accept message speed 5/s, api 1/s -> open 5 threads.
## 3. Web app development

## 4. Distributed Maze Game

## 5. Primary-backup KV Store, Asyn

## 6. Paxos KV Store, Asyn