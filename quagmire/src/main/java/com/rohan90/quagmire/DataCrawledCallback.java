package com.rohan90.quagmire;

/**
 * Created by rohan on 16/11/17.
 */

public interface DataCrawledCallback<T> {
    void onDataCrawled(CrawlerDump<T> dump);
}
