package com.kunpengku.test1.testes.config;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author fupeng.
 */
@Configuration
public class ESConfig implements FactoryBean<TransportClient>, InitializingBean, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(ESConfig.class);

    @Value("${elasticsearch.cluster-nodes}")
    private String clusterNodes;

    @Value("${elasticsearch.cluster-name}")
    private String clusterName;

    private TransportClient transportClient = null;

    @Override
    public void destroy() throws Exception {
        try {
            logger.info("Closing elasticSearch client");
            if (transportClient != null) {
                transportClient.close();
            }
        } catch (final Exception e) {
            logger.error("Error closing ElasticSearch client: ", e);
        }
    }

    @Override
    public TransportClient getObject() throws Exception {
        return transportClient;
    }

    @Override
    public Class<TransportClient> getObjectType() {
        return TransportClient.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        buildClient();
    }

    protected void buildClient() {
        transportClient = new PreBuiltTransportClient(settings());
        try {
            String[] nodes = clusterNodes.split(",");
            for (String node : nodes) {
                String hostname = node.split(":")[0];
                Integer port = Integer.parseInt(node.split(":")[1]);
                transportClient.addTransportAddress(
                        new InetSocketTransportAddress(InetAddress.getByName(hostname), port));
            }
        } catch (UnknownHostException e) { //NOSONAR
            logger.error(e.getMessage());
        }
    }

    /**
     * 初始化默认的client
     */
    private Settings settings() {
        return Settings.builder().put("cluster.name", clusterName).build();
    }
}
