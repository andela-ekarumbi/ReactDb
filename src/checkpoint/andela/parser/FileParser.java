/**
 * This class wraps a {@code FileParseHelper} object to implement file parsing
 * as an asynchronous operation.
 * */

package checkpoint.andela.parser;

public class FileParser implements Runnable {

    private FileParseHelper fileParseHelper;

    /**
     * Creates a {@code FileParser}.
     * @param helper the {@code FileParseHelper} object to be used for parsing.
     * */

    public FileParser(FileParseHelper helper) {
        this.fileParseHelper = helper;
    }

    @Override
    public void run() {
        fileParseHelper.parseFile();
    }
}
