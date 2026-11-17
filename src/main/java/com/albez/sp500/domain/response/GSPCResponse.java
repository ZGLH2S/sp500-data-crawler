package com.albez.sp500.domain.response;

import com.albez.sp500.model.GSPC;
import com.albez.sp500.utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GSPCResponse {
    private Chart chart;

    public List<GSPC> toGSPCList() {
        if (chart == null) {
            throw new IllegalStateException("Missing chart in GSPC response");
        }
        if (chart.getError() != null) {
            throw new IllegalStateException(chart.getError().getCode() + ": "
                    + chart.getError().getDescription());
        }
        List<GSPC> prices = new ArrayList<>();
        if (chart.getResult() == null) {
            return prices;
        }
        for (Result result : chart.getResult()) {
            if (result == null || result.getTimestamp() == null || result.getTimestamp().isEmpty()) {
                continue;
            }
            Indicators indicators = result.getIndicators();
            if (indicators == null || indicators.getQuote() == null
                    || indicators.getQuote().isEmpty() || indicators.getQuote().getFirst() == null) {
                throw new IllegalStateException("Missing quote for GSPC timestamps");
            }
            Quote quote = indicators.getQuote().getFirst();
            AdjClose adjusted = indicators.getAdjclose() == null || indicators.getAdjclose().isEmpty()
                    ? null : indicators.getAdjclose().getFirst();
            for (int index = 0; index < result.getTimestamp().size(); index++) {
                Long timestamp = result.getTimestamp().get(index);
                if (timestamp == null) {
                    throw new IllegalStateException("Null GSPC timestamp at index " + index);
                }
                GSPC price = new GSPC();
                price.setDate(DateUtil.timestampToDate(timestamp));
                price.setOpen(valueAt(quote.getOpen(), index));
                price.setHigh(valueAt(quote.getHigh(), index));
                price.setLow(valueAt(quote.getLow(), index));
                price.setClose(valueAt(quote.getClose(), index));
                price.setAdjClose(adjusted == null ? null : valueAt(adjusted.getAdjclose(), index));
                Long volume = valueAt(quote.getVolume(), index);
                price.setVolume(volume == null ? null : volume.doubleValue());
                prices.add(price);
            }
        }
        return prices;
    }

    private static <T> T valueAt(List<T> values, int index) {
        return values == null || index >= values.size() ? null : values.get(index);
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Chart {
        private List<Result> result;
        private ChartError error;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ChartError {
        private String code;
        private String description;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {
        private Meta meta;
        private List<Long> timestamp;
        private Indicators indicators;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Meta {
        private String currency;
        private String symbol;
        private String exchangeName;
        private String fullExchangeName;
        private String instrumentType;
        private Long firstTradeDate;
        private Long regularMarketTime;
        private Boolean hasPrePostMarketData;
        private Integer gmtoffset;
        private String timezone;
        private String exchangeTimezoneName;
        private Double regularMarketPrice;
        private Double regularMarketChangePercent;
        private Double fulldayPrice;
        private Double fulldayChange;
        private Double fulldayChangePercent;
        private Double fiftyTwoWeekHigh;
        private Double fiftyTwoWeekLow;
        private Double regularMarketDayHigh;
        private Double regularMarketDayLow;
        private Long regularMarketVolume;
        private String longName;
        private String shortName;
        private Double chartPreviousClose;
        private Integer priceHint;
        private CurrentTradingPeriod currentTradingPeriod;
        private String dataGranularity;
        private String range;
        private List<String> validRanges;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CurrentTradingPeriod {
        private TradingPeriod pre;
        private TradingPeriod regular;
        private TradingPeriod post;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TradingPeriod {
        private String timezone;
        private Long start;
        private Long end;
        private Integer gmtoffset;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Indicators {
        private List<Quote> quote;
        private List<AdjClose> adjclose;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Quote {
        private List<Long> volume;
        private List<Double> open;
        private List<Double> high;
        private List<Double> low;
        private List<Double> close;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AdjClose {
        private List<Double> adjclose;
    }
}
