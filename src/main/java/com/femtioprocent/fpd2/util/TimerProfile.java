package com.femtioprocent.fpd2.util;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * A logaritmic profile counter.
 */
public class TimerProfile {
    public String id;
    private static AtomicInteger seq = new AtomicInteger(0);
    double maxTimeSlot = 1000.0;
    int arr[] = new int[3];
    int totalCnt = 0;
    int startSlot = 0;

    public TimerProfile() {
        id = "TP-" + seq.addAndGet(1);
    }

    /**
     * With specified limit and size
     * 
     * @param id
     * @param from
     * @param to
     * @param size
     */
    public TimerProfile(String id, double to, int size) {
        this.id = id;
        maxTimeSlot = to;
        arr = new int[size + 2];
        sizeCheck();
    }

    public TimerProfile(String id, double from, double to, int size) {
        this.id = id;
        maxTimeSlot = to;
        arr = new int[size + 2];
        startSlot = getSlot(from) - 1;
        if (startSlot < 0)
            startSlot = 0;
        sizeCheck();
    }

    /**
     * increase size of the profile. It is not possible to decrease it.
     * 
     * @param nSize
     */
    public void expandTo(int nSize) {
        if (nSize > arr.length - 2) {
            arr = expandArr(arr, nSize + 2);
        }
    }

    private int getCount() {
        return arr.length - 2;
    }

    /**
     * Which profile slot for the value ms
     * 
     * @param ms
     * @return
     */
    private int getSlot(double ms) {
        try {
            if (ms >= maxTimeSlot)
                return arr.length - 1;
            if (ms <= 1)
                return 0;
            // double log10 = Math.log10(maxTimeSlot - minTimeSlot);
            double log10 = Math.log10(maxTimeSlot);
            double f = log10 / getCount();
            double d = Math.log10(ms) / f;
            int sl = (int) d + 1;
            int sla = sl - startSlot;
            if (sla < 0)
                return 0;
            //System.out.println("getSlot " + ms + ' ' + sla + ' ' + d);
            return sla;
        } catch (Exception _) {
            return -1;
        }
    }

    /**
     * The lower label (get upper label with ix+1)
     * 
     * @param ix
     * @return
     */
    private String getLabel(int ix) {
        if (ix >= arr.length - 1)
            return ">=" + maxTimeSlot;
        ix += startSlot;
        // double log10 = Math.log10(maxTimeSlot - minTimeSlot);
        double log10 = Math.log10(maxTimeSlot);
        double f = log10 / getCount();

        return String.format("%.1f", Math.pow(10, f * (ix - 1)));
    }

    /**
     * is the size enough
     */
    private void sizeCheck() {
        int slot = getSlot(Double.MAX_VALUE);
        if (arr.length <= slot) {
            arr = expandArr(arr, slot);
        }
    }

    /**
     * expand the array
     * 
     * @param arr2
     * @param size
     * @return
     */
    private int[] expandArr(int[] arr2, int size) {
        int[] arr3 = new int[size];
        int ov = arr2[arr2.length - 1];
        arr2[arr2.length - 1] = 0;
        System.arraycopy(arr2, 0, arr3, 0, arr2.length);
        arr3[arr3.length - 1] = ov;
        return arr3;
    }

    /**
     * register a time value in ms
     * 
     * @param ms
     */
    public void add(double ms) {
        int slot = getSlot(ms);
        if (slot >= 0)
            arr[slot]++;
        totalCnt++;
    }

    /**
     * validate the numbers
     * 
     * @return
     */
    private boolean validate() {
        int sum = 0;
        for (int a : arr)
            sum += a;
        return sum == totalCnt;
    }

    /**
     * write out the label and value {@inheritDoc}
     */
    public String toString() {
        try {
            DelimitedStringBuilder dsb = new DelimitedStringBuilder(", ");
            dsb.append("0[" + arr[0] + ']');
            for (int i = 1; i < arr.length - 1 - startSlot; i++)
                dsb.append("" + getLabel(i) + '[' + arr[i] + ']');
            dsb.append("" + getLabel(arr.length - 1) + '[' + arr[arr.length - 1] + "]");
            return id + ": " + totalCnt + ' ' + dsb.toString() + ' ' + validate();
        } catch (Exception _) {
            return "?";
        }
    }

    /**
     * write only the values
     * 
     * @return
     */
    public String toStringNoLabel() {
        try {
            DelimitedStringBuilder dsb = new DelimitedStringBuilder(", ");
            dsb.append("" + arr[0]);
            for (int i = 1; i < arr.length - 1; i++)
                dsb.append("" + arr[i]);
            dsb.append("" + arr[arr.length - 1]);
            return id + ": " + dsb.toString();
        } catch (Exception _) {
            return "?";
        }
    }
}
