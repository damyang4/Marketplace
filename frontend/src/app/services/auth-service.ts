import { Injectable, signal, computed } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  // 1. Initialize states directly from browser storage on boot
  currentUserToken = signal<string | null>(localStorage.getItem('auth_token'));
  currentGuestToken = signal<string | null>(localStorage.getItem('guest_token'));

  // 2. Derive authentication flags using computed signals
  isLoggedIn = computed(() => !!this.currentUserToken());
  hasGuestCart = computed(() => !!this.currentGuestToken());

  /**
   * Updates or clears the authenticated user token
   */
  updateToken(token: string | null) {
    if (token) {
      localStorage.setItem('auth_token', token);
      this.currentUserToken.set(token);

      // CRUCIAL: The moment a real user logs in, wipe out the guest session state
      this.clearGuestToken();
    } else {
      localStorage.removeItem('auth_token');
      this.currentUserToken.set(null);
    }
  }

  /**
   * Updates or initializes a guest shopping token
   */
  updateGuestToken(token: string | null) {
    if (token) {
      localStorage.setItem('guest_token', token);
      this.currentGuestToken.set(token);
    } else {
      this.clearGuestToken();
    }
  }

  /**
   * Explicitly purges guest storage tracking variables
   */
  clearGuestToken() {
    localStorage.removeItem('guest_token');
    this.currentGuestToken.set(null);
  }
}
