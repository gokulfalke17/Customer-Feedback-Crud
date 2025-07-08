package com.feedback.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.feedback.entity.CustomerFeedback;
import com.feedback.entity.Register;
import com.feedback.security.CustomUserDetails;
import com.feedback.service.ICustomerFeedbackService;
import com.feedback.service.IRegisterService;

@Controller
public class CustomerFeedbackController {

    @Autowired
    private IRegisterService registerService;

    @Autowired
    private ICustomerFeedbackService feedbackService;

    // Home page
    @GetMapping("/")
    public String getHomePage() {
        return "home";
    }

    // Register
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("register", new Register());
        return "register";
    }

    @PostMapping("/register")
    public String handleRegister(@ModelAttribute("register") Register register, RedirectAttributes attributes) {
        registerService.register(register);
        attributes.addAttribute("data", "Registration Successful!");
        return "redirect:/login";
    }

    // Custom login page
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    // Dashboard (secured)
    @GetMapping("/dashboard")
    public String showDashboard(Authentication authentication, Model model) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("loginUser", userDetails);
        return "dashboard";
    }

    // Add feedback form
    @GetMapping("/feedback/add")
    public String showFeedbackForm(Model model) {
        model.addAttribute("feedback", new CustomerFeedback());
        return "add-feedback";
    }

    @PostMapping("/feedback/save")
    public String saveFeedback(@ModelAttribute("feedback") CustomerFeedback feedback,
                               Authentication authentication,
                               RedirectAttributes attributes) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        feedback.setUser(new Register());
        feedback.getUser().setId(userDetails.getId());
        feedback.setDateTime(LocalDateTime.now());

        feedbackService.addFeedback(feedback);
        attributes.addFlashAttribute("success", "Feedback submitted successfully!");
        return "redirect:/feedback/all";
    }

    // Show feedback list
	/*@GetMapping("/feedback/all")
	public String feedbackList(Model model, Authentication authentication) {
	    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
	
	    List<CustomerFeedback> feedbacks = feedbackService.getFeedbacksId(userDetails.getId());
	    model.addAttribute("loginUser", userDetails);
	    model.addAttribute("feedbacks", feedbacks);
	
	    return "show-feedbacks";
	}*/
    
    @GetMapping("/feedback/all")
    public String feedbackList(Model model,
                               Authentication authentication,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "3") int size) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("dateTime").descending());
        Page<CustomerFeedback> feedbackPage = feedbackService.getFeedbacksByUserPaged(userDetails.getId(), pageable);

        model.addAttribute("loginUser", userDetails);
        model.addAttribute("feedbackPage", feedbackPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", feedbackPage.getTotalPages());

        return "show-feedbacks";
    }

    // Edit form
    @GetMapping("feedback/edit/{feedback_id}")
    public String showEditForm(@PathVariable("feedback_id") Integer feedback_id,
                               Model model,
                               Authentication authentication) {

        CustomerFeedback feedback = feedbackService.getFeedback(feedback_id);
        model.addAttribute("loginUser", authentication.getPrincipal());
        model.addAttribute("feedback", feedback);

        return "edit-feedback";
    }

    @PostMapping("feedback/update/{feedback_id}")
    public String editFeedback(@PathVariable("feedback_id") Integer feedback_id,
                               @ModelAttribute("newFeedback") CustomerFeedback newFeedback,
                               RedirectAttributes attributes) {

        feedbackService.editFeedback(feedback_id, newFeedback);
        attributes.addFlashAttribute("success", "Feedback updated successfully!");
        return "redirect:/feedback/all";
    }

    // Delete feedback
    @GetMapping("/feedback/delete/{feedback_id}")
    public String deleteFeedback(@PathVariable("feedback_id") Integer feedback_id,
                                 RedirectAttributes attributes) {

        feedbackService.deleteFeedback(feedback_id);
        attributes.addFlashAttribute("success", "Feedback deleted successfully!");
        return "redirect:/feedback/all";
    }

    // View feedback details 
    @GetMapping("/feedback/detail/{id}")
    @ResponseBody
    public CustomerFeedback feedbackDetail(@PathVariable Integer id, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return feedbackService.getFeedbackByUser(id, userDetails.getId());
    }

}
