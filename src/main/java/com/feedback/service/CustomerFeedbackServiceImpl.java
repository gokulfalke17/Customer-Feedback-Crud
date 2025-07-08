package com.feedback.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.feedback.entity.CustomerFeedback;
import com.feedback.repository.ICustomerFeedbackRepository;

@Service
public class CustomerFeedbackServiceImpl implements ICustomerFeedbackService {

    @Autowired
    private ICustomerFeedbackRepository feedbackRepository;

    @Override
    public CustomerFeedback addFeedback(CustomerFeedback feedback) {
        return feedbackRepository.save(feedback);
    }

    @Override
    public List<CustomerFeedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    @Override
    public Page<CustomerFeedback> getFeedbacksByUserPaged(Integer id, Pageable pageable) {
        return feedbackRepository.findByUserIdOrderByDateTimeDesc(id, pageable);
    }

    @Override
    public CustomerFeedback getFeedback(Integer feedback_id) {
        return feedbackRepository.findById(feedback_id).orElse(null);
    }

    @Override
    public CustomerFeedback editFeedback(Integer feedback_id, CustomerFeedback newFeedback) {
        CustomerFeedback feedback = getFeedback(feedback_id);
        if (feedback != null) {
            feedback.setWrite_feedback(newFeedback.getWrite_feedback());
            feedback.setDateTime(LocalDateTime.now());
            return feedbackRepository.save(feedback);
        }
        return null;
    }

    @Override
    public void deleteFeedback(Integer feedback_id) {
        feedbackRepository.deleteById(feedback_id);
    }

    @Override
    public CustomerFeedback getFeedbackByUser(Integer fid, Integer uid) {
        return feedbackRepository.findByFeedbackIdAndUserId(fid, uid).orElse(null);
    }
}
