package com.saisonomni.com.trigger_poc.service;

import com.saisonomni.com.trigger_poc.entity.Appform;
import com.saisonomni.com.trigger_poc.entity.BREData;
import com.saisonomni.com.trigger_poc.entity.BorrowerDetail;
import com.saisonomni.com.trigger_poc.entity.repo.AppformRepository;
import com.saisonomni.com.trigger_poc.entity.repo.BREDataRepository;
import com.saisonomni.com.trigger_poc.entity.repo.BorrowerDetailRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AppformService {
    private final AppformRepository appFormRepository;
    private final BREDataRepository breDataRepository;
    private final BorrowerDetailRepository borrowerDetailRepository;

    public AppformService(AppformRepository appFormRepository,BREDataRepository breDataRepository,BorrowerDetailRepository borrowerDetailRepository) {
        this.appFormRepository = appFormRepository;
        this.breDataRepository = breDataRepository;
        this.borrowerDetailRepository = borrowerDetailRepository;
    }

    public Appform createAppForm(Appform appForm) {
        return appFormRepository.save(appForm);
    }
    public String deleteApppform(String id){
//        Optional<Appform> appform = appFormRepository.findById(id);
//        appform.get().setDeleted(true);
//        appform.get().getCoapplicants().forEach(coapplicant -> {
//            coapplicant.getBorrowerDetail().set_deleted(true);
//        });
//        appFormRepository.save(appform.get());
        appFormRepository.deleteById(id);
        return "Deleted";
    }
    public BREData createBREData(BREData breData) {
        return breDataRepository.save(breData);
    }
    public String updateBorrower(String borrowerId,int cibil){
        Optional<BorrowerDetail> appform = borrowerDetailRepository.findById(borrowerId);
        appform.get().setCibil(cibil);
        borrowerDetailRepository.save(appform.get());
        return "Updated";
    }
}

