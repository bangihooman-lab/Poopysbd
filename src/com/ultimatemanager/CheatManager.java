package com.ultimatemanager;

public class CheatManager {
    private static final int REQUIRED = 5;
    private static final long WINDOW_MS = 3000; // 3 seconds
    private long[] presses = new long[REQUIRED];
    private int idx = 0;
    private MainCanvas owner;

    public CheatManager(MainCanvas owner) {
        this.owner = owner;
        for (int i=0;i<REQUIRED;i++) presses[i] = 0;
    }

    public void registerStarPress() {
        long now = System.currentTimeMillis();
        presses[idx] = now;
        idx = (idx + 1) % REQUIRED;

        // check if all presses are within WINDOW_MS
        long oldest = presses[idx];
        if (oldest == 0) return;
        if (now - oldest <= WINDOW_MS) {
            // activate cheat
            showCheat();
            // reset
            for (int i=0;i<REQUIRED;i++) presses[i] = 0;
        }
    }

    private void showCheat() {
        owner.showCheatMenu();
    }
}
