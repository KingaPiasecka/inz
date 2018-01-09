package com.agh.wfiis.piase.inz.presenters.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;

import com.agh.wfiis.piase.inz.R;
import com.agh.wfiis.piase.inz.models.pojo.Dust;
import com.agh.wfiis.piase.inz.utils.ColorGenerator;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by piase on 2017-12-31.
 */

public class PM25MapInterface implements MapInterface {

    private final Context context;

    public PM25MapInterface(Context context) {
        this.context = context;
    }

    @Override
    public MarkerOptions getMeasurementMarker(Dust dust) {
        BitmapDescriptor bitmapDescriptor = getMeasurementDescriptor(dust.getPm25());

        LatLng pos = new LatLng(dust.getLatitude(), dust.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions()
                .position(pos)
                .title("PM2.5: " + dust.getPm25().toString())
                //.snippet("PM2.5: " + dust.getPm25().toString())
                .icon(bitmapDescriptor);

        return markerOptions;
    }

    @Override
    public BitmapDescriptor getMeasurementDescriptor(Double measurement) {
        Drawable drawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.maker, null);
        drawable.setColorFilter(Color.parseColor(ColorGenerator.getMeasurementColor(measurement)), PorterDuff.Mode.SRC_ATOP);
        Canvas canvas = new Canvas();
        int px = context.getResources().getDimensionPixelSize(R.dimen.map_dot_marker_size);
        drawable.setBounds(0, 0, px, px);
        Bitmap bitmap = Bitmap.createBitmap(px, px, Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.draw(canvas);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);

        return bitmapDescriptor;
    }
}
