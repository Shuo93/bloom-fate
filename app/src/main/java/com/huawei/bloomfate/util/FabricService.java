package com.huawei.bloomfate.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.base.Charsets;
import com.google.common.io.ByteSource;
import com.huawei.fabric.sdk.FabricSdk;
import com.huawei.fabric.sdk.bean.OrdererInfo;
import com.huawei.fabric.sdk.bean.PeerInfo;
import com.huawei.fabric.sdk.request.InvokeChaincodeRequest;
import com.huawei.fabric.sdk.request.InvokeChaincodeRequestBuilder;
import com.huawei.fabric.sdk.request.QueryChaincodeRequest;
import com.huawei.fabric.sdk.request.QueryChaincodeRequestBuilder;
import com.huawei.fabric.sdk.response.TransactionResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FabricService {

    private static final String TAG = "FabricService";

    private FabricService() {
        mapper = new ObjectMapper();
        sdk = new FabricSdk();
    }

    public static FabricService getConnection() {
        return Holder.INSTANCE;
    }
    private static class Holder {
        private static final FabricService INSTANCE = new FabricService();
    }

    private static class FabricConfig {
        private String channelId;
        private String chaincodeId;
        private String chaincodeVersion;
        private String msp;
        private String peerHost;
        private String peerOverrideAuthority;
        private int peerPort;
        private int eventPort;
        private String ordererHost;
        private String ordererOverrideAuthority;
        private int ordererPort;

        public String getChannelId() {
            return channelId;
        }

        public void setChannelId(String channelId) {
            this.channelId = channelId;
        }

        public String getChaincodeId() {
            return chaincodeId;
        }

        public void setChaincodeId(String chaincodeId) {
            this.chaincodeId = chaincodeId;
        }

        public String getChaincodeVersion() {
            return chaincodeVersion;
        }

        public void setChaincodeVersion(String chaincodeVersion) {
            this.chaincodeVersion = chaincodeVersion;
        }

        public String getMsp() {
            return msp;
        }

        public void setMsp(String msp) {
            this.msp = msp;
        }

        public String getPeerHost() {
            return peerHost;
        }

        public void setPeerHost(String peerHost) {
            this.peerHost = peerHost;
        }

        public String getPeerOverrideAuthority() {
            return peerOverrideAuthority;
        }

        public void setPeerOverrideAuthority(String peerOverrideAuthority) {
            this.peerOverrideAuthority = peerOverrideAuthority;
        }

        public int getPeerPort() {
            return peerPort;
        }

        public void setPeerPort(int peerPort) {
            this.peerPort = peerPort;
        }

        public int getEventPort() {
            return eventPort;
        }

        public void setEventPort(int eventPort) {
            this.eventPort = eventPort;
        }

        public String getOrdererHost() {
            return ordererHost;
        }

        public void setOrdererHost(String ordererHost) {
            this.ordererHost = ordererHost;
        }

        public String getOrdererOverrideAuthority() {
            return ordererOverrideAuthority;
        }

        public void setOrdererOverrideAuthority(String ordererOverrideAuthority) {
            this.ordererOverrideAuthority = ordererOverrideAuthority;
        }

        public int getOrdererPort() {
            return ordererPort;
        }

        public void setOrdererPort(int ordererPort) {
            this.ordererPort = ordererPort;
        }
    }

    private FabricConfig config;

    private String key;
    private String cert;
    private String peerCert;
    private String ordererCert;

    private ObjectMapper mapper;
    private FabricSdk sdk;

    public boolean loadConfig(Context context) {
        try {
            InputStream is = context.getAssets().open("config.yaml");
            ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
            config = yamlMapper.readValue(is, FabricConfig.class);
            key = getStringFromFile(context, "key.pem");
            cert = getStringFromFile(context, "cert.pem");
            peerCert = getStringFromFile(context, "peer.crt");
            ordererCert = getStringFromFile(context, "orderer.crt");
            Log.i(TAG, "Channel ID: " + config.channelId);
            Log.i(TAG, "Chaincode ID: " + config.chaincodeId);
            Log.i(TAG, "Chaincode Version: " + config.chaincodeVersion);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }


    }

    public String getConfig() {
        return "Channel ID: " + config.channelId + "\nChaincode ID: " + config.chaincodeId + "\nChaincode Version: " + config.chaincodeVersion;
    }

    private String getStringFromFile(final Context context, final String filename) throws IOException {
        ByteSource byteSource = new ByteSource() {
            @Override
            public InputStream openStream() throws IOException {
                return context.getAssets().open(filename);
            }
        };
        return byteSource.asCharSource(Charsets.UTF_8).read();
    }


    public boolean invoke(String func, String args) {
        try {
            InvokeChaincodeRequest request = new InvokeChaincodeRequestBuilder()
                    .setChannelId(config.channelId)
                    .setChaincodeInfo(config.chaincodeId, config.chaincodeVersion)
                    .setFnc(func).setArgs(Collections.singletonList(args))
                    .setUser(key, cert, config.msp)
                    .setPeersInfo(getPeerInfos())
                    .setOrderersInfo(getordererInfos())
                    .setTls(true)
                    .build();
            TransactionResponse response = sdk.invoke(request);
            int statusCode = response.getBroadcastResponse().getStatusValue();
            return statusCode == 200;
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return false;
    }

//    public <T> List<T> query(String func, String args, Class<T> valueType) {
//        return getResults(query(func, args));
//    }

//    public <T> List<T> query(String func, Class<T> valueType) {
//        return getResults(query(func));
//    }

    public <T> List<T> getResults(String response, Class<T> valueType) {
        try {
            return mapper.readValue(response, new TypeReference<List<T>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public String query(String func, String args) {
        try {
            QueryChaincodeRequest request = new QueryChaincodeRequestBuilder()
                    .setChannelId(config.channelId)
                    .setChaincodeInfo(config.chaincodeId, config.chaincodeVersion)
                    .setFnc(func).setArgs(Collections.singletonList(args))
                    .setUser(key, cert, config.msp)
                    .setPeersInfo(getPeerInfos())
                    .setTls(true)
                    .build();
            return new String(sdk.query(request));
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String query(String func) {
        return query(func, "");
    }

    private List<PeerInfo> getPeerInfos() {
        PeerInfo peer = PeerInfo.newBuilder()
                .setHost(config.peerHost)
                .setPort(config.peerPort)
                .setEventPort(config.eventPort)
                .setOverrideAuthority(config.peerOverrideAuthority)
                .setCert(peerCert)
                .build();
        List<PeerInfo> peerList = new ArrayList<>();
        peerList.add(peer);
        return peerList;
    }

    private List<OrdererInfo> getordererInfos() {
        OrdererInfo orderer = OrdererInfo.newBuilder()
                .setHost(config.ordererHost)
                .setPort(config.ordererPort)
                .setOverrideAuthority(config.ordererOverrideAuthority)
                .setCert(ordererCert)
                .build();
        List<OrdererInfo> ordererList = new ArrayList<>();
        ordererList.add(orderer);
        return ordererList;
    }
}
