package com.devflow.devflow.controller;

import com.devflow.devflow.dto.RequestCreateRequest;
import com.devflow.devflow.dto.RequestResponse;
import com.devflow.devflow.entity.Request;
import com.devflow.devflow.service.RequestService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/requests")
public class RequestController {

    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    public Request createRequest(
            @RequestBody RequestCreateRequest request) {

        return requestService.createRequest(request);
    }

    @PostMapping("/{requestId}/approve")
    public Request approveRequest(
            @PathVariable Long requestId) {

        return requestService
                .approveRequest(requestId);
    }

    @GetMapping("/my-requests")
    public List<RequestResponse> getMyRequests() {

        return requestService
                .getMyRequests();
    }

    @GetMapping
    public List<RequestResponse> getAllRequests() {

        return requestService.getAllRequests();
    }

    @GetMapping("/pending")
    public List<RequestResponse> getPendingRequests() {

        return requestService.getPendingRequests();
    }
}
