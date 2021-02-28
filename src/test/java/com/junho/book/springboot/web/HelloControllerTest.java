package com.junho.book.springboot.web;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
/*import org.springframework.security.test.context.support.WithMockUser;*/
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.is;

//@RunWith(SpringRunner.class) // (1)
@ExtendWith(SpringExtension.class) //(1)-2 JUNIT 5 으로 버전업
@WebMvcTest(controllers = HelloController.class) // (2)
public class HelloControllerTest {

    @Autowired // (3)
    private MockMvc mvc; // (4)

    /*@WithMockUser(roles="USER")*/
    @Test
    public void helloTest() throws Exception {
        String hello = "hello";

        mvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string(hello));

    }
    @Test
    public void testHelloDto() throws Exception{
        String name="hello";
        int amount =1000;

        mvc.perform(get("/hello/dto")
                .param("name", name)
                .param("amount",String.valueOf(amount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.amount", is(amount))
        );

    }
}
