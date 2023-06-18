package com.sedsoftware.blinktracker.components.statistic.integration

import com.sedsoftware.blinktracker.components.statistic.BlinkStatistic.Model
import com.sedsoftware.blinktracker.components.statistic.store.BlinkStatisticStore.State

internal val stateToModel: (State) -> Model =
    {
        Model(
            records = it.records,
            checked = it.statsChecked,
            rate = it.averageRate,
            showPlaceholder = it.placeholderVisible,
        )
    }
