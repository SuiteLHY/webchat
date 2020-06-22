package github.com.suitelhy.dingding.core.domain.entity;

import github.com.suitelhy.dingding.core.infrastructure.domain.model.AbstractEntityModel;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityFactory;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.EntityValidator;
import github.com.suitelhy.dingding.core.infrastructure.domain.util.EntityUtil;
import github.com.suitelhy.dingding.core.infrastructure.domain.util.VoUtil;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.Account;
import github.com.suitelhy.dingding.core.infrastructure.domain.vo.Human;
import github.com.suitelhy.dingding.core.infrastructure.web.util.NetUtil;
import github.com.suitelhy.dingding.core.infrastructure.util.CalendarController;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 用户信息
 *
 * <p>关于数据脱敏的自定义注解实现, 可参考: <a href="https://blog.csdn.net/liufei198613/article/details/79009015">
 * ->     注解实现json序列化的时候自动进行数据脱敏_liufei198613的博客-CSDN博客</a>
 * </p>
 *
 * <p>关于数据脱敏的自定义注解实现, 可参考: <a href="https://blog.csdn.net/liufei198613/article/details/79009015">
 *->     注解实现json序列化的时候自动进行数据脱敏_liufei198613的博客-CSDN博客</a>
 * </p>
 *
 * <p>关于 id 生成策略, 个人倾向于使用"代理键" ———— 所选策略还是应该交由数据库来实现.
 * @Reference <a href="https://dzone.com/articles/persisting-natural-key-entities-with-spring-data-j">
 *->     Persisting Natural Key Entities With Spring Data JPA</a>
 * </p>
 */
