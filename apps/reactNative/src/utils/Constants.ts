import { Movie } from "../data/models/Movie";

export const API_CONFIG = {
    BASE_URL: process.env.EXPO_PUBLIC_OMDB_BASE_URL || 'https://www.omdbapi.com/',
    API_KEY: process.env.EXPO_PUBLIC_OMDB_API_KEY || '',
    TIMEOUT: 15000,
};

export const CACHE_CONFIG = {
    MAX_CACHE_SIZE: 50, // Maximum number of images to cache
    CACHE_DURATION: 300000, // 5 minutes in milliseconds
};

// Validate that required environment variables are present
if (!API_CONFIG.API_KEY) {
    console.error('Missing required environment variable: EXPO_PUBLIC_OMDB_API_KEY');
}

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
    {
        imdbID: "tt0468569",
        title: "The Dark Knight",
        year: 2008,
        rated: "PG-13",
        released: "18 Jul 2008",
        runtime: "152 min",
        genres: ["Action", "Crime", "Drama"],
        director: "Christopher Nolan",
        writers: ["Jonathan Nolan", "Christopher Nolan"],
        actors: ["Christian Bale", "Heath Ledger", "Aaron Eckhart"],
        plot: "When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, Batman must accept one of the greatest psychological and physical tests of his ability to fight injustice.",
        languages: ["English", "Mandarin"],
        countries: ["USA", "UK"],
        awards: "Won 2 Oscars. 159 wins & 163 nominations total",
        poster: "https://m.media-amazon.com/images/M/MV5BMTMxNTMwODM0NF5BMl5BanBnXkFtZTcwODAyMTk2Mw@@._V1_SX300.jpg",
        ratings: [
            { source: "Internet Movie Database", value: "9.0/10" },
            { source: "Rotten Tomatoes", value: "94%" },
            { source: "Metacritic", value: "84/100" }
        ],
        metascore: 84,
        imdbRating: 9.0,
        imdbVotes: "2,445,066",
        type: "movie",
        dvd: "09 Dec 2008",
        boxOffice: "$534,858,444",
        production: "Warner Bros. Pictures",
        website: "N/A",
        response: "True"
    },
    {
        imdbID: "tt0133093",
        title: "The Matrix",
        year: 1999,
        rated: "R",
        released: "31 Mar 1999",
        runtime: "136 min",
        genres: ["Action", "Sci-Fi"],
        director: "Lana Wachowski, Lilly Wachowski",
        writers: ["Lana Wachowski", "Lilly Wachowski"],
        actors: ["Keanu Reeves", "Laurence Fishburne", "Carrie-Anne Moss"],
        plot: "When a beautiful stranger leads computer hacker Neo to a forbidding underworld, he discovers the shocking truth--the life he knows is the elaborate deception of an evil cyber-intelligence.",
        languages: ["English"],
        countries: ["USA", "Australia"],
        awards: "Won 4 Oscars. 42 wins & 51 nominations total",
        poster: "https://m.media-amazon.com/images/M/MV5BNzQzOTk3OTAtNDQ0Zi00ZTVkLWI0MTEtMDllZjNkYzNjNTc4L2ltYWdlXkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1_SX300.jpg",
        ratings: [
            { source: "Internet Movie Database", value: "8.7/10" },
            { source: "Rotten Tomatoes", value: "88%" },
            { source: "Metacritic", value: "73/100" }
        ],
        metascore: 73,
        imdbRating: 8.7,
        imdbVotes: "1,758,266",
        type: "movie",
        dvd: "21 Sep 1999",
        boxOffice: "$171,479,930",
        production: "Warner Bros., Village Roadshow Pictures",
        website: "N/A",
        response: "True"
    },
    {
        imdbID: "tt0109830",
        title: "Forrest Gump",
        year: 1994,
        rated: "PG-13",
        released: "06 Jul 1994",
        runtime: "142 min",
        genres: ["Drama", "Romance"],
        director: "Robert Zemeckis",
        writers: ["Winston Groom", "Eric Roth"],
        actors: ["Tom Hanks", "Robin Wright", "Gary Sinise"],
        plot: "The presidencies of Kennedy and Johnson, the Vietnam War, the Watergate scandal and other historical events unfold from the perspective of an Alabama man with an IQ of 75, whose only desire is to be reunited with his childhood sweetheart.",
        languages: ["English"],
        countries: ["USA"],
        awards: "Won 6 Oscars. 45 wins & 75 nominations total",
        poster: "https://m.media-amazon.com/images/M/MV5BNWIwODRlZTUtY2U3ZS00Yzg1LWJhNzYtMmZiYmEyNmU1NjMzXkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_SX300.jpg",
        ratings: [
            { source: "Internet Movie Database", value: "8.8/10" },
            { source: "Rotten Tomatoes", value: "71%" },
            { source: "Metacritic", value: "82/100" }
        ],
        metascore: 82,
        imdbRating: 8.8,
        imdbVotes: "1,893,980",
        type: "movie",
        dvd: "28 Aug 2001",
        boxOffice: "$330,252,182",
        production: "Paramount Pictures",
        website: "N/A",
        response: "True"
    }
];