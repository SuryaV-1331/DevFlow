package com.devflow.devflow.controller;

import com.devflow.devflow.dto.RequestCreateRequest;
import com.devflow.devflow.entity.Request;
import com.devflow.devflow.service.RequestService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
