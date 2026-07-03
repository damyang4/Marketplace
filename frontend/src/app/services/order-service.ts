import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router'; // 1. Added Router import
import { environment } from '../environment';

export interface OrderItemResponse {
  productId: number;
  productName: string;
  price: number;
  quantity: number;
}

export interface OrderDetails {
  id: number;
  userProfile: string;
  cartItems: OrderItemResponse[];
  totalAmount: number;
}

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private http = inject(HttpClient);
  private router = inject(Router); // 2. Injected the Router


  getOrderById(orderId: string) {
    return this.http.get<OrderDetails>(`${environment.serverUrl}/order/${orderId}`);
  }

  checkout = (fallbackUrl: string = '/cart') => {
    this.http.post<any>(environment.serverUrl + '/order/checkout', {}).subscribe({
      next: (orderResponse) => {
        // Clear out the client-side cart cache layout instantly if you maintain it here
        const self = this as any;
        if (typeof self.clearCartState === 'function') {
          self.clearCartState();
        }

        // Redirect to success page passing the database record ID
        this.router.navigate(['/order-success'], {
          queryParams: { order_id: orderResponse.id }
        });
      },
      error: (err) => {
        if (err.status === 401) {
          this.router.navigate(['/login'], { queryParams: { returnUrl: fallbackUrl } });
        }
      }
    });
  }
}
