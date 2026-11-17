package com.albez.sp500.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.yulichang.annotation.Table;
import lombok.Data;

import java.util.Date;

@Data
@TableName("gspc")
public class GSPC {
    @TableId
    private Date date;
    private Double open;
    private Double high;
    private Double low;
    private Double close;
    private Double adjClose;
    private Double volume;
}
