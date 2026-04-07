import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonContent, IonToast, IonImg } from '@ionic/angular/standalone';
import { RessourceService } from '../services/RessourcesServices/ressource-service';
import { ActivatedRoute, Router } from '@angular/router';
import { DsfrBadgeComponent, DsfrButtonComponent } from '@edugouvfr/ngx-dsfr';

@Component({
  selector: 'app-ressource-details',
  templateUrl: './ressource-details.page.html',
  styleUrls: ['./ressource-details.page.scss'],
  standalone: true,
  imports: [
    IonContent,
    CommonModule,
    FormsModule,
    IonToast,
    DsfrButtonComponent,
    DsfrBadgeComponent,
    IonImg
],
})
export class RessourceDetailsPage implements OnInit {
  private ressourceService: RessourceService = inject(RessourceService);
  private route: ActivatedRoute = inject(ActivatedRoute);
  private router:Router = inject(Router);

  public ressourceData: any = {};

  public isToastOpen = false;
  public toastMessage = '';

  constructor() {}

  setToastOpen(toastOpen: boolean) {
    this.isToastOpen = toastOpen;
  }

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    this.ressourceService.getRessourcesDetails(id).subscribe({
      next: (data) => {
        this.ressourceData = data;
        console.log(this.ressourceData);
      },
      error: (err) => {
        this.toastMessage = "Une erreur est survenue : "+err
        this.setToastOpen(true);
      },
    });
  }

  goToRessourcePage(){
    this.router.navigate(['/tabs/ressources'])
  }

  getFormattedDate(dateCreation:Date):string {
    return new Date(dateCreation).toLocaleDateString("fr")
  }
}
