package com.sumscope.optimus.commons.zabbix;

import io.github.hengyunabc.zabbix.sender.DataObject;
import io.github.hengyunabc.zabbix.sender.SenderResult;
import io.github.hengyunabc.zabbix.sender.ZabbixSender;

/**
 * Created by shaoxu.wang on 2016/11/4.
 */
public class SSZabbixSender {
    private String serverIp;
    private int port;
    private String hostName;
    private ZabbixSender zabbixSender;

    public SSZabbixSender(String ip, int port, String hostName) {
        this.serverIp = ip;
        this.port = port;
        this.hostName = hostName;
        zabbixSender = new ZabbixSender(serverIp, port);
    }

    public Boolean send(String key, String value) {
        DataObject dataObject = new DataObject();
        dataObject.setHost(hostName);
        dataObject.setKey(key);
        dataObject.setValue(value);
        dataObject.setClock(System.currentTimeMillis()/1000);

        try {
            SenderResult result = zabbixSender.send(dataObject);
            if (result != null && result.success()) {
                return true;
            }
            else {
                return false;
            }
        }
        catch (Exception ex) {

        }

        return false;
    }
}
