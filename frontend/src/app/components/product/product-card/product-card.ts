import { Component, computed, input } from "@angular/core";
import { ProductCardT } from "../../../types/product-card";
import { environment } from '../../../environment';
import { RouterLink } from "@angular/router";

@Component({
	selector: 'product-card',
	templateUrl: 'product-card.html',
 imports: [RouterLink]
})
export class ProductCard {
	product = input.required<ProductCardT>();

	url = computed(() => environment.imageStorage + this.product().mainImage);
};