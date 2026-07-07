import { Routes } from '@angular/router';
import { Landing } from './pages/landing/landing';
import { ProductsPage } from './pages/products/products';
import { Details } from './pages/details/details';
import { ShoppingCart } from './pages/shopping-cart/shopping-cart';
import { Login } from './pages/login/login';
import { OrderSuccess } from './pages/order-success/order-success';
import { CategoriesPage } from './pages/categories/categories';
import { AddProduct } from './pages/add-product/add-product';
import { Register } from './pages/register/register';

export const routes: Routes = [
  {
    path: '',
    component: Landing
  },
  {
    path: 'products',
    component: ProductsPage
  },
	{
		path: 'categories/:code',
		component: CategoriesPage
	},
	{
		path: 'add-product',
		component: AddProduct
	},
  {
    path: 'details/:slug',
    component: Details
  },
  {
    path: 'cart',
    component: ShoppingCart
  },
  {
    path: 'login',
    component: Login
  },
  {
    path: 'register',
    component: Register
  },
  {
    path: 'order-success',
    component: OrderSuccess
  }
];
