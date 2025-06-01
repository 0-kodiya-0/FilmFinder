export interface MovieFilter {
  title?: string;
  searchTerm?: string;
  imdbID?: string;
  year?: number;
  includeRatings?: boolean;
  page?: number;
}

export interface ActorSearchFilter {
  genre?: string;
  fromYear?: number;
  toYear?: number;
  highRatedOnly?: boolean;
}

export enum MediaType {
  MOVIE = 'movie',
}