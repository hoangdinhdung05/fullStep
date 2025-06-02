
<h1 align="center">BackendService</h1>

---

## 1. Prerequisite

- Cài đặt JDK 17 nếu chưa cài: [cài đặt JDK](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- Install Maven 3.5+ nếu chưa cài: [cài đặt Maven](https://maven.apache.org/install.html)
- Install IntelliJ nếu chưa cài: [cài IntelliJ](https://www.jetbrains.com/idea/)
- Install Docker nếu chưa cài: [cài đặt Docker](https://docs.docker.com/get-docker/)

---

## 2. Technical Stacks

- Java 17
- Maven 3.5+
- Spring Boot 3.1.4
- Spring Data Validation
- Spring Data JPA
- PostgreSQL/MySQL (optional)
- Lombok
- Devtools
- Docker
- Docker compose

---

## 3. Build & Run Application

### ◾ Run application thủ công

- Mở terminal tại thư mục `backend-service`:

```bash
./mvnw spring-boot:run
```

---

### ◾ Run application với Docker

```bash
mvn clean install -P dev
docker build -t backend-service:latest .
docker run -it -p 8080:8080 --name backend-service backend-service:latest
```

---

## 4. Test

### ✅ Check health với `cURL`

```bash
curl --location 'http://localhost:8080/actuator/health'
```

> 📥 Response:

```json
{
  "status": "UP"
}
```

---

### 🔗 Truy cập [Backend service](http://localhost:8080) (để test các API)

---

## ✍️ Author

- **Tên:** Hoàng Đình Dũng
- **Trường:** Đại học Công nghệ Giao thông Vận tải
- **Email:** hoangdinhdung0205@gmail.com
- **GitHub:** [github.com/hoangdinhdung05](https://github.com/hoangdinhdung05)
