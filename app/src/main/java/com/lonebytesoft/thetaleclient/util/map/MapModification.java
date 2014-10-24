package com.lonebytesoft.thetaleclient.util.map;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.lonebytesoft.thetaleclient.api.dictionary.MapCellWindSpeed;
import com.lonebytesoft.thetaleclient.api.model.MapCellTerrainInfo;
import com.lonebytesoft.thetaleclient.api.model.PlaceInfo;
import com.lonebytesoft.thetaleclient.api.response.MapResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Hamster
 * @since 17.10.2014
 */
public enum MapModification {

    NONE("Обычный"),
    WIND("Ветер") {
        @Override
        public void modifyCell(final Canvas canvas, final int tileX, final int tileY, final MapCellTerrainInfo cellInfo) {
            final float size = MapManager.MAP_TILE_SIZE;
            final float x = tileX * size;
            final float y = tileY * size;

            canvas.drawRect(x, y, x + size, y + size, windPaintRect);

            final float centerX = x + size / 2.0f;
            final float centerY = y + size / 2.0f;
            final float length = size * 0.95f * (((float) cellInfo.windSpeed.ordinal() + 1) / MapCellWindSpeed.values().length);
            final double angle = cellInfo.windDirection.getDirection() / 180.0 * Math.PI;
            final float startX = centerX + (length / 2.0f) * ((float) Math.sin(angle));
            final float startY = centerY - (length / 2.0f) * ((float) Math.cos(angle));
            canvas.drawLines(new float[]{
                    centerX - (length / 2.0f) * ((float) Math.sin(angle)),
                    centerY + (length / 2.0f) * ((float) Math.cos(angle)),
                    startX,
                    startY,

                    startX,
                    startY,
                    startX + (length / 4.0f) * ((float) Math.cos(Math.PI * (11.0 / 8.0) - angle)),
                    startY - (length / 4.0f) * ((float) Math.sin(Math.PI * (11.0 / 8.0) - angle)),

                    startX,
                    startY,
                    startX + (length / 4.0f) * ((float) Math.cos(Math.PI * (13.0 / 8.0) - angle)),
                    startY - (length / 4.0f) * ((float) Math.sin(Math.PI * (13.0 / 8.0) - angle)),
            }, windPaintArrow);
        }
    },
    INFLUENCE("Влияние") {
        @Override
        public void modifyCell(final Canvas canvas, final int tileX, final int tileY, final MapCellTerrainInfo cellInfo) {
            final float x = tileX * MapManager.MAP_TILE_SIZE;
            final float y = tileY * MapManager.MAP_TILE_SIZE;
            canvas.drawRect(x, y, x + MapManager.MAP_TILE_SIZE, y + MapManager.MAP_TILE_SIZE,
                    influencePaint.get(cellInfo.isWilderness ? 0 : cellInfo.neighborPlaceId));
        }
    }
    ;

    private static Paint windPaintRect;
    private static Paint windPaintArrow;
    private static Map<Integer, Paint> influencePaint;

    private final String name;

    private MapModification(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void modifyCell(final Canvas canvas, final int tileX, final int tileY, final MapCellTerrainInfo cellInfo) {
        // do nothing by default
    }

//    private static int getInfluenceColor(final int index, final int size) {
//        return Color.HSVToColor(0xc0, // 75% opacity
//                new float[]{360.0f / size * index, 0.9f, 0.5f});
//    }

    // http://phrogz.net/css/distinct-colors.html
    private static final Integer[] colors = new Integer[]{
            0xc04c0f0f, 0xc0997a1f, 0xc052cc8f, 0xc06673ff, 0xc0331f2c,
            0xc0e55c5c, 0xc0ccbb00, 0xc0b8e6d6, 0xc02929cc, 0xc0cc0055,
            0xc0332a29, 0xc0a6a685, 0xc000332f, 0xc03e3d4d, 0xc0330015,
            0xc0cc3300, 0xc0eaff00, 0xc05ce6e6, 0xc0473d66, 0xc0f291ba,
            0xc0bf8673, 0xc0303314, 0xc02e7373, 0xc0300040, 0xc0995c70,
            0xc0ff9966, 0xc0d6e68a, 0xc0004d66, 0xc0cc33ff, 0xc07f1a33,
            0xc0663d29, 0xc066801a, 0xc07ab8cc, 0xc08d42a6, 0xc0ff002b,
            0xc0a66321, 0xc05e6652, 0xc066b3ff, 0xc0cf8ae6, 0xc0665255,
            0xc0ff9500, 0xc073e600, 0xc0476bb3, 0xc08a708c, 0xc08c000c,
            0xc0331e00, 0xc01c8c1c, 0xc0858da6, 0xc0f2c2f2, 0xc0cca3a7,
            0xc0e5bf8a, 0xc0125924, 0xc01a2040, 0xc0ff33bb, 0xc0594300,
            0xc033ff88, 0xc0171f73, 0xc0731754,
    };

    public static void init(final MapModification mapModification, final MapResponse mapInfo) {
        switch(mapModification) {
            case WIND:
                windPaintRect = new Paint();
                windPaintRect.setColor(Color.WHITE);

                windPaintArrow = new Paint();
                windPaintArrow.setColor(Color.BLACK);
                windPaintArrow.setStrokeWidth(1.0f);

                break;

            case INFLUENCE:
                final int size = mapInfo.places.size();
                if(size >= colors.length) {
                    throw new IllegalStateException("Not enough colors");
                }
                influencePaint = new HashMap<>(size + 1);
                final List<Integer> colorsList = new ArrayList<>();
                Collections.addAll(colorsList, colors);
                Collections.shuffle(colorsList);

                final Paint wildernessPaint = new Paint();
                wildernessPaint.setColor(colorsList.get(0));
                influencePaint.put(0, wildernessPaint);

                final List<PlaceInfo> places = new ArrayList<>(mapInfo.places.values());
                for(int i = 0; i < size; i++) {
                    final Paint paint = new Paint();
                    paint.setColor(colorsList.get(i + 1));
                    influencePaint.put(places.get(i).id, paint);
                }

                break;
        }
    }

}