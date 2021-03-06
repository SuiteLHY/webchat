package github.com.suitelhy.dingding.sso.core.social.qq.connet;

import github.com.suitelhy.dingding.sso.core.social.qq.api.QQ;
import github.com.suitelhy.dingding.sso.core.social.qq.api.QQUserInfo;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

/**
 * 针对QQ的API适配器
 *
 * @author zhailiang
 * @Description ApiAdapter
 * @Editor Suite
 */
public class QQAdapter
        implements ApiAdapter<QQ> {

    /**
     * 测试当前API是否可用
     *
     * @param api
     * @return
     * @Description 暂不实现
     */
    @Override
    public boolean test(QQ api) {
        return true;
    }

    /**
     * @param api
     * @param values
     */
    @Override
    public void setConnectionValues(QQ api, ConnectionValues values) {
        QQUserInfo userInfo = api.getUserInfo();

        values.setDisplayName(userInfo.getNickname());
        values.setImageUrl(userInfo.getFigureurl_qq_1());
        values.setProfileUrl(null);
        // 服务商的用户ID
        values.setProviderUserId(userInfo.getOpenId());
    }

    /**
     * 获取用户个人资料
     *
     * @param api
     * @return <code>null</code>
     * @Description 无相关设计，不实现。
     */
    @Override
    public UserProfile fetchUserProfile(QQ api) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 更新用户状态
     *
     * @param api
     * @param message
     * @Description 无相关设计，不实现。
     */
    @Override
    public void updateStatus(QQ api, String message) {
        //do noting
    }

}
