package com.snhu.cs360.banddatabase;

import android.content.Context;
import android.content.res.Resources;
import java.util.ArrayList;
import java.util.List;

public class BandDatabase {

    private static BandDatabase instance;
    private final List<Band> bands;

    private static int nextBandId;

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

        nextBandId = bands.length + 1;
    }

    public List<Band> getBands() {
        List<Band> copy = new ArrayList<>();
        for (Band b : bands){
            copy.add(new Band(b));
        }
        return copy;
    }

    public Band getBand(int bandId) {
        for (Band band : bands) {
            if (band.getId() == bandId) {
                return new Band(band);
            }
        }
        return null;
    }

    /**
     * Adds a band to the database
     * @param band - the band to add
     * @return - the id of the band, or -1 if it was not added.
     */
    public int addBand(Band band){
        int bandId = -1;

        boolean bandExists = false;
        for (Band b : bands){
            if (b.getName().equals(band.getName())){
                bandExists = true;
                break;
            }
        }

        if (!bandExists){
            bandId = nextBandId;
            Band newBand = new Band(bandId, band);
            bands.add(newBand);
            nextBandId++;
        }

        return bandId;
    }

    /**
     * Edit an existing band
     * @param id - the id of an existing band
     * @param band - the updated band details
     * @return - true is updated, false error occurred
     */
    public boolean editBand(int id, Band band){
        boolean isEdited = false;

        for (Band b : bands){
            if (b.getId() == id){
                b.setName(band.getName());
                b.setDescription(band.getDescription());
                isEdited = true;
                break;
            }
        }

        return isEdited;
    }

    /**
     * Delete a band
     * @param id - the id of an existing band
     * @return - true is deleted, false is an error
     */
    public boolean deleteBand(int id){
        boolean isDeleted = false;

        int index = -1;

        for (int i = 0; i < bands.size(); i++){
            if (bands.get(i).getId() == id){
                index = i;
                break;
            }
        }

        if (index != -1) {
            bands.remove(index);
            isDeleted = true;
        }

        return isDeleted;
    }

}