import { Component, input, WritableSignal } from '@angular/core';
import { PageableRequest } from '../../types/pageable';

@Component({
  selector: 'pagination-controller',
  templateUrl: 'pagination-controller.html',
})
export class PaginationController {
	totalPages = input.required<number>();

	pageInfo = input.required<WritableSignal<PageableRequest>>();

	prev() {
		const sig = this.pageInfo();
		const cur = sig();

		if (cur.page <= 0) return;

		sig.set({ ...cur, page: cur.page - 1 });
	}

	next() {
		const sig = this.pageInfo();
		const cur = sig();
		const last = Math.max(0, this.totalPages() - 1);

		if (cur.page >= last) return;

		sig.set({ ...cur, page: cur.page + 1 });
	}

}
