package com.josuesch.assets;

public enum Levels {
    LEVEL_1(WaveType.FIRST, "/map.png"),
    LEVEL_2(WaveType.SECOND, "/map2.png"),
    LEVEL_3(WaveType.THIRD, "/map3.png");

    private WaveType waveType;
    private String mapPath;

    Levels(WaveType waveType, String mapPath) {
        this.waveType = waveType;
        this.mapPath = mapPath;
    }

    public Levels getNextLevel() {
        Levels[] allLevels = Levels.values();
        int nextIndex = this.ordinal() + 1;

        if (nextIndex < allLevels.length) {
            return allLevels[nextIndex];
        }

        return null;
    }

    public WaveType getWaveType() {
        return waveType;
    }

    public String getMapPath() {
        return mapPath;
    }
}
