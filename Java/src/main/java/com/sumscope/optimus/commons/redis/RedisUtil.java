package com.sumscope.optimus.commons.redis;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by yinan.zhang on 2016/11/10.
 */
public class RedisUtil {

    private JedisCluster jc;

    /**
     *根据host 端口号，key获取redis返回的值
     * @param key
     * @param host
     * @param port
     * @return
     */
    public String redis(String key,String host,int port){
        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
        jedisClusterNodes.add(new HostAndPort(host, port));
        this.jc = new JedisCluster(jedisClusterNodes);
        return this.jc.get(key);
    }
}
