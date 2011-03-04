package com.phonegap.plugins.barcode;

import java.util.Hashtable;

import javax.microedition.media.MediaException;

import net.rim.device.api.barcodelib.BarcodeDecoder;
import net.rim.device.api.barcodelib.BarcodeDecoderListener;
import net.rim.device.api.notification.NotificationsManager;
import net.rim.device.api.system.KeyListener;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.UiApplication;

import com.google.zxing.DecodeHintType;
import com.phonegap.PhoneGapExtension;
import com.phonegap.api.PluginResult;
import com.phonegap.util.Logger;

public class BarcodeScanner implements BarcodeDecoderListener, KeyListener {

    /**
     * Captures the barcode image.
     */
    private net.rim.device.api.barcodelib.BarcodeScanner scanner = null;
    
    /**
     * Displays a view finder for scanning barcodes.
     */
    private BarcodeScannerScreen barcodeScreen = null;
    
    private String callbackId;
    
    /**
     * Creates and manages a barcode scanner and view finder for capturing barcodes.
     * @param options   barcode decorder options 
     * @see BarcodeDecoder
     */
    public BarcodeScanner(BarcodeOptions options, String callbackId) {

        // configure hints for interpreting barcode image
        Hashtable hints = new Hashtable();
        hints.put(DecodeHintType.POSSIBLE_FORMATS, options.getFormats());
        if (options.getHighAccuracy() == true) {
            hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        }
        
        // create decoder 
        BarcodeDecoder decoder = new BarcodeDecoder(hints);
        
        // initialize scanner
        try {
            scanner = new net.rim.device.api.barcodelib.BarcodeScanner(decoder, this);
            barcodeScreen = new BarcodeScannerScreen(scanner);
            barcodeScreen.addKeyListener(this);
        } catch (Exception e) {
            Logger.log(this.getClass().getName() + ": Unable to initialize barcode scanner: " + e);
        }
        
        this.callbackId = callbackId;
    }
    
    /**
     * Starts barcode scanner and displays the barcode view finder screen.
     */
    public void start() {
        final UiApplication app = UiApplication.getUiApplication();
        app.invokeLater(new Runnable() {
            public void run() {
                try {
                    scanner.startScan();
                    Logger.log(BarcodeScanner.class.getName() + ": start scan");
                } catch (MediaException e) {
                    Logger.log(BarcodeScanner.class.getName() + ": Unable to start barcode scanner: " + e);
                }
                app.pushScreen(barcodeScreen);
            }
        });        
    }
    
    /**
     * Stops barcode scanner and removes the barcode view finder screen.
     */
    public void stop() {
        final UiApplication app = UiApplication.getUiApplication();
        app.invokeLater(new Runnable() {
            public void run() {
                try {
                    scanner.stopScan();
                    Logger.log(BarcodeScanner.class.getName() + ": stop scan");
                } catch (Exception e) {
                    Logger.log(BarcodeScanner.class.getName() + ": Error stopping barcode scan: " + e);
                }
                app.popScreen(barcodeScreen);
            }
        });                
    }
    
    /**
     * Closes the barcode scanner and releases it's resources.
     */
    public void close() {
        // release scanner resources
        scanner.getPlayer().close();

        // close screen
        barcodeScreen.removeKeyListener(this);
        final UiApplication app = UiApplication.getUiApplication();
        UiApplication.getUiApplication().invokeLater(new Runnable() {
            public void run() {
                app.popScreen(barcodeScreen);
            }
        });        
    }
    
    /**
     * Invoked when barcode scanner decodes a barcode.
     * @param rawText   decoded text
     */
    public void barcodeDecoded(String rawText) {
        Logger.log(this.getClass().getName() + ": barcode raw text=" + rawText);
        
        // trigger a notification event (plays notification profile)
        UiApplication.getUiApplication().invokeLater(new Runnable() {
            public void run() {
                NotificationsManager.triggerImmediateEvent(PhoneGapExtension.getAppID(), 0, null, null);
            }
        });        
        
        // close the scanner
        close();
        
        // send result
        PluginResult result = new PluginResult(PluginResult.Status.OK, rawText);
        PhoneGapExtension.invokeSuccessCallback(this.callbackId, result);
    }    
    
    /**
     * Invoked when a key has been pressed.
     */
    public boolean keyDown(int keycode, int time) {
        // convert the keycode into an actual key event
        int key = Keypad.key(keycode);

        // if escape key, stop the scan and pop this screen off the stack
        if (key == Keypad.KEY_ESCAPE) {
            stop();
            return true;
        }

        return false;
    }

    public boolean keyChar(char key, int status, int time) {
        return false;
    }

    public boolean keyRepeat(int keycode, int time) {
        return false;
    }

    public boolean keyStatus(int keycode, int time) {
        return false;
    }

    public boolean keyUp(int keycode, int time) {
        return false;
    }
}
