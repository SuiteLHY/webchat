package github.com.suitelhy.dingding.user.service.provider.domain.service;

import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityModel;
import github.com.suitelhy.dingding.core.infrastructure.exception.BusinessAtomicException;
import github.com.suitelhy.dingding.core.infrastructure.util.CalendarController;
import github.com.suitelhy.dingding.security.service.api.domain.entity.User;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUser;
import github.com.suitelhy.dingding.security.service.api.domain.entity.security.SecurityUserRole;
import github.com.suitelhy.dingding.security.service.api.domain.service.read.SecurityUserReadService;
import github.com.suitelhy.dingding.security.service.api.domain.service.write.idempotence.SecurityUserIdempotentService;
import github.com.suitelhy.dingding.security.service.api.domain.service.write.non.idempotence.SecurityUserNonIdempotentService;
import github.com.suitelhy.dingding.user.service.api.domain.entity.UserAccountOperationInfo;
import github.com.suitelhy.dingding.user.service.api.domain.entity.UserPersonInfo;
import github.com.suitelhy.dingding.user.service.api.domain.service.read.UserReadService;
import github.com.suitelhy.dingding.user.service.api.domain.service.write.idempotence.UserIdempotentWriteService;
import github.com.suitelhy.dingding.user.service.api.domain.service.write.non.idempotence.UserNonIdempotentWriteService;
import github.com.suitelhy.dingding.user.service.provider.domain.event.UserEvent;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户 - 业务 <- 测试单元
 *
 * @Description 测试单元.
 */
