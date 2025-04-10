package com.bitsclassmgmt.chatservice.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.bitsclassmgmt.chatservice.client.ClassGroupServiceClient;
import com.bitsclassmgmt.chatservice.dto.ClassesDto;
import com.bitsclassmgmt.chatservice.dto.GroupsDto;
import com.bitsclassmgmt.chatservice.dto.VoiceChannelDto;
import com.bitsclassmgmt.chatservice.exc.NotFoundException;
import com.bitsclassmgmt.chatservice.model.VoiceChannel;
import com.bitsclassmgmt.chatservice.repository.VoiceChannelRepository;
import com.bitsclassmgmt.chatservice.request.chat.VoiceChannelCreateRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VoiceChannelService {
    
    private final VoiceChannelRepository voiceChannelRepository;
    private final ModelMapper modelMapper;
    private final ClassGroupServiceClient classGroupServiceClient;

    public VoiceChannelDto createVoiceChannel(VoiceChannelCreateRequest request) {
    	 // Validate class existence if provided
        if (request.getClassId() != null) {
            getClassById(request.getClassId());
        }

        // Validate group existence if provided
        if (request.getGroupId() != null) {
            getGroupById(request.getGroupId());
        }
        VoiceChannel voiceChannel = VoiceChannel.builder()
                .classId(request.getClassId())
                .groupId(request.getGroupId())
                .isActive(true)
                .build();
        
        return modelMapper.map(voiceChannelRepository.save(voiceChannel), VoiceChannelDto.class);
    }
    
    public ClassesDto getClassById(String id) {
        return Optional.ofNullable(classGroupServiceClient.getClassesById(id).getBody())
                .orElseThrow(() -> new NotFoundException("Class not found"));
    }

    public GroupsDto getGroupById(String id) {
        return Optional.ofNullable(classGroupServiceClient.getGroupsById(id).getBody())
                .orElseThrow(() -> new NotFoundException("Group not found"));
    }

    public List<VoiceChannelDto> getAllActiveVoiceChannels() {
        return voiceChannelRepository.findByIsActiveTrue()
                .stream()
                .map(channel -> modelMapper.map(channel, VoiceChannelDto.class))
                .collect(Collectors.toList());
    }

    public VoiceChannelDto getVoiceChannelById(String id) {
        VoiceChannel voiceChannel = voiceChannelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Voice Channel not found"));
        
        return modelMapper.map(voiceChannel, VoiceChannelDto.class);
    }

    public void deleteVoiceChannel(String id) {
        VoiceChannel voiceChannel = voiceChannelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Voice Channel not found"));
        
        voiceChannelRepository.delete(voiceChannel);
    }

    public void deactivateVoiceChannel(String id) {
        VoiceChannel voiceChannel = voiceChannelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Voice Channel not found"));
        
        voiceChannel.setActive(false);
        voiceChannelRepository.save(voiceChannel);
    }
}
