package dev.asper.clickhouse;

import ru.yandex.clickhouse.domain.ClickHouseDataType;


public record TypeColumn(ClickHouseDataType clickHouseDataType, String name) {
}
