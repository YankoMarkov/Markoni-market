package org.yanmark.markoni.web.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.yanmark.markoni.repositories.ReceiptRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ReceiptControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReceiptRepository receiptRepository;

    @Test
    public void testReceipt_getReceipt_returnReceiptPage() throws Exception {
        mockMvc.perform(get("/receipts"))
                .andExpect(view().name("/receipts/receipt"));
    }
}
