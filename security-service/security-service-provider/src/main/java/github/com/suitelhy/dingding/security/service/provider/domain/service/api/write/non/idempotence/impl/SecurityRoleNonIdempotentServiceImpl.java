package github.com.suitelhy.dingding.security.service.provider.domain.service.api.write.non.idempotence.impl;

import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityRole;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.security.service.api.domain.service.write.non.idempotence.SecurityRoleNonIdempotentService;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import github.com.suitelhy.dingding.security.service.provider.domain.service.SecurityRoleService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * (安全) 角色
 *
 * @Description (安全) 角色 - 业务接口.
 *
 * @Design
 * · 写入操作
 * · 非幂等性
 *
 * @see SecurityRole
 * @see github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo.Dubbo.Strategy.ClusterVo#FAIL_FAST
 */
@Service(cluster = "failfast")
public class SecurityRoleNonIdempotentServiceImpl
        implements SecurityRoleNonIdempotentService {

    @Autowired
    private SecurityRoleService securityRoleService;

    /**
     * 更新指定的角色
     *
     * @Description 增量更新.
     * · 完整的业务流程
     *
     * @param old_role      {@linkplain SecurityRole 原始版本业务全量数据}.
     * @param new_role_data 需要更新的数据.
     *                      · 数据格式:
     *                      {
     *                      "role_name" : [角色名称],
     *                      "role_description" : [角色描述]
     *                      }
     * @param operator      {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    public boolean update(@NotNull SecurityRole old_role, @NotNull Map<String, Object> new_role_data, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return securityRoleService.update(old_role, new_role_data, operator);
    }

    /**
     * 删除指定的角色
     *
     * @Description 删除成功后校验持久化数据; 主要为了避免在未提交的事务中进行对操作结果的非预期判断.
     * · 完整业务流程的一部分
     *
     * @param role     {@linkplain SecurityRole [（安全认证）角色]}
     * @param operator {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    public boolean delete(@NotNull SecurityRole role, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return securityRoleService.delete(role, operator);
    }

}