@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @Reference
    private SecurityUserReadService securityUserReadService;

    @Reference
    private SecurityUserIdempotentService securityUserIdempotentService;

    @Reference
    private SecurityUserNonIdempotentService securityUserNonIdempotentService;

    @Autowired
    private UserEvent userEvent;

    /**
     * 获取(测试用的)操作者信息
     *
     * @return {@link SecurityUser}
     */
    private @NotNull SecurityUser operator() {
        final SecurityUser securityUser = securityUserReadService.selectByUsername("admin");

        System.err.println("【调试用】获取(测试用的)操作者信息 => "
                .concat((null != securityUser) ? securityUser.toString() : "{}"));

        return securityUser;
    }

    /**
     * 获取测试用的用户相关 {@link EntityModel} 集合
     *
     * @return {@link Map}
     * · 数据结构:
     * {
     * "user": {@link User},
     * "userAccountOperationInfo": {@link UserAccountOperationInfo},
     * "userPersonInfo": {@link UserPersonInfo}
     * }
     *
     * @see this#getEntityForTest(Integer)
     */
    private @NotNull /*User*/Map<String, EntityModel<?>> getEntityForTest() {
        return getEntityForTest(null);
    }

    /**
     * 获取测试用的用户相关 {@link EntityModel} 集合
     *
     * @param seed
     *
     * @return {@link Map}
     * · 数据结构:
     * {
     * "user": {@link User},
     * "userAccountOperationInfo": {@link UserAccountOperationInfo},
     * "userPersonInfo": {@link UserPersonInfo}
     * }
     */
    private @NotNull /*User*/Map<String, EntityModel<?>> getEntityForTest(Integer seed) {
        /*return User.Factory.USER.create(20
                , new CalendarController().toString()
                , ip()
                , new CalendarController().toString()
                , "测试用户"
                        .concat(new CalendarController().toString().replaceAll("[-:\\s]", ""))
                        .concat((null == seed || seed < 0) ? "" : Integer.toString(seed))
                , "test123"
                , "测试数据"
                , null
                , "测试"
                        .concat(new CalendarController().toString().replaceAll("[-:\\s]", ""))
                        .concat((null == seed || seed < 0) ? "" : Integer.toString(seed))
                , Human.SexVo.MALE);*/
        @NotNull Map<String, EntityModel<?>> result = new HashMap<>(3);

        @NotNull User newUser = User.Factory.USER.create(
                "测试用户".concat(new CalendarController().toString().replaceAll("[-:\\s]", ""))
                        .concat((null == seed || seed < 0) ? "" : Integer.toString(seed))
                , "test123");

        final @NotNull String currentTime = new CalendarController().toString();
        @NotNull UserAccountOperationInfo userAccountOperationInfo = UserAccountOperationInfo.Factory.USER.create(
                newUser.getUsername()
                , ip()
                , currentTime
                , currentTime);

        @NotNull UserPersonInfo userPersonInfo = UserPersonInfo.Factory.USER.create(
                newUser.getUsername()
                , "测试用户"
                , null
                , null
                , null
                , null
        );

        result.put("user", newUser);
        result.put("userAccountOperationInfo", userAccountOperationInfo);
        result.put("userPersonInfo", userPersonInfo);

        return result;
    }

    private @NotNull String ip() {
        return "127.0.0.0";
    }

    @Test
    public void contextLoads() {
        Assert.notNull(userService, "获取测试单元失败");
    }

    /**
     * @see UserReadService#selectAll(int, int)
     */
    @Test
    @Transactional
    public void selectAll() {
        final Page<User> result;
        Assert.isTrue(! (result = userService.selectAll(0, 10)).isEmpty()
                , "The result is empty");
        System.out.println(result);
    }

    /**
     * @see UserReadService#selectCount(int)
     */
    @Test
    @Transactional
    public void selectCount() {
        final long result;
        Assert.isTrue((result = userService.selectCount(10)) > 0
                , "The result equaled to or less than 0");
        System.out.println(result);
    }

    /**
     * @see UserReadService#selectUserByUserid(String)
     */
    @Test
    @Transactional
    public void selectUserByUserid()
            throws BusinessAtomicException
    {
        final User result;

        System.err.println(String.format("===== %s => %s ====="
                , this.getClass().getName()
                , Thread.currentThread().getStackTrace()[1].getMethodName()));

        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();

        // 添加测试数据
        final @NotNull Map<String, EntityModel<?>> newEntity = getEntityForTest();

        /*Assert.isTrue(userService.insert((User) newEntity.get("user"), operator, (UserAccountOperationInfo) newEntity.get("userAccountOperationInfo"))
                , "===== 添加测试数据失败!");*/
        Assert.isTrue(userEvent.registerUser((User) newEntity.get("user")
                    , (UserAccountOperationInfo) newEntity.get("userAccountOperationInfo")
                    , (UserPersonInfo) newEntity.get("userPersonInfo")
                    , operator)
                , "===== 添加测试数据失败!");

        // selectUserByUserid(...)
        result = userService.selectUserByUserid(((User) newEntity.get("user")).getUserid());
        Assert.isTrue((null != result && ! result.isEmpty())
                , "===== The result -> null");

        System.out.println(result);

        System.err.println("==========");
    }

    /**
     * @throws BusinessAtomicException
     * @see UserNonIdempotentWriteService#insert(User, SecurityUser)
     */
    @Test
    @Transactional
    public void insert()
            throws BusinessAtomicException
    {
        System.err.println(String.format("===== %s => %s ====="
                , this.getClass().getName()
                , Thread.currentThread().getStackTrace()[1].getMethodName()));

        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();

        // 添加测试数据
        final @NotNull Map<String, EntityModel<?>> newEntity = getEntityForTest();

        Assert.isTrue(((User) newEntity.get("user")).isEntityLegal()
                , "getEntityForTest() -> 无效的 Entity");
        /*Assert.isTrue(userService.insert((User) newEntity.get("user"), operator, (UserAccountOperationInfo) newEntity.get("userAccountOperationInfo"))
                , "===== insert(Entity...) -> false");*/
        Assert.isTrue(userEvent.registerUser((User) newEntity.get("user")
                    , (UserAccountOperationInfo) newEntity.get("userAccountOperationInfo")
                    , (UserPersonInfo) newEntity.get("userPersonInfo")
                    , operator)
                , "===== 添加测试数据失败!");
        Assert.isTrue(! newEntity.isEmpty()
                , "===== insert(Entity...) -> 无效的 Entity");

        System.out.println(newEntity);

        System.err.println("==========");
    }

