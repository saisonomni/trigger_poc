package com.saisonomni.com.trigger_poc.service;

import com.saisonomni.com.trigger_poc.entity.Appform;
import com.saisonomni.com.trigger_poc.entity.repo.AppformRepository;
import org.springframework.stereotype.Service;

@Service
public class AppformService {
    private final AppformRepository appFormRepository;

    public AppformService(AppformRepository appFormRepository) {
        this.appFormRepository = appFormRepository;
    }

    public Appform createAppForm(Appform appForm) {
        return appFormRepository.save(appForm);
    }
}

