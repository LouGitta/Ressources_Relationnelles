import { createAvatar } from '@dicebear/core';
import { FriendService } from './../services/FriendService/friend-service';
import { Component, inject, OnInit } from '@angular/core';
import { IonContent, IonList, IonItem, IonAvatar, IonLabel, IonImg, IonIcon, IonButton } from '@ionic/angular/standalone';
import { Friend } from '../models/Friend';
import { botttsNeutral } from '@dicebear/collection';
import { DsfrButtonComponent } from "@edugouvfr/ngx-dsfr"
import { closeOutline } from 'ionicons/icons';
import { addIcons } from 'ionicons';



@Component({
  selector: 'app-friends',
  templateUrl: 'friends.page.html',
  styleUrls: ['friends.page.scss'],
  imports: [IonButton, IonIcon, IonImg, IonLabel, IonAvatar, IonItem, IonList, IonContent, DsfrButtonComponent],
})
export class FriendsPage implements OnInit {

  private friendService = inject(FriendService);
  public friends:Friend[] = []

  constructor() {
    addIcons({closeOutline})
  }

  ngOnInit(): void {
    this.friends = this.friendService.getDemoFriends();
  }

  public getAvatarUrl(username:string):string{
    return createAvatar(botttsNeutral, {
      seed: username
    }).toDataUri()
  }

  removeFwiend(username:string){
    this.friendService.removeFriend(username);
    this.friends = this.friendService.getDemoFriends();
  }
}
