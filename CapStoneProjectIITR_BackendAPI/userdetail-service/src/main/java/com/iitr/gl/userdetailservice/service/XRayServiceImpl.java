package com.iitr.gl.userdetailservice.service;

import com.iitr.gl.userdetailservice.data.*;
import com.iitr.gl.userdetailservice.shared.DownloadFileDto;
import com.iitr.gl.userdetailservice.shared.GenericDto;
import com.iitr.gl.userdetailservice.shared.UploadFileDto;
import com.iitr.gl.userdetailservice.ui.model.ListUserFilesResponseModel;
import com.iitr.gl.userdetailservice.ui.model.XRayFileModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class XRayServiceImpl implements XRayService {
    @Autowired
    private PneumoniaXRayMongoDBRepository pneumoniaXRayMongoDBRepository;
    @Autowired
    private TuberculosisXRayMongoDBRepository tuberculosisXRayMongoDBRepository;
    @Autowired
    private XRayDetailMySqlRepository xRayDetailMySqlRepository;

    @Override
    public DownloadFileDto downloadXRay(DownloadFileDto downloadFileDto) {

        XRayDetailEntity xRayDetailEntity = xRayDetailMySqlRepository.findByXrayIdAndUserId(downloadFileDto.getXrayId(), downloadFileDto.getUserId());
        DownloadFileDto responseDto = new DownloadFileDto();
        if (xRayDetailEntity != null) {
            if (xRayDetailEntity.getXrayType().equalsIgnoreCase("pneumonia")) {
                PneumoniaXRayDocument pneumoniaXRayDocument = pneumoniaXRayMongoDBRepository.findByXrayId(downloadFileDto.getXrayId());
                responseDto.setFile(pneumoniaXRayDocument.getData());
                responseDto.setFilename(pneumoniaXRayDocument.getFilename());
                responseDto.setXrayId(pneumoniaXRayDocument.getXrayId());
            } else if (xRayDetailEntity.getXrayType().equalsIgnoreCase("tuberculosis")) {
                TuberculosisXRayDocument tuberculosisXRayDocument = tuberculosisXRayMongoDBRepository.findByxrayId(downloadFileDto.getXrayId());
                responseDto.setFile(tuberculosisXRayDocument.getData());
                responseDto.setFilename(tuberculosisXRayDocument.getFilename());
                responseDto.setXrayId(tuberculosisXRayDocument.getXrayId());
            }

            return responseDto;
        } else {
            responseDto.setErrorMessage("For given userId, xrayId, no xray is present");
            responseDto.setFile(null);
            return responseDto;
        }
    }

    @Override
    public void uploadXRay(UploadFileDto uploadFileDto) {
        XRayDetailEntity xRayDetailEntity = new XRayDetailEntity();
        xRayDetailEntity.setUserId(uploadFileDto.getUserId());
        xRayDetailEntity.setXrayType(uploadFileDto.getXrayType());
        xRayDetailEntity.setXrayId(uploadFileDto.getXrayId());
        if (xRayDetailEntity.getXrayType().equalsIgnoreCase("pneumonia")) {
            PneumoniaXRayDocument xrayDocumentPneumonia = new PneumoniaXRayDocument();
            xrayDocumentPneumonia.setXrayId(uploadFileDto.getXrayId());
            xrayDocumentPneumonia.setHaspneumonia("Processing");
            xrayDocumentPneumonia.setFilename(uploadFileDto.getFileName());
            xrayDocumentPneumonia.setData(Base64Utils.decodeFromString(uploadFileDto.getFileData()));
            pneumoniaXRayMongoDBRepository.save(xrayDocumentPneumonia);
        } else if (xRayDetailEntity.getXrayType().equalsIgnoreCase("tuberculosis")) {
            TuberculosisXRayDocument tuberculosisXRayDocument = new TuberculosisXRayDocument();
            tuberculosisXRayDocument.setXrayId(uploadFileDto.getXrayId());
            tuberculosisXRayDocument.setHastb("Processing");
            tuberculosisXRayDocument.setFilename(uploadFileDto.getFileName());
            tuberculosisXRayDocument.setData(Base64Utils.decodeFromString(uploadFileDto.getFileData()));
            tuberculosisXRayMongoDBRepository.save(tuberculosisXRayDocument);
        }

        xRayDetailMySqlRepository.save(xRayDetailEntity);
    }

    @Override
    @Transactional
    public HttpStatus deleteXRay(GenericDto genericDto) {
        XRayDetailEntity xRayDetailEntity = xRayDetailMySqlRepository.findByXrayIdAndUserId(genericDto.getXrayId(), genericDto.getUserId());

        if (xRayDetailEntity != null) {
            if ((xRayDetailEntity.getXrayType().equalsIgnoreCase("pneumonia")))
                pneumoniaXRayMongoDBRepository.deleteByXrayId(xRayDetailEntity.getXrayId());
            else if (xRayDetailEntity.getXrayType().equalsIgnoreCase("tuberculosis"))
                tuberculosisXRayMongoDBRepository.deleteByXrayId(xRayDetailEntity.getXrayId());
            xRayDetailMySqlRepository.deleteByXrayId(xRayDetailEntity.getXrayId());
            return HttpStatus.OK;
        }

        return HttpStatus.NOT_FOUND;

    }

    @Override
    public HttpStatus deleteAllXRay(String userId) {
        List<XRayDetailEntity> xRayDetailEntityList = xRayDetailMySqlRepository.findByUserId(userId);

        if (xRayDetailEntityList != null) {
            List<String> pneumoniaXrayIds = new ArrayList<>();
            List<String> tuberculosisXrayIds = new ArrayList<>();
            xRayDetailEntityList.forEach(xRayDetailEntity -> {
                if (xRayDetailEntity.getXrayType().equalsIgnoreCase("pneumonia"))
                    pneumoniaXrayIds.add(xRayDetailEntity.getXrayId());
                else if (xRayDetailEntity.getXrayType().equalsIgnoreCase("tuberculosis"))
                    tuberculosisXrayIds.add(xRayDetailEntity.getXrayId());
            });

            if (!pneumoniaXrayIds.isEmpty())
                pneumoniaXRayMongoDBRepository.deleteAllUsingXrayId(pneumoniaXrayIds);
            if (!tuberculosisXrayIds.isEmpty())
                tuberculosisXRayMongoDBRepository.deleteAllUsingXrayId(tuberculosisXrayIds);

            return HttpStatus.OK;
        }

        return HttpStatus.NOT_FOUND;

    }


    @Override
    public HttpStatus updateXRay(UploadFileDto fileDto) {
        XRayDetailEntity xRayDetailEntity = xRayDetailMySqlRepository.findByXrayIdAndUserId(fileDto.getXrayId(), fileDto.getUserId());

        if (xRayDetailEntity != null) {
            if ((xRayDetailEntity.getXrayType().equalsIgnoreCase("pneumonia"))) {
                PneumoniaXRayDocument document = pneumoniaXRayMongoDBRepository.findByXrayId(fileDto.getXrayId());
                document.setFilename(fileDto.getFileName());
                document.setData(Base64Utils.decodeFromString(fileDto.getFileData()));
                document.setHaspneumonia("Processing");
                pneumoniaXRayMongoDBRepository.save(document);
            } else if ((xRayDetailEntity.getXrayType().equalsIgnoreCase("tuberculosis"))) {
                TuberculosisXRayDocument document = tuberculosisXRayMongoDBRepository.findByxrayId(fileDto.getXrayId());
                document.setFilename(fileDto.getFileName());
                document.setData(Base64Utils.decodeFromString(fileDto.getFileData()));
                document.setHastb("Processing");
                tuberculosisXRayMongoDBRepository.save(document);
            }
            return HttpStatus.OK;
        }

        return HttpStatus.NOT_FOUND;
    }

    @Override
    public ListUserFilesResponseModel listUserFiles(String userId) {
        List<XRayDetailEntity> xRayDetailEntityList = xRayDetailMySqlRepository.findByUserId(userId);

        if (xRayDetailEntityList != null) {
            List<String> tuberculosisXrayIds = new ArrayList<>();
            List<String> pneumoniaXrayIds = new ArrayList<>();

            xRayDetailEntityList.forEach((entity) -> {
                if (entity.getXrayType().equalsIgnoreCase("pneumonia"))
                    pneumoniaXrayIds.add(entity.getXrayId());
                else if (entity.getXrayType().equalsIgnoreCase("tuberculosis"))
                    tuberculosisXrayIds.add(entity.getXrayId());
            });

            List<PneumoniaXRayDocument> pneumoniaXRayDocuments = null;
            List<TuberculosisXRayDocument> tuberculosisXRayDocuments = null;

            if (!pneumoniaXrayIds.isEmpty())
                pneumoniaXRayDocuments = pneumoniaXRayMongoDBRepository.findAllUsingXrayId(pneumoniaXrayIds);
            if (!tuberculosisXrayIds.isEmpty())
                tuberculosisXRayDocuments = tuberculosisXRayMongoDBRepository.findAllUsingXrayId(pneumoniaXrayIds);

            ListUserFilesResponseModel listUserFilesResponseModel = new ListUserFilesResponseModel();

            if (pneumoniaXRayDocuments != null && !pneumoniaXRayDocuments.isEmpty()) {
                List<XRayFileModel> pneumoniaXRayFileModelList = new ArrayList<>();
                pneumoniaXRayDocuments.forEach(pneumoniaXRayDocument -> {
                    XRayFileModel xRayFileModel = new XRayFileModel();
                    xRayFileModel.setFileData(Base64Utils.encodeToString(pneumoniaXRayDocument.getData()));
                    xRayFileModel.setFileName(pneumoniaXRayDocument.getFilename());
                    xRayFileModel.setXrayId(pneumoniaXRayDocument.getXrayId());
                    xRayFileModel.setTestResult(pneumoniaXRayDocument.getHaspneumonia());
                    pneumoniaXRayFileModelList.add(xRayFileModel);
                });
                listUserFilesResponseModel.setPneumoniaXray(pneumoniaXRayFileModelList);
            }

            if (tuberculosisXRayDocuments != null && !tuberculosisXRayDocuments.isEmpty()) {
                List<XRayFileModel> tuberculosisXRayFileModelList = new ArrayList<>();
                tuberculosisXRayDocuments.forEach(tuberculosisXRayDocument -> {
                    XRayFileModel xRayFileModel = new XRayFileModel();
                    xRayFileModel.setFileData(Base64Utils.encodeToString(tuberculosisXRayDocument.getData()));
                    xRayFileModel.setFileName(tuberculosisXRayDocument.getFilename());
                    xRayFileModel.setXrayId(tuberculosisXRayDocument.getXrayId());
                    xRayFileModel.setTestResult(tuberculosisXRayDocument.getHastb());
                    tuberculosisXRayFileModelList.add(xRayFileModel);
                });
                listUserFilesResponseModel.setTuberculosisXray(tuberculosisXRayFileModelList);
            }
            return listUserFilesResponseModel;
        }
        return null;
    }
}
