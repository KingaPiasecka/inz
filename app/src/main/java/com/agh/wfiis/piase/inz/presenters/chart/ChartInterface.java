package com.agh.wfiis.piase.inz.presenters.chart;

import com.agh.wfiis.piase.inz.models.pojo.Dust;
import com.agh.wfiis.piase.inz.utils.TimeAxisValueFormatter;

import java.util.List;

/**
 * Created by piase on 2017-12-29.
 */

public interface ChartInterface {
    void updateChart(List<Dust> dustList);
    void setTimeAxisValueFormatter(TimeAxisValueFormatter timeAxisValueFormatter);
}
