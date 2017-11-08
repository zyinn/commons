package com.sumscope.optimus.commons.redis;

import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.client.codec.StringCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

//import com.sumscope.cdh.realtime.gateway.ISubListener;

/**
 * @author Binson Qian (binson.qian@sumscope.com)
 */
public class RedisClient implements AutoCloseable {

    private static final Logger logger = LoggerFactory.getLogger(RedisClient.class);

    private String host;
    private int port;
    private boolean isCluster;
    private RedissonClient client;

    public RedisClient(String host, int port, boolean isCluster) {
        this.host = host;
        this.port = port;
        this.isCluster = isCluster;
    }

    public void open() {
        if (client != null) {
            return;
        }
        String hostAndPort = String.format("%s:%d", host, port);
        org.redisson.config.Config redisConfig = new org.redisson.config.Config();
        if (isCluster) {
            redisConfig.useClusterServers().addNodeAddress(hostAndPort);
        } else {
            redisConfig.useSingleServer().setAddress(hostAndPort);
        }
        redisConfig.setCodec(StringCodec.INSTANCE);
        client = Redisson.create(redisConfig);
        logger.info("Redis connection established: " + hostAndPort);
    }

    @Override
    public void close() {
        if (client != null) {
            client.shutdown();
            client = null;
        }
    }

    ////////////////////////////////////////////////////////////////
    // keys                                                       //
    ////////////////////////////////////////////////////////////////
    public Collection<String> getKeys(String pattern) {
        RKeys keys = client.getKeys();
        return keys.findKeysByPattern(pattern);
    }

    public Future<Collection<String>> getKeysAsync(String pattern) {
        RKeys keys = client.getKeys();
        return keys.findKeysByPatternAsync(pattern);
    }

    public long delKeys(String pattern) {
        RKeys keys = client.getKeys();
        return keys.deleteByPattern(pattern);
    }

    public Future<Long> delKeysAsync(String pattern) {
        RKeys keys = client.getKeys();
        return keys.deleteByPatternAsync(pattern);
    }

    ////////////////////////////////////////////////////////////////
    // get/set                                                    //
    ////////////////////////////////////////////////////////////////
    public String get(String key) {
        RBucket<String> bucket = client.getBucket(key);
        return bucket.get();
    }

    public Future<String> getAsync(String key) {
        RBucket<String> bucket = client.getBucket(key);
        return bucket.getAsync();
    }

    public void set(String key, String value) {
        RBucket<String> bucket = client.getBucket(key);
        bucket.set(value);
    }

    public Future<Void> setAsync(String key, String value) {
        RBucket<String> bucket = client.getBucket(key);
        return bucket.setAsync(value);
    }

    /* in second */
    public void set(String key, String value, int expire) {
        RBucket<String> bucket = client.getBucket(key);
        bucket.set(value, expire, TimeUnit.SECONDS);
    }

    /* in second */
    public Future<Void> setAsync(String key, String value, int expire) {
        RBucket<String> bucket = client.getBucket(key);
        return bucket.setAsync(value, expire, TimeUnit.SECONDS);
    }

    ////////////////////////////////////////////////////////////////
    // mget/mset                                                  //
    ////////////////////////////////////////////////////////////////
    public Map<String, Future<String>> mgetAsync(Set<String> keys) {
        return keys.stream().collect(Collectors.toMap(x -> x, x -> {
            RBucket<String> bucket = client.getBucket(x);
            return bucket.getAsync();
        }));
    }

    public Future<Void> msetAsync(Map<String, String> keyValues) {
        List<Future<Void>> futures = keyValues.entrySet().stream().map(x -> {
            RBucket<String> bucket = client.getBucket(x.getKey());
            return bucket.setAsync(x.getValue());
        }).collect(Collectors.toList());
        return CompletableFuture.supplyAsync(() -> {
            try {
                for (Future<Void> f : futures) {
                    f.get();
                }
            } catch (InterruptedException | ExecutionException e) {
                /* ignore */
            }
            return null;
        });
    }

