package com.yupi.springbootinit.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.yupi.springbootinit.annotation.AuthCheck;
import com.yupi.springbootinit.common.BaseResponse;
import com.yupi.springbootinit.common.DeleteRequest;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.common.ResultUtils;
import com.yupi.springbootinit.constant.UserConstant;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.exception.ThrowUtils;
import com.yupi.springbootinit.model.dto.notification.NotificationAddRequest;
import com.yupi.springbootinit.model.dto.notification.NotificationQueryRequest;
import com.yupi.springbootinit.model.dto.notification.NotificationUpdateRequest;
import com.yupi.springbootinit.model.entity.Notification;
import com.yupi.springbootinit.model.entity.User;
import com.yupi.springbootinit.model.vo.NotificationVO;
import com.yupi.springbootinit.service.NotificationService;
import com.yupi.springbootinit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;



/**
 * 帖子接口
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@RestController
@RequestMapping("/notification")
@Slf4j
public class NotificationController {

    @Resource
    private NotificationService notificationService;

    @Resource
    private UserService userService;

    private final static Gson GSON=new Gson();

    // region 增删改查

    /**
     * 创建
     *
     * @param notificationAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addNotification(@RequestBody NotificationAddRequest notificationAddRequest, HttpServletRequest request) {
        if (notificationAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Notification notification = new Notification();
        BeanUtils.copyProperties(notificationAddRequest, notification);

        //从前端的请求类中获取域名
        List<String> domains = notificationAddRequest.getDomain();
        if (domains != null){
            notification.setDomain(GSON.toJson(domains));
        }
        notificationService.validNotification(notification,domains,true);


        User loginUser = userService.getLoginUser(request);
        notification.setUserId(loginUser.getId());
        boolean result = notificationService.save(notification);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newNotificationId = notification.getId();
        return ResultUtils.success(newNotificationId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteNotification(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Notification oldNotification = notificationService.getById(id);
        ThrowUtils.throwIf(oldNotification == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldNotification.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = notificationService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param notificationUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateNotification(@RequestBody NotificationUpdateRequest notificationUpdateRequest) {
        if (notificationUpdateRequest == null || notificationUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Notification notification = new Notification();
        BeanUtils.copyProperties(notificationUpdateRequest, notification);
        List<String> domains = notificationUpdateRequest.getDomain();
        if (domains != null){
            notification.setDomain(GSON.toJson(domains));
        }

        // 参数校验
        notificationService.validNotification(notification,domains,false);
        long id = notificationUpdateRequest.getId();
        // 判断是否存在
//        Notification oldNotification = notificationService.getById(id);
        long count = notificationService.count(new QueryWrapper<Notification>().eq("id",id));
        ThrowUtils.throwIf(count == 0, ErrorCode.NOT_FOUND_ERROR);
        boolean result = notificationService.updateById(notification);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<Notification> getNotificationById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Notification notification = notificationService.getById(id);
        if (notification == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(notification);
    }

    /**
     * 分页获取列表（仅管理员）
     *
     * @param notificationQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Notification>> listNotificationByPage(@RequestBody NotificationQueryRequest notificationQueryRequest) {
        long current = notificationQueryRequest.getCurrent();
        long size = notificationQueryRequest.getPageSize();
        Page<Notification> notificationPage = notificationService.page(new Page<>(current, size),
                notificationService.getQueryWrapper(notificationQueryRequest));
        return ResultUtils.success(notificationPage);
    }

    /**
     * 根据 id 获取
     *
     * @param domain
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<NotificationVO> getNotificationVO(@RequestParam String domain) {
        //1.校验参数
        if (domain.isEmpty()){
            throw  new BusinessException(ErrorCode.PARAMS_ERROR,"域名为空");
        }
        //2.查询通知
        Notification notification = notificationService.getOne(new QueryWrapper<Notification>().like("domain","\""+domain+"\""));
        if (notification == null){
            throw  new BusinessException(ErrorCode.NOT_FOUND_ERROR,"暂时没有通知");
        }
        //3.检验通知是否开启
        Integer status = notification.getStatus();
        //检验未开启状态
        if (status == 0){
            return  ResultUtils.success(null);
        }

        //4.检验是否在开始时间到结束时间内
        Date startTime = notification.getStartTime();
        Date endTime = notification.getEndTime();
        if (startTime == null || endTime == null){
            throw  new BusinessException(ErrorCode.PARAMS_ERROR,"开始时间或结束时间为空");
        }
        //获取当前时间
        Date date = DateUtil.date();
        //判断当前时间是否在开始时间到结束时间内
        if (date.before(startTime) || date.after(endTime)){
            return ResultUtils.success(null);
        }
        log.info("getNotification: {}",notification);
        NotificationVO notificationVO = notificationService.getNotificationVo(notification);
        return ResultUtils.success(notificationVO);
    }


}
