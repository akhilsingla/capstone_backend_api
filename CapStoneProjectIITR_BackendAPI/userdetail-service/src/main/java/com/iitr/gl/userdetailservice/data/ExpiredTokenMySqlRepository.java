package com.iitr.gl.userdetailservice.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpiredTokenMySqlRepository extends JpaRepository<ExpiredToken, Long> {
}
