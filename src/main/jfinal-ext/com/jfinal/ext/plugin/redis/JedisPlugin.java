/**
 * Copyright (c) 2011-2013, kidzhou 周磊 (zhouleib1412@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jfinal.ext.plugin.redis;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.jfinal.ext.kit.ResourceKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.IPlugin;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;

public class JedisPlugin implements IPlugin {

    public JedisPool pool;
    private String config = "RedisConnector.properties";

    private String host = "localhost";
    private int port = 6379;
    private int timeout = 2000;
    private String password;
    private int maxidle = JedisPoolConfig.DEFAULT_MAX_IDLE;
    private long minevictableidletimemillis = JedisPoolConfig.DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS;
    private int minidle = JedisPoolConfig.DEFAULT_MIN_IDLE;
    private int numtestsperevictionrun = JedisPoolConfig.DEFAULT_NUM_TESTS_PER_EVICTION_RUN;
    private long softminevictableidletimemillis = JedisPoolConfig.DEFAULT_SOFT_MIN_EVICTABLE_IDLE_TIME_MILLIS;
    private long timebetweenevictionrunsmillis = JedisPoolConfig.DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS;
    private boolean testwhileidle = JedisPoolConfig.DEFAULT_TEST_WHILE_IDLE;
    private boolean testonreturn = JedisPoolConfig.DEFAULT_TEST_ON_RETURN;
    private boolean testonborrow = JedisPoolConfig.DEFAULT_TEST_ON_BORROW;

    public JedisPlugin() {
    }

    public JedisPlugin(String host) {
        this.host = host;
    }

    public JedisPlugin(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public JedisPlugin(String host, int port, int timeout) {
        this.host = host;
        this.port = port;
        this.timeout = timeout;
    }

    public JedisPlugin config(String config) {
        this.config = config;
        return this;
    }

    @Override
    public boolean start() {
        Map<String, String> map = ResourceKit.readProperties(config);
        Set<Entry<String, String>> entrySet = map.entrySet();
        for (Entry<String, String> entry : entrySet) {
            parseSetting(entry.getKey(), entry.getValue().trim());
        }
        JedisShardInfo shardInfo = new JedisShardInfo(host, port, timeout);
        if (StrKit.notBlank(password)) {
            shardInfo.setPassword(password);
        }
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        setPoolConfig(poolConfig);
        pool = new JedisPool(poolConfig, shardInfo.getHost(), shardInfo.getPort(), shardInfo.getConnectionTimeout(),
                shardInfo.getPassword());
        JedisKit.init(pool);
        return true;
    }

    private void setPoolConfig(JedisPoolConfig poolConfig) {
        poolConfig.setMaxIdle(maxidle);
        poolConfig.setMinEvictableIdleTimeMillis(minevictableidletimemillis);
        poolConfig.setMinIdle(minidle);
        poolConfig.setNumTestsPerEvictionRun(numtestsperevictionrun);
        poolConfig.setSoftMinEvictableIdleTimeMillis(softminevictableidletimemillis);
        poolConfig.setTimeBetweenEvictionRunsMillis(timebetweenevictionrunsmillis);
        poolConfig.setTestWhileIdle(testwhileidle);
        poolConfig.setTestOnReturn(testonreturn);
        poolConfig.setTestOnBorrow(testonborrow);
    }

    @Override
    public boolean stop() {
        try {
            pool.destroy();
        } catch (Exception ex) {
            System.err.println("Cannot properly close Jedis pool:" + ex);
        }
        pool = null;
        return true;
    }

    private void parseSetting(String key, String value) {
        if ("timeout".equalsIgnoreCase(key)) {
            timeout = Integer.valueOf(value);
        } else if ("password".equalsIgnoreCase(key)) {
            password = value;
        } else if ("host".equalsIgnoreCase(key)) {
            host = value;
        }else if ("maxidle".equalsIgnoreCase(key)) {
            maxidle = Integer.valueOf(value);
        }else if ("minevictableidletimemillis".equalsIgnoreCase(key)) {
            minevictableidletimemillis = Long.valueOf(value);
        } else if ("minidle".equalsIgnoreCase(key)) {
            minidle = Integer.valueOf(value);
        } else if ("numtestsperevictionrun".equalsIgnoreCase(key)) {
            numtestsperevictionrun = Integer.valueOf(value);
        } else if ("softminevictableidletimemillis".equalsIgnoreCase(key)) {
            softminevictableidletimemillis = Long.valueOf(value);
        } else if ("timebetweenevictionrunsmillis".equalsIgnoreCase(key)) {
            timebetweenevictionrunsmillis = Long.valueOf(value);
        } else if ("testwhileidle".equalsIgnoreCase(key)) {
            testwhileidle = Boolean.getBoolean(value);
        } else if ("testonreturn".equalsIgnoreCase(key)) {
            testonreturn = Boolean.getBoolean(value);
        } else if ("testonborrow".equalsIgnoreCase(key)) {
            testonborrow = Boolean.getBoolean(value);
        }
    }
}