@Entity
@Table
public class User
        extends AbstractEntityModel</*String*/Object[]> {

    private static final long serialVersionUID = 1L;

    // 用户ID
    @GeneratedValue(generator = "USER_ID_STRATEGY")
    @GenericGenerator(name = "USER_ID_STRATEGY", strategy = "uuid")
    @Id
    private String userid;

    // 用户名称
    @Column(nullable = false, unique = true)
    private String username;

    // 用户 - 昵称
    @Column(nullable = false, unique = true)
    private String nickname;

    // 用户 - 年龄
    @Column
    private Integer age;

    // 数据更新时间 (由数据库管理)
    @Column(name = "data_time")
    @Transient
    private LocalDateTime dataTime;

    // 用户 - 头像
    @Column(name = "face_image")
    private String faceImage;

    // 注册时间
    @Column(nullable = false)
    private String firsttime;

    // 用户 - 简介
    @Column
    private String introduction;

    // 最后登陆IP
    @Column(nullable = false)
    private String ip;

    // 最后登录时间
    @Column(nullable = false)
    private String lasttime;

    // 用户密码
    @Column(nullable = false)
    private String password;

    // 用户 - 性别
    @Column
    @Convert(converter = Human.SexVo.Converter.class)
    private Human.SexVo sex;

    // 账号状态
    @Column(nullable = false)
    @Convert(converter = Account.StatusVo.Converter.class)
    private Account.StatusVo status;

    //===== Entity Model =====//

    @Override
    public @NotNull /*String*/Object[] id() {
        return new Object[]{
                this.username
                , this.nickname
        };
    }

    /**
     * 是否无效
     *
     * @Description 保证 User 的基本业务实现中的合法性.
     *
     * @return
     */
    @Override
    public boolean isEmpty() {
        /*return super.isEmpty()
                && !Account.StatusVo.NORMAL.equals(this.status);*/
        return !Validator.USER.userid(this.userid)
                || !this.isEntityLegal()
                || !Account.StatusVo.NORMAL.equals(this.status);
    }

    /**
     * 是否符合基础数据合法性要求
     *
     * @Description 只保证 User 的数据合法, 不保证 User 的业务实现中的合法性.
     *
     * @return
     */
    @Override
    public boolean isEntityLegal() {
        return Validator.USER.age(this.age)                 // 用户 - 年龄
                && Validator.USER.faceImage(this.faceImage) // 用户 - 头像
                && Validator.USER.firsttime(this.firsttime) // 注册时间
                && Validator.USER.introduction(this.introduction) // 用户 - 简介
                && Validator.USER.ip(this.ip)               // 最后登陆IP
                && Validator.USER.lasttime(this.lasttime)   // 最后登录时间
                && Validator.USER.nickname(this.nickname)   // 用户 - 昵称
                && Validator.USER.password(this.password)   // 用户密码
                && Validator.USER.username(this.username)   // 用户名称
                && Validator.USER.sex(this.sex)             // 用户 - 性别
                && Validator.USER.status(this.status)/* 账号状态 */;
    }

    /**
     * 校验 Entity - ID
     *
     * @Description <abstractClass>AbstractEntityModel</abstractClass>提供的模板设计.
     *
     * @param entityId      <method>id()</method>
     * @return
     */
    @Override
    protected boolean validateId(@NotNull /*String*/Object[] entityId) {
        return Validator.USER.entity_id(entityId);
    }

    //===== Entity Validator =====//

    /**
     * 用户 - 属性校验器
     *
     * @Description 各个属性的基础校验(注意 : ≠ 完全校验).
     */
    public enum Validator implements EntityValidator<User, Object[]> {
        USER;

        @Override
        public boolean validateId(@NotNull User entity) {
            return null != entity.id()
                    && entity_id(entity.id());
        }

        @Override
        public boolean entity_id(@NotNull /*String*/Object[] entityId) {
            return null != entityId
                    && entityId.length == 2
                    && this.username((String) entityId[0])
                    && this.nickname((String) entityId[1]);
        }

        public boolean userid(@NotNull String userid) {
            return EntityUtil.Regex.validateId(userid);
        }

        public boolean username(@NotNull String username) {
            return EntityUtil.Regex.validateUsername(username);
        }

        public boolean nickname(@NotNull String nickname) {
            return null != nickname && !"".equals(nickname.trim());
        }

        public boolean age(@Nullable Integer age) {
            return null == age || age > 0;
        }

        public boolean firsttime(@NotNull String firsttime) {
            return null != firsttime && CalendarController.isParse(firsttime);
        }

        public boolean ip(@NotNull String ip) {
            return NetUtil.validateIpAddress(ip);
        }

        public boolean lasttime(@NotNull String lasttime) {
            return null != lasttime && CalendarController.isParse(lasttime);
        }

        public boolean password(@NotNull String password) {
            return EntityUtil.Regex.validateUserPassword(password);
        }

        public boolean introduction(String introduction) {
            //--- 暂无业务设计约束
            return true;
        }

        public boolean faceImage(String faceImage) {
            //--- 暂无业务设计约束
            return true;
        }

        public boolean sex(Human.SexVo sex) {
            if (null == sex) {
                return null != VoUtil.getVoByValue(Human.SexVo.class, null);
            }
            return true;
        }

        public boolean status(@NotNull Account.StatusVo status) {
            if (null == status) {
                return null != VoUtil.getVoByValue(Account.StatusVo.class, null);
            }
            return true;
        }

    }

    //===== base constructor =====//

    /**
     * 仅用于持久化注入
     */
    public User() {
    }

    //===== entity factory =====//

    /**
     * 创建/更新用户 -> Entity对象
     *
     * @Description 添加 (<param>id</param>为 null) / 更新 (<param>id</param>合法) 用户.
     * @param userid        用户 ID
     * @param age           用户 - 年龄
     * @param firsttime     注册时间
     * @param ip            最后登陆IP
     * @param lasttime      最后登录时间
     * @param nickname      用户 - 昵称
     * @param password      用户密码
     * @param introduction  用户 - 简介
     * @param faceImage     用户 - 头像
     * @param username      用户名称
     * @param sex           用户 - 性别
     * @throws IllegalArgumentException
     */
    private User(@NotNull String userid
            , @Nullable Integer age
            , @NotNull String firsttime
            , @NotNull String ip
            , @NotNull String lasttime
            , @NotNull String nickname
            , @NotNull String password
            , @Nullable String introduction
            , @Nullable String faceImage
            , @NotNull String username
            , @Nullable Human.SexVo sex)
            throws IllegalArgumentException {
        if (null == userid) {
            //--- 添加用户功能
        } else {
            //--- 更新用户功能
            if (!Validator.USER.userid(userid)) {
                //-- 非法输入: 用户ID
                throw new IllegalArgumentException(this.getClass().getSimpleName()
                        .concat(" -> 非法输入: 用户ID"));
            }
        }
        if (!Validator.USER.username(username)) {
            //-- 非法输入: 用户名称
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: 用户名称"));
        }
        if (!Validator.USER.age(age)) {
            //-- 非法输入: 用户 - 年龄
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: 用户 - 年龄"));
        }
        if (!Validator.USER.firsttime(firsttime)) {
            //-- 非法输入: 注册时间
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: 注册时间"));
        }
        if (!Validator.USER.ip(ip)) {
            //-- 非法输入: 最后登陆IP
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: 最后登陆IP"));
        }
        if (!Validator.USER.lasttime(lasttime)) {
            //-- 非法输入: 最后登录时间
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: 最后登录时间"));
        }
        if (!Validator.USER.nickname(nickname)) {
            //-- 非法输入: 用户 - 昵称
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: 用户 - 昵称"));
        }
        if (!Validator.USER.password(password)) {
            //-- 非法输入: 用户密码
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    .concat(" -> 非法输入: 用户密码"));
        }

        // 用户ID
        this.setUserid(userid);
        // 用户名称
        this.setUsername(username);
        // 用户 - 昵称
        this.setNickname(nickname);
        // 用户 - 年龄
        this.setAge(age);
        // 注册时间
        this.setFirsttime(new CalendarController().toString());
        // 最后登陆IP
        this.setIp(ip);
        // 最后登录时间
        this.setLasttime(lasttime);
        // 用户密码
        this.setPassword(password);
        // 用户 - 简介
        this.setIntroduction(introduction);
        // 用户 - 头像
        this.setFaceImage(faceImage);
        // 用户 - 性别
        this.setSex(sex);
        // 账号状态
        this.setStatus(Account.StatusVo.NORMAL);
    }

    public enum Factory
            implements EntityFactory<User> {
        USER;

        /**
         * 创建
         *
         * @param age           用户 - 年龄
         * @param firsttime     注册时间
         * @param ip            最后登陆IP
         * @param lasttime      最后登录时间
         * @param nickname      用户 - 昵称
         * @param password      用户密码
         * @param introduction       用户 - 简介
         * @param faceImage   用户 - 头像
         * @param username      用户名称
         * @param sex           用户 - 性别
         * @return {@link User}
         * @throws IllegalArgumentException
         */
        public User create(@Nullable Integer age
                , @NotNull String firsttime
                , @NotNull String ip
                , @NotNull String lasttime
                , @NotNull String nickname
                , @NotNull String password
                , @Nullable String introduction
                , @Nullable String faceImage
                , @NotNull String username
                , @Nullable Human.SexVo sex)
                throws IllegalArgumentException {
            return new User(null, age, firsttime
                    , ip, lasttime, nickname
                    , password, introduction, faceImage
                    , username, sex);
        }

        /**
         * 更新
         *
         * @param userid            用户 ID
         * @param age           用户 - 年龄
         * @param firsttime     注册时间
         * @param ip            最后登陆IP
         * @param lasttime      最后登录时间
         * @param nickname      用户 - 昵称
         * @param password      用户 - 密码
         * @param introduction  用户 - 简介
         * @param faceImage     用户 - 头像
         * @param sex           用户 - 性别
         * @return 可为 null, 此时输入参数非法.
         * @throws IllegalArgumentException 此时 <param>id</param> 非法.
         */
        public User update(@NotNull String userid
                , @Nullable Integer age
                , @NotNull String firsttime
                , @NotNull String ip
                , @NotNull String lasttime
                , @NotNull String nickname
                , @NotNull String password
                , @Nullable String introduction
                , @Nullable String faceImage
                , @NotNull String username
                , @Nullable Human.SexVo sex)
                throws IllegalArgumentException {
            if (!Validator.USER.userid(userid)) {
                throw new IllegalArgumentException("非法输入: 用户ID");
            }
            return new User(userid, age, firsttime
                    , ip, lasttime, nickname
                    , password, introduction, faceImage
                    , username, sex);
        }

        /**
         * 销毁 Entity 实例
         *
         * @param user
         * @return {<code>true</code> : <b>销毁成功</b>, <code>false</code> : <b>销毁失败; 此时 <param>user</param></b> 无效或无法销毁}
         */
        public boolean delete(@NotNull User user) {
            if (null != user && !user.isEmpty()) {
                user.setStatus(Account.StatusVo.DESTRUCTION);
                return true;
            }
            return false;
        }

    }

    //===== getter & setter =====//

    @Nullable
    public String getUserid() {
        return userid;
    }

    private boolean setUserid(@NotNull String userid) {
        if (Validator.USER.userid(userid)) {
            this.userid = userid;
            return true;
        }
        return false;
    }

    public Integer getAge() {
        return age;
    }

    public boolean setAge(Integer age) {
        if (Validator.USER.age(age)) {
            this.age = age;
            return true;
        }
        return false;
    }

    public String getFirsttime() {
        return firsttime;
    }

    public boolean setFirsttime(String firsttime) {
        if (Validator.USER.firsttime(firsttime)) {
            this.firsttime = firsttime;
            return true;
        }
        return false;
    }

    public String getIp() {
        return ip;
    }

    public boolean setIp(String ip) {
        if (Validator.USER.ip(ip)) {
            this.ip = ip;
            return true;
        }
        return false;
    }

    public String getLasttime() {
        return lasttime;
    }

    public boolean setLasttime(String lasttime) {
        if (Validator.USER.lasttime(lasttime)) {
            this.lasttime = lasttime;
            return true;
        }
        return false;
    }

    public String getNickname() {
        return nickname;
    }

    public boolean setNickname(String nickname) {
        if (Validator.USER.nickname(nickname)) {
            this.nickname = nickname;
            return true;
        }
        return false;
    }

    @NotNull
    public String getPassword() {
        return password;
    }

    private boolean setPassword(@NotNull String password) {
        if (Validator.USER.password(password)) {
            this.password = password;
            return true;
        }
        return false;
    }

    /**
     * 判断密码是否相同
     *
     * @param password
     * @return {true: <tt>密码相同</tt>, false: <tt>密码不相同</tt>, null: <tt>Entity无效</tt>}
     */
    public Boolean equalsPassword(@NotNull String password) {
        if (Validator.USER.password(password)) {
            if (this.isEmpty()) {
                return null;
            }
            return this.password.equals(password);
        }
        return false;
    }

    public String getIntroduction() {
        return introduction;
    }

    public boolean setIntroduction(String introduction) {
        if (Validator.USER.introduction(introduction)) {
            this.introduction = introduction;
            return true;
        }
        return false;
    }

    public String getFaceImage() {
        return faceImage;
    }

    public boolean setFaceImage(String faceImage) {
        if (Validator.USER.faceImage(faceImage)) {
            this.faceImage = faceImage;
            return true;
        }
        return false;
    }

    public Human.SexVo getSex() {
        return sex;
    }

    public boolean setSex(Human.SexVo sexVo) {
        if (Validator.USER.sex(sexVo)) {
            this.sex = sexVo;
            return true;
        }
        return false;
    }

    public Account.StatusVo getStatus() {
        return status;
    }

    private boolean setStatus(Account.StatusVo statusVo) {
        if (Validator.USER.status(statusVo)) {
            this.status = statusVo;
            return true;
        }
        return false;
    }

    @NotNull
    public String getUsername() {
        return username;
    }

    private boolean setUsername(@NotNull String username) {
        if (Validator.USER.username(username)) {
            this.username = username;
            return true;
        }
        return false;
    }

}
