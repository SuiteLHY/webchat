package github.com.suitelhy.dingding.sso.core.validate.code.sms;

/**
 * @author zhailiang
 */
public interface SmsCodeSender {

    /**
     * @param mobile
     * @param code
     */
    void send(String mobile, String code);

}
