import { Injectable } from '@angular/core';
import { Friend } from 'src/app/models/Friend';

@Injectable({
  providedIn: 'root',
})
export class FriendService {

  private demoFriends:Friend[] =  [
      { nom: 'PILLOT', prenom: 'Jules', username: 'sunfox' },
      { nom: 'DURAND', prenom: 'Lucas', username: 'lucky77' },
      { nom: 'MARTIN', prenom: 'Emma', username: 'pixelia' },
      { nom: 'BERNARD', prenom: 'Hugo', username: 'stormix' },
      { nom: 'THOMAS', prenom: 'Chloé', username: 'chocoPop' },
      { nom: 'ROBERT', prenom: 'Nathan', username: 'neoWave' },
      { nom: 'RICHARD', prenom: 'Léa', username: 'bluecat' },
      { nom: 'PETIT', prenom: 'Gabriel', username: 'gigaByte' },
      { nom: 'DUBOIS', prenom: 'Manon', username: 'moonlit' },
      { nom: 'MOREAU', prenom: 'Louis', username: 'redNova' },
      { nom: 'LAURENT', prenom: 'Sarah', username: 'sparkleX' },
      { nom: 'SIMON', prenom: 'Arthur', username: 'driftKing' },
      { nom: 'MICHEL', prenom: 'Camille', username: 'camZone' },
      { nom: 'LEFEBVRE', prenom: 'Ethan', username: 'ethereal' },
      { nom: 'LEROY', prenom: 'Inès', username: 'icyWind' },
      { nom: 'ROUX', prenom: 'Noah', username: 'shadowFox' },
      { nom: 'DAVID', prenom: 'Zoé', username: 'zappy' },
      { nom: 'BERTRAND', prenom: 'Adam', username: 'alphaRun' },
      { nom: 'MOREL', prenom: 'Clara', username: 'clariSky' },
      { nom: 'FOURNIER', prenom: 'Paul', username: 'paulux' },
      { nom: 'GIRARD', prenom: 'Anna', username: 'auraX' },
      { nom: 'BONNET', prenom: 'Tom', username: 'tomster' },
      { nom: 'DUPONT', prenom: 'Eva', username: 'evaGlow' },
      { nom: 'LAMBERT', prenom: 'Leo', username: 'leonix' },
      { nom: 'FONTAINE', prenom: 'Mila', username: 'milight' },
      { nom: 'ROUSSEAU', prenom: 'Maxime', username: 'maxVolt' },
      { nom: 'VINCENT', prenom: 'Jade', username: 'jadeFire' },
      { nom: 'MULLER', prenom: 'Axel', username: 'axStorm' },
      { nom: 'LECLERC', prenom: 'Lina', username: 'linaria' },
      { nom: 'PERRIN', prenom: 'Sacha', username: 'sachX' },
      { nom: 'MORIN', prenom: 'Nina', username: 'ninova' },
      { nom: 'GARNIER', prenom: 'Theo', username: 'theOrbit' },
      { nom: 'CHEVALIER', prenom: 'Lou', username: 'loupiX' },
      { nom: 'FRANCOIS', prenom: 'Enzo', username: 'enzoo7' },
      { nom: 'LEGRAND', prenom: 'Rose', username: 'rosebud' },
      { nom: 'GAUTHIER', prenom: 'Mathis', username: 'mathSpark' },
      { nom: 'GARIN', prenom: 'Alice', username: 'aliceZen' },
      { nom: 'PICARD', prenom: 'Bastien', username: 'bastiCore' },
      { nom: 'COLIN', prenom: 'Juliette', username: 'julyMoon' },
      { nom: 'RENAULT', prenom: 'Victor', username: 'victron' }
    ];

  public getDemoFriends(): Friend[] {
    return this.demoFriends;
  }

  public removeFriend(username:string) {
    this.demoFriends = this.demoFriends.filter(friend => friend.username !== username);
  }
}
