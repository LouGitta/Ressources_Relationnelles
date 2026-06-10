import { environment } from 'src/environments/environment';

export const API_ENDPOINTS = {
  USERS: `${environment.apiUrl}/api/users`,
  RESSOURCES: `${environment.apiUrl}/api/ressources`,
  COMMENTS: `${environment.apiUrl}/api/comments`,
  FRIENDS: `${environment.apiUrl}/api/friends`,
  PROGRESSIONS: `${environment.apiUrl}/api/progressions`
};
