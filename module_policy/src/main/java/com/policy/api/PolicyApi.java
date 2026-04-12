package com.policy.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/policy")
public class PolicyApi {

    @GetMapping("/patient/{patient_id}")
    public String getPolicyOfPatient(@PathVariable("patient_id") final Long patient_id){
        return String.valueOf(patient_id);
    }
}
