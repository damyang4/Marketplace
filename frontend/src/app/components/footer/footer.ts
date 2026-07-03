import { Component, signal } from "@angular/core";

@Component({
	selector: 'app-footer',
	templateUrl: 'footer.html'
})
export class Footer {
	year = signal(new Date().getFullYear());
};