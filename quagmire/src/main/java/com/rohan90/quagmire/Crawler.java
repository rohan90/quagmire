package com.rohan90.quagmire;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.content.LocalBroadcastManager;

import com.rohan90.quagmire.utils.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by rohan on 2/11/17.
 */

public class Crawler {
    private static Crawler instance = null;
    private Context context;
    private List<ContactsGist> contactsList;
    private DataCrawledCallback callback;

    private Crawler(Context context) {
        this.context = context;
    }

    public static Crawler getInstance(Context context) {
        if (instance == null) {
            instance = new Crawler(context);
        }
        return instance;
    }

    public void init() throws RequestPermissionException {
        readContacts();
    }

    public List<ContactsGist> getContactsList() {
        return contactsList;
    }

    private void readContacts() throws RequestPermissionException {
        try {
            Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            contactsList = new ArrayList<ContactsGist>();
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                Logger.logInfo("name = " + name + ", phoneNumber = " + phoneNumber);
                contactsList.add(new ContactsGist(name, phoneNumber));
            }

            if (contactsList != null && !contactsList.isEmpty()) {
                sendThroughAppropriateChannels(contactsList);
            }
            cursor.close();
        } catch (SecurityException e) {
            //dont have permission to read contacts, handle flow
            Logger.logError(e.getMessage());
            e.printStackTrace();
            String permsionString = Manifest.permission.READ_CONTACTS;

            RequestPermissionException exception = new RequestPermissionException(new Exception("ask or implement permission for" + permsionString));
            exception.setPermissionString(permsionString);
            throw exception;
        } catch (NullPointerException e){
            // TODO: 11/12/17 Have to find out why curson is null sometimes, even though cursor.close is called. 
        }
    }

    private void sendThroughAppropriateChannels(List<ContactsGist> contactsList) {
        if (callback != null) {
            callback.onDataCrawled(new CrawlerDump(Constants.TYPE.Contacts, contactsList));
        } else {
            sendBroadCastWithContactsDump(contactsList);
        }
    }

    /**
     * notifies that contacts have been crawled.
     * todo: this is for the contract when [I am done, now you may ask crawler to fetch contacts from db or in-memory]
     * todo: next version to have builder and db pattern
     * @param contactsList
     */
    private void sendBroadCastWithContactsDump(List<ContactsGist> contactsList) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.setAction(CrawlerConstants.ACTION.DUMP);
        intent.putExtra(CrawlerConstants.BUNDLE.DUMP, new CrawlerDump(Constants.TYPE.Contacts, null, ContactsGist.class.getCanonicalName()));
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public void setLogging(boolean enabled) {
        Logger.setLogging(enabled);
    }

    public void setCallback(DataCrawledCallback callback) {
        this.callback = callback;
    }
}
