package com.empowerme.osgi.interaction.impl;

import com.empowerme.osgi.interaction.api.InteractionService;
import org.osgi.service.component.annotations.Component;

@Component(service = InteractionService.class)
public class InteractionServiceImpl implements InteractionService {

    @Override
    public void postComment(String campaignId, String text, String author) {
        System.out.println("OSGi Interaction: New comment for Campaign " + campaignId + " by " + author);
        
        if (text.toLowerCase().contains("scam") || text.toLowerCase().contains("spam")) {
            System.out.println("OSGi Interaction: [FLAGGED] Comment contains banned words.");
        } else {
            System.out.println("OSGi Interaction: Comment approved: " + text);
        }

        if (text.toLowerCase().contains("price")) {
             System.out.println("OSGi Interaction: [AUTO-REPLY] 'Check our bio for pricing details.'");
        }
    }
}
