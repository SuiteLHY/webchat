package github.com.suitelhy.dingding.sso.authorization.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * @author Suite
 * @Description 参考官方 Demo (<a href="https://github.com/jgrandja/spring-security-oauth-2-4-migrate/blob/master/resource-server/src/main/java/org/springframework/security/oauth/samples/web/MessagesController.java">
 * ->     spring-security-oauth-2-4-migrate/MessagesController.java at master · jgrandja/spring-security-oauth-2-4-migrate</a>).
 */
@RestController
public class MessagesController {

    @GetMapping("/messages")
    public String[] getMessages() {
        String[] messages = new String[]{"Message 1", "Message 2", "Message 3"};
        return messages;
    }

}
