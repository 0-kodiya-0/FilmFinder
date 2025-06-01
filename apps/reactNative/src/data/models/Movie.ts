export interface Rating {
    source: string;
    value: string;
}

export interface Movie {
    imdbID: string;
    title: string;
    year: number;
    rated: string;
    released: string;
    runtime: string;
    genres: string[];
    director: string;
    writers: string[];
    actors: string[];
    plot: string;
    languages: string[];
    countries: string[];
    awards: string;
    poster: string;
    ratings: Rating[];
    metascore: number;
    imdbRating: number;
    imdbVotes: string;
    type: string;
    dvd: string;
    boxOffice: string;
    production: string;
    website: string;
    response: string;
}