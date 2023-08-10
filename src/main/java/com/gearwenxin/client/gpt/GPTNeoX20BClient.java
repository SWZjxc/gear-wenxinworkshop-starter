package com.gearwenxin.client.gpt;

import com.gearwenxin.client.DefaultClient;
import com.gearwenxin.entity.Message;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ge Mingjia
 * @date 2023/8/5
 */
public abstract class GPTNeoX20BClient extends DefaultClient {

    protected GPTNeoX20BClient() {
    }

    private String accessToken = null;
    private static final String TAG = "GPT-NeoX-20B-Client_";
    private static Map<String, Queue<Message>> GPT_NEOX_20B_MESSAGES_HISTORY_MAP = new ConcurrentHashMap<>();

    protected abstract String getAccessToken();

    // 获取不固定的模型URL
    protected abstract String getCustomURL();

    @Override
    public String getCustomAccessToken() {
        return accessToken != null ? accessToken : getAccessToken();
    }

    @Override
    public Map<String, Queue<Message>> getMessageHistoryMap() {
        return GPT_NEOX_20B_MESSAGES_HISTORY_MAP;
    }

    @Override
    public void initMessageHistoryMap(Map<String, Queue<Message>> map) {
        GPT_NEOX_20B_MESSAGES_HISTORY_MAP = map;
    }

    @Override
    public String getURL() {
        return getCustomURL();
    }

    @Override
    public void setCustomAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String getTag() {
        return TAG;
    }
}
