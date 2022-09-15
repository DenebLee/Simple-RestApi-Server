package kr.nanoit.object.config;

public final class DataBaseConfigValidation {
    private DataBaseConfigValidation() {
    }

    public static void valid(DataBaseConfig dataBaseConfig) {
        if (dataBaseConfig.getIp() == null) {
            throw new IllegalArgumentException("invalid DataBaseConfig.ip");
        }
    }
}
