package com.example.jucdemo.phaser;

public enum PhaserEnum {

    PHASE0(0, "This is phase0", false),
    PHASE1(1, "This is phase1", false),
    PHASE2(2, "This is phase2", true),
    UNKNOWN(-1, "Unknown Phase", true);

    private Integer phase;
    private String description;
    private boolean finalPhase;

    PhaserEnum(Integer phase, String description, boolean finalPhase) {
        this.phase = phase;
        this.description = description;
        this.finalPhase = finalPhase;
    }

    public Integer getPhase() {
        return phase;
    }

    public String getDescription() {
        return description;
    }


    public static PhaserEnum getByPhase(int phase) {
        PhaserEnum[] phaserEnum = PhaserEnum.values();
        for (int i = 0; i < phaserEnum.length; i++) {
            if (phaserEnum[i].getPhase() == phase) {
                return phaserEnum[i];
            }
        }
        return UNKNOWN;
    }

    public boolean isFinalPhase() {
        return finalPhase;
    }
}
