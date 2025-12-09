package com.lovable.lovable_clone.entity;

import com.lovable.lovable_clone.enums.MessageRole;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMessage {
    Long id;
    ChatSession chatSession;
    String content;
    MessageRole role;
    String toolCalls;
    String toolCallId;
    Integer tokenUsed;
    Instant createdAt;

}
