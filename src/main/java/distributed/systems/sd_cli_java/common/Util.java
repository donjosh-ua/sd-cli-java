package distributed.systems.sd_cli_java.common;

import java.util.concurrent.atomic.AtomicReference;

public class Util {

    private static final AtomicReference<Util> instance = new AtomicReference<>();

    private Util() {
        if (instance.get() != null) {
            throw new IllegalStateException("Util class already initialized");
        }
    }

    public static Util getInstance() {

        Util current = instance.get();

        if (current != null) {
            return current;
        }

        Util newUtil = new Util();

        if (instance.compareAndSet(null, newUtil)) {
            return newUtil;
        }

        return instance.get();

    }

}