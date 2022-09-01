package com.iitr.gl.userdetailservice.data;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface XRayDetailMySqlRepository extends JpaRepository<XRayDetailEntity, Long> {
    XRayDetailEntity findByXrayIdAndUserId(String xrayId, String userId);

    void deleteByXrayId(String xrayId);

    List<XRayDetailEntity> findByUserId(String userId);

    @Transactional
    void deleteByUserId(String userId);
}
