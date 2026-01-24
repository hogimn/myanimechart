package com.hogimn.myanimechart.core.common.p6spy;

import com.p6spy.engine.event.JdbcEventListener;
import com.p6spy.engine.spy.P6Factory;
import com.p6spy.engine.spy.P6LoadableOptions;
import com.p6spy.engine.spy.P6SpyOptions;
import com.p6spy.engine.spy.option.P6OptionsRepository;

public class CustomP6Factory implements P6Factory {
    private final JdbcEventListener listener = new CustomJdbcEventListener();

    @Override
    public JdbcEventListener getJdbcEventListener() {
        return listener;
    }

    @Override
    public P6LoadableOptions getOptions(P6OptionsRepository optionsRepository) {
        return new P6SpyOptions(optionsRepository);
    }
}