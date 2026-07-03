export type PageResponse<T> = {
	content: T[];
	totalElements: number;
	totalPages: number;
};