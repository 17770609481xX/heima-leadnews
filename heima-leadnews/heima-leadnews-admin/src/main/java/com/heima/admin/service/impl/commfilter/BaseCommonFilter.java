package com.heima.admin.service.impl.commfilter;

import com.heima.model.admin.dtos.CommonWhereDto;
import com.heima.model.admin.pojos.AdUser;
import com.heima.model.admin.dtos.CommonDto;


//通用过滤的过滤类 后置增强类
public interface BaseCommonFilter {


    void doListAfter(AdUser adUser, CommonDto dto);
    void doUpdateAfter(AdUser adUser, CommonDto dto);
    void doDeleteAfter(AdUser adUser, CommonDto dto);
    void doInsertAfter(AdUser adUser, CommonDto dto);


    /*获取更新字段里面的值*/
    default CommonWhereDto findUpdateValue(String fileId, CommonDto dto){
        if (dto!=null){
            for (CommonWhereDto cw:dto.getSets()){
                if (fileId.equals(cw.getFiled())){
                    return cw;
                }
            }
        }
        return null;
    }

    /*获取查询字段里面的值*/
    default CommonWhereDto findWhereValue(String fileId, CommonDto dto){
        if (dto!=null){
            for (CommonWhereDto cw:dto.getWhere()){
                if (fileId.equals(cw.getFiled())){
                    return cw;
                }
            }
        }
        return null;
    }


}
