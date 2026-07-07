import { Component, inject, OnInit, signal, computed } from '@angular/core';
import { Searchbar } from './searchbar/searchbar';
import { RouterLink } from '@angular/router';
import { DropDown } from './dropdown/dropdown';
import { Cart } from './cart/cart';
import { Categroy } from '../../types/category';
import { CartService } from '../../services/cart-service';
import { handleError } from '../../services/errorHandler';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth-service';
import { ProductService } from '../../services/product-service';

@Component({
  selector: 'app-navbar',
  styleUrl: 'navbar.css',
  templateUrl: 'navbar.html',
  imports: [Searchbar, RouterLink, DropDown, Cart],
})
export class Navbar implements OnInit {
  private productService = inject(ProductService);
  private cartService = inject(CartService);
  private authService = inject(AuthService);
  private router = inject(Router);
  private toastr = inject(ToastrService);

  categories = signal<Categroy[]>([]);

  isDropDownOpen = signal<boolean>(false);

  isCartOpen = signal<boolean>(false);

  isUserMenuOpen = signal<boolean>(false);
  isLoggedIn = computed(() => !!this.authService.currentUserToken());

  cart = computed(() => this.cartService.cartState());
  cartItemsCount = computed(() => this.cart()?.totalItems ?? 0);

  ngOnInit() {
    this.productService
      .listCategories()
      .subscribe({
        next: (categories: Categroy[]) => {
          this.categories.set(categories);
        },
        error: (err: any) => handleError(err, this.toastr),
      });

    this.cartService.getShoppingCart();
  }

  toggleOpen() {
    this.isDropDownOpen.update((curr) => !curr);
  }

  toggleCart() {
    this.isCartOpen.update((curr) => !curr);
  }

  toggleUserMenu() {
    this.isUserMenuOpen.update((curr) => !curr);
  }

  logout() {
    this.authService.updateToken(null);

    this.isUserMenuOpen.set(false);
    this.router.navigate(['/']);
    this.toastr.success('User signed out!')
  }
}
