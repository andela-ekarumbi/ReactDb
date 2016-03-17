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
    public synchronized boolean addToBuffer(T item) {
        return addToList(item);
    }

    private boolean addToList(T item) {
        list.add(item);
        return true;
    }

    @Override
    public synchronized boolean addListToBuffer(List<T> itemsList) {
        if (itemsList.size() > 0) {
            for (T item : itemsList) {
                addToList(item);
            }
            return true;
        }
        return false;
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
                lastAccessedTracker.get(issuedKey)
                        < list.size() - 1;
    }

    @Override
    public synchronized List<T> getLatestData(String trackingKey) {
        List<T> latestData = new ArrayList<>();
        if (isKeyRegistered(trackingKey)) {
            fillLatestData(trackingKey, latestData);
        }
        return latestData;
    }

    private void fillLatestData(String issuedkey, List<T> latestData) {
        int lastFetchPosition = lastAccessedTracker.get(issuedkey);
        int listSize = list.size();
        for (int i = lastFetchPosition; i < listSize; i++) {
            latestData.add(list.get(i));
        }
        modifyTracker(issuedkey, listSize - 1);
    }

}
