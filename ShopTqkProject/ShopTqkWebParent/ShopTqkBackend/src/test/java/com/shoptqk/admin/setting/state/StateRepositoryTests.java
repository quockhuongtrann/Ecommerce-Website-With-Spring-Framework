package com.shoptqk.admin.setting.state;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shoptqk.common.entity.Country;
import com.shoptqk.common.entity.State;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class StateRepositoryTests {
	@Autowired private StateRepository stateRepository;
	@Autowired private TestEntityManager entityManager;
	
	@Test
	public void testCreateStatesInIndia() {
		Integer countryId = 2;
		Country country = entityManager.find(Country.class, countryId);
		
		// State state = stateRepository.save(new State("West Bengal", country));
		// State state = stateRepository.save(new State("Karnataka", country));
		// State state = stateRepository.save(new State("Punjab", country));
		State state = stateRepository.save(new State("Uttar Pradesh", country));
		
		assertThat(state).isNotNull();
		assertThat(state.getId()).isGreaterThan(0);
	}
}
