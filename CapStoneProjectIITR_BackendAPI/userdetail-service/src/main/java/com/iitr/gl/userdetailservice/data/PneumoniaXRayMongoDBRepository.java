package com.iitr.gl.userdetailservice.data;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PneumoniaXRayMongoDBRepository extends MongoRepository<PneumoniaXRayDocument, String> {
    PneumoniaXRayDocument findByXrayId(String xrayId);

    void deleteByXrayId(String xrayId);

    @Query(value = "{'xrayid' : {$in: ?0} }")
    List<PneumoniaXRayDocument> findAllUsingXrayId(List<String> xrayids);

    @Query(value = "{'xrayid' : {$in: ?0} }", delete = true)
    void deleteAllUsingXrayId(List<String> xrayids);

}
