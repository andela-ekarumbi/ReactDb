package checkpoint.andela.buffer;

import java.util.List;

public interface Buffer<T extends Object> {
    void addToBuffer(T item);

    void addListToBuffer(List<T> itemsList);

    String registerClientForTracking();

    boolean checkIfNewData(String issuedKey);

    List<T> getLatestData(String issuedKey);
}
