package dev.asper.clickhouse;

public enum ClickhouseEngineType {
    VIEW("View"), MERGE_TREE("MergeTree");

    private final String strRep;

    ClickhouseEngineType(String strRep) {
        this.strRep = strRep;
    }

    @Override
    public String toString() {
        return strRep;
    }
}
