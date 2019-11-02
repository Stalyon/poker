package com.stalyon.poker.service;

import com.stalyon.poker.domain.Game;
import com.stalyon.poker.domain.Hand;
import com.stalyon.poker.domain.Player;
import com.stalyon.poker.domain.PlayerAction;
import com.stalyon.poker.repository.PlayerActionRepository;
import com.stalyon.poker.service.mapper.PlayerMapper;
import com.stalyon.poker.web.websocket.dto.LiveEventDto;
import com.stalyon.poker.web.websocket.dto.PlayerDataDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class LiveService {

    private static final Logger log = LoggerFactory.getLogger(LiveService.class);

    private final SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private PlayerActionRepository playerActionRepository;

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
            Set<Player> players = playerActions.stream().map(PlayerAction::getPlayer).collect(Collectors.toSet());

            List<PlayerDataDto> playerDatas = this.playerMapper.playerToPlayerDataDtos(new ArrayList<>(players));

            OptionalInt myPosition = IntStream.range(0, playerDatas.size())
                .filter(i -> playerDatas.get(i).getMe() == Boolean.TRUE)
                .findFirst();
            if (myPosition.isPresent()) {
                Collections.rotate(playerDatas, -myPosition.getAsInt());
            }

            liveEventDto.getPlayers().addAll(playerDatas);
        }

        log.info("Sending live notif");
        this.messagingTemplate.convertAndSend("/topic/live", liveEventDto);
    }
}
