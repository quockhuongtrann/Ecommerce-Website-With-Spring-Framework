package com.shoptqk.admin.setting.state;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.shoptqk.common.entity.Country;
import com.shoptqk.common.entity.State;

public interface StateRepository extends CrudRepository<State, Integer> {
	
	public List<State> findByCountryOrderByNameAsc(Country country);
}
