package com.simtechdata.settings;

import java.util.prefs.Preferences;

public enum LABEL {

    BUFFER_SIZE;

    public static final Preferences prefs = Preferences.userNodeForPackage(LABEL.class);

}
