package com.iitr.gl.userdetailservice.ui.controller;

import com.iitr.gl.userdetailservice.data.ExpiredTokenMySqlRepository;
import com.iitr.gl.userdetailservice.service.PythonScriptService;
import com.iitr.gl.userdetailservice.service.SignoutServiceImpl;
import com.iitr.gl.userdetailservice.service.XRayService;
import com.iitr.gl.userdetailservice.shared.ExpiredTokenDto;
import com.iitr.gl.userdetailservice.ui.model.GenericRequestModel;
import com.iitr.gl.userdetailservice.ui.model.ListUserFilesResponseModel;
import com.iitr.gl.userdetailservice.util.GetJwtSubject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user_detail/user/")
public class UserDetailController {

    @Autowired
    XRayService xRayService;

    @Autowired
    PythonScriptService pythonScriptService;

    @Autowired
    Environment environment;

    @Autowired
    GetJwtSubject getJwtSubject;

    @Autowired
    SignoutServiceImpl signoutService;

    @PostMapping("/userFiles")
    public ResponseEntity<ListUserFilesResponseModel> listUserFiles(@RequestBody GenericRequestModel requestModel,
                                                                    @RequestHeader("Authorization") String token) {
        getJwtSubject.verifyIfAuthorized(token, requestModel.getUserId(), environment, false);
        ListUserFilesResponseModel listUserFilesResponseModel = xRayService.listUserFiles(requestModel.getUserId());
        if (listUserFilesResponseModel == null)
            listUserFilesResponseModel = new ListUserFilesResponseModel();
        listUserFilesResponseModel.setScripts(pythonScriptService.listUserFiles(requestModel.getUserId()));
        return ResponseEntity.ok().body(listUserFilesResponseModel);
    }

    @PostMapping("/signout")
    public ResponseEntity<String> signOut(@RequestBody GenericRequestModel requestModel, @RequestHeader("Authorization") String token)
    {
        getJwtSubject.verifyIfAuthorized(token, requestModel.getUserId(), environment, false);
        ExpiredTokenDto expiredTokenDto = new ExpiredTokenDto();
        expiredTokenDto.setUserId(requestModel.getUserId());
        expiredTokenDto.setToken(token);
        HttpStatus httpStatus = signoutService.signOut(expiredTokenDto);
        return ResponseEntity.status(httpStatus).body("Signed out");
    }
}
