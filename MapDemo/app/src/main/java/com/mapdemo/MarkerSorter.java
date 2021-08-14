package com.mapdemo;

import com.google.android.gms.maps.model.Marker;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MarkerSorter {

    public static void sort(List<Marker> latLngs) {
        double centerX = 0, centerY = 0;

        for (Marker point : latLngs) {
            centerX += point.getPosition().latitude;
            centerY += point.getPosition().longitude;
        }
        centerX /= latLngs.size();
        centerY /= latLngs.size();

        Collections.sort(latLngs, new Sorter(centerX, centerY));
    }

    private static class Sorter implements Comparator<Marker> {

        private double centerX, centerY;

        public Sorter(double centerX, double centerY) {
            this.centerX = centerX;
            this.centerY = centerY;
        }

        @Override
        public int compare(Marker a, Marker b) {
            boolean result = sorter(a.getPosition().latitude, a.getPosition().longitude,
                    b.getPosition().latitude, b.getPosition().longitude, centerX, centerY);
            return result ? 1 : -1;
        }

        private boolean sorter(double ax, double ay, double bx, double by, double centerX, double centerY) {
            if (ax - centerX >= 0 && bx - centerX < 0)
                return true;
            if (ax - centerX < 0 && bx - centerX >= 0)
                return false;
            if (ax - centerX == 0 && bx - centerX == 0) {
                if (ay - centerY >= 0 || by - centerY >= 0)
                    return ay > by;
                return by > ay;
            }

            double det = ((ax - centerX) * (by - centerY))
                    - ((bx - centerX) * (ay - centerY));

            if (det < 0)
                return true;
            if (det > 0)
                return false;

            double d1 = ((ax - centerX) * (ax - centerX)) + ((ay - centerY) * (ay - centerY));
            double d2 = ((bx - centerX) * (bx - centerX)) + ((by - centerY) * (by - centerY));
            return d1 > d2;
        }

    }

}
