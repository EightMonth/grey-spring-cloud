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

package org.eightmonth.cloud.grey.feign.listener;

import feign.RequestInterceptor;
import org.eightmonth.cloud.grey.feign.intercept.HttpInterceptor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigFeignClientListener implements ApplicationListener<ApplicationReadyEvent> {
    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        ConfigurableEnvironment env =applicationReadyEvent.getApplicationContext().getEnvironment();
        MutablePropertySources sources = env.getPropertySources();

        Map<String, Object> feignClientProperties = new HashMap<>();
        List<Class<? extends RequestInterceptor>> requestInterceptors = new ArrayList<>();
        requestInterceptors.add(HttpInterceptor.class);
        feignClientProperties.put("feign.client.config.default.requestInterceptors", requestInterceptors);

        sources.addLast(new MapPropertySource("add-feign", feignClientProperties));
    }
}
