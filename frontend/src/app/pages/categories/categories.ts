import { Component, computed, effect, signal, inject, input } from '@angular/core';
import { ProductCard } from '../../components/product/product-card/product-card';
import { CartService } from '../../services/cart-service';
import { ProductService } from '../../services/product-service';
import { ToastrService } from 'ngx-toastr';
import type { ProductCardT } from '../../types/product-card';
import { PageableRequest } from '../../types/pageable';
import { handleError } from '../../services/errorHandler';
import { PaginationController } from '../../components/pagination-controller/pagination-controller';
import { form, FormField } from '@angular/forms/signals';
import { environment } from '../../environment';

@Component({
  selector: 'categories-page',
  templateUrl: '../../components/product/products-grid.html',
  imports: [ProductCard, PaginationController, FormField],
})
export class CategoriesPage {
  private productService = inject(ProductService);
  private cartService = inject(CartService);
  private toastr = inject(ToastrService);

  // 1. Modern route parameter binding!
  // Angular automatically links this to the path: 'categories/:code'
  code = input.required<string>();

  private sortText = signal<'createdAt,desc' | 'price,desc' | 'price,asc'>('createdAt,desc');
  selectForm = form(this.sortText);
  url = signal(environment.imageStorage);

  products = signal<ProductCardT[]>([]);
  totalElements = signal<number>(0);
  totalPages = signal<number>(0);

  pageInfo = signal<PageableRequest>({
    page: 0,
    size: environment.pageSize,
  });

  // Derived reactive signal for the title header
  title = computed(() => this.code() || "");

  constructor() {
    // 2. Register the effect in the constructor (Safe Injection Context)
    effect((onCleanup) => {
      const currentCode = this.code();
      const params = {
        ...this.pageInfo(),
        sort: this.sortText()
      };

      if (!currentCode) return;

      const sub = this.productService.listProductsByCategory(params, currentCode).subscribe({
        next: (products: any) => {
          this.products.set(products.content);
          this.totalElements.set(products.totalElements);
          this.totalPages.set(products.totalPages);
        },
        error: (err: any) => handleError(err, this.toastr),
      });

      onCleanup(() => sub.unsubscribe());
    });
  }
}
