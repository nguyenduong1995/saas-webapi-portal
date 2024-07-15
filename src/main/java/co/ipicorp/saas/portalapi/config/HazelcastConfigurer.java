/**
 * HazelcastConfigurer.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     hieumicro
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.config;

import co.ipicorp.saas.portalapi.security.CustomerSessionInfo;
import co.ipicorp.saas.portalapi.util.Constants;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import grass.micro.apps.component.SystemConfiguration;
import grass.micro.apps.web.component.SessionPool;

/**
 * HazelcastConfigurer. <<< Detail note.
 * 
 * @author hieumicro
 * @access public
 */
@Configuration
public class HazelcastConfigurer {
    
    @Autowired
    private SystemConfiguration systemConfiguration;
    
    @Bean
    public HazelcastInstance hazelcastInstance() {
        ClientConfig clientConfig = new ClientConfig();

        // Configure cluster member addresses to be connected.
        String endpoint = systemConfiguration.getProperty("hazelcast.endpoint");
        int timeout = systemConfiguration.getInt("hazelcast.timeout", 60000);
        String clusterName = systemConfiguration.getProperty("hazelcast.clusterName");
        clientConfig.getNetworkConfig().addAddress(endpoint);
        clientConfig.getNetworkConfig().setConnectionTimeout(timeout);
        clientConfig.setClusterName(clusterName);
        clientConfig.getNetworkConfig().getSocketOptions().setKeepAlive(true);
        return HazelcastClient.newHazelcastClient(clientConfig);
    }

    @Bean
    public SessionPool<CustomerSessionInfo> sessionPool(IMap<String, CustomerSessionInfo> poolMap) {
        return new SessionPool<CustomerSessionInfo>(poolMap);
    }

    @Bean
    public IMap<String, CustomerSessionInfo> poolMap(HazelcastInstance hzInstance) {
        String sessionPoolKey = systemConfiguration.getProperty(Constants.APP_HAZELCAST_SESSION_POOL_KEY, Constants.APP_HAZELCAST_SESSION_POOL);
        IMap<String, CustomerSessionInfo> map = hzInstance.getMap(sessionPoolKey);
        return map;
    }
    
}
