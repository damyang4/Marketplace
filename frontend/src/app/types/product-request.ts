import type { Categroy } from "./category";
import type { Image } from "./image";

export type ProductRequest = {
	name: string;
	description: string;
	price: number;
	quantity: number;
	type: Categroy;
	mainImage: Image;
	additionalImage: Image[];
};