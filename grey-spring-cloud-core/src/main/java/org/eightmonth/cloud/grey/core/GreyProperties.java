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

package org.eightmonth.cloud.grey.core;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@ConfigurationProperties(prefix = "grey")
@RefreshScope
public class GreyProperties {

    private String header = GreyConstants.DEFAULT_HEADER_KEY;
    private Map<String, String> rules = new HashMap<>();
    private String defaultChoose = GreyConstants.DEFAULT_ALL_VALUE;
    private String tag = GreyConstants.DEFAULT_TAG;
    private String innerChoose = GreyConstants.DEFAULT_ALL_VALUE;


    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public Map<String, String> getRules() {
        return rules;
    }

    public void setRules(Map<String, String> rules) {
        this.rules = rules;
    }

    public String getDefaultChoose() {
        return defaultChoose;
    }

    public void setDefaultChoose(String defaultChoose) {
        this.defaultChoose = defaultChoose;
    }

    public Set<String> keySet() {
        return rules.keySet();
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getInnerChoose() {
        return innerChoose;
    }

    public void setInnerChoose(String innerChoose) {
        this.innerChoose = innerChoose;
    }
}
