package com.okta.developer.blog.config;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.utility.DockerImageName;

/**
 * Base class for starting/stopping Neo4j during tests.
 */
public class Neo4jTestContainer implements InitializingBean, DisposableBean {

    private static final Logger log = LoggerFactory.getLogger(Neo4jTestContainer.class);
    private static final Integer CONTAINER_STARTUP_TIMEOUT_MINUTES = 10;
    private Neo4jContainer neo4jContainer;

    @Override
    public void destroy() {
        if (null != neo4jContainer && neo4jContainer.isRunning()) {
            neo4jContainer.close();
        }
    }

    @Override
    public void afterPropertiesSet() {
        if (null == neo4jContainer) {
            neo4jContainer =
                (Neo4jContainer) new Neo4jContainer(DockerImageName.parse("neo4j:5.5.0"))
                    .withoutAuthentication()
                    .withStartupTimeout(Duration.of(CONTAINER_STARTUP_TIMEOUT_MINUTES, ChronoUnit.MINUTES))
                    .withLogConsumer(new Slf4jLogConsumer(log))
                    .withReuse(true);
        }
        if (!neo4jContainer.isRunning()) {
            neo4jContainer.start();
        }
    }

    public Neo4jContainer getNeo4jContainer() {
        return neo4jContainer;
    }
}
