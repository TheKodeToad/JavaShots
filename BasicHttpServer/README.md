# BasicHttpServer

Example:
```java
BasicHttpServer server = new BasicHttpServer("0.0.0.0", 8080, (method, page) -> new Response(200, "text/plain", "Hello, world!"));
server.run();
```

The server will keep running and block the thread until `server.close()` is called.
