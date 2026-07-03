import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject, Service } from '@angular/core';
import { environment } from '../environment';
import type { Image } from '../types/image';
import type { StringResponse } from '../types/string-response';

@Service()
export class ImageService {
  private http = inject(HttpClient);
  private path = environment.serverUrl + `/images`;

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('auth-token');

    return new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
  }

  uploadImages = (images: File[], productId?: number) => {
    const formData = new FormData();

    images.forEach((image) => formData.append('images', image));

    if (productId !== undefined) {
      formData.append('productId', String(productId));
    }

    return this.http.post<Image[]>(this.path, formData, {
      headers: this.getAuthHeaders(),
    });
  };

  removeImage = (name: string) => {
    return this.http.delete<StringResponse>(`${this.path}/${name}`, {
      headers: this.getAuthHeaders(),
    });
  };
}
