import { HttpErrorResponse } from "@angular/common/http";
import { ToastrService } from "ngx-toastr";

export const handleError = (err: unknown, toastr: ToastrService) => {
	if (err instanceof HttpErrorResponse) {
		if (err.error.message) {
			toastr.error(err.error.message);
		} else {
			toastr.error(err.message);
		}
	} else {
		toastr.error("An error occurred.");
	}
};
