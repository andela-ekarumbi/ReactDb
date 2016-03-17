/**
 * This interface defines operations to be implemented by a buffer.
 */

package checkpoint.andela.buffer;

import java.util.List;

public interface Buffer<T> {
    /**
     * Adds a single item to the buffer.
     *
     * @param item the item to be added to the buffer.
     */

    boolean addToBuffer(T item);

    /**
     * Adds a list of items to the buffer.
     *
     * @param itemsList the list of items to be added to the buffer.
     */

    boolean addListToBuffer(List<T> itemsList);

    /**
     * Registers a client of the buffer for tracking, and returns a string key
     * used to track the current read position for that particular client.
     *
     * @return A string key used for tracking the client's read position.
     */

    String registerClientForTracking();

    /**
     * Returns a boolean value indicating whether any new data has been added
     * to the buffer since the last read by a particular client.
     *
     * @param trackingKey the tracking key issued to the client after
     *                    {@code registerClientForTracking()} was called.
     * @return true if new data exists, false otherwise.
     */

    boolean isThereNewData(String trackingKey);

    /**
     * Returns the data from the current read position for a particular client
     * to the end of the list.
     *
     * @param trackingKey the tracking key issued to the client after
     *                    {@code registerClientForTracking()} was called.
     * @return a list with the most recent data for the client in question.
     */

    List<T> getLatestData(String trackingKey);
}
