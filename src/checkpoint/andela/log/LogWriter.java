/**
 * This class wraps a LogHelper object to implement logging as an asynchronous
 * operation.
 */

package checkpoint.andela.log;

public class LogWriter implements Runnable {

    private LogHelper logHelper;

    /**
     * Creates a {@code LogWriter}
     * @param helper the {@code LogHelper} object to be used for logging.
     * */

    public LogWriter(LogHelper helper) {
        this.logHelper = helper;
    }

    @Override
    public void run() {
        logHelper.startLogging();
    }
}
