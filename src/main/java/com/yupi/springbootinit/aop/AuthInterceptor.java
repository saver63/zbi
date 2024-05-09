package com.yupi.springbootinit.aop;

import com.yupi.springbootinit.annotation.AuthCheck;
import com.yupi.springbootinit.annotation.UseCheck;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.model.entity.User;
import com.yupi.springbootinit.model.enums.UserRoleEnum;
import com.yupi.springbootinit.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.yupi.springbootinit.constant.ChartConstant.SYNC_METHOD;

/**
 * 权限校验 AOP
 *
 */
@Aspect
@Component
public class AuthInterceptor {

    @Resource
    private UserService userService;

    /**
     * 执行拦截
     *
     * @param joinPoint
     * @param authCheck
     * @return
     */
    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        String mustRole = authCheck.mustRole();
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 当前登录用户
        User loginUser = userService.getLoginUser(request);
        // 必须有该权限才通过
        if (StringUtils.isNotBlank(mustRole)) {
            UserRoleEnum mustUserRoleEnum = UserRoleEnum.getEnumByValue(mustRole);
            if (mustUserRoleEnum == null) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
            }
            String userRole = loginUser.getUserRole();
            // 如果被封号，直接拒绝
            if (UserRoleEnum.BAN.equals(mustUserRoleEnum)) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
            }
            // 必须有管理员权限
            if (UserRoleEnum.ADMIN.equals(mustUserRoleEnum)) {
                if (!mustRole.equals(userRole)) {
                    throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
                }
            }
        }
        // 用户未被封禁，继续执行原方法
        return joinPoint.proceed();
    }


    /**
     * 校验是否还有次数
     *
     * @param joinPoint
     * @return
     */
    @Around("@annotation(useCheck)")
    public Object doIntercepto(ProceedingJoinPoint joinPoint, UseCheck useCheck) throws Throwable {
        // 调用的方式为哪种？（同步/异步）
        String method = useCheck.method();
        if(StringUtils.isBlank(method)){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 是否还有次数
        // 1. 获取request
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 2.获取当前登录用户
        User loginUser = userService.getLoginUser(request);
        // 3. 从数据库查询用户信息
        Long userId = loginUser.getId();
        User user = userService.getById(userId);
        if(user == null){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR,"未登录");
        }
        Integer leftNum = user.getLeftNum();
        Integer totalNum = user.getTotalNum();
        // 4. 剩余次数是否大于0
        if(leftNum <= 0){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR,"剩余使用次数不足，请联系管理员");
        }
        // 通行
        Object proceed = joinPoint.proceed();
        // 异步到此结束 -》 等消息队列或线程执行完成后再减次数
        // 同步 -》 目前已执行完成，减次数
        if(SYNC_METHOD.equals(method)){
            // 同步则次数-1
            User updateUser = new User();
            updateUser.setId(userId);
            updateUser.setLeftNum(leftNum - 1);
            updateUser.setTotalNum(totalNum + 1);
            // 更新用户信息
            boolean result = userService.updateById(updateUser);
            if(!result){
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        }
        return proceed;
    }
}

