package com.bitsclassmgmt.chatservice.controller;

import com.bitsclassmgmt.chatservice.dto.VoiceChannelDto;
import com.bitsclassmgmt.chatservice.request.chat.VoiceChannelCreateRequest;
import com.bitsclassmgmt.chatservice.service.VoiceChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/chat-service/voice-channels")
@RequiredArgsConstructor
public class VoiceChannelController {

    private final VoiceChannelService voiceChannelService;

    @PostMapping("/")
    public ResponseEntity<VoiceChannelDto> createVoiceChannel(@Valid @RequestBody VoiceChannelCreateRequest request) {
        return ResponseEntity.ok(voiceChannelService.createVoiceChannel(request));
    }

    @GetMapping("/active")
    public ResponseEntity<List<VoiceChannelDto>> getAllActiveVoiceChannels() {
        return ResponseEntity.ok(voiceChannelService.getAllActiveVoiceChannels());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VoiceChannelDto> getVoiceChannelById(@PathVariable String id) {
        return ResponseEntity.ok(voiceChannelService.getVoiceChannelById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVoiceChannel(@PathVariable String id) {
        voiceChannelService.deleteVoiceChannel(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/deactivate/{id}")
    public ResponseEntity<Void> deactivateVoiceChannel(@PathVariable String id) {
        voiceChannelService.deactivateVoiceChannel(id);
        return ResponseEntity.ok().build();
    }
}
