package com.albez.sp500.mapper;

import com.albez.sp500.model.GSPC;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GSPCMapper extends BaseMapper<GSPC> {

    @Insert("INSERT INTO gspc (`date`, `open`, `high`, `low`, `close`, adj_close, volume) "
            + "VALUES (#{date}, #{open}, #{high}, #{low}, #{close}, #{adjClose}, #{volume}) "
            + "ON DUPLICATE KEY UPDATE `open` = #{open}, `high` = #{high}, `low` = #{low}, "
            + "`close` = #{close}, adj_close = #{adjClose}, volume = #{volume}")
    int upsert(GSPC price);
}
