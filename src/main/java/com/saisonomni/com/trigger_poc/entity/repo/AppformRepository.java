package com.saisonomni.com.trigger_poc.entity.repo;

import com.saisonomni.com.trigger_poc.entity.Appform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppformRepository extends JpaRepository<Appform, String> {
}
