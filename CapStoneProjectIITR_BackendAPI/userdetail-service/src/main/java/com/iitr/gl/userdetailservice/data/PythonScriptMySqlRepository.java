package com.iitr.gl.userdetailservice.data;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface PythonScriptMySqlRepository extends JpaRepository<PythonScriptEntity, Long> {
    PythonScriptEntity findByScriptIdAndUserId(String scriptId, String userId);

    List<PythonScriptEntity> findByUserId(String userId);

    @Transactional
    void deleteByScriptId(String scriptId);

    @Transactional
    void deleteByUserId(String userId);
}