    ////////////////////////////////////////////////////////////////
    // hget/hset                                                  //
    ////////////////////////////////////////////////////////////////
    public String hget(String hash, String key) {
        RMap<String, String> map = client.getMap(hash);
        return map.get(key);
    }

    public Future<String> hgetAsync(String hash, String key) {
        RMap<String, String> map = client.getMap(hash);
        return map.getAsync(key);
    }

    public void hset(String hash, String key, String value) {
        RMap<String, String> map = client.getMap(hash);
        map.fastPut(key, value);
    }

    public Future<Void> hsetAsync(String hash, String key, String value) {
        RMap<String, String> map = client.getMap(hash);
        // return map.fastPutAsync(key, value);
        /* convert Future<Boolean> to Future<Void> */
        return CompletableFuture.supplyAsync(() -> {
            try {
                map.fastPutAsync(key, value).get();
            } catch (InterruptedException | ExecutionException e) {
                /* ignore */
            }
            return null;
        });
    }

    ////////////////////////////////////////////////////////////////
    // hmget/hmset                                                //
    ////////////////////////////////////////////////////////////////
    public Map<String, String> hmget(String hash, Set<String> keys) {
        RMap<String, String> map = client.getMap(hash);
        return map.getAll(keys);
    }

    public Future<Map<String, String>> hmgetAsync(String hash, Set<String> keys) {
        RMap<String, String> map = client.getMap(hash);
        return map.getAllAsync(keys);
    }

    public void hmset(String hash, Map<String, String> keyValues) {
        RMap<String, String> map = client.getMap(hash);
        map.putAll(keyValues);
    }

    public Future<Void> hmsetAsync(String hash, Map<String, String> keyValues) {
        RMap<String, String> map = client.getMap(hash);
        return map.putAllAsync(keyValues);
    }

    ////////////////////////////////////////////////////////////////
    // publish/subscribe                                          //
    ////////////////////////////////////////////////////////////////
    public void publish(String channel, String message) {
        RTopic<String> topic = client.getTopic(channel);
        topic.publish(message);
    }

//    public void subscribe(ISubListener listener, String channel) {
//        RTopic<String> topic = client.getTopic(channel);
//        topic.addListener(new MessageListener<String>() {
//            @Override
//            public void onMessage(String channel, String message) {
//                listener.OnMessage(channel, message);
//            }
//        });
//    }
//
//    public void psubscribe(ISubListener listener, String pattern) {
//        RPatternTopic<String> topic = client.getPatternTopic(pattern);
//        topic.addListener(new PatternMessageListener<String>() {
//            @Override
//            public void onMessage(String pattern, String channel, String message) {
//                listener.OnMessage(channel, message);
//            }
//        });
//    }

    ////////////////////////////////////////////////////////////////
    // non-standard function wrappers                             //
    ////////////////////////////////////////////////////////////////
    public void hsetex(String hash, String key, String value, int expire) {
        RMapCache<String, String> map = client.getMapCache(hash);
        map.fastPut(key, value, expire, TimeUnit.SECONDS);
    }

    public Future<Void> hsetexAsync(String hash, String key, String value, int expire) {
        RMapCache<String, String> map = client.getMapCache(hash);
        // return map.fastPutAsync(key, value, expire, TimeUnit.SECONDS);
        /* convert Future<Boolean> to Future<Void> */
        return CompletableFuture.supplyAsync(() -> {
            try {
                map.fastPutAsync(key, value, expire, TimeUnit.SECONDS).get();
            } catch (InterruptedException | ExecutionException e) {
                /* ignore */
            }
            return null;
        });
    }

    public String hgetex(String hash, String key) {
        RMapCache<String, String> map = client.getMapCache(hash);
        return map.get(key);
    }

    public Future<String> hgetexAsync(String hash, String key) {
        RMapCache<String, String> map = client.getMapCache(hash);
        return map.getAsync(key);
    }

}
