package com.iitr.gl.userdetailservice.ui.controller;

import com.iitr.gl.userdetailservice.exception.UnauthorizedException;
import com.iitr.gl.userdetailservice.service.AdminService;
import com.iitr.gl.userdetailservice.service.PythonScriptService;
import com.iitr.gl.userdetailservice.service.XRayService;
import com.iitr.gl.userdetailservice.shared.AdminDashboardDto;
import com.iitr.gl.userdetailservice.shared.GenericDto;
import com.iitr.gl.userdetailservice.shared.PythonScriptDto;
import com.iitr.gl.userdetailservice.ui.model.*;
import com.iitr.gl.userdetailservice.util.ExecutePython;
import com.iitr.gl.userdetailservice.util.GetJwtSubject;
import com.iitr.gl.userdetailservice.util.PythonScriptActions;
import com.iitr.gl.userdetailservice.util.XRayActions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user_detail/admin/")
public class AdminController {

    @Autowired
    AdminService adminService;

    @Autowired
    Environment environment;

    @Autowired
    GetJwtSubject getJwtSubject;

    @Autowired
    PythonScriptService pythonScriptService;

    @Autowired
    PythonScriptActions pythonScriptActions;

    @Autowired
    XRayService XRayService;

    @Autowired
    XRayActions xRayActions;

    @Autowired
    ExecutePython executePython;

    @PostMapping("/upgradeUserToAdmin")
    public String upgradeUserToAdmin(@RequestBody GenericRequestModel requestModel, @RequestHeader("Authorization") String token)
            throws UnauthorizedException {
        getJwtSubject.verifyIfAuthorized(token, requestModel.getAdminId(), environment, true);
        GenericDto dto = new GenericDto();
        dto.setUserId(requestModel.getUserId());
        HttpStatus status = adminService.upgradeUserToAdmin(dto);
        if (status == HttpStatus.OK)
            return "Successfully Updated";
        else
            return "User not found";
    }

    @PostMapping("/listUsers")
    public List<AdminDashboardDto> listUsers(@RequestBody GenericRequestModel requestModel, @RequestHeader("Authorization") String token)
            throws UnauthorizedException {
        getJwtSubject.verifyIfAuthorized(token, requestModel.getAdminId(), environment, true);
        return adminService.listUsers();
    }

