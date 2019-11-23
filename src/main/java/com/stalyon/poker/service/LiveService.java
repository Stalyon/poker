package com.stalyon.poker.service;

import com.stalyon.poker.domain.*;
import com.stalyon.poker.repository.PlayerActionRepository;
import com.stalyon.poker.repository.PlayerDataRepository;
import com.stalyon.poker.service.mapper.PlayerMapper;
import com.stalyon.poker.web.dto.LiveEventDto;
import com.stalyon.poker.web.dto.PlayerDataDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class LiveService {

    private static final Logger log = LoggerFactory.getLogger(LiveService.class);
    private final SimpMessageSendingOperations messagingTemplate;
    @Value("${poker.me}")
    private String myName;
    @Autowired
    private PlayerActionRepository playerActionRepository;

    @Autowired
    private PlayerDataRepository playerDataRepository;

    @Autowired
    private PlayerMapper playerMapper;

    public LiveService(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendNotif(Game game, Hand hand) {
        LiveEventDto liveEventDto = new LiveEventDto();
        liveEventDto.setGameId(game.getId());
        liveEventDto.setGameName(game.getName());

        List<PlayerAction> playerActions = this.playerActionRepository.findAllByHand(hand);
        if (playerActions != null) {
            Set<Long> playerIds = playerActions.stream().map(PlayerAction::getPlayer).map(Player::getId).collect(Collectors.toSet());

            List<PlayerData> playerDatas = this.playerDataRepository.findAllById(playerIds);

            List<PlayerDataDto> playerDataDtos = this.playerMapper.playerToPlayerDataDtos(playerDatas);

            OptionalInt myPosition = IntStream.range(0, playerDatas.size())
                .filter(i -> playerDataDtos.get(i).getName().equals(this.myName))
                .findFirst();
            if (myPosition.isPresent()) {
                Collections.rotate(playerDatas, -myPosition.getAsInt());
            }

            liveEventDto.getPlayers().addAll(playerDataDtos);
        }

        log.info("Sending live notif");
        this.messagingTemplate.convertAndSend("/topic/live", liveEventDto);
    }
}
