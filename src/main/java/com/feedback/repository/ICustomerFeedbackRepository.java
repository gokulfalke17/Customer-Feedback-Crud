package com.feedback.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.feedback.entity.CustomerFeedback;

@Repository
public interface ICustomerFeedbackRepository extends JpaRepository<CustomerFeedback, Integer> {

    //Page<CustomerFeedback> findByUserId(Integer id, Pageable pageable);
	Page<CustomerFeedback> findByUserIdOrderByDateTimeDesc(Integer userId, Pageable pageable);


    @Query("SELECT f FROM CustomerFeedback f WHERE f.feedback_id = :feedbackId AND f.user.id = :userId")
    Optional<CustomerFeedback> findByFeedbackIdAndUserId(@Param("feedbackId") Integer feedbackId,
                                                         @Param("userId") Integer userId);
}
