import { Movie, Rating } from "./Movie";

export interface SearchItem {
    title: string;
    year: number;
    imdbID: string;
    type: string;
    poster: string;
}

export interface SearchResponse {
    Search: SearchItem[];
    totalResults: string;
    Response: string;
}

export interface MovieResponse extends Omit<Movie, 'genres' | 'writers' | 'actors' | 'languages' | 'countries'> {
    Genre: string;
    Writer: string;
    Actors: string;
    Language: string;
    Country: string;
    Ratings: Rating[];
}