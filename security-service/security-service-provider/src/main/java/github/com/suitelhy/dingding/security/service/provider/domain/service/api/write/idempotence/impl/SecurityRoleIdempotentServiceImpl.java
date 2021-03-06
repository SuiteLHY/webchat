package github.com.suitelhy.dingding.security.service.provider.domain.service.api.write.idempotence.impl;

import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityRole;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.security.service.api.domain.service.write.idempotence.SecurityRoleIdempotentService;
import github.com.suitelhy.dingding.security.service.api.domain.vo.Security;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import github.com.suitelhy.dingding.security.service.provider.domain.service.SecurityRoleService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;

/**
 * (安全) 角色
 *
 * @Description (安全) 角色 - 业务接口.
 *
 * @Design
 * · 写入操作
 * · 幂等性
 *
 * @see SecurityRole
 * @see github.com.suitelhy.dingding.core.infrastructure.config.dubbo.vo.Dubbo.Strategy.ClusterVo#FORKING
 */
@Service(cluster = "forking")
public class SecurityRoleIdempotentServiceImpl
        implements SecurityRoleIdempotentService {

    @Autowired
    private SecurityRoleService securityRoleService;

    /**
     * 新增一个角色
     *
     * @Description
     * · 完整的业务流程
     *
     * @param roleVo {@linkplain Security.RoleVo [安全模块 VO -> 角色]}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功 / 是否已存在相同的有效数据}
     */
    @Override
    public boolean insert(Security.@NotNull RoleVo roleVo)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return securityRoleService.insert(roleVo);
    }

    /**
     * 新增一个角色
     *
     * @Description
     * · 完整的业务流程
     *
     * @param role     {@linkplain SecurityRole [（安全认证）角色]}, 必须合法.
     * @param operator {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功 / 是否已存在相同的有效数据}
     */
    @Override
    public boolean insert(@NotNull SecurityRole role, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return securityRoleService.insert(role, operator);
    }

    /**
     * 更新指定的角色
     *
     * @Description 全量更新.
     * · 完整的业务流程
     *
     * @param role     {@linkplain SecurityRole [（安全认证）角色]}
     * @param operator {@linkplain SecurityUser 操作者}
     *
     * @return {@linkplain Boolean#TYPE 操作是否成功}
     */
    @Override
    public boolean update(@NotNull SecurityRole role, @NotNull SecurityUser operator)
            throws IllegalArgumentException, BusinessAtomicException
    {
        return securityRoleService.update(role, operator);
    }
}
