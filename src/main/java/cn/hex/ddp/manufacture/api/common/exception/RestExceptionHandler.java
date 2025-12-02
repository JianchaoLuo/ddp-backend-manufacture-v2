package cn.hex.ddp.manufacture.api.common.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.hex.ddp.manufacture.api.common.respond.CommonResult;
import cn.hex.ddp.manufacture.domain.common.exception.*;
import jakarta.validation.ConstraintViolationException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.stream.Collectors;

/**
 * Rest全局异常处理器
 *
 * @author Huhaisen
 * @date 2024/05/07
 */
@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {


    // 全局异常拦截（拦截项目中的NotLoginException异常）
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(NotLoginException.class)
    public CommonResult handlerNotLoginException(NotLoginException nle) {
        // 打印堆栈，以供调试
        nle.printStackTrace();

        // 判断场景值，定制化异常信息
        String message;
        if (nle.getType().equals(NotLoginException.NOT_TOKEN)) {
            message = "未能读取到有效 token";
        } else if (nle.getType().equals(NotLoginException.INVALID_TOKEN)) {
            message = "token 无效";
        } else if (nle.getType().equals(NotLoginException.TOKEN_TIMEOUT)) {
            message = "token 已过期";
        } else if (nle.getType().equals(NotLoginException.BE_REPLACED)) {
            message = "token 已被顶下线";
        } else if (nle.getType().equals(NotLoginException.KICK_OUT)) {
            message = "token 已被踢下线";
        } else if (nle.getType().equals(NotLoginException.TOKEN_FREEZE)) {
            message = "token 已被冻结";
        } else if (nle.getType().equals(NotLoginException.NO_PREFIX)) {
            message = "未按照指定前缀提交 token";
        } else {
            message = "当前会话未登录";
        }

        // 返回给前端
        return CommonResult.failure(401, message);
    }

    /**
     * 请求的操作非法
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenException.class)
    public CommonResult handleForbiddenException(Exception e) {
        log.warn(format(e));
        return CommonResult.failure(e.getMessage());
    }

    /**
     * 无权限
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(NotRoleException.class)
    public CommonResult handleNotRoleException(NotRoleException e) {
        log.warn(format(e));
        return CommonResult.failure("无权限");
    }

    /**
     * 参数校验失败【针对于请求中Body部分的校验失败，方法形参要加@RequestBody来指明处理的Body部分，
     * 别和下面Path部分和Query部分搞混，这俩抛出的异常是不一样的】
     * 校验Body的Controller方法样例：search(@RequestBody @Valid UserParam userParam)
     * 提示：想校验Body参数，就算不在Controller类上加@Validated注解也能生效（当然加了也不碍事，为了省事，干脆把所有Controller都加上@Validated）
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public CommonResult handleBindException(BindException e) {
        String message = formatBindExceptionAsMsg(e);
        log.warn(format(e));
        return CommonResult.failure(BssExType.PARAM_VALIDATE_FAILED.getCode(), message);
    }

    /**
     * 参数校验失败【针对于请求中Path部分和Query部分的校验失败，方法形参不加@RequestBody
     * （如果是Path参数则要加@PathVariable，Query参数则不用额外加注解），别和上面Body部分搞混，这俩抛出的异常是不一样的】
     * 校验Query的Controller方法样例：search(@Valid @NotBlank(message= "姓名不能为空") String name)，并且务必在Controller类上加@Validated
     * 校验Path的Controller方法样例：search(@PathVariable @Valid @Min(value = 1,message = "用户ID最小是1")  Long userId)
     * ，并且务必在Controller类上加@Validated
     * 特别注意：如果你想校验Query参数或者Path参数，必须要在Controller类上加@Validated注解，否则校验不生效
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult handleBindException(ConstraintViolationException e) {
        String message = formatConstraintViolationExceptionAsMsg(e);
        log.warn(format(e));
        return CommonResult.failure(BssExType.PARAM_VALIDATE_FAILED.getCode(), message);
    }

    /**
     * 请求方式不支持
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public CommonResult handleMethodNotAllowed(HttpRequestMethodNotSupportedException e) {
        log.warn(format(e));
        return CommonResult.failure(405, ExConst.METHOD_NOT_ALLOWED);
    }

    /**
     * 请求格式不对
     */
    @ExceptionHandler({ServletRequestBindingException.class,
            MissingRequestHeaderException.class,
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult handleBadRequest(Exception e) {
        log.warn(format(e));
        return CommonResult.failure(400, ExConst.REQUEST_FORMAT_ALLOWED);
    }

    /**
     * 请求URL有误，无法解析这个URL该对应Controller中哪个方法
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public CommonResult handleNotFoundException(NoHandlerFoundException e) {
        log.warn(format(e));
        return CommonResult.failure(404, ExConst.URL_NOT_EXIST);
    }

    /**
     * 请求资源不存在
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public CommonResult handleNotFoundException(NotFoundException e) {
        log.warn(format(e));
        return CommonResult.failure(404, e.getMessage());
    }

    /**
     * 业务异常，可细分为多种情况，可见ExceptionType
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(BusinessException.class)
    public CommonResult handleBusinessException(BusinessException e) {
        log.warn(format(e));
        return CommonResult.failure(e.getCode(), e.getMessage());
    }

    /**
     * 数据主键重复
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateKeyException.class)
    public CommonResult handleDuplicateKeyException(Exception e) {
        log.warn(format(e));
        return CommonResult.failure(ExConst.DUPLICATE_KEY);
    }

    /**
     * 如果前面的处理器都没拦截住，最后兜底
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public CommonResult handleException(Exception e) {
        log.error(format(e));
        return CommonResult.failure(ExConst.SERVICE_ERR);
    }


    /**
     * BindException格式化
     */
    public String formatBindExceptionAsMsg(BindException e) {
        //提示：error.getField()得到的是校验失败的字段名字，error.getDefaultMessage()得到的是校验失败的原因
        return e.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("，"));
    }

    /**
     * ConstraintViolationException格式化
     */
    public String formatConstraintViolationExceptionAsMsg(ConstraintViolationException e) {
        //提示：((PathImpl) violation.getPropertyPath()).getLeafNode().getName()得到的是校验失败的字段名字，violation.getMessage()
        // 得到的是校验失败的原因
        return e.getConstraintViolations().stream()
                .map(v -> ((PathImpl) v.getPropertyPath()).getLeafNode().getName() + ":" + v.getMessage())
                .collect(Collectors.joining("，"));
    }

    /**
     * 把异常信息格式化成自己喜欢的格式，这个方法用于格式化Exception，为了方便日志收集器采集，格式化异常到一行代码
     */
    @SneakyThrows
    public String format(Exception e) {
        e.printStackTrace();
        return e.getMessage();
    }

}
