/* (C)2022 */
package com.advendio.marketplace.openservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
        classes = MarketplaceOpenServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MarketplaceOpenServiceApplicationTest {
    @Test
    public void contextLoads() {}
}
