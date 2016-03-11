package checkpoint.andela.buffer;

import java.util.List;

public interface Buffer<T> {
    void addToBuffer(T item);

    void addListToBuffer(List<T> itemsList);

    String registerClientForTracking();

    boolean isThereNewData(String issuedKey);

    List<T> getLatestData(String issuedKey);
}