    @PostMapping("/deleteUser")
    ResponseEntity<String> deleteUser(@RequestBody GenericRequestModel requestModel, @RequestHeader("Authorization") String token)
            throws UnauthorizedException {
        getJwtSubject.verifyIfAuthorized(token, requestModel.getAdminId(), environment, true);
        HttpStatus httpStatus = adminService.deleteUser(requestModel.getUserId());

        if (HttpStatus.OK == httpStatus)
            return ResponseEntity.ok().body("Successfully deleted user");
        else if (HttpStatus.NOT_FOUND == httpStatus)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).
                    body("For given userId, no user found");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                body("");
    }


    @PostMapping("/uploadPythonScript")
    public ResponseEntity<UploadPythonScriptResponseModel> uploadPythonScript(@RequestBody UploadPythonScriptRequestModel requestModel,
                                                                              @RequestHeader("Authorization") String token) {
        getJwtSubject.verifyIfAuthorized(token, requestModel.getAdminId(), environment, true);
        return pythonScriptActions.uploadPythonScript(requestModel, pythonScriptService);
    }

    @PostMapping("/updatePythonScript")
    public ResponseEntity<String> updatePythonScript(@RequestBody UploadPythonScriptRequestModel requestModel,
                                                     @RequestHeader("Authorization") String token) {
        getJwtSubject.verifyIfAuthorized(token, requestModel.getAdminId(), environment, true);
        return pythonScriptActions.updatePythonScript(requestModel, pythonScriptService);
    }

    @PostMapping("/deletePythonScript")
    public ResponseEntity<String> deletePythonScript(@RequestBody GenericRequestModel requestModel,
                                                     @RequestHeader("Authorization") String token) {
        getJwtSubject.verifyIfAuthorized(token, requestModel.getAdminId(), environment, true);
        return pythonScriptActions.deletePythonScript(requestModel, pythonScriptService);
    }

    @PostMapping("/deleteAllPythonScript")
    public ResponseEntity<String> deleteAllPythonScript(@RequestBody GenericRequestModel requestModel,
                                                        @RequestHeader("Authorization") String token) {
        getJwtSubject.verifyIfAuthorized(token, requestModel.getAdminId(), environment, true);
        return pythonScriptActions.deleteAllPythonScript(requestModel, pythonScriptService);
    }

    @PostMapping("/downloadPythonScript")
    public ResponseEntity<ByteArrayResource> downloadPythonScript(@RequestBody GenericRequestModel genericRequestModel,
                                                                  @RequestHeader("Authorization") String token) {
        getJwtSubject.verifyIfAuthorized(token, genericRequestModel.getAdminId(), environment, true);
        return pythonScriptActions.downloadPythonScript(genericRequestModel, pythonScriptService);
    }

    @PostMapping("/viewPythonScript")
    public ResponseEntity<String> viewPythonScript(@RequestBody GenericRequestModel genericRequestModel,
                                                   @RequestHeader("Authorization") String token) {
        getJwtSubject.verifyIfAuthorized(token, genericRequestModel.getAdminId(), environment, true);
        return pythonScriptActions.viewPythonScript(genericRequestModel, pythonScriptService);
    }

    @PostMapping("/downloadXray")
    public ResponseEntity<ByteArrayResource> downloadXray(@RequestBody GenericRequestModel genericRequestModel,
                                                          @RequestHeader("Authorization") String token) {
        getJwtSubject.verifyIfAuthorized(token, genericRequestModel.getAdminId(), environment, true);
        return xRayActions.downloadXray(genericRequestModel, XRayService);
    }

    @PostMapping("/viewXray")
    public ResponseEntity<String> viewXray(@RequestBody GenericRequestModel genericRequestModel,
                                           @RequestHeader("Authorization") String token) {
        getJwtSubject.verifyIfAuthorized(token, genericRequestModel.getAdminId(), environment, true);
        return xRayActions.viewXray(genericRequestModel, XRayService);
    }

    @PostMapping("/uploadXray")
    public ResponseEntity<UploadXRayFileResponseModel> uploadXray(@RequestBody UploadXRayFileRequestModel requestModel,
                                                                  @RequestHeader("Authorization") String token) {
        getJwtSubject.verifyIfAuthorized(token, requestModel.getAdminId(), environment, true);
        return xRayActions.uploadXray(requestModel, XRayService);
    }

    @PostMapping("/deleteXray")
    public ResponseEntity<String> deleteXray(@RequestBody GenericRequestModel genericRequestModel,
                                             @RequestHeader("Authorization") String token) {
        getJwtSubject.verifyIfAuthorized(token, genericRequestModel.getAdminId(), environment, true);
        return xRayActions.deleteXray(genericRequestModel, XRayService);
    }

    @PostMapping("/deleteAllXray")
    public ResponseEntity<String> deleteAllXray(@RequestBody GenericRequestModel requestModel,
                                                @RequestHeader("Authorization") String token) {
        getJwtSubject.verifyIfAuthorized(token, requestModel.getAdminId(), environment, true);
        return xRayActions.deleteAllXray(requestModel, XRayService);
    }


    @PostMapping("/updateXray")
    public ResponseEntity<String> updateXray(@RequestBody UploadXRayFileRequestModel requestModel,
                                             @RequestHeader("Authorization") String token) {
        getJwtSubject.verifyIfAuthorized(token, requestModel.getAdminId(), environment, true);
        return xRayActions.updateXray(requestModel, XRayService);
    }

    @PostMapping("/listUserFiles")
    public ResponseEntity<ListUserFilesResponseModel> listUserFiles(@RequestBody GenericRequestModel requestModel,
                                                                    @RequestHeader("Authorization") String token) {
        getJwtSubject.verifyIfAuthorized(token, requestModel.getAdminId(), environment, true);
        ListUserFilesResponseModel listUserFilesResponseModel = XRayService.listUserFiles(requestModel.getUserId());
        if (listUserFilesResponseModel == null)
            listUserFilesResponseModel = new ListUserFilesResponseModel();
        listUserFilesResponseModel.setScripts(pythonScriptService.listUserFiles(requestModel.getUserId()));
        return ResponseEntity.ok().body(listUserFilesResponseModel);
    }

    @PostMapping(value = "/runPy", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Object> runPythonScript(@RequestBody PythonScriptRequestModel requestModel,
                                        @RequestHeader("Authorization") String token) {
        getJwtSubject.verifyIfAuthorized(token, requestModel.getAdminId(), environment, true);
        return executePython.runPythonScript(requestModel);

    }
}
