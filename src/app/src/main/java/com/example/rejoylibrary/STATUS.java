package com.example.rejoylibrary;

public class STATUS {
    public static int CANCELLED     = 0;
    public static int PROCESSING    = 1;
    public static int SUCCESS       = 2;

    public static String getString(int x) {
        switch (x) {
            case 0: return "Cancelled";
            case 1: return "Processing";
            case 2: return "Success";
            default: return "?";
        }
    }

    public static int getImage(int x) {
        switch (x) {
            case 0: return R.drawable.ic_baseline_status_cancelled_32;
            case 1: return R.drawable.ic_baseline_status_processing_32;
            case 2: return R.drawable.ic_baseline_status_success_32;
            default: return R.drawable.ic_baseline_status_processing_32;
        }
    }
}