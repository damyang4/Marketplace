import { Component, inject, OnInit, signal } from '@angular/core';
import { form, FormField, submit } from '@angular/forms/signals';
import { ToastrService } from 'ngx-toastr';
import { firstValueFrom } from 'rxjs';
import { ImageUpload } from '../../components/image-upload-button/image-upload';
import { handleError } from '../../services/errorHandler';
import { ProductService } from '../../services/product-service';
import type { Categroy } from '../../types/category';
import { ProductRequest } from '../../types/product-request';
import { ImageService } from '../../services/imageService';
import { environment } from '../../environment';

type ProductFormModel = Omit<ProductRequest, 'type'> & { typeCode: string };

@Component({
  templateUrl: 'add-product.html',
  selector: 'add-product',
  imports: [ImageUpload, FormField],
})
export class AddProduct implements OnInit {
  private productService = inject(ProductService);
  private imageService = inject(ImageService);

  constructor(private toastr: ToastrService) {}

  categories = signal<Categroy[]>([]);

  readonly productModel = signal<ProductFormModel>({
    name: '',
    description: '',
    price: 0,
    quantity: 0,
    typeCode: '',
    mainImage: { name: '' },
    additionalImage: [],
  });

  readonly productForm = form(this.productModel);

  token = signal<string | null>(localStorage.getItem('auth-token'));

  ngOnInit() {
    this.productService.listCategories().subscribe({
      next: (categories) => this.categories.set(categories),
      error: (err) => handleError(err, this.toastr),
    });
  }

  addAdditionalImage(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    if (!file) return;

    this.imageService.uploadImages([file]).subscribe({
      next: ([image]) => {
        this.productModel.update((value) => ({
          ...value,
          additionalImage: [...value.additionalImage, image],
        }));
      },
      error: (err) => handleError(err, this.toastr),
    });

    input.value = '';
  }

  removeAdditionalImage(index: number): void {
    const image = this.productModel().additionalImage[index];

    this.imageService.removeImage(image.name).subscribe({
      error: (err) => handleError(err, this.toastr),
    });

    this.productModel.update((value) => ({
      ...value,
      additionalImage: value.additionalImage.filter((_, i) => i !== index),
    }));
  }

  async onSubmit(): Promise<void> {
    await submit(this.productForm, async () => {
      const value = this.productModel();
      const type = this.categories().find((c) => c.code === value.typeCode);

      if (!type) {
        this.toastr.error('Please add a valid category');
        return;
      }
			
			if (!value.mainImage.name) {
        this.toastr.error('Please add a valid main image');
        return;
			}

      const productRequest: ProductRequest = {
        name: value.name,
        description: value.description,
        price: value.price,
        quantity: value.quantity,
        mainImage: value.mainImage,
        additionalImage: value.additionalImage,
        type,
      };

      try {
        await firstValueFrom(this.productService.createProduct(productRequest));
        this.toastr.success('Product uploaded successfully');
        this.productModel.set({
          name: '',
          description: '',
          price: 0,
          quantity: 0,
          typeCode: '',
          mainImage: { name: '' },
          additionalImage: [],
        });
      } catch (err) {
        handleError(err, this.toastr);
      }
    });
  }

  imageUrl(path: string) {
    return environment.imageStorage + path;
  }
}
