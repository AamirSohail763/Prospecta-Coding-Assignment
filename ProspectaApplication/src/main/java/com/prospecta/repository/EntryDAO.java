package com.prospecta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prospecta.modules.Entry;

@Repository
public interface EntryDAO extends JpaRepository<Entry, String>{

}
