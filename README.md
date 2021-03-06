# interview notes
<a name="toc"></a>
### Table of Contents
1. [ Network ](#network)
2. [ Operating System ](#OS)
3. [ Database ](#db)
4. [ languages ](#languages)
5. [ Docker & VM ](#docker)
6. [ Data Structure ](#data_structure)
7. [ Experience ](#experience)


<a name="network"></a>
# 1. [ Network ](#toc)
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
    * 301& 302 Moved Permanently & temply, location, django: redirct
    * 304 not modified -> static file loaded 
    * 400 Bad Request
    * 404 Not Found
    * 405 method not allowed (put delete)
    * 409 conflict (post)
    * 505 HTTP Version Not Supported
* cookie & session
    * cookie
        * A cookie is used to create a user session layer on the top of stateless HTTP.
        * can be used to identify user and track activity.
        * stored on client browser
        * user browser may have a sessionid as cookie and append it in every request\
         -> server knows which session to look up to
    * session
        * In PHP, a session provides a way to store visitor preferences on a web server in the form of variables that can be used across multiple pages.
        * The information is retrieved from the web server when a session is opened at the beginning of each web page. The session expires when the web page is closed.
        * each client has its own session
        * web server maintains the information
        * sessions are required for implementing login function
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
* DHCP -> bootstrap
    * as computer first starts up, it has no IP address
    * therefore it cannot use TCP -> only UDP, 0.0.0.0 as source and 255.255.255.255 as dest to broadcast
    * receive an ip address in LAN -> convert to WAN IP through router if it has request.

## 1.3 DNS
* a large scale distributed system -> multi-level
* naming system
* how it works \
brower URL -> look up in local DNS -> if not found -> look up level DNS -> return IP -> establish TCP/IP connection -> send HTTP request to the server

## 1.4 RESTful API
* compare with SOAP\
    soap = RPC + HTTP + XML, parsing xml makes it slower than REST.\
    SOAP is harder to use, not as clear as REST.\
    SOAP has better security feature, it supports SSL & others.
* Methods
    * GET
    * DELETE
    * PUT -> indempotency (update) \
        -> making multiple identical requests has the same effect as making a single request
    * POST -> not indempotency
    * HEAD -> GET with no response body.

## 1.5 CORS issues
* IP, Domain, url, host
    * domain is a name of organization
    * url = protocol(like https) + 主机(like www) + domain
    * ip reqresents a machine. a domain can include multiple machines (related to domain, ip mapping)
    * host is the hostname and ip of a domain name.
* what is CORS 
    * def: restriction on resource sharing between different origin. for security.
    * default restriction: same domain, same port, same protocol(like http and https is diff origin)
    * to allow sepcific origin, set allow-origin in header. to allow cookies, set allow-credentials in header. (server side)
    * requst handling
        * simple reqs: GET, HEAD, POST(only with certain content type)
        * non simple ones: ajax client will send a OPTIONS req first. and then the original request, like POST.\
        OPTIONS method: HTTP 的 OPTIONS 方法 用于获取目的资源所支持的通信选项. find supported options like origin, credentials, content type etc.

<a name="OS"></a>
# 2. [ Operating System ](#toc)
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
Some kernel functions that are exposed to user space in certain way.\
implemented in kernel and listed on sys_call_table.
* how to add new syscall
    1. SYSCALL_DEFINE\[x](vars)\
        use this to implement the syscall function
    2. provide Makefile\
        obj+=...\
        then add this new syscall folder to kernel Makefile core (core-y).
    3. Add to sys_call_table and header (arch/x86/entry/syscalls/syscall_64.tbl); (include/linux/syscalls.h)
        in header, add like:\
        ```
        asmlinkage long sys_xxx();
        ```
    4. recompile the kernel use Makefile (check using "make menuconfig").

* What happened when use syscall
    1. how to call
    ```
    #include <linux/unistd.h>
    int printmsg(int i){
    return syscall(your_syscall_number, i); } 
    ```
    2. how it works
    syscall -> context switch (into kernel) -> look up sys_call_table -> call kernel function -> return, context switch (into user space) -> end
## 2.5.1 Module
按需载入的，动态加载卸载的模块（代码块）。 
* How to write one
    1. implement __init, __exit\
        import init.h, kernel.h, module.h\
        bind these two functions with module_init(), module_exit()
    2. Makefile
        obj-m += xxx.o
        make -C /lib/modules/$(shell uname -r)/build M=$(PWD) modules
    3. make, insmod. rmmod
* Relation with driver & devices
    1. driver can be either build-in into kernel or a module(can be installed at runtime)
    2. driver associate OS with hardwares.
    3. devices can be character device(tty), block device (disk) etc.
## 2.6 Process & thread
* Process
    * __Process control block__ (PCB) - task_struct in linuc
        * includes PID, name, userID, relations, states, priority, code entry, queue pointer, virtual memory space, \
        file lists, CPU information like PSW,PC, stack pointer & pages pointers
        ![ task_struct ](resource/task_struct.png)
    * process hierachy
        1. each proc has exactly one parent except init proc.
        2. if parent die, the proc will be adopted by init.
        3. parent 
            * real parent: the current parent of the proc
            * parent: the proc that will handle the termination signal of the child.
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
        * techniques
            1. Copy-on-Write 
            * Parent and the child process initially share same physical pages
            * Linux terminology: physical pages are called page frames or just frames; “pages” refers to virtual pages.
            * As long as they are shared, they cannot be modified, Any attempt to write a shared frame triggers an exception, Kernel duplicates the page into a new page frame and marks it as writable.
            * Original page frame remains write-protected: kernel checks whether the writing process is the only owner of the page frame. If so, makes the page frame writable for the process
            2. clone()
            3. vfork()
        * fock() in linux 
        ```
        pid=fork()
        if (pid<0){//err}
        if (pid==0){//new process}
        else {//old process}
        ```
        * procedure
            1. Allocate PID,PCB
            2. allocation location/space
            3. ini PCB (state=new)
            4. set queue pointer -> start queueing 
    * switching
        * Done in Linux schedule() function 
        * Switch the Page Global Directory(which points to PML4E) to install a new address space
        * Switch the Kernel Mode stack and the hardware context using the switch_to macro
        * Two task may have same cr3 if they belong to same task
        * Different cr3 val points to diff page global dir -> which translate to different physical addr

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
    * RPC & RMI 
        * details: \
        Client Stub & Server Skeleton：They are agent responsible for sending parameters of remote calls and\
        hide RPC details from programmer\
        Registry：Server bind its remote obj to it so that the client can lookup the stub using its naming.\
        The stub and skeleton uses tcp sockets as underlying communicaiton.\
        all obj passed through RMI needs to extends serializable interface.
        * Process: 
            1. Server extends Remote interface
            2. Server implement its interface and extends UnicastObject
            3. Server bind its remote obj into rmiregistry
            4. client lookup with the name -> obtain stub
            5. client call remote methods -> stub send serialized parameter/obj to server skeleton
            6. server deserialize the request and send response
            7. stub deserialize reply for client    
    * pipe
    * Message queue
    * process shared memory

## 2.7 CPU and scheduling
* registers
    1. visible to users
    2. control & states: PC, PSW (program status word, indicate kernal/ user mode), Stack ptr
* scheduling
    1. criteria
        * min response time
        * max throughput
        * fairness
    2. algorithms
        * FCFS
        * RR -> each proc get a small unit of CPU time
        * Shortest job first(SJF) 
            - best to optimize avg response time
        * Shortest remaining time first(SRTF) 
            - best to optimize avg response time
            - preemptive version of SJF
            - cons: 1. starvation 2. hard to predict future
        * Priority scheduling 
            - niceness in linux, from -20 to 19, most favored to least favored
            - priority inversion, low p proc gets a lock, high p preemot and try get it but blocked. now mid p proc runs and it is the highest p ready process, then it will starve the high p process. -> solution: priority inheritance.
        * multi-level feedback queue -> cpu-bound task drops to low level
        * Completelu fair scheduling CFS
            - based on RBT, indexed by virtual running time.
            - Time slice for a task = period * (weight of task) / (total weight of runqueue)
            - vruntime += (time process ran) * (load weight niceness 0 ) / (load weight of this process)
             - therefore, vruntime increment for each process that run out time slice is the same.
        * brain fuck scheduler
            - designed for linux desktop.
            - skip list




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
an abstraction of physical memory
    
    
* 虚拟页式 paging\
combination of virtual memory and page.
    1. not all pages are loaded when the process starts
    2. if memory is full, replace old page
        * TLB -> consists of cache
        * PAGE FAULT
        缺页异常(有无空页框？)，违反权限，访问地址错误
        * <b>looking up process</b>
        ![proc](resource/addr_trans_process.png)
        check TLB -> if no hit -> MMU translate -> check cache -> if no hit -> load from main memory.\
        therefore, TLB is to reduce translation times, cache is to reduce times to read from memory.
        
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

* memory management in linux - segmentation & paging\
    segmentation & paging
    ![seg_paging](resource/segmentation_paging.png)
    the process follows: logical addr -> linear addr -> physical addr.\
    \
    paging in detail:
    ![paging](resource/page_lookup.png)
    look up 4 levels of page tables.
    * PML is pointed by CR3.
    * CR3 also contains PCID(process context ID or address space ID)
        * PCID enables faster context switching
        * it distinguishs diff process's address space 
        * TLB flushed if CR3 changed
    * canonical address
        * bit 48-63 is a signed extension of bit 47
        * kernel space starts with 0xffff
        * user space starts with 0x0000
    * page table isolation (PTI)
        * Two sets of page tables
        * When the kernel is entered via syscalls, interrupts or exceptions, the page tables are switched to the full “kernel” copy
        * When the system switches back to user mode, the user copy is used again
        * The userspace page tables contain only a minimal amount of kernel data
    * page types
        1. Unreclaimable\
            free pages, kernel pages, reserved pages, locked pages
        2. Swappable\
            anonymous pages of user addr space, mapped pages of tmpfs file sys( eg. shared memory) .
        3. Syncable\
            mapped pages of user addr space, block device buffer pages, pages of disk cache like inode cache.
        4. Discardable - unused pages.
        - mapped means it is mapped to some file. anonymous means it should be saved to swap area.
    * page reclaimation\
        LRU with inactive and active list.\
        simple way: accessed in inactive list -> go to active list; tail of inactive and active list gets evicted while list is full and activation happens.\
        Use refault distance and min access distance to decide if the page goes into inactive or active list when page fault happens. (R-E)<NR_ACTIVE ? active : inactive


    * memory allocators\
        general system
        ![allocator](resource/mem_allocator.png)

        1. buddy system
            - 2^n as size of blocks to be allocated
            - if not small blocks free, split bigger blocks
            - merge contiguous blocks with same size n into 2*n.
            - external fragmentation is eliminated, but internal still exists.
                1. Objects smaller than a page
                2. Objects of different sizes are allocated and freed, resulting in “holes” inside a page
        2. object allocator
            1. SLOB (Simple List of Blocks)\
                Goal: compactness of memory, minimum memory overhead
                - first proposal: first fit, merge neighbours while freed.
                - optimized by 1. use list of different object sizes 2. best fit
            2. SLAB
                Goal: cache friendly, waste some space to gain cache efficiency
            
            3. SLUB
                Goal: speed


## 2.10 file system
1. object in common file model
    * superblock\
        stored info about a mounted fs.

    * inode\
        general info about a file.

    * file\
        info about interaction with an opened file and a process.

    * dentry\
        stores information about the linking of a directory entry with the corresponding file.

2. data structure - inside task_struct
    * fs_struct\
        contain info about user, root, pwd etc.
    * files (file opened)\
        contains an important pointer: struct file ** fd  (pointer to array of file obj ptr)\
        index 0,1,2 of the array is: stdin, stdout, stderr
3. journaling file system\
    Keeps track of changes not yet committed to the file system's main part.\
    If system crashes, will be able to can recover to a consistent state.
    * journaling in ext3 - handled by journaling block device
        - journal: Metadata and content are saved in the journal
        - ordered: Only metadata is saved in the journal. Metadata are journaled only after writing the content to disk. This is the default.
        - writeback: Only metadata is saved in the journal. Metadata might be journaled either before or after the content is written to the disk



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

<a name="db"></a>
# 3. [ Database ](#toc)
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
### 3.2.1 Create & populate Tables
* constraints
    * PRIMARY KEY
    * FOREIGN KEY, REFERENCES tablename(entry1,entry2)
    * NOT NULL
    * UNIQUE -> unique + not null = candidate key
    * CHECK, CHECK (price > 0)
* diff between view and a new sub-table
    '''
    CREATE VIEW singapore_customers1 AS
    SELECT c.first_name, c.last_name
    FROM customers c 
    WHERE country='Singapore';
    '''
    * view updated with the original table
    * a new table will not auto updated

### 3.2.2 Logic
* CASE 
'''
CASE 
    WHEN
    THEN
    ELSE
END
'''
* WHERE AND
'''
WHERE (X=Y AND Z=Y)
AND (XXX BETWEEN 1 AND 10)
'''
* COALESCE(col2 , 0)
```
SELECT column1, column2, COALESCE(column2, 0) %if column2 is null, return 0
FROM example
WHERE column2 IS NULL;
```
### 3.3.3 quires
* Aggregate query
    * count()
    '''
    SELECT COUNT(DISTINCT c.customerid) 
    FROM customers c;
    '''
    * MAX()
    * AVG()
    * GROUP BY
    apply aggregate func on groups
    ```
    SELECT c.first_name, c.last_name, SUM(g.price)
    FROM customers c, downloads d, games g
    WHERE c.customerid = d.customerid
    AND d.name = g.name AND d.version = g.version
    GROUP BY c.customerid, c.first_name, c.last_name;
    ```
    * SUM()
    * EXTRACT()
    ```
    SELECT c.country, EXTRACT(YEAR FROM c.since ) AS regyear, COUNT(*) AS total
    FROM customers c, downloads d
    WHERE c.customerid = d.customerid
    GROUP BY c.country, regyear
    ORDER BY regyear, c.country;
    ```
    * HAVING -> aggregate func not allowed in where
    ```
    SELECT c.country
    FROM customers c
    GROUP BY c.country
    HAVING COUNT(*) >= 100;
    ```
* nested queries
    *  (NOT) IN, ANY, ALL(similar to max)
    ```
    SELECT d.name
    FROM  downloads d
    WHERE d.customerid = ANY (
    SELECT c.customerid
    FROM customers c
    WHERE c.country = 'Singapore');
        
    SELECT g1.name, g1.version, g1.price
    FROM  games g1
    WHERE g1.price >= ALL (
    SELECT g2.price
    FROM  games g2);
        
    SELECT g1.name, g1.version, g1.price
    FROM  games g1
    WHERE g1.price >= ANY (
    SELECT g2.price
    FROM  games g2);

    SELECT c1.country
    FROM customers c1
    GROUP BY c1.country
    HAVING COUNT(*) >= ALL (
    SELECT COUNT(*)
    FROM customers c2
    GROUP BY c2.country);
    ```
    * EXIST -> test if there is any result
    ```
    SELECT c.first_name, c.last_name
    FROM customers c
    WHERE NOT EXISTS( 
    SELECT *   
    FROM games g  
    WHERE g.name ='Aerified'  
    AND NOT EXISTS (      
    SELECT *      
    FROM downloads d     
    WHERE d.customerid = c.customerid     
    AND d.name = g.name      
    AND d.version = g.version));
    ```
    * UNION, INTERSECT, EXCEPT

* slow queries -> use of NOT EXIST\
    especially double negation

* EXPLAIN, EXPLAIN ANALYSIS\
    Here you can check execution plans 

* JOIN\
    there are sql joins
    * inner join
    * cross join
    * full (outer) join
    * left join
    * right join

    and there are joins in execution plan:
    * hash joins -> hash is how they computed
    * hash semi joins 
    * hash anti joins
    ```
    R1 join R2 on a condition theta returns the rows of the cross product R1 x R2 that agree with theta.
    R1 semi-join R2 on a condition theta returns the rows of R1 that agree with theta in the cross product R1 x R2.
    R1 semi-join R2 on a condition theta returns the rows of R1 that do not agree with theta in the cross product R1 x R2.

        R1(A, B) = {(a, 1), (a, 2), (b, 1), (c, 3)}, 
        R2(C, D) = {(1, d), (2, d), (4, d)}
        then

        R1 x R2 = {
        (a, 1, 1, d), (a, 1, 2, d), (a, 1, 4, d),
        (a, 2, 1, d), (a, 2, 2, d), (a, 2, 4, d),
        (b, 1, 1, d), (b, 1, 2, d), (b, 1, 4, d),
        (c, 3, 1, d), (c, 3, 2, d), (c, 3, 4, d)}

        R1 join R2 on R1.B = R2.C = {
        (a, 1, 1, d),
        (a, 2, 2, d),
        (b, 1, 1, d)}

        R1 semi-join R2 on R1.B = R2.C = {
        (a, 1),
        (a, 2),
        (b, 1)}

        R1 anti_join R2 on R1.B = R2.C = {(c, 3)}

    ```
    * nested loop join\
        algo with nested loops to join data -> inside execute plan, not sql
        ```
        for  i in tb1:
            for j in tb2:
                check conditions: 
                    add to new tb
        ```
    



## 3.3 NoSQL DB
* redis
* MangoDB

## 3.4 Inbexes
* B Tree

* B+ Tree

* Index scan & Bitmap hash scan & Sequencial Scan


<a name="languages"></a>
# 4. [ Languages ](#toc)
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
### OOP
type()\
dir(obj) -> show all the methods including magic functions
### shortcut functions
count = collections.Count(some_list) -> return a dict with frequency as value.
dict.get(x,0) -> if x not exist, return 0, can be 1 less "if else"
## 4.3 Golang

## 4.4 shell scripting
### 4.4.1 shell

### 4.4.2 sed

### 4.4.3 awk

## 4.5 deep copy & shallow copy

<a name="docker"></a>
# 5. [ Docker & VM ](#toc)
## 5.1 comparasion
* docker is lightweight as it built on the OS kernel & they share its libs
* VM OS level isolations & docker is process level iso
* docker: same OS kernel, VM is different kernel.

<a name="data_structure"></a>
# 6. [ Data Structure ](#toc)
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
    * red black tree\
    n internal nodes has a height of at most 2*log(n+1)
        - properties
            - nodes are red or black
            - root is black
            - all leaves black
            - if a node is red, its children are black
            - every path from a node to a descendent leaf must contain the same number of black nodes(path)

* linked list
    * normal linked list
    * double linked list

* Queue

* Stack

* hashtable/ dictionary
    * O(1) get
    * O(1) put

* array


<a name="experience"></a>
# 7. [ Experience ](#toc)
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
* currency handling -> synchronized on server & avoid open multiple sockets to same server at a time.

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

## 7. Microsoft Access development
* ER-diagram
* implementation

## 8. Django
* ToDo app -> add task via form, and able to delete and modify
* reconstruct ## 3 project using django as backend -> for modern development
* transaction supported

## 9. Attire Checking system
* integrate with sensors and gentry with motors
* data collection, processing
* objection recognition with pretrained model
* application to run inference and open/close gentry.

## 10. Java -> Spring boot

## 11. Java -> SSM Spring, spring mvc, MyBatis
