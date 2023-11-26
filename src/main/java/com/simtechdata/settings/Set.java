package com.simtechdata.settings;

import java.util.prefs.Preferences;

import static com.simtechdata.settings.LABEL.*;

public class Set {

    public static final Set INSTANCE = new Set();

    private Set() {
    }

    private final Preferences prefs = LABEL.prefs;

    public void bufferSize(int value) {
        AppSettings.clear.bufferSize();
        prefs.putInt(BUFFER_SIZE.name(), value);
    }
}
