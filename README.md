# webadmin

## 多语言支持
### 使用方式
* 前端请求方式  
请求时，Header中指定：Accept-Language
```text
curl --location 'http://localhost:8080/greeting/hello' \
--header 'Accept-Language: zh-CN'
```
* 后端使用

```java
import java.util.List;

public class GreetingController {
    public String hello() {
        String message = Localization.getLocalizedString("greeting.hello");
        message = Localization.getLocalizedString("greeting.hello", List.of("test"));
        return message;
    }
}
```
### 配置文件说明  
路径：resources/localization/messages  
规则：message_{locate}.properties  
yaml配置：
```yml
spring:
  messages:
    # 指定多语言配置文件路径
    basename: localization/messages
    encoding: UTF-8
    # 是否回退到系统默认语言环境，默认为true，LocaleConfig已配置默认语言，禁用此配置
    fallback-to-system-locale: false
    # 消息解析时是否始终使用 MessageFormat 格式化消息
    # MessageFormat.format("Hello, {0}! Today is {1,date}.", "Alice", new Date());
    always-use-message-format: true
```


