package com.home.commands;

import com.home.parameters.InstaParameters;
import com.home.util.InstaStatistics;
import org.brunocvcunha.inutils4j.MyNumberUtils;

import java.io.IOException;

public abstract class InstagramOperation {

    private InstaParameters params;
    private InstaStatistics statistics = new InstaStatistics(0, 0);

    public InstagramOperation(InstaParameters p) {
        this.params = p;
    }

    public abstract InstaStatistics execute() throws IOException;

    public void randomWait(int from, int to) {
        try {
            Thread.sleep(MyNumberUtils.randomLongBetween(from, to));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public InstaStatistics getStatistics() {
        return statistics;
    }

    public InstaParameters getParams() {
        return params;
    }

    public void setParams(InstaParameters params) {
        this.params = params;
    }
}
