export interface User {
  id: number;
  username: string;
  email: string;
  role: 'CITIZEN' | 'MODERATOR' | 'ADMINISTRATOR' | 'SUPERADMIN';
  createdAt: string;
  active: boolean;
}
