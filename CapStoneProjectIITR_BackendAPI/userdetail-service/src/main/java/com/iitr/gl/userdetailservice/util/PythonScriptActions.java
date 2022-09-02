package com.iitr.gl.userdetailservice.util;

import com.iitr.gl.userdetailservice.service.PythonScriptService;
import com.iitr.gl.userdetailservice.shared.DownloadFileDto;
import com.iitr.gl.userdetailservice.shared.GenericDto;
import com.iitr.gl.userdetailservice.shared.UploadFileDto;
import com.iitr.gl.userdetailservice.ui.model.GenericRequestModel;
import com.iitr.gl.userdetailservice.ui.model.UploadPythonScriptRequestModel;
import com.iitr.gl.userdetailservice.ui.model.UploadPythonScriptResponseModel;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import java.util.UUID;

@Component
public class PythonScriptActions {
    public ResponseEntity<UploadPythonScriptResponseModel> uploadPythonScript(UploadPythonScriptRequestModel requestModel,
                                                                              PythonScriptService pythonScriptService) {
        UploadFileDto fileDto = new UploadFileDto();
        fileDto.setFileName(requestModel.getFileName());
        fileDto.setFileData(requestModel.getData());
        fileDto.setScriptId(UUID.randomUUID().toString());
        fileDto.setUserId(requestModel.getUserId());
        pythonScriptService.uploadPythonScript(fileDto);
        UploadPythonScriptResponseModel response = new UploadPythonScriptResponseModel();
        response.setMessage("script successfully saved");
        response.setScriptId(fileDto.getScriptId());
        return ResponseEntity.status(HttpStatus.OK).
                body(response);
    }

    public ResponseEntity<String> updatePythonScript(UploadPythonScriptRequestModel requestModel,
                                                     PythonScriptService pythonScriptService) {
        UploadFileDto fileDto = new UploadFileDto();
        fileDto.setFileName(requestModel.getFileName());
        fileDto.setFileData(requestModel.getData());
        fileDto.setScriptId(requestModel.getScriptId());
        fileDto.setUserId(requestModel.getUserId());
        HttpStatus httpStatus = pythonScriptService.updatePythonScript(fileDto);
        if (HttpStatus.OK == httpStatus)
            return ResponseEntity.status(HttpStatus.OK).
                    body("Successfully Updated");
        else if (HttpStatus.NOT_FOUND == httpStatus)
            return ResponseEntity.status(HttpStatus.OK).
                    body("Python script with given scriptId/userId is not found");
        return null;
    }

    public ResponseEntity<String> deletePythonScript(GenericRequestModel requestModel,
                                                     PythonScriptService pythonScriptService) {
        GenericDto dto = new GenericDto();
        dto.setScriptId(requestModel.getScriptId());
        HttpStatus httpStatus = pythonScriptService.deletePythonScript(dto);
        if (HttpStatus.OK == httpStatus)
            return ResponseEntity.status(HttpStatus.OK).
                    body("Successfully deleted");
        else if (HttpStatus.NOT_FOUND == httpStatus)
            return ResponseEntity.status(HttpStatus.OK).
                    body("Python script with given scriptId/userId is not found");
        return null;
    }

    public ResponseEntity<String> deleteAllPythonScript(GenericRequestModel requestModel,
                                                        PythonScriptService pythonScriptService) {
        HttpStatus httpStatus = pythonScriptService.deleteAllPythonScript(requestModel.getUserId());
        if (HttpStatus.OK == httpStatus)
            return ResponseEntity.status(HttpStatus.OK).
                    body("Successfully deleted");
        else if (HttpStatus.NOT_FOUND == httpStatus)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).
                    body("Python scripts with given userId are not found");
        return null;
    }

    public ResponseEntity<ByteArrayResource> downloadPythonScript(GenericRequestModel genericRequestModel,
                                                                  PythonScriptService pythonScriptService) {
        DownloadFileDto downloadFileDto = new DownloadFileDto();
        downloadFileDto.setScriptId(genericRequestModel.getScriptId());
        downloadFileDto.setUserId(genericRequestModel.getUserId());
        DownloadFileDto file = pythonScriptService.downloadPythonScript(downloadFileDto);
        if (file.getFile() != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("text/plain"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                    .body(new ByteArrayResource(file.getFile()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    public ResponseEntity<String> viewPythonScript(GenericRequestModel genericRequestModel,
                                                   PythonScriptService pythonScriptService) {
        DownloadFileDto downloadFileDto = new DownloadFileDto();
        downloadFileDto.setScriptId(genericRequestModel.getScriptId());
        downloadFileDto.setUserId(genericRequestModel.getUserId());
        DownloadFileDto file = pythonScriptService.downloadPythonScript(downloadFileDto);
        if (file.getFile() != null) {
            return ResponseEntity.ok()
                    .body(Base64Utils.encodeToString(file.getFile()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(file.getErrorMessage());
        }
    }
}
