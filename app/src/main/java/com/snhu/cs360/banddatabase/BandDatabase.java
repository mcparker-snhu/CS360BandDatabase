package com.snhu.cs360.banddatabase;

import android.content.Context;
import android.content.res.Resources;
import java.util.ArrayList;
import java.util.List;

public class BandDatabase {

    private static BandDatabase instance;
    private List<Band> bands;

    public static BandDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new BandDatabase(context);
        }
        return instance;
    }

    private BandDatabase(Context context) {
        bands = new ArrayList<>();
        Resources res = context.getResources();
        String[] bands = res.getStringArray(R.array.bands);
        String[] descriptions = res.getStringArray(R.array.descriptions);
        for (int i = 0; i < bands.length; i++) {
            this.bands.add(new Band(i + 1, bands[i], descriptions[i]));
        }
    }

    public List<Band> getBands() {
        return bands;
    }

    public Band getBand(int bandId) {
        for (Band band : bands) {
            if (band.getId() == bandId) {
                return band;
            }
        }
        return null;
    }
}