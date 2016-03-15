/**
 * This class is an implementation of the {@code Buffer} interface that has
 * been designed for concurrent access by multiple threads.
 */

package checkpoint.andela.buffer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SynchronizedBuffer<T> implements Buffer<T> {

    private volatile List<T> list;

    private volatile Map<String, Integer> lastAccessedTracker;

    protected SynchronizedBuffer() {
        list = new ArrayList<>();
        lastAccessedTracker = new HashMap<>();
    }

    @Override
    public synchronized void addToBuffer(T item) {
        addToList(item);
    }

    private void addToList(T item) {
        list.add(item);
    }

    @Override
    public synchronized void addListToBuffer(List<T> itemsList) {
        if (itemsList.size() > 0) {
            for (T item : itemsList) {
                addToList(item);
            }
        }
    }

    @Override
    public synchronized String registerClientForTracking() {
        String randomKey = Double.toString(Math.random() * 100000);
        modifyTracker(randomKey, 0);
        return randomKey;
    }

    private void modifyTracker(String key, int value) {
        synchronized (this) {
            lastAccessedTracker.put(key, value);
        }
    }

    @Override
    public synchronized boolean isThereNewData(String trackingKey) {
        return isKeyRegistered(trackingKey) && isNewDataPresent(trackingKey);
    }

    private boolean isKeyRegistered(String issuedKey) {
        return lastAccessedTracker.containsKey(issuedKey);
    }

    private boolean isNewDataPresent(String issuedKey) {
        return list.size() > 0 &&
                lastAccessedTracker.get(issuedKey) < lastAccessedTracker.size();
    }

    @Override
    public synchronized List<T> getLatestData(String trackingKey) {
        List<T> latestData = new ArrayList<>();
        if (isKeyRegistered(trackingKey)) {
            getLatestBatch(trackingKey, latestData);
        }
        return latestData;
    }

    private void getLatestBatch(String issuedkey, List<T> latestBatch) {
        int lastFetchPosition = lastAccessedTracker.get(issuedkey);
        int listSize = list.size();
        for (int i = lastFetchPosition; i < listSize; i++) {
            latestBatch.add(list.get(i));
        }
        modifyTracker(issuedkey, listSize);
    }

}
