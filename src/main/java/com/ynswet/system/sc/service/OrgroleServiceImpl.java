package com.ynswet.system.sc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ynswet.system.sc.repository.OrgroleRepository;

@Service
public class OrgroleServiceImpl implements OrgroleService {
	@Autowired
	private OrgroleRepository orgroleRepository;

	@Transactional
	public void deleteOrgrole() {
		
	}

}
