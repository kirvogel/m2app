package com.example.data;

import android.annotation.SuppressLint;

import java.util.HashMap;
import java.util.Map;

public class MobileCountryCodeMobileNetworkCode {
    @SuppressLint("UseSparseArrays")
    private Map<Integer, int[]> map = new HashMap<>();

    MobileCountryCodeMobileNetworkCode() {
        map.put(289, new int[]{67, 68}); //абхазия

        map.put(250, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 15, 16, 17, 19, 20,
        23, 28, 32, 35, 37, 38, 39, 44, 50, 92, 93, 99}); //россия

    }

    public int[] getMNCList(int mcc) {
        try {
            return map.get(mcc);
        } catch (Exception e) {
            return new int[]{};
        }
    }
}

/*
{
    "token": "a65aee3fdcc744",
    "radio": "gsm",
    "mcc": 250,
    "mnc": 1,
    "cells": [{
        "cid": 2
    }],
    "address": 0
}
 */
