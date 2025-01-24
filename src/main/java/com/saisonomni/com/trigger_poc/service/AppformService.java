package com.saisonomni.com.trigger_poc.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saisonomni.com.trigger_poc.entity.Appform;
import com.saisonomni.com.trigger_poc.entity.BREData;
import com.saisonomni.com.trigger_poc.entity.BorrowerDetail;
import com.saisonomni.com.trigger_poc.entity.EntityMapper;
import com.saisonomni.com.trigger_poc.entity.repo.AppformRepository;
import com.saisonomni.com.trigger_poc.entity.repo.BREDataRepository;
import com.saisonomni.com.trigger_poc.entity.repo.BorrowerDetailRepository;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Optional;

@Service
public class AppformService {
    private final AppformRepository appFormRepository;
    private final BREDataRepository breDataRepository;
    private final BorrowerDetailRepository borrowerDetailRepository;
    private final EntityMapper entityMapper;

    public AppformService(AppformRepository appFormRepository, BREDataRepository breDataRepository, BorrowerDetailRepository borrowerDetailRepository, EntityMapper entityMapper) {
        this.appFormRepository = appFormRepository;
        this.breDataRepository = breDataRepository;
        this.borrowerDetailRepository = borrowerDetailRepository;
        this.entityMapper = entityMapper;
    }

    public Appform createAppForm(Appform appForm) {
        return appFormRepository.save(appForm);
    }
    public String deleteApppform(String id){
        Optional<Appform> appform = appFormRepository.findById(id);
        appform.get().setDeleted(true);
        appform.get().getCoapplicants().forEach(coapplicant -> {
            coapplicant.getBorrowerDetail().set_deleted(true);
        });
        appFormRepository.save(appform.get());
//        appFormRepository.deleteById(id);
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
    public String createBorrower(BorrowerDetail borrowerDetail){
        try {
            BorrowerDetail b = borrowerDetailRepository.findById("22c564d5-6655-4e31-aa55-451948de6bd2").get();
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> mp2 = objectMapper.convertValue(
                    b,
                    new TypeReference<Map<String, Object>>() {}
            );
//            Map<String,String> mp2 = BeanUtils.describe(b);
            Map<String,String> mp1 = BeanUtils.describe(borrowerDetail);
            System.out.println(0);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        borrowerDetailRepository.save(borrowerDetail);
        return "save";
    }
}

