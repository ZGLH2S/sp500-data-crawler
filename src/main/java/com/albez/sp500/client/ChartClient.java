package com.albez.sp500.client;

import com.albez.sp500.domain.response.GSPCResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "chartClient", url = "${finance.url}")
public interface ChartClient {
    @GetMapping("/%5EGSPC")
    GSPCResponse getGSPCResponse(@RequestParam("interval") String interval,
                                @RequestParam("period1") Integer period1,
                                @RequestParam("period2") Integer period2);

    default GSPCResponse getGSPCResponse(Integer period1, Integer period2) {
        return getGSPCResponse("1d", period1, period2);
    }
}
