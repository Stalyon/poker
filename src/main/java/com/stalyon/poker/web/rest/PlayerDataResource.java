package com.stalyon.poker.web.rest;

import com.stalyon.poker.service.PlayerDataService;
import com.stalyon.poker.web.websocket.dto.PlayerDataDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller.
 */
@RestController
@RequestMapping("/api")
public class PlayerDataResource {

    @Autowired
    private PlayerDataService playerDataService;

    @GetMapping("/players-datas/search")
    public List<PlayerDataDto> get(String searchText) {
        return this.playerDataService.getPlayerDatas(searchText);
    }
}
