import { Component, inject, computed, signal } from '@angular/core';
import { CartService } from '../../services/cart-service';
import { environment } from '../../environment';
import { CartItem } from '../../services/cart-service';
import { LoadingCart } from './loading-cart/loading-cart';

@Component({
  selector: 'app-shopping-cart',
  imports: [LoadingCart],
  templateUrl: './shopping-cart.html',
  styleUrls: ['./shopping-cart.css']
})
export class ShoppingCart {
  private service = inject(CartService);
  url = signal(environment.imageStorage);

  cartTotalAmount = computed(() => this.service.cartState()?.totalAmount?? 0);
  cartItems = computed(() => this.service.cartState()?.cartItems);

  removeItem(productId: number) {
    this.service.removeItemFromCart(productId);
  }

  decreaseQuantity(item: CartItem) {
    if (item.quantity <= 1) {
      this.removeItem(item.productId);
      return;
    }
    this.service.updateItemQuantity(item.productId, item.quantity - 1);
  }

  increaseQuantity(item: CartItem) {
    if (item.quantity >= item.maxQuantity) {
      alert(`You cannot add more than ${item.maxQuantity} items of this product to the cart.`);
      return;
    }
    this.service.updateItemQuantity(item.productId, item.quantity + 1);
  }

  checkout() {
    this.service.checkout();
  }
}
