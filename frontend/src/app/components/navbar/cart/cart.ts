import { Component, input, inject, computed, signal } from '@angular/core';
import { environment } from '../../../environment';
import { RouterLink } from '@angular/router';
import { CartService } from '../../../services/cart-service';

@Component({
	selector: 'cart',
	imports: [RouterLink],
	templateUrl: 'cart.html'
})
export class Cart {
  private cartService = inject(CartService);

  url = signal(environment.imageStorage);
  cartTotalAmount = computed(() => this.cartService.cartState()?.totalAmount?? 0);
  cartItems = computed(() => this.cartService.cartState()?.cartItems ?? []);

	close = input.required<() => void>();

	remove(productId: number) {
    this.cartService.removeItemFromCart(productId);
  }
}
