package com.heima.model.mappers.wemedia;

import com.heima.model.admin.pojos.AdChannel;

import java.util.List;

public interface AdChannelMapper {
    /**
     * 查询所有
     */
    public List<AdChannel> selectAll();
}