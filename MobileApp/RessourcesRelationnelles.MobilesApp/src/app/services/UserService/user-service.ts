import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  getDemoUserData():any {
    return {
      id: 0,
      username: 'demo',
      email: 'demo@mail.com',
      password: '1234',
      role: 'citizen',
      createdAt: '2026-04-05T16:54:26',
      active: 'true',
    };
  }
}
