package com.albez.sp500.controller;

import com.albez.sp500.model.GSPC;
import com.albez.sp500.service.SP500Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SP500Controller {
    private final SP500Service sp500Service;

    @GetMapping("/^GSPC")
    public List<GSPC> getGSPCList(@RequestParam("period1") String period1,
                                 @RequestParam("period2") String period2) {
        return sp500Service.getGSPCList(period1, period2);
    }

    @GetMapping(value = "/^GSPC/csv", produces = "text/csv;charset=UTF-8")
    public ResponseEntity<String> getGSPCCsv(@RequestParam("period1") String period1,
                                            @RequestParam("period2") String period2) {
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/csv;charset=UTF-8"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=GSPC.csv")
                .body(sp500Service.getGSPCCsv(period1, period2));
    }
}
