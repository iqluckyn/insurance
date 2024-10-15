package com.iql.policyadmin.domain;

import static com.iql.policyadmin.domain.ProductTestSamples.*;
import static com.iql.policyadmin.domain.QuotationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.iql.policyadmin.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Product.class);
        Product product1 = getProductSample1();
        Product product2 = new Product();
        assertThat(product1).isNotEqualTo(product2);

        product2.setId(product1.getId());
        assertThat(product1).isEqualTo(product2);

        product2 = getProductSample2();
        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    void quotationTest() {
        Product product = getProductRandomSampleGenerator();
        Quotation quotationBack = getQuotationRandomSampleGenerator();

        product.addQuotation(quotationBack);
        assertThat(product.getQuotations()).containsOnly(quotationBack);
        assertThat(quotationBack.getProduct()).isEqualTo(product);

        product.removeQuotation(quotationBack);
        assertThat(product.getQuotations()).doesNotContain(quotationBack);
        assertThat(quotationBack.getProduct()).isNull();

        product.quotations(new HashSet<>(Set.of(quotationBack)));
        assertThat(product.getQuotations()).containsOnly(quotationBack);
        assertThat(quotationBack.getProduct()).isEqualTo(product);

        product.setQuotations(new HashSet<>());
        assertThat(product.getQuotations()).doesNotContain(quotationBack);
        assertThat(quotationBack.getProduct()).isNull();
    }
}
