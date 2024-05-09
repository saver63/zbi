package com.yupi.springbootinit.datasource;

import com.yupi.springbootinit.model.enums.SearchTypeEnum;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据源注册中心
 */
@Component
public class DataSourceRegistry {

    @Resource
    private PostDataSource postDataSource;
    @Resource
    private UserDataSource userDataSource;
    //注入
    @Resource
    private PictureDataSource pictureDataSource;

    private  Map<String, DataSource<T>> typeDataSourceMap;

    //依赖注入后才调用（也可以用静态代码块，只要保证项目初始化时调用即可）
    @PostConstruct
    public void doInit(){
        typeDataSourceMap =new HashMap(){{
            put(SearchTypeEnum.POST.getValue(), postDataSource);
            put(SearchTypeEnum.USER.getValue(), userDataSource);
            put(SearchTypeEnum.PICTURE.getValue(), pictureDataSource);
        }};
    }
    /**
     * 从map中获取对应的数据源
     * @param type
     * @return
     */
    public  DataSource getDataSourceByType(String type){
        //防止系统刚启动时就被别人调用
        if (typeDataSourceMap == null){
            return null;
        }
        return  typeDataSourceMap.get(type);
    }
}
