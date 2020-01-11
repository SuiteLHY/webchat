package github.com.suitelhy.webchat.infrastructure.domain.util;

import github.com.suitelhy.webchat.infrastructure.domain.model.EntityModel;
import github.com.suitelhy.webchat.infrastructure.util.RegexUtil;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * entity 层 - 业务辅助工具
 */
public final class EntityUtil {

    public static class Regex {

        /**
         * 校验字段名称格式
         * @Description 使用正则表达式校验
         * @param fieldName
         * @return
         */
        public static boolean validateFieldName(@Nullable String fieldName) {
                return RegexUtil.getPattern("[a-z][A-Za-z0-9]+").matcher(fieldName).matches();
        }

        private Regex() {}

    }

    /**
     * 通过反射执行指定方法
     * @param entity
     * @param fieldName
     * @param parameterTypes
     * @param args
     * @param <T>
     * @return 所执行的方法的返回值 - 可为 null, 此时成功执行方法且方法返回值
     *-> 为 null, 或者方法执行失败且由 <code>invokeMethod</code> 方法处理异常.
     */
    public static <T extends EntityModel> Object invokeMethod(@NotNull T entity
            , @NotNull String fieldName
            , @Nullable Class<?>[] parameterTypes
            , @Nullable Object[] args) {
        Object result;
        Method method;
        try {
            method = entity.getClass().getMethod(fieldName, parameterTypes);
            result = method.invoke(entity, args);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("反射调用工具 - 通过反射执行指定方法出错", e);
        }
        return result;
    }

}