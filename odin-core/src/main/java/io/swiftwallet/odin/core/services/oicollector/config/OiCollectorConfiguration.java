/*
 * Copyright 2017 SwiftWallet Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"),
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.swiftwallet.odin.core.services.oicollector.config;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;
import io.swiftwallet.odin.core.services.oicollector.OiCollectorProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * Created by gibugeorge on 07/02/2017.
 */
@Configuration
@EnableMetrics
@EnableConfigurationProperties(OiCollectorProperties.class)
public class OiCollectorConfiguration extends MetricsConfigurerAdapter {

    @Autowired
    private MetricRegistry metricRegistry;

    @Autowired
    private HealthCheckRegistry healthCheckRegistry;

    @Autowired
    private OiCollectorProperties properties;

    @PostConstruct
    public void init() {
        metricRegistry.register("gc", new GarbageCollectorMetricSet());
        metricRegistry.register("memory", new MemoryUsageGaugeSet());
        metricRegistry.register("threads", new ThreadStatesGaugeSet());
        configureReporters(metricRegistry);
    }


    @Override
    public void configureReporters(final MetricRegistry metricRegistry) {
        if (properties.isJmxEnabled()) {
            registerReporter(JmxReporter.forRegistry(metricRegistry).build()).start();
        }
        if (properties.getGraphite().isEnabled()) {
            getGraphiteReporterBuilder(metricRegistry).build(graphite()).start(properties.getGraphite().getAmountOfTimeBetweenPolls(), TimeUnit.MILLISECONDS);
        }

    }

    private GraphiteReporter.Builder getGraphiteReporterBuilder(final MetricRegistry metricRegistry) {
        return GraphiteReporter.forRegistry(metricRegistry).convertRatesTo(TimeUnit.SECONDS).convertDurationsTo(TimeUnit.MILLISECONDS).filter(MetricFilter.ALL);
    }

    @Bean
    @ConditionalOnProperty(prefix = "operational-intelligence.graphite", name = "enabled", havingValue = "true", matchIfMissing = false)
    public Graphite graphite() {
        return new Graphite(properties.getGraphite().getHost(), properties.getGraphite().getPort());
    }


}
