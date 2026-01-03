package com.empowerme.osgi.interaction.api;

public interface InteractionService {
    void postComment(String campaignId, String text, String author);
    // Simple void for demo, printing logs to Karaf console
}
