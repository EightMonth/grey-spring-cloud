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

package org.eightmonth.cloud.grey.core.interceptor;

import org.eightmonth.cloud.grey.core.GreyContextHolder;
import org.eightmonth.cloud.grey.core.GreyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 此处为伪代码，请勿照抄。
 * 此拦截器用于在网关（zuul、gateway）中起作用，而应用方如果想拦截service to service的请求，不应使用此拦截器。
 * 因为网关是接收外部请求，内部发起新请求，拦截器用于获取外部请求的请求路线，再用于到新请求中选择服务实例。
 * 而应用之间的service to service的调用不是这种模式，所以不能用。
 * 当然zuul、gateway中也不会用这个类。
 * @author eightmonth
 */
public final class GreyInterceptor implements HandlerInterceptor {

    @Autowired
    private GreyProperties greyProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String headerKey = greyProperties.getHeader();
        String value = (StringUtils.hasText(request.getHeader(headerKey)))
                && (greyProperties.keySet().contains(request.getHeader(headerKey)))
                ? request.getHeader(headerKey)
                : greyProperties.getDefaultChoose();
        GreyContextHolder.getCurrentContext().add(headerKey, value);
        return true;
    }
}
