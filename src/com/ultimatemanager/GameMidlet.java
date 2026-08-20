package com.ultimatemanager;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public class GameMidlet extends MIDlet {
    private Display display;
    private MainCanvas canvas;

    public void startApp() {
        if (display == null) {
            display = Display.getDisplay(this);
        }
        if (canvas == null) {
            canvas = new MainCanvas(this);
        }
        display.setCurrent(canvas);
        canvas.requestFocus();
    }

    public void pauseApp() {
        // nothing for now
    }

    public void destroyApp(boolean unconditional) {
        // cleanup
        canvas = null;
    }

    public void exit() {
        try {
            destroyApp(false);
        } catch (Exception e) { }
        notifyDestroyed();
    }
}
