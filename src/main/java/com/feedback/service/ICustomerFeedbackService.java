package com.feedback.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.feedback.entity.CustomerFeedback;

public interface ICustomerFeedbackService {
    CustomerFeedback addFeedback(CustomerFeedback feedback);
    List<CustomerFeedback> getAllFeedbacks();
    Page<CustomerFeedback> getFeedbacksByUserPaged(Integer id, Pageable pageable);
    CustomerFeedback getFeedback(Integer feedback_id);
    CustomerFeedback editFeedback(Integer feedback_id, CustomerFeedback newFeedback);
    void deleteFeedback(Integer feedback_id);
    CustomerFeedback getFeedbackByUser(Integer fid, Integer uid);
}
