import { Component, signal } from '@angular/core';
import { form, FormField } from '@angular/forms/signals';

@Component({
  selector: 'searchbar',
  templateUrl: 'searchbar.html',
  imports: [FormField],
})
export class Searchbar {
  private text = signal<string>('');

  textForm = form(this.text);

  search = () => {
		this.text.set('');
	};
}
