package com.example.photosapplication.util;

public class SearchPhotoInputParameters {
    public int searchMode = 1;
    public String tagType1 = null;
    public String tagValue1 = null;
    public String tagType2 = null;
    public String tagValue2 = null;

    public boolean tag2IsNull() {
        return tagType2 == null && tagValue2 == null;
    }
}
