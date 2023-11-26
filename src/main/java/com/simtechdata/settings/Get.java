package com.simtechdata.settings;

import java.util.prefs.Preferences;

import static com.simtechdata.settings.LABEL.*;

public class Get {

    public static final Get INSTANCE = new Get();

    private Get() {
    }

    private final Preferences prefs = LABEL.prefs;

    public int bufferSize() {
        return prefs.getInt(BUFFER_SIZE.name(), 0);
    }
}
