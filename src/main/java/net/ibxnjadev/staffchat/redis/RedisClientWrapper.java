package net.ibxnjadev.staffchat.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.function.Consumer;

public class RedisClientWrapper {

    private JedisPool jedisPool;

    private final String host, password;
    private final int port;

    public RedisClientWrapper(String host, String password, int port) {
        this.host = host;
        this.port = port;
        this.password = password;
    }

    public void establishConnection() {

        try {
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxTotal(8);

            JedisPool jedisPool;
            if (password != null && !password.trim().isEmpty()) {
                jedisPool = new JedisPool(jedisPoolConfig, host, port, 2000, password);
            } else {
                jedisPool = new JedisPool(jedisPoolConfig, host, port, 2000);
            }

            this.jedisPool = jedisPool;
        } catch (JedisConnectionException e) {
            System.out.println("Error connecting");
        }

    }

    public void execute(Consumer<Jedis> jedisConsumer) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedisConsumer.accept(jedis);
        }
    }

    public JedisPool getClient() {
        return jedisPool;
    }

}
