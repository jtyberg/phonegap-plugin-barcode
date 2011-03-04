package com.phonegap.plugins.barcode;

import net.rim.device.api.barcodelib.BarcodeScanner;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.container.MainScreen;

import com.phonegap.util.Logger;

/**
 * Creates a screen with a view finder for scanning barcodes.
 */
public final class BarcodeScannerScreen extends MainScreen {

    /**
     * Constructs a screen with a view finder for scanning barcodes.
     * @param scanner   Barcode scanner object
     */
    public BarcodeScannerScreen(BarcodeScanner scanner) {
        super();
        
        try {
            // use full screen
            scanner.getVideoControl().setDisplayFullScreen(true);

            // add barcode scanner viewfinder to screen
            Field viewFinder = scanner.getViewfinder();
            this.add(viewFinder);
        } catch (Exception e) {
            Logger.log(this.getClass().getName() + ": Unable to initialize barcode scanner screen: " + e);
        }        
    }
}
