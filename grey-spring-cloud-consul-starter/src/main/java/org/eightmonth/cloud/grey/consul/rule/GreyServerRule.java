/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.eightmonth.cloud.grey.consul.rule;

import com.google.common.base.Optional;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ZoneAvoidanceRule;
import org.eightmonth.cloud.grey.core.GreyConstants;
import org.eightmonth.cloud.grey.core.GreyContextHolder;
import org.eightmonth.cloud.grey.core.GreyProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.consul.discovery.ConsulServer;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GreyServerRule extends ZoneAvoidanceRule {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private GreyProperties greyProperties;

    @Override
    public Server choose(Object key) {
        ILoadBalancer lb = this.getLoadBalancer();
        Optional<Server> server = this.getPredicate().chooseRoundRobinAfterFiltering(choose(lb), key);
        return server.isPresent() ? server.get() : null;
    }


    protected List<Server> choose(ILoadBalancer lb) {
        if (Objects.isNull(lb)) {
            logger.warn("no load balancer");
            return null;
        }

        // 从consul上获取所有可用的实例列表，用于从中做出选择
        List<Server> reachableServers = lb.getReachableServers();
        if (reachableServers.size() == 0 ) {
            logger.warn("No up server available from load balancer : " + lb);
            return null;
        }

        List<Server> availableServers = new ArrayList<>();

        for (Server  reachableServer : reachableServers) {
            // 转成consulClient实现的server，获取各实例的tag属性，用于区分是否是灰度实例
            ConsulServer current = (ConsulServer) reachableServer;
            if (decision(current.getHealthService().getService().getTags())) {
                availableServers.add(current);
            }
        }

        logger.debug("availableServers: {}", availableServers);

        if (availableServers.size() != 0) {
            return availableServers;
        }
        return reachableServers;
    }

    private boolean decision(List<String> tags) {
        if (GreyContextHolder.getCurrentContext().get(greyProperties.getHeader()).equals(GreyConstants.DEFAULT_ALL_VALUE))
            return true;

        String pattern = greyProperties.getRules().get(GreyContextHolder.getCurrentContext().get(greyProperties.getHeader()));
        if (StringUtils.isEmpty(pattern))
            return true;

        boolean reverse = pattern.split(" ")[0].equals("!");
        boolean equals = pattern.split(" ")[0].equals("=");

        String rule;
        if (!reverse && !equals) {
            rule = pattern;
        } else  {
            rule = pattern.split(" ")[1];
        }

        logger.debug("tags:{}, tag:{}, rule:{}, reverse:{}", tags, greyProperties.getTag(), rule, reverse);

        if (reverse) {
            return !finalEquals(tags, rule);
        } else {
            return finalEquals(tags, rule);
        }
    }

    protected boolean finalStartsWith(List<String> tagList, String rule) {
        boolean correct = false;
        if (tagList.contains(greyProperties.getTag())) {

        }
        return correct;
    }

    protected boolean finalEquals(List<String> tagList, String rule) {
        boolean correct = false;
        if (tagList.contains(greyProperties.getTag()) && greyProperties.getTag().equals(rule)) {
            correct = true;
        }
        return correct;
    }
}
