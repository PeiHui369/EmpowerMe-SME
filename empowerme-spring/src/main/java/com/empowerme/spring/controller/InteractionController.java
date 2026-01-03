package com.empowerme.spring.controller;

import com.empowerme.spring.domain.CommentEntity;
import com.empowerme.spring.service.InteractionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class InteractionController {

    private final InteractionService interactionService;

    public InteractionController(InteractionService interactionService) {
        this.interactionService = interactionService;
    }

    @PostMapping
    public ResponseEntity<CommentEntity> postComment(@RequestBody CommentEntity comment) {
        return ResponseEntity.ok(interactionService.postComment(comment));
    }

    @GetMapping("/campaign/{campaignId}")
    public ResponseEntity<List<CommentEntity>> getComments(@PathVariable Long campaignId) {
        return ResponseEntity.ok(interactionService.getCommentsForCampaign(campaignId));
    }
}
