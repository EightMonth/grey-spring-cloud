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

package org.eightmonth.cloud.grey.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.eightmonth.cloud.grey.core.GreyContextHolder;
import org.eightmonth.cloud.grey.core.GreyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class GreyFilter extends ZuulFilter {

    @Autowired
    private GreyProperties greyProperties;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
        String headerKey = greyProperties.getHeader();
        String value = (StringUtils.hasText(request.getHeader(headerKey)))
                && (greyProperties.keySet().contains(request.getHeader(headerKey)))
                ? request.getHeader(headerKey)
                : greyProperties.getDefaultChoose();
        GreyContextHolder.getCurrentContext().add(headerKey, value);
        return null;
    }
}
