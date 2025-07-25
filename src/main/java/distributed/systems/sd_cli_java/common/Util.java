package distributed.systems.sd_cli_java.common;

import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicReference;

public class Util {

    private static final AtomicReference<Util> instance = new AtomicReference<>();
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int CODE_LENGTH = 10;
    private static final SecureRandom RANDOM = new SecureRandom();

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

    public static String generatePlanCode() {

        StringBuilder code = new StringBuilder(CODE_LENGTH);

        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(index));
        }

        return code.toString();
    }

}