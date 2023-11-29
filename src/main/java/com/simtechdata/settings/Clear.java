package com.simtechdata.settings;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import static com.simtechdata.settings.LABEL.*;

public class Clear {

    public static final Clear INSTANCE = new Clear();

    private Clear() {
    }

    private final Preferences prefs = LABEL.prefs;

    public void bufferSize() {
        prefs.remove(BUFFER_SIZE.name());
    }

    public void stealthMode() {
        prefs.remove(STEALTH_MODE.name());
    }

    public void clearAll() throws BackingStoreException {
        prefs.clear();
    }
}
