package com.quagmire.rohan90.sample;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.rohan90.quagmire.Crawler;
import com.rohan90.quagmire.CrawlerBroadcastReceiver;
import com.rohan90.quagmire.CrawlerConstants;
import com.rohan90.quagmire.CrawlerDump;
import com.rohan90.quagmire.RequestPermissionException;
import com.rohan90.quagmire.utils.ContactsCrawledCallback;
import com.rohan90.quagmire.utils.Logger;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PERMISSION_CONTACTS = 1;
    private Crawler crawler;
    private CrawlerBroadcastReceiver crawlerBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        initQuagmire();
    }

    private void initQuagmire() {
        crawler = Crawler.getInstance(this);
        crawler.setLogging(true);
        try {
            registerCrawlerReceiver();
            //if callback is null, an event is broadcasted on completion
            crawler.setCallback(new ContactsCrawledCallback(){
                @Override
                public void onDataCrawled(CrawlerDump dump) {
                    Logger.logInfo("received dump=["+dump.getType()+", "+dump.getData());
                    makeApiCallWithCrawlerDump(dump);
                }
            });
            crawler.init();
        } catch (RequestPermissionException e) {
            e.printStackTrace();
            ActivityCompat.requestPermissions(this, new String[]{e.getPermissionString()}, REQUEST_CODE_PERMISSION_CONTACTS);
        }
    }

    private void registerCrawlerReceiver() {
        IntentFilter filter = new IntentFilter(CrawlerConstants.ACTION.DUMP);

        crawlerBroadcastReceiver = new CrawlerBroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                super.onReceive(context, intent);
                CrawlerDump dump = (CrawlerDump) intent.getParcelableExtra(CrawlerConstants.BUNDLE.DUMP);
                makeApiCallWithCrawlerDump(dump);
            }

        };
        LocalBroadcastManager.getInstance(this).registerReceiver(crawlerBroadcastReceiver, filter);
    }

    private void makeApiCallWithCrawlerDump(CrawlerDump dump) {
        Toast.makeText(this, "dump received " + dump.getType(), Toast.LENGTH_SHORT).show();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(crawlerBroadcastReceiver);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // TODO: 13/11/17 this could be made generic in a utils :p, but will address as this grows
        if (requestCode == REQUEST_CODE_PERMISSION_CONTACTS) {
            //this is for all denied situations
            if (ContextCompat.checkSelfPermission(this,permissions[0]) == PackageManager.PERMISSION_DENIED) {
                //denied
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,permissions[0])) {
                    Log.i(Constants.APP_TAG, "not given read contacts permission");
                } else {
                    //never ask again
                    Log.i(Constants.APP_TAG, "never ask again read contacts permission");
                }
            } else {
                //user granted permission for contacts;
                Log.i(Constants.APP_TAG, "user allowed read contacts permission");
                initQuagmire();// or call specific method again ie for now crawl all over again
            }
        }

    }
}
