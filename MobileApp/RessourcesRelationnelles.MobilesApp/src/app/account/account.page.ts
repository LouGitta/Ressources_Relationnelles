import { UserService } from './../services/UserService/user-service';
import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import {
  DsfrButtonComponent,
  DsfrFormInputComponent,
} from '@edugouvfr/ngx-dsfr';
import {
  IonContent,
  IonGrid,
  IonRow,
  IonToast,
} from '@ionic/angular/standalone';

@Component({
  selector: 'app-account',
  templateUrl: 'account.page.html',
  styleUrls: ['account.page.scss'],
  imports: [
    IonGrid,
    IonContent,
    DsfrFormInputComponent,
    DsfrButtonComponent,
    FormsModule,
    IonRow,
    IonToast
],
})
export class AccountPage implements OnInit {
  private userService: UserService = inject(UserService);
  public userData: any = {};
  public isToastOpen = false;
  public tostMessage = '';
  public toastIcon = '';
  public toastColor = '';
  constructor() {}

  ngOnInit(): void {
    this.userData = this.userService.getDemoUserData();
  }

  setToastOpen(toastOpen: boolean) {
    this.isToastOpen = toastOpen;
  }

  saveUserData(): void {
    this.tostMessage = 'Modifications sauvegardées';
    this.toastIcon = 'checkmark-outline';
    this.toastColor = 'success';
    this.isToastOpen = true;
  }

  deleteUserData(): void {
    this.tostMessage = 'Compte supprimé';
    this.toastIcon = 'trash-outline';
    this.toastColor = 'danger';
    this.isToastOpen = true;
  }
}
