package dev.asper.app.component;

import com.clickhouse.jdbc.ClickHouseConnection;
import com.clickhouse.jdbc.ClickHouseDataSource;
import dev.asper.app.config.ClickhouseConfig;
import dev.asper.common.feature.CategoricalFeature;
import dev.asper.common.feature.Descriptor;
import dev.asper.common.feature.Feature;
import dev.asper.common.feature.StringFeature;
import dev.asper.poker.engine.PokerFeatureName;
import dev.asper.poker.engine.PokerFeatures;
import dev.asper.poker.engine.PokerGame;
import dev.asper.poker.engine.PokerSituation;
import dev.asper.poker.enums.Street;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.clickhouse.domain.ClickHouseDataType;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ClickhouseCollector {
    private final ClickHouseConnection connection;
    private final JdbcTemplate clickhouseJdbcTemplate;
    protected final Logger log = Logger.getLogger(this.getClass().getName());

    @Autowired
    public ClickhouseCollector(ClickHouseDataSource clickHouseDataSource) throws SQLException {
        this.connection = clickHouseDataSource.getConnection();
        this.clickhouseJdbcTemplate = new JdbcTemplate(clickHouseDataSource);
    }

    public ClickHouseDataType getClickHouseDataType(Feature<?, ?> feature) {
        return !(feature instanceof CategoricalFeature) && !(feature instanceof StringFeature) ? ClickHouseDataType.Float64 : ClickHouseDataType.String;
    }

    private void createTableIfNotExists(String schemaName, String tableName, List<Feature<?, PokerGame>> pokerFeatures) {
        String sql = "create table if not exists " + schemaName + "." + tableName + " (" + pokerFeatures.stream().map((feature) -> {
            String name = feature.name().toString();
            return name + " " + this.getClickHouseDataType(feature).name();
        }).collect(Collectors.joining(", ")) +
                ", " + PokerFeatureName.MINING_TYPE.name() + " " + ClickHouseDataType.String.name() +
                ", " + PokerFeatureName.PROFIT_BB.name() + " " + ClickHouseDataType.Float64.name() +
                ", " + PokerFeatureName.COMPETITION_TYPE.name() + " " + ClickHouseDataType.String.name() +
                ", " + PokerFeatureName.OPP_TYPES.name() + " " + "Array(String)" +
                ") engine = MergeTree  ORDER BY (tuple())  SETTINGS index_granularity = 8192";
        try {
            this.connection.prepareStatement(sql).execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void createHoldemStatTableIfNotExists(String schemaName, String tableName, List<Feature<?, PokerGame>> pokerFeatures) {
        String sql = "create table if not exists " + schemaName + "." + tableName + " (" + pokerFeatures.stream().map((feature) -> {
            String name = feature.name().toString();
            return name + " " + this.getClickHouseDataType(feature).name();
        }).collect(Collectors.joining(", ")) +
                ", " + PokerFeatureName.HERO_BRANCH.name() + " " + ClickHouseDataType.String.name() +
                ", " + PokerFeatureName.PROFIT_BB.name() + " " + ClickHouseDataType.Float64.name() +
                ", " + PokerFeatureName.COMPETITION_TYPE.name() + " " + ClickHouseDataType.String.name() +
                ", " + PokerFeatureName.OPP_TYPES.name() + " " + "Array(String)" +
                ") engine = MergeTree  ORDER BY (tuple())  SETTINGS index_granularity = 8192";
        try {
            this.connection.prepareStatement(sql).execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    protected void saveAll(String schemaName, String tableName, List<List<Descriptor<?>>> descriptorList) {
        if (!descriptorList.isEmpty()) {
            List<Descriptor<?>> descriptors = descriptorList.get(0);
            int featuresCount = descriptors.size() + 1;
            String sql = "insert into " + schemaName + "." + tableName + " (*) values (" + "?,".repeat(featuresCount) + "?)";
            try {
                flushBatch(descriptorList, sql);
                Thread.sleep(50);
                log.severe(descriptorList.size() + " rows were flushed to tableName=" + tableName);
            } catch (Exception exception) {
                log.severe("Flush datasets error: " + exception.getMessage() + ", tableName=" + tableName);
            }
        }
    }

    private void flushBatch(List<List<Descriptor<?>>> descriptorList, String sql) {
        clickhouseJdbcTemplate.batchUpdate(sql,
                descriptorList, ClickhouseConfig.CLICKHOUSE_BATCH_SIZE, (ps, descriptorsRow) -> {
                    int idx = 0;
                    for (Descriptor<?> descriptor : descriptorsRow) {
                        ps.setObject(++idx, descriptor.value());
                    }
                });
    }

    public void save(String schemaName, Map<Pair<PokerSituation, String>, List<List<Descriptor<?>>>> dataset) {
        AtomicInteger cnt = new AtomicInteger();
        dataset.forEach((p, lists) -> {
            if (!lists.isEmpty()) {
                PokerSituation pokerSituation = p.getLeft();
                List<Feature<?, PokerGame>> features = Stream.of(pokerSituation.getServiceFeatures(), pokerSituation.getFeatures(), pokerSituation.getLabels())
                        .flatMap(Collection::stream).toList();
                String tableName = p.getRight();
                createTableIfNotExists(schemaName, tableName, features);
                saveAll(schemaName, tableName, lists);
                cnt.addAndGet(lists.size());
            }
        });
        log.info("Flushed " + cnt.get() + " rows in total");
    }

    public void save(String schemaName, Map<Street, List<Feature<?, PokerGame>>> holdemStatFeatures, Map<PokerSituation, List<List<Descriptor<?>>>> holdemStats) {
        AtomicInteger cnt = new AtomicInteger();
        holdemStats.forEach((pokerSituation, lists) -> {
            if (!lists.isEmpty()) {
                List<Feature<?, PokerGame>> features = holdemStatFeatures.get(pokerSituation.getStreet());
                String tableName = pokerSituation.name();
                createHoldemStatTableIfNotExists(schemaName, tableName, features);
                saveAll(schemaName, tableName, lists);
                cnt.addAndGet(lists.size());
            }
        });
        log.info("Flushed " + cnt.get() + " rows in total");
    }
}
