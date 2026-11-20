package com.albez.sp500.service;

import com.albez.sp500.client.ChartClient;
import com.albez.sp500.domain.response.GSPCResponse;
import com.albez.sp500.mapper.GSPCMapper;
import com.albez.sp500.model.GSPC;
import com.albez.sp500.utils.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SP500Service {
    private final GSPCMapper gspcMapper;
    private final ChartClient chartClient;

    @Transactional
    public int syncGSPC() {
        Instant start = getMaxDate().toInstant()
                .atZone(ZoneOffset.UTC)
                .toLocalDate()
                .plusDays(1)
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant();
        Instant end = Instant.now();
        if (!start.isBefore(end)) {
            return 0;
        }
        GSPCResponse response = chartClient.getGSPCResponse(
                Math.toIntExact(start.getEpochSecond()), Math.toIntExact(end.getEpochSecond()));
        if (response == null) {
            throw new IllegalStateException("Missing GSPC response");
        }
        List<GSPC> prices = response.toGSPCList();
        for (GSPC price : prices) {
            gspcMapper.upsert(price);
        }
        return prices.size();
    }

    public Date getMaxDate() {
        GSPC latest = gspcMapper.selectOne(Wrappers.<GSPC>lambdaQuery()
                .select(GSPC::getDate)
                .orderByDesc(GSPC::getDate)
                .last("LIMIT 1"));
        return latest == null ? DateUtil.stringToDate("1929-12-30") : latest.getDate();
    }

    public List<GSPC> getGSPCList(Date period1, Date period2) {
        return gspcMapper.selectList(Wrappers.<GSPC>lambdaQuery()
                .between(GSPC::getDate, period1, period2)
                .orderByAsc(GSPC::getDate));
    }

    public List<GSPC> getGSPCList(String period1, String period2) {
        return getGSPCList(DateUtil.stringToDate(period1), DateUtil.stringToDate(period2));
    }

    public String getGSPCCsv(String period1, String period2) {
        List<GSPC> prices = getGSPCList(period1, period2);
        StringBuilder csv = new StringBuilder("Date,Open,High,Low,Close,Adj Close,Volume\r\n");
        for (GSPC price : prices) {
            csv.append(price.getDate() == null ? "" : Instant.ofEpochMilli(price.getDate().getTime())
                            .atZone(ZoneOffset.UTC).toLocalDate().toString())
                    .append(',').append(Objects.toString(price.getOpen(), ""))
                    .append(',').append(Objects.toString(price.getHigh(), ""))
                    .append(',').append(Objects.toString(price.getLow(), ""))
                    .append(',').append(Objects.toString(price.getClose(), ""))
                    .append(',').append(Objects.toString(price.getAdjClose(), ""))
                    .append(',').append(Objects.toString(price.getVolume(), ""))
                    .append("\r\n");
        }
        return csv.toString();
    }
}
