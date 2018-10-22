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

package in.cfcomputing.odin.mvc.exception;

import java.util.HashMap;
import java.util.Map;

public class ErrorResponse {
    private int status;
    private String code;
    private Map<String, String> params = new HashMap<>();

    public int getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public ErrorResponse withStatus(int status) {
        this.status = status;
        return this;
    }

    public ErrorResponse withCode(String code) {
        this.code = code;
        return this;
    }

    public ErrorResponse withParams(Map<String, String> params) {
        this.params = params;
        return this;
    }

    @Override
    public String toString() {
        return String.format("[Status: %s, Error Code: %s, Params: %s]", status, code, params);
    }
}
