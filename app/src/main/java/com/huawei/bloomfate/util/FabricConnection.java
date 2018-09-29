package com.huawei.bloomfate.util;

public class FabricConnection {

    private FabricConnection() {}

    public static FabricConnection getInstance() {
        return Holder.INSTANCE;
    }
    private static class Holder {
        private static final FabricConnection INSTANCE = new FabricConnection();
    }

    public String invoke() {
        return "";
    }

    public String query() {
        return "";
    }
}
