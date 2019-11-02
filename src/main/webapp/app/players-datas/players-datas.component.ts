import {Component} from '@angular/core';
import {PlayerData} from "app/shared/model/player-data.model";
import {PlayersDatasService} from "app/players-datas/players-datas.service";
import {FormBuilder, Validators} from "@angular/forms";
import {HttpResponse} from "@angular/common/http";
import {JhiAlertService} from "ng-jhipster";

@Component({
  selector: 'jhi-players-datas',
  templateUrl: './players-datas.component.html',
  styleUrls: ['players-datas.scss']
})
export class PlayersDatasComponent {
  players: PlayerData[] = [];

  editForm = this.fb.group({
    searchText: ['', [Validators.maxLength(50)]]
  });

  constructor(private playersDataService: PlayersDatasService, private fb: FormBuilder,
              private alertService: JhiAlertService) {}

  search(): void {
    this.playersDataService.search(this.editForm.get(['searchText']).value)
      .subscribe((res: HttpResponse<PlayerData[]>) => this.onSuccess(res.body, res.headers),
        (res: HttpResponse<any>) => this.onError(res.body));
  }

  private onSuccess(data, headers) {
    this.players = data;
  }

  private onError(error) {
    this.alertService.error(error.error, error.message, null);
  }
}
