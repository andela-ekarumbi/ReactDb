package checkpoint.andela.buffer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SynchronizedBuffer<T> implements Buffer<T> {

    private List<T> list;

    private Map<String, Integer> lastAccessedTracker;

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
    public synchronized boolean isThereNewData(String issuedKey) {
        return checkIfKeyExists(issuedKey) && checkForNewData(issuedKey);
    }

    private boolean checkIfKeyExists(String issuedKey) {
        return lastAccessedTracker.containsKey(issuedKey);
    }

    private boolean checkForNewData(String issuedKey) {
        return list.size() > 0 &&
                lastAccessedTracker.get(issuedKey) < lastAccessedTracker.size();
    }

    @Override
    public synchronized List<T> getLatestData(String issuedKey) {
        List<T> latestData = new ArrayList<>();
        if (checkIfKeyExists(issuedKey)) {
            getLatestBatch(issuedKey, latestData);
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
