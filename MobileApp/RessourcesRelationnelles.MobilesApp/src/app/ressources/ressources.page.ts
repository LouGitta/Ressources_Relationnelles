import { resetConsumerBeforeComputation } from '@angular/core/formatter.d';
import { RessourceService } from './../services/RessourcesServices/ressource-service';
import { Component, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DsfrCardComponent } from '@edugouvfr/ngx-dsfr';
import { IonContent, IonList, IonItem } from '@ionic/angular/standalone';

@Component({
  selector: 'app-ressources',
  templateUrl: 'ressources.page.html',
  styleUrls: ['ressources.page.scss'],
  imports: [IonItem, IonList, IonContent, DsfrCardComponent]
})
export class RessourcesPage implements OnInit{

  private ressourceService:RessourceService = inject(RessourceService);
  private router:Router = inject(Router);

  public ressources: any[] = [];
  
  constructor() {}

  ngOnInit(): void {
    this.ressourceService.getRessources().subscribe({
      next: (data) => {
        this.ressources = data
      }, 
      error: (err) => {
        console.error("erreur : ", err)
      }
    })
  }

  goToRessourceDetailsPage(ressourceId:any){
    this.router.navigate(['/tabs/ressources/', ressourceId])
  }

  getFormattedDate(dateCreation:Date):string {
    return new Date(dateCreation).toLocaleDateString("fr")
  }
}
