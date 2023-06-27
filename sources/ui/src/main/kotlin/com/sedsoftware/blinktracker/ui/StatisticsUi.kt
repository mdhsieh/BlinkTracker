package com.sedsoftware.blinktracker.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.edges.rememberFadingEdges
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.axis.AxisPosition.Horizontal
import com.patrykandpatrick.vico.core.axis.AxisPosition.Vertical
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.axis.vertical.VerticalAxis.HorizontalLabelPosition.Outside
import com.patrykandpatrick.vico.core.chart.line.LineChart.PointPosition.Start
import com.patrykandpatrick.vico.core.entry.ChartEntry
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.sedsoftware.blinktracker.components.statistic.BlinkStatistic
import com.sedsoftware.blinktracker.ui.R.string
import com.sedsoftware.blinktracker.ui.component.rememberChartStyle
import com.sedsoftware.blinktracker.ui.model.CustomChartEntry
import com.sedsoftware.blinktracker.ui.model.toChartEntries
import com.sedsoftware.blinktracker.ui.preview.PreviewStubs
import com.sedsoftware.blinktracker.ui.theme.BlinkTrackerTheme

@Composable
fun BlinkStatisticContent(
    state: BlinkStatistic.Model,
    modifier: Modifier = Modifier,
) {

    StatsPanelCard(
        model = state,
        modifier = modifier,
    )
}

@Composable
private fun StatsPanelCard(
    model: BlinkStatistic.Model,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = RoundedCornerShape(size = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp,
        ),
        modifier = modifier
            .fillMaxWidth()
    ) {
        when {
            !model.checked -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = stringResource(id = string.loading),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(all = 32.dp),
                    )
                }
            }

            model.checked && model.showPlaceholder -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = stringResource(id = string.stats_placeholder),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(all = 32.dp),
                    )
                }
            }

            else -> {
                StatsPanelDetails(
                    min = model.min,
                    max = model.max,
                    average = model.average,
                    entries = model.records.toChartEntries(),
                )
            }
        }
    }
}

@Composable
private fun StatsPanelDetails(
    min: Int,
    max: Int,
    average: Float,
    entries: List<ChartEntry>,
) {
    val color1: Color = MaterialTheme.colorScheme.primary
    val color2: Color = MaterialTheme.colorScheme.secondary
    val chartColors: List<Color> = listOf(color1, color2)
    val chartEntryModelProducer = remember { ChartEntryModelProducer() }

    LaunchedEffect(entries) {
        chartEntryModelProducer.setEntries(entries)
    }

    Column {
        Text(
            text = stringResource(id = string.today),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            ProvideChartStyle(rememberChartStyle(chartColors)) {
                Chart(
                    chart = lineChart(pointPosition = Start),
                    chartModelProducer = chartEntryModelProducer,
                    startAxis = startAxis(
                        guideline = null,
                        horizontalLabelPosition = Outside,
                        valueFormatter = startAxisFormatter,
                    ),
                    bottomAxis = bottomAxis(
                        valueFormatter = bottomAxisFormatter,
                        labelRotationDegrees = -90f,
                    ),
                    fadingEdges = rememberFadingEdges(),
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 0.dp)
                        .fillMaxSize(),
                )
            }
        }

        Text(
            text = "${stringResource(id = string.min)}: $min | " +
                "${stringResource(id = string.max)}: $max | " +
                "${stringResource(id = string.average)}: $average",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.padding(all = 16.dp)
        )
    }
}

private val startAxisFormatter = AxisValueFormatter<Vertical.Start> { value, _ ->
    "${value.toInt()}"
}

private val bottomAxisFormatter = AxisValueFormatter<Horizontal.Bottom> { value, chartValues ->
    (chartValues.chartEntryModel.entries.firstOrNull()?.getOrNull(value.toInt()) as? CustomChartEntry)
        ?.date
        .orEmpty()
}

@Composable
@Preview(showBackground = true, widthDp = 400, heightDp = 300)
private fun PreviewStatsLoadingLight() {
    BlinkTrackerTheme(darkTheme = false) {
        Surface(color = MaterialTheme.colorScheme.primaryContainer) {
            StatsPanelCard(
                model = PreviewStubs.statsEmptyNotChecked,
            )
        }
    }
}

@Composable
@Preview(showBackground = true, widthDp = 400, heightDp = 300)
private fun PreviewStatsLoadedLight() {
    BlinkTrackerTheme(darkTheme = false) {
        Surface(color = MaterialTheme.colorScheme.primaryContainer) {
            StatsPanelCard(
                model = PreviewStubs.statsEmptyChecked,
            )
        }
    }
}

@Composable
@Preview(showBackground = true, widthDp = 400, heightDp = 300)
private fun PreviewStatsLoadingDark() {
    BlinkTrackerTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.primaryContainer) {
            StatsPanelCard(
                model = PreviewStubs.statsEmptyNotChecked,
            )
        }
    }
}

@Composable
@Preview(showBackground = true, widthDp = 400, heightDp = 300)
private fun PreviewStatsLoadedDark() {
    BlinkTrackerTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.primaryContainer) {
            StatsPanelCard(
                model = PreviewStubs.statsEmptyChecked,
            )
        }
    }
}

@Composable
@Preview(showBackground = true, widthDp = 400, heightDp = 300)
private fun PreviewStatsContentLight() {
    BlinkTrackerTheme(darkTheme = false) {
        Surface(color = MaterialTheme.colorScheme.primaryContainer) {
            StatsPanelCard(
                model = PreviewStubs.statsFull,
            )
        }
    }
}

@Composable
@Preview(showBackground = true, widthDp = 400, heightDp = 300)
private fun PreviewStatsContentDark() {
    BlinkTrackerTheme(darkTheme = false) {
        Surface(color = MaterialTheme.colorScheme.primaryContainer) {
            StatsPanelCard(
                model = PreviewStubs.statsFull,
            )
        }
    }
}