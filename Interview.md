# interview notes
# 1. network
## 1.1 TCP & UDP
* TCP 3-way & 4-way handshake
    * start: SYN -> SYN + ACK -> ACK
    * end: FIN\
    this is two way termination:\
    client first send FIN -> to indicate I want to close connection\
    but server may not ready to close as its buffer or queue not clear\
    when server is certain that it has no more data to send -> then it will send server side FIN to client.
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
    * encapsulation of TCP/IP protocol, an API -> easy for programmer to use
    * ip + port
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
    * 301/302 Moved Permanently, location, redirct
    * 400 Bad Request
    * 404 Not Found
    * 405 method not allowed (put delete)
    * 409 conflict (post)
    * 505 HTTP Version Not Supported
* cookie & session
    * cookie
        * A cookie is used to create a user session layer on the top of stateless HTTP .
        * can be used to identify user and track activity.
        * stored on client browser
    * session
        * In PHP, a session provides a way to store visitor preferences on a web server in the form of variables that can be used across multiple pages.
        * The information is retrieved from the web server when a session is opened at the beginning of each web page. The session expires when the web page is closed.
        * each client has its own session
        * web server maintains the information
* web caching
    * A Web cache or a proxy server is a network entity that satisfies HTTP requests on behalf of an origin Web server
    * improve performance -> reduce response time
    * reduce traffic load
* HTTPS protocol
    * TLS layer
    * after 3-way handshake to connect
    * server sends back public key & certificate
    * client use this to generate private key (a key encrypted by public key) and send back
    * server ack the private key.
    * start communication

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
    TODO
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

* rwlock\
shared-exclusive lock: suitable for read frequency much larger than write frequency cases.\
It ensures that thread only read latest values -> if this is not required, than no need for read lock.
    * if there is no write lock acquired -> any thread can apply read lock
    * if there is a read lock acquired -> no thread can apply write lock
```
/* 读模式下加锁  */
int pthread_rwlock_rdlock (pthread_rwlock_t *__rwlock);
 
/* 非阻塞的读模式下加锁  成功返回0，失败返回错误码*/
int pthread_rwlock_tryrdlock (pthread_rwlock_t *__rwlock);
 
# ifdef __USE_XOPEN2K
/*  限时等待的读模式加锁 */
int pthread_rwlock_timedrdlock (pthread_rwlock_t *__restrict __rwlock,
                                       __const struct timespec *__restrict __abstime);
# endif
 
/* 写模式下加锁  */
int pthread_rwlock_wrlock (pthread_rwlock_t *__rwlock);
 
/* 非阻塞的写模式下加锁 */
int pthread_rwlock_trywrlock (pthread_rwlock_t *__rwlock);
 
# ifdef __USE_XOPEN2K
/* 限时等待的写模式加锁 */
int pthread_rwlock_timedwrlock (pthread_rwlock_t *__restrict __rwlock,
                                       __const struct timespec *__restrict __abstime);
# endif
 
/* 解锁 */
int pthread_rwlock_unlock (pthread_rwlock_t *__rwlock);
```
In java: 写锁可降级
```
rwl.writeLock().lock()
rwl.readLock().lock()
rwl.writeLock().unlock()
//now write lock has been changed to read lock.
```
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

* implementation using semaphore
    * TODO

* __difference to semaphore__
1. semaphore allows multiple threads to enter the region.
2. monitor easier to use -> can lock on a certain condition like a shared object.

### 2.8.4 Java wait() & notify()
obj methods -> can only be used inside synchronized(obj){...}\
wait(): release obj lock, wait for notify()\
notify(): wake up a thread waiting for obj lock, and let it gets the lock\
notifyall(): wake up all threads waiting for obj lock. -> threads will compete for the obj lock.

## 2.9 Memory & virtual memory
* Overall model:

| Memory model |
|---|
| kernel space, 0xFFFF |
| user space, 0x0000 |

* User space model:

| User Space |
| --- |
| stack, expand down <- SP |
| empty space|
| heap, expand upwards |
| data, literals, const |
| codes <-PC |

* virtual address -> actual address
    * the address a program have is logical
    * need MMU to convert it into physical address

