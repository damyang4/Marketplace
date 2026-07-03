import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ProductService } from '../../services/product-service';
import type { ProductDetails } from '../../types/product-details';
import { handleError } from '../../services/errorHandler';
import { ToastrService } from 'ngx-toastr';
import { environment } from '../../environment';
import { LoadingDetails } from "./loading-details/loading-details";
import { CartService } from "../../services/cart-service";

@Component({
  selector: 'details-page',
  templateUrl: 'details.html',
  imports: [LoadingDetails, RouterLink],
})
export class Details implements OnInit {
  private service = inject(ProductService);
  private cartService = inject(CartService);
  private activatedRoute = inject(ActivatedRoute);

  constructor(private toastr: ToastrService) {}

  product = signal<ProductDetails | undefined>(undefined);
  selectedImage = signal<string>('');
  qty = signal<number>(1);

  ngOnInit(): void {
    this.activatedRoute.params.subscribe((param) => {
      this.service.getDetails(param['slug']).subscribe({
        next: (details) => {
          this.product.set(details);
					this.selectedImage.set(details.mainImage)
        },
        error: (err) => handleError(err, this.toastr),
      });
    });
  }

  increment(max: number) {
    if (this.qty() < max) this.qty.update((v) => v + 1);
  }

  decrement() {
    if (this.qty() > 1) this.qty.update((v) => v - 1);
  }

  imageUrl(path: string) {
    return environment.imageStorage + path;
  }

  addToCart() {
    // 1. Read the signal safely using extra parentheses: this.product()
    const currentProduct = this.product();

    // 2. Guard against the asynchronous race condition
    if (!currentProduct) {
      this.toastr.warning('Product data is still loading, please wait.');
      return;
    }

    this.cartService.addProductToCart(currentProduct.id, this.qty());
  }
}
