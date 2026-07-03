import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { OrderService, OrderDetails } from '../../services/order-service';

@Component({
  selector: 'app-order-success',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './order-success.html',
  styleUrl: './order-success.css'
})
export class OrderSuccess implements OnInit {
  private route = inject(ActivatedRoute);
  private orderService = inject(OrderService);

  // Use signals to hold the state reactively
  order = signal<OrderDetails | null>(null);
  isLoading = signal<boolean>(true);
  errorMessage = signal<string | null>(null);

  ngOnInit() {
    // 1. Read 'order_id' from query parameters instead of 'session_id'
    debugger;
    const orderId = this.route.snapshot.queryParamMap.get('order_id');

    if (!orderId) {
      this.errorMessage.set('No order identification found.');
      this.isLoading.set(false);
      return;
    }

    // 2. Call your backend order lookup endpoint via the service
    this.orderService.getOrderById(orderId).subscribe({
      next: (details: OrderDetails) => {
        debugger;
        this.order.set(details);
        this.isLoading.set(false);
      },
      error: (err: any) => {
        console.error(err);
        this.errorMessage.set('Could not retrieve your order details.');
        this.isLoading.set(false);
      }
    });
  }
}
