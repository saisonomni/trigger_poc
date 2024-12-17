package com.saisonomni.com.trigger_poc.service;

import com.saisonomni.com.trigger_poc.entity.Appform;
import com.saisonomni.com.trigger_poc.entity.BREData;
import com.saisonomni.com.trigger_poc.entity.repo.AppformRepository;
import com.saisonomni.com.trigger_poc.entity.repo.BREDataRepository;
import org.springframework.stereotype.Service;

@Service
public class AppformService {
    private final AppformRepository appFormRepository;
    private final BREDataRepository breDataRepository;

    public AppformService(AppformRepository appFormRepository,BREDataRepository breDataRepository) {
        this.appFormRepository = appFormRepository;
        this.breDataRepository = breDataRepository;
    }

    public Appform createAppForm(Appform appForm) {
        return appFormRepository.save(appForm);
    }
    public BREData createBREData(BREData breData) {
        return breDataRepository.save(breData);
    }
}

