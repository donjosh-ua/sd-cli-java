package distributed.systems.sd_cli_java.common;

public class Util {

    private static volatile Util instance;

    private Util() {
        if (instance != null) {
            throw new IllegalStateException("Util class already initialized");
        }
    }

    public static Util getInstance() {
        if (instance == null) {
            synchronized (Util.class) {
                if (instance == null) {
                    instance = new Util();
                }
            }
        }
        return instance;
    }

}
