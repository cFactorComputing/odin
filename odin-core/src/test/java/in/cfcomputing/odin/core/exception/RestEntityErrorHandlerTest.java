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

package in.cfcomputing.odin.core.exception;

import in.cfcomputing.odin.core.exception.support.TestController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@EnableWebMvc
@WebAppConfiguration
@ComponentScan("in.cfcomputing.odin.core.exception.support")
public class RestEntityErrorHandlerTest {

    private MockMvc mockMvc;

    @Mock
    private TestController controller;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();
    }

    @Test
    public void testGlobalExceptionHandlerError() throws Exception {
        when(controller.test()).thenThrow(new RuntimeException("Unexpected Exception"));
        mockMvc.perform(get("/test")).andExpect(status().is(500)).andReturn();
    }

    @Test
    public void testCustomServiceException() throws Exception {
        when(controller.test()).thenThrow(new ServiceException("Unexpected Exception", ErrorCode.TEST_ERROR_CODE).withParam("name", "My Name"));
        mockMvc.perform(get("/test"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("code").value(ErrorCode.TEST_ERROR_CODE.toString()))
                .andExpect(jsonPath("params.name").value("My Name"))
                .andReturn();
    }

    class ServiceException extends AbstractOdinServiceException {

        public ServiceException(String message, ErrorCode code) {
            super(message, code);
        }

    }

    public enum ErrorCode {
        TEST_ERROR_CODE
    }

}