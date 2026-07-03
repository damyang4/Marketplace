import { Component, inject, model, signal } from '@angular/core';
import type { FormValueControl } from '@angular/forms/signals';
import { ToastrService } from 'ngx-toastr';
import { handleError } from '../../services/errorHandler';
import type { Image } from '../../types/image';
import { ImageService } from '../../services/imageService';
import { environment } from '../../environment';

let nextId = 0;

@Component({
  templateUrl: 'image-upload.html',
  selector: 'image-upload',
})
export class ImageUpload implements FormValueControl<Image> {
  private service = inject(ImageService);

  constructor(private toastr: ToastrService) {}

  readonly value = model<Image>({ name: '' });

  readonly uploading = signal(false);
  readonly inputId = `imageUpload-${nextId++}`;

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    if (!file) return;

    this.uploading.set(true);

    this.service.uploadImages([file]).subscribe({
      next: ([image]) => {
        this.value.set(image);
        this.uploading.set(false);
      },
      error: (err) => {
        handleError(err, this.toastr);
        this.uploading.set(false);
      },
    });

    input.value = '';
  }

  clear(event: Event): void {
    event.preventDefault();
    event.stopPropagation();

    const current = this.value();
    if (!current.name) return;

    this.service.removeImage(current.name).subscribe({
      error: (err) => handleError(err, this.toastr),
    });

    this.value.set({ name: '' });
  }

  imageUrl(path: string) {
    return environment.imageStorage + path;
  }
}
