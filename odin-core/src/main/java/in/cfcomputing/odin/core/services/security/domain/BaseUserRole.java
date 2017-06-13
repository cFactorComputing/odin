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

package in.cfcomputing.odin.core.services.security.domain;

import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by gibugeorge on 08/06/2017.
 */
public class BaseUserRole implements Serializable {

    @Id
    private String id = UUID.randomUUID().toString();
    private IRoleType role;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public IRoleType getRole() {
        return role;
    }

    public void setRole(IRoleType role) {
        this.role = role;
    }
}
