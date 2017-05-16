package com.wrewolf.thetaleclient.service.watcher;

import com.wrewolf.thetaleclient.api.response.GameInfoResponse;

/**
 * @author Hamster
 * @since 17.10.2014
 */
public interface GameStateWatcher {

    void processGameState(GameInfoResponse gameInfoResponse);

}
