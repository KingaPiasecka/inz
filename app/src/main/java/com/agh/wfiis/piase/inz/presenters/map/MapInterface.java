package com.agh.wfiis.piase.inz.presenters.map;

import com.agh.wfiis.piase.inz.models.pojo.Dust;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by piase on 2017-12-29.
 */

public interface MapInterface {
    MarkerOptions getMeasurementMarker(Dust dust);
    BitmapDescriptor getMeasurementDescriptor(Double measurement);
}
