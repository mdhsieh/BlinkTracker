package com.sedsoftware.blinktracker.components.statistic.model

enum class DisplayedPeriod(val takeLast: Int) {
    MINUTE(60), QUARTER_HOUR(60), HOUR(24), SIX_HOURS(24), DAY(30), MONTH(12);
}