* memory management methods
    * 单一连续区 -> a process gets continuous address space
    * 固定分区 -> 一个分区一个进程 -> waste & cannot support large process
    * 可变分区 -> deal with fractions -> memory compaction
    * 页式 page\
    user process address space is divided into pages, 4k or 4M, index starts from 0.\
    通过页表查询物理地址 \
    Logical address:

    | page number | offset (位移量，也是页内地址) | 
    | --- | --- |

    Memory mapping\
    -> look up 页表
    | page number | block number | 
    | --- | --- |
    -> obtain physical memory block address.

    * 段式 \
    [check link for more info](https://blog.csdn.net/wang379275614/article/details/13765599)
    * 段页式

* swapping -> processes switched between memory and disk space

* virtual memory \
Combination of memory and disk -> to get a very large "memory" \
an abstraction of physical memory\
    
    
* 虚拟页式 paging\
combination of virtual memory and page.
1. not all pages are loaded when the process starts
2. if memory is full, replace old page
    * TLB -> consists of cache
    * PAGE FAULT
    缺页异常(有无空页框？)，违反权限，访问地址错误
    * looking up process
    check cache -> not hit -> MMU check page table -> 
    [click](https://blog.csdn.net/cwl421/article/details/49678371)
    
    
    * Replacement algorithms
    1. Optimum page replacement algo -> just as a critiria.
    2. FIFO -> implementation, page linked list
    3. SCR (second chance) -> FIFO list, check bit R, if 0, replace; if 1, set to 0 and put to the end of list.
    4. Clock -> circle design + pointer
    5. NRU not recently used -> check R,M bit, R is clear every period of time
    6. LRU least recently used -> close to OPT, but cost is large\
    implementation: double linked list + hashtable
    7. NFU not frequently used -> software counter
    8. AGING -> 计数器在+R前右移一位，R加在左端
    9. 工作集 -> 动态调整活跃页面的工作集

## 2.10 file system

## 2.11 Deadlocks & Classic problems
* Conditions of deadlock
    * mutual exclusion
    * occupy & wait -> can release before waiting
    * non-preemptive
    * circular waiting ->资源的有序分配 - 编号

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
## 6.1 Sorting
* insertion sort/ selection sort O(n^2)
* BubbleSort
* Mergesort
* quick sort -> O(n^2)
* counting sort -> O(n)
* topological sort

## 6.2 Searching
* linear search -> O(n)
* Binary search -> O(log(n))\
However it requires sorted list
* BFS
* DFS

## 6.3 Data structs
* Tree
    * binary tree
    * binary search tree (BST)
    * heap -> can be used to implement priority queue
    * tranversal order:
        1. in-order
        2. pre-order
        3. post-order

* linked list
    * normal linked list
    * double linked list

* Queue

* Stack

* hashtable/ dictionary
    * O(1) get
    * O(1) put

* array

# 7. experience 
## 1. C# & C++ dev
* liveness detection
* edit the C++ code and load it as a .dll file into C# wrapper
* multi-threading -> worker thread sends request
* C# web clients & restful API
* captured image converted to Base64

difficulties:
1. get used to visual c++ & c#
2. first time using REST api

## 2. Python Kafka dev
* set up kafka message queues
* accept messages and process -> put into a message queue
* otherside, use multi-threading to reading from same topic -> consume message and send it using api
* get result and append to Redis
* Goal: solve concurrency problem as accept messages faster than the api processing the request. accept message speed 5/s, api 1/s -> open 5 threads.

## 3. Web app development
* make use of HTML, CSS, PHP, JS, mysqli
* select slots and submit, can view my booking etc.
* make use of session of information 

Problem
1. should implement transaction

## 4. Distributed Maze Game
* Using Java RMI& socket to implement the game
* Java RMI as a boostraping point -> tracker of current gamers
* Random game players promoted as logical primary& backup server
* Fault tolerance, able to perform normally if players/server crash.\
backup promoted to primary if prim fails
* multi-threading model -> one dispatcher thread -> one request, one thread
* backend pinging for certain number of servers -> if dead -> contact servers


Problem -> need more precautions
1. server should not accept request if another server is online
2. need more sequencing between prim & backup -> sync request needs sequence number to keep linearizability\

Difficulties: 
1. debugging
2. more than 1 threads using a socket at two code blocks at the same time -> lockings

## 5. Primary-backup KV Store, Asyn
* event-driven model & unreliable link
* set timers for retransmission
* implement a view server to respond to servers ping requests & return view
* using view_number to prevent split brain problem -> needs ack with current view number from prim then can proceed to next view
* Replicated state machine -> prim only sends commands to backup
* cache all requests received while getting promoted
* fault tolerance

Difficulties:
1. replicated commands into server -> server shim -> (At Most Once semantic)
2. track every requests from each client -> seq number = uuid+ seq
3. how to make prim & backup linearizable -> seq number in the KV store -> order the sync commands to backup.

## 6. Multi-Paxos KV Store, Asyn
* using Paxos to implement a replicated state machine
* servers agree on a command for a certain slot
* phase 1: prepare; phase 2: accept; phase 3: commit
* leader election -> reduce conflicts
* eliminate phase 1 -> nomoreaccepted returned -> if reach majority -> skip phase 1

Problem: 
1. deadlock
2. log compaction & garbage collection
3. cannot deal with configuration change