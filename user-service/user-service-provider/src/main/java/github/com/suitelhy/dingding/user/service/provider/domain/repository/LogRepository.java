package github.com.suitelhy.dingding.user.service.provider.domain.repository;

import github.com.suitelhy.dingding.user.service.api.domain.entity.Log;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * 日志记录 - 基础业务
 *
 * @Description 日志记录数据 - 基础交互业务接口.
 *
 * @see Log
 */
/*// 此处选择使用 Mybatis-Spring 的XML文件配置方式实现 mapper, 用来演示复杂SQL情景下的一种设计思路:
//-> 聚焦于 SQL.
@Mapper*/
public interface LogRepository
        extends JpaRepository<Log, Long>, EntityRepository {

    //===== Select Data =====//

    /**
     * 查询日志记录总数
     *
     * @return
     */
    @Override
    long count();

    /**
     * 查询日志记录数量
     *
     * @param operatorUsername [操作者 - 用户名称]
     * @return {@link Long#TYPE}
     */
    long countByOperatorUsername(@NotNull String operatorUsername);

    /**
     * 查询日志记录数量
     *
     * @param targetId [被操作对象 - 用于日志追踪的 ID]
     * @return {@link Long#TYPE}
     */
    long countByTargetId(@NotNull String targetId);

    /**
     * 查询所有日志记录
     *
     * @param pageable
     * @return
     */
    @Override
    Page<Log> findAll(Pageable pageable);

    /**
     * 查询指定的日志记录
     *
     * @param id [日志记录 - 编号]
     * @return {@link Optional}
     */
    @NotNull
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED)
    Optional<Log> findLogById(@NotNull Long id);

    /**
     * 查询指定的日志记录
     *
     * @param operatorUsername [操作者 - 用户名称]
     * @param pageable
     * @return {@link List}
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED)
    List<Log> findByOperatorUsername(@NotNull String operatorUsername, Pageable pageable);

    /**
     * 查询指定的日志记录
     *
     * @param targetId [被操作对象 - 用于日志追踪的 ID]
     * @param pageable
     * @return
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED)
    List<Log> findByTargetId(@NotNull String targetId, Pageable pageable);

    //===== Insert Data =====//

    /**
     * 新增/更新日志记录
     *
     * @param log
     * @return {@link Log}
     */
    @Override
    @Modifying
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED)
    Log saveAndFlush(@NotNull Log log);

    //===== Delete Data =====//

    /**
     * 删除指定的日志记录
     *
     * @param id 记录ID
     */
    @Override
    @Modifying
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED)
    void deleteById(@NotNull Long id);

    /**
     * 删除指定用户对应的所有日志记录
     *
     * @param operatorUsername [操作者 - 用户名称]
     * @return {@link Long#TYPE}
     */
    @Modifying
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED)
    long removeByOperatorUsername(@NotNull String operatorUsername);

    // 参考项目中提供了 deleteAll 业务接口; 如果更深入一些, 在实际生产情景下,
    //-> 不应该提供这类不严谨的业务接口 (...), 删除操作也仅限于用户权限以内.
    // 即便是删除指定账户, 也应该在数据库中保留必要的信息以供备案 (常见的策略
    //-> 是提供业务层面的删除接口, 而持久层数据不做修改或者在保留必要信息的基
    //-> 础上改变状态).
    /*boolean deleteAll();*/

}
