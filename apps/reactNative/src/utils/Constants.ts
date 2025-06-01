import { Movie } from "../data/models/Movie";

export const API_CONFIG = {
    BASE_URL: 'https://www.omdbapi.com/',
    API_KEY: '4b324f15', // Replace with your actual API key
    TIMEOUT: 15000,
};

export const CACHE_CONFIG = {
    MAX_CACHE_SIZE: 50, // Maximum number of images to cache
    CACHE_DURATION: 300000, // 5 minutes in milliseconds
};

export const PREDEFINED_MOVIES: Movie[] = [
    {
        imdbID: "tt0111161",
        title: "The Shawshank Redemption",
        year: 1994,
        rated: "R",
        released: "14 Oct 1994",
        runtime: "142 min",
        genres: ["Drama"],
        director: "Frank Darabont",
        writers: ["Stephen King", "Frank Darabont"],
        actors: ["Tim Robbins", "Morgan Freeman", "Bob Gunton"],
        plot: "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.",
        languages: ["English"],
        countries: ["USA"],
        awards: "Nominated for 7 Oscars. 21 wins & 43 nominations total",
        poster: "https://m.media-amazon.com/images/M/MV5BMDFkYTc0MGEtZmNhMC00ZDIzLWFmNTEtODM1ZmRlYWMwMWFmXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_SX300.jpg",
        ratings: [
            { source: "Internet Movie Database", value: "9.3/10" },
            { source: "Rotten Tomatoes", value: "91%" },
            { source: "Metacritic", value: "80/100" }
        ],
        metascore: 80,
        imdbRating: 9.3,
        imdbVotes: "2,405,546",
        type: "movie",
        dvd: "21 Dec 1999",
        boxOffice: "$28,699,976",
        production: "Columbia Pictures, Castle Rock Entertainment",
        website: "N/A",
        response: "True"
    },
    {
        imdbID: "tt0068646",
        title: "The Godfather",
        year: 1972,
        rated: "R",
        released: "24 Mar 1972",
        runtime: "175 min",
        genres: ["Crime", "Drama"],
        director: "Francis Ford Coppola",
        writers: ["Mario Puzo", "Francis Ford Coppola"],
        actors: ["Marlon Brando", "Al Pacino", "James Caan"],
        plot: "The aging patriarch of an organized crime dynasty in postwar New York City transfers control of his clandestine empire to his reluctant youngest son.",
        languages: ["English", "Italian", "Latin"],
        countries: ["USA"],
        awards: "Won 3 Oscars. 31 wins & 30 nominations total",
        poster: "https://m.media-amazon.com/images/M/MV5BM2MyNjYxNmUtYTAwNi00MTYxLWJmNWYtYzZlODY3ZTk3OTFlXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_SX300.jpg",
        ratings: [
            { source: "Internet Movie Database", value: "9.2/10" },
            { source: "Rotten Tomatoes", value: "97%" },
            { source: "Metacritic", value: "100/100" }
        ],
        metascore: 100,
        imdbRating: 9.2,
        imdbVotes: "1,733,648",
        type: "movie",
        dvd: "11 May 2004",
        boxOffice: "$136,381,073",
        production: "Paramount Pictures",
        website: "N/A",
        response: "True"
    },
];