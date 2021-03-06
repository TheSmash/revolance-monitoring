package com.smash.revolance.process.monitoring.statistics;

import com.smash.revolance.commons.Series;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ebour on 08/12/13.
 */
public class Stats
{
    private Map<String, Series> metaSeries = new HashMap<String, Series>();

    public void addSeries(String label, Series series)
    {
        metaSeries.put(label, series);
    }

    public Series getSeries(String label)
    {
        return metaSeries.get(label);
    }
}
