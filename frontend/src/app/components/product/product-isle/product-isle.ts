import { Component, inject, input, OnInit, signal } from '@angular/core';
import type { ProductCardT } from '../../../types/product-card';
import { ProductCard } from '../product-card/product-card';
import { ProductService } from '../../../services/product-service';
import { handleError } from '../../../services/errorHandler';
import { ToastrService } from 'ngx-toastr';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'product-isle',
  templateUrl: 'product-isle.html',
  imports: [ProductCard, RouterLink],
})
export class ProductIsle implements OnInit {
  private service = inject(ProductService);

  constructor(private toastr: ToastrService) {
    window.addEventListener('resize', () => {
      this.screenWidth.set(window.innerWidth);
    });
  }

  title = input.required<string>();

  products = signal<ProductCardT[]>([]);

  screenWidth = signal<number>(window.innerWidth);

  ngOnInit() {
    this.service
      .listProducts({
        page: 0,
        size: 5,
      })
      .subscribe({
        next: (products) => {
          this.products.set(products.content);
        },
        error: (err) => handleError(err, this.toastr),
      });
  }
}
