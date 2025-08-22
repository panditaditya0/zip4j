@GzipResponse Annotation
=========================

The `@GzipResponse` annotation provides a declarative way to enable **response compression** in your application.  
It can be applied on **method** level, offering fine-grained control over compression behavior.

---

## Features

- **Flexible scope** – Apply to individual methods or entire controllers/classes.
- **Compression control** – Configure algorithm, level, MIME types, and minimum size.
- **Conditional execution** – Enable compression only under certain conditions or for specific status codes.
- **Fail-safe fallback** – Automatically returns the original (uncompressed) response if compression fails.
- **Streaming support** – Stream compressed responses for large payloads.
- **Optional logging** – Track and log compression stats for debugging and optimization.
- **Force mode** – Apply compression even if the client did not request it.

## Usage
### Checkout updated dependency [MvnRepo](https://mvnrepository.com/artifact/io.github.panditaditya0/gzip-response)
### Add Maven Dependency 
```java
    <dependency>
        <groupId>io.github.panditaditya0</groupId>
        <artifactId>gzip-response</artifactId>
        <version>2.0.0</version>
    </dependency>
```

### Method-Level Example

```java
@RestController
@RequestMapping("/api")
public class UserController {

    @GetMapping("/users")
    @GzipResponse(
        minResponseSize = "4KB",
        algorithm = CompressionAlgorithm.GZIP,
        mimeTypes = {"application/json"},
        compressionLevel = 7,
        logCompressionStats = true
    )
    public List<User> getUsers() {
        return userService.getAllUsers();
    }
}
```

---

## Parameters

| Parameter              | Type                   | Default Value                        | Description                                                                  |
|-------------------------|------------------------|--------------------------------------|-----------------------------------------------------------------------------|
| `minResponseSize`       | `String`               | `"2KB"`                              | Minimum response size before compression is applied. Supports KB/MB units.  |
| `algorithm`             | `CompressionAlgorithm` | `GZIP`                               | Compression algorithm (e.g., `GZIP`, `DEFLATE`).                            |
| `mimeTypes`             | `String[]`             | `{"application/json","text/plain","text/html"}` | Restrict compression to these MIME types.                        |
| `compressionLevel`      | `int`                  | `6`                                  | Compression level (1 = fastest, 9 = highest compression).                   |
| `condition`             | `String`               | `""`                                 | SpEL/conditional expression to decide when to compress.                     |
| `fallbackToOriginal`    | `boolean`              | `true`                               | If true, returns the original response if compression fails.                |
| `logCompressionStats`   | `boolean`              | `false`                              | Enables logging of compression stats (ratio, time, etc.).                   |
| `streaming`             | `boolean`              | `false`                              | Enables streaming compression for large responses.                          |
| `force`                 | `boolean`              | `false`                              | Forces compression even if the client does not request it.                  |
| `statusCodes`           | `int[]`                | `{200}`                              | Apply compression only for these HTTP status codes.                         |

---

## Example Scenarios

- **Compress JSON APIs only**  
  ```java
  @GzipResponse(mimeTypes = {"application/json"})
  ```

- **Force compression regardless of client request**  
  ```java
  @GzipResponse(force = true)
  ```

- **Stream large file responses**  
  ```java
  @GzipResponse(streaming = true, algorithm = CompressionAlgorithm.GZIP)
  ```

- **Apply only for success responses (200, 201)**  
  ```java
  @GzipResponse(statusCodes = {200, 201})
  ```

---

## 📊 Logging Example

When `logCompressionStats = true`, logs may look like:

```
[DEBUG] GzipResponse: Compressed 48KB → 12KB (75% reduction) in 15ms
```

## Best Practices
- Use `streaming = true` for large datasets to reduce memory usage.
- Avoid compressing already compressed formats (e.g., `application/zip`, `image/png`).
- Tune `compressionLevel` based on **performance vs. bandwidth** needs.
- Use `condition` to apply compression dynamically based on headers, user roles, or business logic.
