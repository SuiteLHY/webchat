package github.com.suitelhy.dingding.core.domain.repository.security;

import github.com.suitelhy.dingding.core.domain.entity.security.SecurityResourceUrl;
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

/**
 * 资源 - URL
 *
 * @Description 资源 - URL 关联关系 -> 基础交互业务接口.
 */
public interface SecurityResourceUrlRepository
        extends JpaRepository<SecurityResourceUrl, Long>, EntityRepository {

    //===== Select Data =====//

    /**
     * 查询总数
     *
     * @return
     */
    @Override
    long count();

    /**
     * 判断存在
     *
     * @param resourceCode
     * @return
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED)
    boolean existsAllByCode(@NotNull String resourceCode);

    /**
     * 查询所有
     *
     * @param resourceCode  资源编码
     * @return
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED)
    List<SecurityResourceUrl> findAllByCode(@NotNull String resourceCode);

    /**
     * 查询所有
     *
     * @param resourceCode  资源编码
     * @param pageable      {@link org.springframework.data.domain.Pageable}
     * @return {@link Page}
     */
    Page<SecurityResourceUrl> findAllByCode(@NotNull String resourceCode, Pageable pageable);

    /**
     * 查询所有
     *
     * @param clientId      资源服务器 ID
     * @param urlPath       资源对应的 URL (Path部分)
     * @return
     */
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED)
    List<SecurityResourceUrl> findAllByClientIdAndUrlPath(@NotNull String clientId, @NotNull String urlPath);

    /**
     * 查询所有
     *
     * @param clientId      资源服务器 ID
     * @param urlPath       资源对应的 URL (Path部分)
     * @param pageable      {@link org.springframework.data.domain.Pageable}
     * @return {@link Page}
     */
    Page<SecurityResourceUrl> findAllByClientIdAndUrlPath(@NotNull String clientId
            , @NotNull String urlPath
            , Pageable pageable);

    //===== Insert Data =====//

    /**
     * 新增/更新日志记录
     *
     * @param entity        {@link github.com.suitelhy.dingding.core.domain.entity.security.SecurityResourceUrl}
     * @return  {@link github.com.suitelhy.dingding.core.domain.entity.security.SecurityResourceUrl}
     */
    @Override
    @Modifying
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED)
    SecurityResourceUrl saveAndFlush(SecurityResourceUrl entity);

    //===== Delete Data =====//

    /**
     * 删除
     *
     * @param id    数据ID
     */
    @Override
    @Modifying
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED)
    void deleteById(@NotNull Long id);

    /**
     * 删除
     *
     * @param resourceCode  资源编码
     * @return
     */
    @Modifying
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED)
    long removeByCode(@NotNull String resourceCode);

    /**
     * 删除
     *
     * @param clientId      资源服务器 ID
     * @param urlPath       资源对应的 URL (Path部分)
     * @return
     */
    @Modifying
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED)
    long removeByClientIdAndUrlPath(@NotNull String clientId, @NotNull String urlPath);

    /**
     * 删除
     *
     * @param resourceCode  资源编码
     * @param clientId      资源服务器 ID
     * @param urlPath       资源对应的 URL (Path部分)
     * @return
     */
    @Modifying
    @Transactional(isolation = Isolation.SERIALIZABLE
            , propagation = Propagation.REQUIRED)
    long removeByCodeAndClientIdAndUrlPath(@NotNull String resourceCode, @NotNull String clientId, @NotNull String urlPath);

}
