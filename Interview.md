# interview notes
# 1. network
## 1.1 TCP & UDP\
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

## 1.2 HTTP & HTTPS
* persistent & non-persistent
* response time (rely on TCP)-> two RTT
* HTTP method (REST API)
    * GET
    * PUT
    * DELETE
    * POST
    * HEAD
* response status code
    * 200 OK
    * 301 Moved Permanently, location, 
    * 400 Bad Request
    * 404 Not Found
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
## 1.4 DNS
* a large scale distributed system -> multi-level
* naming system
* how it works \
brower URL -> look up in local DNS -> if not found -> look up level DNS -> return IP -> establish TCP/IP connection -> send HTTP request to the server

# 2. operating system
System software, collection of system modules.
## 2.1 OS types
* batch OS - 批处理, SPOOLING
* Timesharing OS
* Real time OS - 多道程序?
* Multi-tasking OS
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

## 2.5 System call

## 2.6 Process & thread

# 3. database

# 4. languages
## 4.1 java

## 4.2 python

## 4.3 Golang

## 4.4 shell scripting

# 5. docker & VM

# 6. experience 