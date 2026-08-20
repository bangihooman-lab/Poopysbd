package com.ultimatemanager;

import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
import java.util.*;

public class MainCanvas extends Canvas {
    private static final int WIDTH = 240;
    private static final int HEIGHT = 320;

    private final String[] MENU = {
        "FOOTBALL MANAGER",
        "CRICKET MANAGER",
        "SETTINGS",
        "HELP",
        "CREDITS",
        "EXIT"
    };

    private int selected = 0;
    private GameMidlet midlet;
    private SaveManager saveManager;
    private CheatManager cheatManager;
    private long lastPaint = 0;
    private final int headerHeight = 48;

    public MainCanvas(GameMidlet m) {
        midlet = m;
        saveManager = new SaveManager();
        cheatManager = new CheatManager(this);
        setFullScreenMode(true);
    }

    protected void paint(Graphics g) {
        // Background
        g.setColor(0x0B2A3A); // deep teal
        g.fillRect(0,0,getWidth(),getHeight());

        // Header
        g.setColor(0xFFD700); // gold
        g.fillRect(0,0,getWidth(), headerHeight);
        g.setColor(0x004040);
        g.setFont(Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE));
        g.drawString("ULTIMATE MANAGER", getWidth()/2, headerHeight/2, Graphics.TOP | Graphics.HCENTER);

        // Subheader / sport badges (stylized)
        int yStart = headerHeight + 8;
        g.setColor(0xFFFFFF);
        g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_MEDIUM));

        // Draw menu
        for (int i=0;i<MENU.length;i++) {
            int y = yStart + i*34;
            if (i==selected) {
                // highlight
                g.setColor(0x224488);
                g.fillRoundRect(8, y-6, getWidth()-16, 28, 6, 6);
                g.setColor(0xFFFFFF);
            } else {
                g.setColor(0xE6F0F5);
            }
            g.drawString(MENU[i], getWidth()/2, y+6, Graphics.TOP | Graphics.HCENTER);
        }

        // Footer / status
        g.setColor(0xFFFFFF);
        g.setFont(Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL));
        String controls = "2/8 Up/Down 5 Select 0 Info  * Secret";
        g.drawString(controls, getWidth()/2, getHeight()-12, Graphics.BASELINE | Graphics.HCENTER);
    }

    protected void keyPressed(int keyCode) {
        // Attempt to read as char - many emulators and devices map numeric keys to their ASCII value.
        char ch = (char) keyCode;
        // Star detection (best-effort): check if the keyCode cast looks like '*'
        if (ch == '*') {
            cheatManager.registerStarPress();
            return;
        }

        int ga = getGameAction(keyCode);
        if (ga == UP) {
            selected--;
            if (selected < 0) selected = MENU.length - 1;
            repaint();
        } else if (ga == DOWN) {
            selected++;
            if (selected >= MENU.length) selected = 0;
            repaint();
        } else if (ga == FIRE) {
            activateSelected();
        } else {
            // handle numeric shortcuts: 2 up, 8 down, 5 select are typical numeric keypad mappings
            if (keyCode == -2 || keyCode == 50) { // '2' numeric (50 ascii)
                selected--;
                if (selected < 0) selected = MENU.length - 1;
                repaint();
            } else if (keyCode == -8 || keyCode == 56) { // '8' numeric
                selected++;
                if (selected >= MENU.length) selected = 0;
                repaint();
            } else if (keyCode == 53 || keyCode == -5) { // '5' numeric
                activateSelected();
            } else if (keyCode == 48) { // '0'
                showInfo();
            }
        }
    }

    private void activateSelected() {
        String item = MENU[selected];
        if ("FOOTBALL MANAGER".equals(item)) {
            // Load football save or start new
            boolean has = saveManager.hasSave("football");
            if (!has) {
                saveManager.createDefaultSave("football");
            }
            showDialog("Football Manager", "Entered Football Manager (save loaded).");
        } else if ("CRICKET MANAGER".equals(item)) {
            boolean has = saveManager.hasSave("cricket");
            if (!has) {
                saveManager.createDefaultSave("cricket");
            }
            showDialog("Cricket Manager", "Entered Cricket Manager (save loaded).");
        } else if ("SETTINGS".equals(item)) {
            showDialog("Settings", "No settings yet. Coming soon.");
        } else if ("HELP".equals(item)) {
            showDialog("Help", "Use numeric keys or D-pad to navigate. * for secret.");
        } else if ("CREDITS".equals(item)) {
            showDialog("Credits", "Ultimate Manager\nBuilt for J2ME Loader demo.");
        } else if ("EXIT".equals(item)) {
            ((GameMidlet)midlet).exit();
        }
    }

    private void showDialog(String title, String msg) {
        Alert a = new Alert(title, msg, null, AlertType.INFO);
        a.setTimeout(2000);
        Display.getDisplay(midlet).setCurrent(a);
        // after timeout, restore menu
        try {
            Thread.sleep(400);
        } catch (Exception e) { }
        Display.getDisplay(midlet).setCurrent(this);
    }

    private void showInfo() {
        showDialog("Info", "Saves kept separately for Football and Cricket.");
    }

    // Allow CheatManager to show the cheat menu overlay
    public void showCheatMenu() {
        // Simple cheat menu - toggles
        StringBuffer sb = new StringBuffer();
        sb.append("CHEAT MODE\n\n");
        sb.append("1) Unlimited money\n");
        sb.append("2) Max player development\n");
        sb.append("3) Restore fitness\n\n");
        sb.append("Cheats are placeholders in this prototype.");
        showDialog("CHEAT MODE", sb.toString());
    }
}
