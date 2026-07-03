import { Component, input } from '@angular/core';
import { Categroy } from '../../../types/category';
import { RouterLink } from "@angular/router";

@Component({
  selector: 'dropdown',
  templateUrl: 'dropdown.html',
  imports: [RouterLink],
})
export class DropDown {
  categories = input.required<Categroy[]>();

  toggleOpen = input.required<() => void>();

  close = () => {
		this.toggleOpen()();
  };
}
