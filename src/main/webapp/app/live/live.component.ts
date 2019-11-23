import {ChangeDetectorRef, Component, OnDestroy, OnInit} from '@angular/core';
import { Account } from 'app/core/user/account.model';
import {LiveService} from "app/live/live.service";
import {PlayerData} from "app/shared/model/player-data.model";

@Component({
  selector: 'jhi-live',
  templateUrl: './live.component.html',
  styleUrls: ['live.scss']
})
export class LiveComponent implements OnInit, OnDestroy {
  account: Account;
  games: Map<number, { gameName: number, players: PlayerData[] }> = new Map();
  displayedPlayers: PlayerData[] = [];
  currentGameId: number;

  constructor(private liveService: LiveService, private cdr: ChangeDetectorRef) {}

  ngOnInit() {
    this.liveService.subscribe();
    this.liveService.receive().subscribe((liveEvent) => {
      this.games.set(liveEvent.gameId, {gameName: liveEvent.gameName, players: liveEvent.players});

      if (this.games.size === 1) {
        this.displayedPlayers = this.games.get(liveEvent.gameId).players;
        this.currentGameId = liveEvent.gameId;
      } else if (this.currentGameId === liveEvent.gameId) {
        this.displayedPlayers = this.games.get(liveEvent.gameId).players;
      }
      this.cdr.detectChanges();
    });
  }

  ngOnDestroy() {
    this.liveService.unsubscribe();
  }

  onGameChange(event: any): void {
    this.displayedPlayers = this.games.get(Number(event.target.value)).players;
    this.currentGameId = Number(event.target.value);
  }
}
