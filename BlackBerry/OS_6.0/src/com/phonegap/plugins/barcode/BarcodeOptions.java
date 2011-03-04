/*
 * PhoneGap is available under *either* the terms of the modified BSD license *or* the
 * MIT License (2008). See http://opensource.org/licenses/alphabetical for full text.
 *  
 * Copyright (c) 2011, IBM Corporation
 */
package com.phonegap.plugins.barcode;

import java.util.Vector;

import com.google.zxing.BarcodeFormat;
import com.phonegap.json4j.JSONArray;
import com.phonegap.json4j.JSONException;
import com.phonegap.util.Logger;

public class BarcodeOptions {

    private Vector formats;
    private boolean highAccuracy;
    
    public BarcodeOptions() {
        this.formats = new Vector();
    }
    
    public Vector getFormats() {
        return formats;
    }

    public boolean getHighAccuracy() {
        return highAccuracy;
    }

    private void addFormat(BarcodeFormat format) {
        this.formats.addElement(format);
    }

    private void setHighAccuracy(boolean highAccuracy) {
        this.highAccuracy = highAccuracy;
    }
    
    public static BarcodeOptions fromJSONArray(JSONArray options) throws JSONException {
        BarcodeOptions bco = new BarcodeOptions();
        if (options != null) {
            // retrieve specified barcode formats
            if (options.length() > 0 && !options.isNull(0)) {
                Logger.log(Barcode.class.getName() + " options=" + options.toString());                    
                JSONArray fmts = options.getJSONArray(0);    
                for (int i=0; i<fmts.length(); i++) {
                    try {
                        bco.addFormat(BarcodeFormat.valueOf(fmts.getString(i)));
                    } catch (IllegalArgumentException e) {
                        // ignore invalid formats
                    }
                }
            }

            // use high accuracy scan?
            if(options.length() > 1 && !options.isNull(1)) {
                bco.setHighAccuracy(options.getBoolean(1));
            }
        }
        return bco;
    }
}
