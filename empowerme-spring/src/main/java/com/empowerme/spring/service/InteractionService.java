package com.empowerme.spring.service;

import com.empowerme.spring.domain.CommentEntity;
import com.empowerme.spring.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class InteractionService {

    private final CommentRepository commentRepository;
    
    private static final Set<String> BANNED_WORDS = Set.of("scam", "spam", "fake", "bad");
    private static final String AUTO_REPLY_TRIGGER = "price";
    private static final String AUTO_REPLY_TEXT = "Thanks for asking! Check our bio for pricing details.";

    public InteractionService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public CommentEntity postComment(CommentEntity comment) {
        // 1. Check for Banned Words
        String lowerText = comment.getText().toLowerCase();
        for (String banned : BANNED_WORDS) {
            if (lowerText.contains(banned)) {
                comment.setIsFlagged(true);
                break; // Flag and stop checking
            }
        }
        
        // Save the user's comment
        CommentEntity saved = commentRepository.save(comment);

        // 2. Check for Auto-Reply
        if (!comment.getIsFlagged() && lowerText.contains(AUTO_REPLY_TRIGGER)) {
            CommentEntity autoReply = CommentEntity.builder()
                .campaignId(comment.getCampaignId())
                .author("EmpowerMe-Bot")
                .text(AUTO_REPLY_TEXT)
                .isFlagged(false)
                .build();
            commentRepository.save(autoReply);
        }

        return saved;
    }

    public List<CommentEntity> getCommentsForCampaign(Long campaignId) {
        return commentRepository.findByCampaignId(campaignId);
    }
}
