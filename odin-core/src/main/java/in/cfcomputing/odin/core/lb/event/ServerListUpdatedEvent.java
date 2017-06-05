/*
 * Copyright (c) 2017 cFactor Computing Pvt. Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
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

package in.cfcomputing.odin.core.lb.event;

import org.springframework.context.ApplicationEvent;

/**
 * Created by gibugeorge on 30/03/2017.
 */
public class ServerListUpdatedEvent extends ApplicationEvent {

    private final String serviceName;

    public ServerListUpdatedEvent(Object serviceName) {
        super(serviceName);
        this.serviceName = (String) serviceName;
    }

    public String getServiceName() {
        return serviceName;
    }
}
