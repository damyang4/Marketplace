import { Component } from "@angular/core";
import { Hero } from "../../components/hero/hero";
import { ProductIsle } from "../../components/product/product-isle/product-isle";
import { SellerSection } from "../../components/seller-section/seller-section";

@Component({
	selector: 'landing',
	templateUrl: 'landing.html',
	imports: [Hero, ProductIsle, SellerSection]
})
export class Landing {

};