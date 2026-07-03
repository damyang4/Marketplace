import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const authGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const token = localStorage.getItem('auth_token');

  if (token) {
    return true; // Token exists, let them pass!
  }

  // No token found! Redirect them to the login page
  router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
  return false;
};
