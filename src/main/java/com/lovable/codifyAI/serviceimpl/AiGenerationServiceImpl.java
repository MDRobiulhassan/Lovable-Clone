package com.lovable.codifyAI.serviceimpl;

import com.lovable.codifyAI.llm.PromptUtils;
import com.lovable.codifyAI.security.AuthUtil;
import com.lovable.codifyAI.service.AiGenerationService;
import com.lovable.codifyAI.service.ProjectFileService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class AiGenerationServiceImpl implements AiGenerationService {

    private static final Pattern FILE_TAG_PATTERN = Pattern.compile("<file path=\"([^\"]+)\">([\\s\\S]*?)</file>", Pattern.DOTALL);
    private final ChatClient chatClient;
    private final AuthUtil authUtil;
    private final ProjectFileService projectFileService;

    @Override
    @PreAuthorize("@security.canEditProject(#projectId)")
    public Flux<@NonNull String> streamResponse(String userMessage, Long projectId) {
        Long userId = authUtil.getCurrentUserId();
        createChatSessionIfNotExists(projectId, userId);

        Map<String, Object> advisorParams = Map.of(
                "userId", userId,
                "projectId", projectId
        );

        StringBuilder fullResponseBuffer = new StringBuilder();

        return chatClient.prompt()
                .system(PromptUtils.CODE_GENERATION_SYSTEM_PROMPT)
                .user(userMessage)
                .advisors(
                        advisorSpec -> advisorSpec.params(advisorParams)
                )
                .stream()
                .chatResponse()
                .doOnNext(response -> {
                    String content = Objects.requireNonNull(response.getResult()).getOutput().getText();
                    fullResponseBuffer.append(content);
                })
                .doOnComplete(() -> Schedulers.boundedElastic().schedule(() -> parseAndSaveFile(fullResponseBuffer.toString(), projectId)))
                .doOnError(error -> log.error("Error while streaming chat response: {}", error.getMessage(), error))
                .mapNotNull(response -> Objects.requireNonNull(response.getResult()).getOutput().getText());
    }

    private void parseAndSaveFile(String fullResponse, Long projectId) {
        Matcher matcher = FILE_TAG_PATTERN.matcher(fullResponse);

        while (matcher.find()){
            String filePath = matcher.group(1);
            String fileContent = matcher.group(2);
            projectFileService.saveFile(projectId, filePath, fileContent);
        }
    }

    private void createChatSessionIfNotExists(Long projectId, Long userId) {
    }
}
