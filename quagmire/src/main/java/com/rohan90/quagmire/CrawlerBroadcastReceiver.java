package com.rohan90.quagmire;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.rohan90.quagmire.utils.Logger;


/**
 * Created by rohan on 10/11/17.
 */
public class CrawlerBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.logInfo("received, crawler dump");
        CrawlerDump dump = (CrawlerDump) intent.getParcelableExtra(CrawlerConstants.BUNDLE.DUMP);
        Logger.logInfo("dump="+dump.toString());
    }
}

