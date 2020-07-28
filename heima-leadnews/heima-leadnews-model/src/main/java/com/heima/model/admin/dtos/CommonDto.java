package com.heima.model.admin.dtos;

import com.heima.model.admin.enums.CommonTableEnum;
import lombok.Data;

import java.util.List;

//类用于接收前端传入的接口参数（包括CRUD接口的参数）
@Data
public class CommonDto {

    private Integer size;
    private Integer page;
    // 操作模式add 新增，edit编辑
    private String model;
    // 操作的对象
    private CommonTableEnum name;
    // 查询的条件
    private List<CommonWhereDto> where;
    // 修改的字段
    private List<CommonWhereDto> sets;

}
