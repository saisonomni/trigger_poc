package com.saisonomni.com.trigger_poc.entity.repo;

import com.saisonomni.com.trigger_poc.entity.BREData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BREDataRepository extends MongoRepository<BREData,String> {
}
