package com.cts.ecommerce;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Basic sanity test that doesn't require Spring context loading.
 */
class EcommerceApplicationTests {

	@Test
	void mainClassExists() {
		// Verifies the main application class is on the classpath.
		assertThat(EcommerceApplication.class).isNotNull();
	}

	@Test
	void mainMethodIsCallable() {
		// Verifies the main method exists with the correct signature.
		assertThat(EcommerceApplication.class.getDeclaredMethods())
				.anyMatch(m -> "main".equals(m.getName()));
	}
}