package com.iitr.gl.userdetailservice.ui.controller;

import com.iitr.gl.userdetailservice.service.PythonScriptService;
import com.iitr.gl.userdetailservice.shared.PythonScriptDto;
import com.iitr.gl.userdetailservice.ui.model.GenericRequestModel;
import com.iitr.gl.userdetailservice.ui.model.PythonScriptRequestModel;
import com.iitr.gl.userdetailservice.ui.model.UploadPythonScriptRequestModel;
import com.iitr.gl.userdetailservice.ui.model.UploadPythonScriptResponseModel;
import com.iitr.gl.userdetailservice.util.ExecutePython;
import com.iitr.gl.userdetailservice.util.GetJwtSubject;
import com.iitr.gl.userdetailservice.util.PythonScriptActions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user_detail/python_script/")
public class UserPythonScriptController {

    @Autowired
    PythonScriptService pythonScriptService;

    @Autowired
    PythonScriptActions pythonScriptActions;

    @Autowired
    Environment environment;

    @Autowired
    GetJwtSubject getJwtSubject;

    @Autowired
    ExecutePython executePython;

    @PostMapping("/upload")
    public ResponseEntity<UploadPythonScriptResponseModel> uploadPythonScript(@RequestBody UploadPythonScriptRequestModel requestModel,
                                                                              @RequestHeader("Authorization") String token) {
        getJwtSubject.verifyIfAuthorized(token, requestModel.getUserId(), environment, false);
        return pythonScriptActions.uploadPythonScript(requestModel, pythonScriptService);
    }

    @PostMapping("/update")
    public ResponseEntity<String> updatePythonScript(@RequestBody UploadPythonScriptRequestModel requestModel,
                                                     @RequestHeader("Authorization") String token) {
        getJwtSubject.verifyIfAuthorized(token, requestModel.getUserId(), environment, false);
        return pythonScriptActions.updatePythonScript(requestModel, pythonScriptService);
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deletePythonScript(@RequestBody GenericRequestModel requestModel,
                                                     @RequestHeader("Authorization") String token) {
        getJwtSubject.verifyIfAuthorized(token, requestModel.getUserId(), environment, false);
        return pythonScriptActions.deletePythonScript(requestModel, pythonScriptService);
    }

    @PostMapping("/deleteAll")
    public ResponseEntity<String> deleteAllPythonScript(@RequestBody GenericRequestModel requestModel,
                                                        @RequestHeader("Authorization") String token) {
        getJwtSubject.verifyIfAuthorized(token, requestModel.getUserId(), environment, false);
        return pythonScriptActions.deleteAllPythonScript(requestModel, pythonScriptService);
    }

    @PostMapping("/download")
    public ResponseEntity<ByteArrayResource> downloadPythonScript(@RequestBody GenericRequestModel genericRequestModel,
                                                                  @RequestHeader("Authorization") String token) {
        getJwtSubject.verifyIfAuthorized(token, genericRequestModel.getUserId(), environment, false);
        return pythonScriptActions.downloadPythonScript(genericRequestModel, pythonScriptService);
    }

    @PostMapping("/view")
    public ResponseEntity<String> viewPythonScript(@RequestBody GenericRequestModel genericRequestModel,
                                                   @RequestHeader("Authorization") String token) {
        getJwtSubject.verifyIfAuthorized(token, genericRequestModel.getUserId(), environment, false);
        return pythonScriptActions.viewPythonScript(genericRequestModel, pythonScriptService);
    }

    @PostMapping(value = "/runPy", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Object> runPythonScript(@RequestBody PythonScriptRequestModel requestModel,
                                        @RequestHeader("Authorization") String token) {
        getJwtSubject.verifyIfAuthorized(token, requestModel.getUserId(), environment, false);
        return executePython.runPythonScript(requestModel);

    }
}
