package com.phonegap.plugins.barcode;

import com.phonegap.api.Plugin;
import com.phonegap.api.PluginResult;
import com.phonegap.json4j.JSONArray;
import com.phonegap.json4j.JSONException;
import com.phonegap.util.Logger;

public class Barcode extends Plugin {

    /**
     * Possible plugin actions
     */
    protected static final int ACTION_SCAN = 0;
    
    /**
     * Executes the requested action and returns a PluginResult.
     * 
     * @param action        The action to execute.
     * @param callbackId    The callback ID to be invoked upon action completion.
     * @param args          JSONArry of arguments for the action.
     * @return              A PluginResult object with a status and message.
     */
    public PluginResult execute(String action, JSONArray args, String callbackId) {
        PluginResult result = null;

        // perform specified action
        int a = getAction(action);
        if (a == ACTION_SCAN) {
            BarcodeOptions options;
            try {
                options = BarcodeOptions.fromJSONArray(args);
            } catch (JSONException e) {
                Logger.log(this.getClass().getName() + ": " + e);
                return new PluginResult(PluginResult.Status.JSONEXCEPTION, 
                        "Invalid or missing parameter");
            }

            // start the scan
            BarcodeScanner scanner = new BarcodeScanner(options, callbackId);
            scanner.start();

            // Return NO_RESULT status so plugin manager does not invoke a callback,
            // but keep callback so we can invoke it later.
            result = new PluginResult(PluginResult.Status.NO_RESULT);
            result.setKeepCallback(true);
        } 
        else {
            // invalid action
            result = new PluginResult(PluginResult.Status.INVALIDACTION, 
                    "BarcodeScanner: invalid action " + action);
        }
        
        return result;
    }

    /**
     * Returns action to perform.
     * @param action action to perform
     * @return action to perform
     */
    protected static int getAction(String action) {
        if ("scan".equals(action)) return ACTION_SCAN;
        return -1;
    }
    
    /**
     * Called when Plugin is paused. 
     */
    public void onPause() {
        
    }

    /**
     * Called when Plugin is resumed. 
     */
    public void onResume() {
        
    }
    
    /**
     * Called when Plugin is destroyed. 
     */
    public void onDestroy() {
        
    }    
}
