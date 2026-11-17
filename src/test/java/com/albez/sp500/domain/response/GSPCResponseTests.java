package com.albez.sp500.domain.response;

import com.albez.sp500.model.GSPC;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GSPCResponseTests {

    @Test
    void convertsQuotesByTimestampIndex() {
        GSPCResponse.Quote quote = new GSPCResponse.Quote();
        quote.setOpen(Arrays.asList(1314.95, null));
        quote.setHigh(Arrays.asList(1334.67, 1340.0));
        quote.setLow(Arrays.asList(1307.02, 1310.0));
        quote.setClose(Arrays.asList(1315.23, 1324.97));
        quote.setVolume(Arrays.asList(11456230000L, 0L));
        GSPCResponse.AdjClose adjusted = new GSPCResponse.AdjClose();
        adjusted.setAdjclose(Collections.singletonList(1315.23));
        GSPCResponse.Indicators indicators = new GSPCResponse.Indicators();
        indicators.setQuote(Collections.singletonList(quote));
        indicators.setAdjclose(Collections.singletonList(adjusted));
        GSPCResponse.Result result = new GSPCResponse.Result();
        result.setTimestamp(Arrays.asList(975681000L, 975940200L));
        result.setIndicators(indicators);
        GSPCResponse response = responseWith(result);

        List<GSPC> prices = response.toGSPCList();

        assertEquals(2, prices.size());
        assertEquals(975681000000L, prices.get(0).getDate().getTime());
        assertEquals(Double.valueOf(1314.95), prices.get(0).getOpen());
        assertEquals(Double.valueOf(1334.67), prices.get(0).getHigh());
        assertEquals(Double.valueOf(1307.02), prices.get(0).getLow());
        assertEquals(Double.valueOf(1315.23), prices.get(0).getClose());
        assertEquals(Double.valueOf(1315.23), prices.get(0).getAdjClose());
        assertEquals(Double.valueOf(11456230000.0), prices.get(0).getVolume());
        assertEquals(975940200000L, prices.get(1).getDate().getTime());
        assertNull(prices.get(1).getOpen());
        assertNull(prices.get(1).getAdjClose());
        assertEquals(Double.valueOf(0.0), prices.get(1).getVolume());
    }

    @Test
    void returnsEmptyListForEmptyResults() {
        GSPCResponse response = new GSPCResponse();
        response.setChart(new GSPCResponse.Chart());
        assertTrue(response.toGSPCList().isEmpty());
    }

    @Test
    void rejectsApiErrorsAndMissingChart() {
        GSPCResponse response = new GSPCResponse();
        assertThrows(IllegalStateException.class, response::toGSPCList);
        GSPCResponse.Chart chart = new GSPCResponse.Chart();
        GSPCResponse.ChartError error = new GSPCResponse.ChartError();
        error.setCode("Not Found");
        error.setDescription("No data found");
        chart.setError(error);
        response.setChart(chart);
        assertEquals("Not Found: No data found",
                assertThrows(IllegalStateException.class, response::toGSPCList).getMessage());
    }

    @Test
    void rejectsMissingQuotes() {
        GSPCResponse.Result result = new GSPCResponse.Result();
        result.setTimestamp(Collections.singletonList(975681000L));
        assertThrows(IllegalStateException.class, () -> responseWith(result).toGSPCList());
    }

    private GSPCResponse responseWith(GSPCResponse.Result result) {
        GSPCResponse.Chart chart = new GSPCResponse.Chart();
        chart.setResult(Collections.singletonList(result));
        GSPCResponse response = new GSPCResponse();
        response.setChart(chart);
        return response;
    }
}
