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

package org.eightmonth.cloud.grey.feign.intercept;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.eightmonth.cloud.grey.core.GreyContextHolder;
import org.eightmonth.cloud.grey.core.GreyProperties;
import org.eightmonth.cloud.grey.core.util.GreyPropertiesUtil;
import org.springframework.util.StringUtils;

/**
 * service to service 拦截路线选择需求
 * @author eightmonth
 */
public class HttpInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        GreyProperties greyProperties = GreyPropertiesUtil.getGreyPropertiesUtil().greyProperties();
        GreyContextHolder.getCurrentContext().add(greyProperties.getHeader(),
                StringUtils.isEmpty(greyProperties.getInnerChoose())? greyProperties.getDefaultChoose() : greyProperties.getInnerChoose());
    }
}
