package com.yupi.springbootinit.manager;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.common.BaseResponse;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.common.ResultUtils;
import com.yupi.springbootinit.datasource.*;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.exception.ThrowUtils;
import com.yupi.springbootinit.model.dto.post.PostQueryRequest;
import com.yupi.springbootinit.model.dto.search.SearchRequest;
import com.yupi.springbootinit.model.dto.user.UserQueryRequest;
import com.yupi.springbootinit.model.entity.Picture;
import com.yupi.springbootinit.model.enums.SearchTypeEnum;
import com.yupi.springbootinit.model.vo.PostVO;
import com.yupi.springbootinit.model.vo.SearchVO;
import com.yupi.springbootinit.model.vo.UserVO;
import com.yupi.springbootinit.service.PictureService;
import com.yupi.springbootinit.service.PostService;
import com.yupi.springbootinit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 搜索门面
 */
@Component
@Slf4j
public class SearchFacade {

    @Resource
    private PostDataSource postDataSource;

    @Resource
    private UserDataSource userDataSource;

    //注入
    @Resource
    private PictureDataSource pictureDataSource;

    @Resource
    private DataSourceRegistry dataSourceRegistry;

    @PostMapping("/all")
    public SearchVO searchALL(@RequestBody SearchRequest searchRequest, HttpServletRequest request){


        String type = searchRequest.getType();
        //判断取出的type值中是否有对应的参数类型
        SearchTypeEnum searchTypeEnum = SearchTypeEnum.getEnumByValue(type);
        ThrowUtils.throwIf(StringUtils.isBlank(type), ErrorCode.PARAMS_ERROR);
        String searchText = searchRequest.getSearchText();
        long current = searchRequest.getCurrent();
        long pageSize = searchRequest.getPageSize();

        //如果为空则输出所有数据
        if (searchTypeEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "未指定查询类型");
        } else {
            SearchVO searchVO = new SearchVO();
            DataSource<?> dataSource = dataSourceRegistry.getDataSourceByType(type);
            Page<?> page = dataSource.doSearch(searchText, current, pageSize);
            searchVO.setPageData(page);
            return searchVO;
        }
            //同步将所有前端的请求进行封装
//        Page<Picture> picturePage = pictureService.searchPicture(searchText, 1, 10);
//
//        UserQueryRequest userQueryRequest = new UserQueryRequest();
//        userQueryRequest.setUserName(searchText);
//        Page<UserVO> userVOPage = userService.listUserVoByPage(userQueryRequest);
//        PostQueryRequest postQueryRequest = new PostQueryRequest();
//        postQueryRequest.setSearchText(searchText);
//        Page<PostVO> postVOPage = postService.listPostVOByPage(postQueryRequest, request);
//
//        SearchVO searchVO = new SearchVO();
//        searchVO.setUserList(userVOPage.getRecords());
//        searchVO.setPostList(postVOPage.getRecords());
//        searchVO.setPictureList(picturePage.getRecords());

//            //异步将所有前端的请求进行封装
//            CompletableFuture<Page<UserVO>> userTask = CompletableFuture.supplyAsync(()->{
//                UserQueryRequest userQueryRequest = new UserQueryRequest();
//                userQueryRequest.setUserName(searchText);
//                Page<UserVO> userVOPage = userDataSource.doSearch(searchText,current,pageSize);
//                return userVOPage;
//            });
//
//            CompletableFuture<Page<PostVO>> postTask = CompletableFuture.supplyAsync(()->{
//                PostQueryRequest postQueryRequest = new PostQueryRequest();
//                postQueryRequest.setSearchText(searchText);
//                Page<PostVO> postVOPage = postDataSource.doSearch(searchText,current,pageSize);
//                return postVOPage;
//            });

//            CompletableFuture<Page<Picture>> pictureTask = CompletableFuture.supplyAsync(()->{
//                UserQueryRequest userQueryRequest = new UserQueryRequest();
//                userQueryRequest.setUserName(searchText);
//                Page<Picture> picturePage = pictureDataSource.doSearch(searchText,current,pageSize);
//                return picturePage;
//            });




//            try{
//                //将所有的任务装在一起后才能继续往下执行
//                CompletableFuture.allOf(userTask,postTask,pictureTask).join();
//
//                Page<UserVO> userVOPage = userTask.get();
//                Page<PostVO> postVOPage = postTask.get();
//                Page<Picture> picturePage = pictureTask.get();
//                SearchVO searchVO = new SearchVO();
//                searchVO.setUserList(userVOPage.getRecords());
//                searchVO.setPostList(postVOPage.getRecords());
//                searchVO.setPictureList(picturePage.getRecords());
//                return searchVO;
//            }catch (Exception e){
//                log.error("查询异常",e);
//                throw new BusinessException(ErrorCode.SYSTEM_ERROR,"查询异常");
//            }
//        }else{
//            //如果不为空
//
//            //本质上是做了一个注册
//
//            SearchVO searchVO = new SearchVO();
//            DataSource<?> dataSource = dataSourceRegistry.getDataSourceByType(type);
//            Page page = dataSource.doSearch(searchText, current, pageSize);
//            searchVO.setDataList(page.getRecords());
//            return searchVO;
//        }
    }
}
