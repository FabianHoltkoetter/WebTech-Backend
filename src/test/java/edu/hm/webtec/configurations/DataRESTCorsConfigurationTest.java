package edu.hm.webtec.configurations;

import edu.hm.webtec.ItsApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

/**
 * @author peter-mueller
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ItsApplication.class)
@WebAppConfiguration
public class DataRESTCorsConfigurationTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    CorsFilter corsFilter;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .addFilter(corsFilter).build();
    }

    @Test
    public void testPreflight() throws Exception {
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .options("/")
                .header(HttpHeaders.ORIGIN, "*")
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET")
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, "test");

        final ResultMatcher origin = MockMvcResultMatchers
                .header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        final ResultMatcher methods = MockMvcResultMatchers
                .header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET,HEAD,POST,PUT,PATCH,DELETE,OPTIONS");
        final ResultMatcher headers = MockMvcResultMatchers
                .header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "test");
        mockMvc.perform(requestBuilder)
                .andExpect(origin).andExpect(methods).andExpect(headers);
    }

    @Test
    public void testCors() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/").header(HttpHeaders.ORIGIN, "*"))
          .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*"));
    }
}