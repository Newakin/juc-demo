package com.example.jucdemo.phaser;

import java.util.concurrent.Phaser;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyPhaser extends Phaser {

    @Override
    protected boolean onAdvance(int phase, int registeredParties) {
        log.info("Phase:{}, Welcome:{},Thread:{}",phase,PhaserEnum.getByPhase(phase).getDescription(),Thread.currentThread().getName());
        return PhaserEnum.getByPhase(phase).isFinalPhase();
    }
}
