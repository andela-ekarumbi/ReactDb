package checkpoint.andela.config;

public class Constants {

    public static final String[] ALLOWED_ATTRIBUTES = {
            "UNIQUE-ID",
            "TYPES",
            "ATOM-MAPPINGS",
            "CREDITS",
            "EC-NUMBER",
            "ENZYMATIC-REACTION",
            "LEFT",
            "ORPHAN",
            "PHYSIOLOGICALLY-RELEVANT",
            "REACTION-DIRECTION",
            "RIGHT"
    };

    public static final String MYSQL_URL
            = "jdbc:mysql://localhost/reactiondb";

    public static final String MYSQL_USERNAME = "eston";

    public static final String MYSQL_PASSWORD = "emkar2010";

    public static final String MYSQL_DRIVER_NAME = "com.mysql.jdbc.Driver";

    public static final String MYSQL_TABLE_NAME = "reactions";
}
