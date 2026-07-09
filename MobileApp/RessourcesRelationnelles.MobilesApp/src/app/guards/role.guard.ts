import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

export const roleGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const expectedRoles = route.data?.['roles'] as Array<string>;
  const user = authService.getCurrentUser();

  if (user && expectedRoles && expectedRoles.includes(user.role)) {
    return true;
  }

  router.navigate(['/tabs/ressources']);
  return false;
};
