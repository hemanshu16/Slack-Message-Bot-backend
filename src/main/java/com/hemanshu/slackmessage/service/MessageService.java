package com.hemanshu.slackmessage.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hemanshu.slackmessage.dto.Preferences;
import com.hemanshu.slackmessage.dto.UserDetailsDTO;
import com.hemanshu.slackmessage.exception.BadRequestException;
import com.hemanshu.slackmessage.exception.NotFoundException;
import com.hemanshu.slackmessage.models.UserDetails;
import com.hemanshu.slackmessage.repository.UserDetailsRepository;
import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.model.Conversation;
import com.slack.api.model.Message;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

@Service
public class MessageService {

    @Value("${SLACK_BOT_TOKEN}")
    private String SLACK_TOKEN;

    private final UserDetailsRepository userDetailsRepository;

    private final ObjectMapper mapper = new ObjectMapper();

    public MessageService(UserDetailsRepository userDetailsRepository) {
        this.userDetailsRepository = userDetailsRepository;
    }

    public String findChannelId(String name) {
        // you can get this instance via ctx.client() in a Bolt app
        var client = Slack.getInstance().methods();
        var logger = LoggerFactory.getLogger("my-awesome-slack-app");
        String channelId = null;
        try {
            // Call the conversations.list method using the built-in WebClient
            var result = client.conversationsList(r -> r
                    // The token you used to initialize your app
                    .token(SLACK_TOKEN)
            );

            for (Conversation channel : result.getChannels()) {
                if (channel.getName().equals(name)) {
                    channelId = channel.getId();
                    // Print result

                    logger.info("Found conversation ID: {}", channelId);
                    // Break from for loop
                    break;
                }
            }
        } catch (IOException | SlackApiException e) {
            logger.error("error: {}", e.getMessage(), e);
        }
        return channelId;
    }

    public void sendMessage(String channelName, UserDetailsDTO userDetails)  {


        String channelId = findChannelId(channelName);

        if(channelId == null) {
            throw new BadRequestException("ChannelName not exist");
        }

        Optional<UserDetails> byPhoneNumber = userDetailsRepository.findByPhoneNumberAndSuccessfullySentIsTrue(userDetails.getPhoneNumber());

        if(byPhoneNumber.isPresent()) {
            throw new BadRequestException("You have already fill the form details");
        }

        UserDetails userDetails1 = UserDetails.builder()
                .name(userDetails.getName())
                .phoneNumber(userDetails.getPhoneNumber())
                .address(userDetails.getAddress())
                .gender(userDetails.getGender())
                .newsLetter(userDetails.getPreferences().isNewsLetter())
                .updates(userDetails.getPreferences().isUpdates())
                .build();

        var client = Slack.getInstance().methods();
        var logger = LoggerFactory.getLogger("my-awesome-slack-app");
        String message = userDetails.toString();

        try {
            // Call the chat.postMessage method using the built-in WebClient
            String finalMessage = message;
            var result = client.chatPostMessage(r -> r
                            // The token you used to initialize your app
                            .token(SLACK_TOKEN)
                            .channel(channelId)
                            .text(finalMessage)
                    // You could also use a blocks[] array to send richer content
            );
            userDetails1.setSuccessfullySent(true);

            // Print result, which includes information about the message (like TS)
            logger.info("result {}", result);
        } catch (IOException | SlackApiException e) {
            userDetails1.setSuccessfullySent(false);

            logger.error("error: {}", e.getMessage(), e);
            return;
        }

        userDetailsRepository.save(userDetails1);

    }

    /**
     * Fetch conversation history using ID from last example
     */
    public List<String> fetchHistory(String channelName) {

        String channelId = findChannelId(channelName);

        if(channelId == null) {
            return new ArrayList<>();
        }

        Optional<List<Message>> conversationHistory = Optional.empty();
        // you can get this instance via ctx.client() in a Bolt app
        var client = Slack.getInstance().methods();
        var logger = LoggerFactory.getLogger("my-awesome-slack-app");
        try {
            // Call the conversations.history method using the built-in WebClient
            var result = client.conversationsHistory(r -> r
                    // The token you used to initialize your app
                    .token(SLACK_TOKEN)
                    .channel(channelId)
            );
            conversationHistory = Optional.ofNullable(result.getMessages());
            // Print results
            logger.info("{} messages found in {}", conversationHistory.orElse(emptyList()).size(), channelId);
        } catch (IOException | SlackApiException e) {
            logger.error("error: {}", e.getMessage(), e);
        }
        if(conversationHistory.isPresent())
        {
            return conversationHistory.get().stream().map(message -> message.getText()).toList();
        }
        return new ArrayList<>();
    }

    public UserDetailsDTO getSavedFormDetails(String phoneNumber)
    {
        Optional<UserDetails> byPhoneNumber = userDetailsRepository.findByPhoneNumberAndSuccessfullySentIsTrue(phoneNumber);
        if(byPhoneNumber.isPresent()) {
            UserDetails userDetails = byPhoneNumber.get();
            return UserDetailsDTO.builder()
                    .name(userDetails.getName())
                    .phoneNumber(userDetails.getPhoneNumber())
                    .gender(userDetails.getGender())
                    .address(userDetails.getAddress())
                    .preferences(Preferences.builder()
                            .newsLetter(userDetails.isNewsLetter())
                            .updates(userDetails.isUpdates())
                            .build())
                    .build();
        }
        else{
            throw new NotFoundException("You have not Submitted form details");
        }
    }

}
