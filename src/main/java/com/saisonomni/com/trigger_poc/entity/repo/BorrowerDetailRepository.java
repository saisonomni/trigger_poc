package com.saisonomni.com.trigger_poc.entity.repo;

import com.saisonomni.com.trigger_poc.entity.BorrowerDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowerDetailRepository extends JpaRepository<BorrowerDetail, String> {
}
