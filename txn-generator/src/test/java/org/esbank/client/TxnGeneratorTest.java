package org.esbank.client;

import org.junit.*;
import java.util.List;

public class TxnGeneratorTest {
    
    @Test
    public void testRandomUserSelection() {
        List<String> usersList = TxnGenerator.generateUsers(10);
        System.out.println("users: "+usersList);
        String user = null;
        for(int i=0; i<10;i++) {
            user = usersList.get(TxnGenerator.getRandom(usersList.size()));
            System.out.println("user: "+user);
        }
        Assert.assertNotNull(user);
    }

    @Test
    public void testRandomAmount() {
        int amount = 0;
        for(int i=0; i<10;i++) {
            amount = TxnGenerator.getRandom(1000);
            System.out.println("amount: "+amount);
        }
        Assert.assertNotNull(amount);
    }
}