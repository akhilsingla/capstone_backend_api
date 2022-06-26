package com.iitr.gl.patientdetailservice.controller;

import com.iitr.gl.patientdetailservice.model.PatientXRayDetails;
import com.iitr.gl.patientdetailservice.repository.PatientXRayMongoDBRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patient_detail/")
@RequiredArgsConstructor
public class PatientXRayController {
    private final PatientXRayMongoDBRepository patientXRayMongoDBRepository;
    @Autowired
    Environment environment;

    @PostMapping("/addDetail")
    public void addXRayDetail(@RequestBody PatientXRayDetails patientXRayDetails) {
        patientXRayMongoDBRepository.save(patientXRayDetails);
    }

    @GetMapping("/{id}")
    public Object getPatientDetail(@PathVariable String id) {
        return patientXRayMongoDBRepository.findById(id);
    }

    @GetMapping("/test")
    public String test() {
        return environment.getProperty("value");
    }
}
