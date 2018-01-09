package com.agh.wfiis.piase.inz.utils;

/**
 * Created by piase on 2017-12-20.
 */

enum ColorValue {
    VERY_GOOD (0x28af03, 10.0),
    GOOD (0x9ad813, 40.0),
    MODERATE (0xf2dc15, 80.0),
    FAIR (0xe5830b, 120.0),
    BAD (0xe01604, 170.0),
    VERY_BAD (0x8c0b00, 250.0);

    private final Integer color;
    private final Double threshold;

    ColorValue(Integer color, Double threshold) {
        this.color = color;
        this.threshold = threshold;
    }

    public Integer getColor() {
        return color;
    }

    public Double getThreshold() {
        return threshold;
    }
}
