package com.yryc.imlib.model.net;

import com.amap.api.services.core.PoiItem;

public class MapPoi {

    private boolean isChecked;

    private PoiItem poiItem;

    public MapPoi() {
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public PoiItem getPoiItem() {
        return poiItem;
    }

    public void setPoiItem(PoiItem poiItem) {
        this.poiItem = poiItem;
    }
}