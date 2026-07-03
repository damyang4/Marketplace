import { HttpClient } from '@angular/common/http';
import { inject, Service, signal, effect } from '@angular/core';
import { Router } from '@angular/router';
import { environment } from '../environment';
import { AuthService } from './auth-service';

export interface CartItem {
  id: number;
  productId: number;
  productName: string;
  price: number;
  quantity: number;
  mainImage: string;
  maxQuantity: number;
}

export interface CartResponse {
  id: number;
  userProfile: string;
  cartItems: CartItem[];
  totalAmount: number;
  totalItems: number;
  guestToken: string;
}

@Service()
export class CartService {
  private http = inject(HttpClient);
  private router = inject(Router);
  private authService = inject(AuthService);

  cartState = signal<CartResponse | undefined>(undefined);
  currentOrderId = signal<string | null>(null);

  constructor() {
    effect(() => {
      const token = this.authService.currentUserToken();
      console.log('Auth token changed state. Syncing shopping cart items...');
      this.getShoppingCart();
    });
  }

  private updateCartState(cart: CartResponse) {
    if (cart) {
      if (cart.userProfile === 'GUEST' && cart.guestToken) {
        const currentToken = localStorage.getItem('guest_token');
        if (!currentToken || currentToken !== cart.guestToken) {
          localStorage.setItem('guest_token', cart.guestToken);
        }
      } else {
        localStorage.removeItem('guest_token');
      }

      if (cart.cartItems) {
        cart.cartItems.sort((a, b) => a.id - b.id);
      }
    }
    this.cartState.set(cart);
  }

  getShoppingCart = () => {
    this.http.get<CartResponse>(environment.serverUrl + '/cart').subscribe({
      next: (cartFromServer) => this.updateCartState(cartFromServer),
      error: (err) => console.error('Failed to fetch user cart', err)
    });
  };

  addProductToCart = (productId: number, quantity: number) => {
    this.http.post<CartResponse>(
      environment.serverUrl + `/cart/add/${productId}`,
      { requestedQuantity: quantity }
    ).subscribe({
      next: (fullCartFromServer) => this.updateCartState(fullCartFromServer),
      error: (err) => console.error('Failed to add product to cart', err)
    });
  };

  removeItemFromCart = (productId: number) => {
    this.http.delete<CartResponse>(environment.serverUrl + `/cart/remove/${productId}`).subscribe({
      next: (fullCartFromServer) => this.updateCartState(fullCartFromServer),
      error: (err) => console.error('Failed to remove product from cart', err)
    });
  };

  updateItemQuantity = (productId: number, quantity: number) => {
    this.http.put<CartResponse>(
      environment.serverUrl + `/cart/update/${productId}`,
      { requestedQuantity: quantity }
    ).subscribe({
      next: (fullCartFromServer) => this.updateCartState(fullCartFromServer),
      error: (err) => console.error('Failed to update product quantity in cart', err)
    });
  };

  clearCart = () => {
    this.http.post<CartResponse>(environment.serverUrl + '/cart/clear', {}).subscribe({
      next: (fullCartFromServer) => this.updateCartState(fullCartFromServer),
      error: (err) => console.error('Failed to clear cart', err)
    });
  };

  checkout = () => {
    if (this.cartState()?.totalAmount === 0) {
      console.warn('Cannot checkout with an empty cart');
      return;
    }

    this.http.post<{ sessionUrl: string }>(
      environment.serverUrl + '/api/payment/create-intent',
      {
        amount: Math.round((this.cartState()?.totalAmount ?? 0) * 100),
        currency: 'EUR'
      }
    ).subscribe({
      next: (response) => {
        window.location.href = response.sessionUrl;
      },
      error: (err) => {
        if (err.status === 401) {
          this.router.navigate(['/login'], {
            queryParams: { returnUrl: this.router.url }
          });
        } else {
          console.error('Failed to redirect to Stripe', err);
        }
      }
    });
  };
}
