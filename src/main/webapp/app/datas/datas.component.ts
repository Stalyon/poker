import {Component} from '@angular/core';
import {DatasService} from "app/datas/datas.service";
import {JhiAlertService} from "ng-jhipster";

@Component({
  selector: 'jhi-datas',
  templateUrl: './datas.component.html',
  styleUrls: ['datas.scss']
})
export class DatasComponent {

  constructor(private datasService: DatasService, private alertService: JhiAlertService) {}

  updateDatas(): void {
    this.datasService.updateDatas().subscribe(() => {
      this.alertService.success("Datas mises à jour avec succès", null, null);
    });
  }
}
