import { Categroy } from "./category"
import { Image } from "./image";

export type ProductDetails = {
	readonly id: number
  readonly slug: number,
  name: string,
  description: string,
  price: number,
  quantity: number,
  type: Categroy,
  createdAt: string,
  mainImage: string,
  additionalImages: Image[]
};