//    /**
//     * @throws BusinessAtomicException
//     * @see UserIdempotentWriteService#update(User, SecurityUser)
//     */
//    @Test
//    @Transactional
//    public void update()
//            throws BusinessAtomicException
//    {
//        /*final User result;*/
//
//        System.err.println(String.format("===== %s => %s ====="
//                , this.getClass().getName()
//                , Thread.currentThread().getStackTrace()[1].getMethodName()));
//
//        // 获取必要的测试用身份信息
//        final @NotNull SecurityUser operator = operator();
//
//        // 添加测试数据
//        final @NotNull Map<String, EntityModel<?>> newEntity = getEntityForTest();
//
//        Assert.isTrue(((User) newEntity.get("user")).isEntityLegal()
//                , "===== getEntityForTest() -> 无效的 Entity");
//        /*Assert.isTrue(userService.insert((User) newEntity.get("user"), operator, (UserAccountOperationInfo) newEntity.get("userAccountOperationInfo"))
//                , "===== 添加测试数据失败!");*/
//        Assert.isTrue(userEvent.registerUser((User) newEntity.get("user")
//                    , (UserAccountOperationInfo) newEntity.get("userAccountOperationInfo")
//                    , (UserPersonInfo) newEntity.get("userPersonInfo")
//                    , operator)
//                , "===== 添加测试数据失败!");
//
//        //=== update(..)
//        System.out.println(((UserPersonInfo) newEntity.get("userPersonInfo")).getIntroduction());
//
//        ((UserPersonInfo) newEntity.get("userPersonInfo")).setIntroduction("最新测试数据");
//
//        @NotNull UserAccountOperationInfo operator_UserAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());
//        Assert.isTrue(userService.update((User) newEntity.get("user"), operator, operator_UserAccountOperationInfo)
//                , "===== update - update(Entity) -> false");
//        Assert.isTrue(! ((User) newEntity.get("user")).isEmpty()
//                , "===== update - update(Entity) -> 无效的 Entity");
//
//        System.err.println("==========");
//    }

    /**
     * @see UserNonIdempotentWriteService#delete(User, SecurityUser)
     */
    @Test
    @Transactional
    public void delete()
            throws BusinessAtomicException
    {
        System.err.println(String.format("===== %s => %s ====="
                , this.getClass().getName()
                , Thread.currentThread().getStackTrace()[1].getMethodName()));

        // 获取必要的测试用身份信息
        final @NotNull SecurityUser operator = operator();

        // 添加测试数据
        final @NotNull Map<String, EntityModel<?>> newEntity = getEntityForTest();

        Assert.isTrue(((User) newEntity.get("user")).isEntityLegal()
                , "===== getEntityForTest() -> 无效的 Entity");
        /*Assert.isTrue(userService.insert((User) newEntity.get("user"), operator, (UserAccountOperationInfo) newEntity.get("userAccountOperationInfo"))
                , "===== 添加测试数据失败!");*/
        Assert.isTrue(userEvent.registerUser((User) newEntity.get("user")
                    , (UserAccountOperationInfo) newEntity.get("userAccountOperationInfo")
                    , (UserPersonInfo) newEntity.get("userPersonInfo")
                    , operator)
                , "===== 添加测试数据失败!");

        //=== delete(..)
        @NotNull UserAccountOperationInfo operator_UserAccountOperationInfo = userEvent.selectUserAccountOperationInfoByUsername(operator.getUsername());
        Assert.isTrue(userService.delete((User) newEntity.get("user"), operator, operator_UserAccountOperationInfo)
                , "===== delete(Entity) -> false");
        System.out.println((User) newEntity.get("user"));

        System.err.println("==========");
    }

}
