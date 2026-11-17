package com.albez.sp500.controller;

import com.albez.sp500.model.GSPC;
import com.albez.sp500.service.SP500Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SP500Controller {
    private final SP500Service sp500Service;

    @GetMapping("/%5EGSPC")
    public List<GSPC> getGSPCList(@RequestParam("period1") String period1,
                                 @RequestParam("period2") String period2) {
        return sp500Service.getGSPCList(period1, period2);
    }
}
