package com.example.filmfinder.data.utils

import com.example.filmfinder.data.local.entity.Movie
import com.example.filmfinder.data.local.entity.Rating

/**
 * Utility object containing predefined movie data.
 * Used to populate the database with initial movie data as required in the assignment.
 */
object PredefinedMovies {
    val movies = listOf(
        Movie(
            imdbID = "tt0111161",
            title = "The Shawshank Redemption",
            year = 1994,
            rated = "R",
            released = "14 Oct 1994",
            runtime = "142 min",
            genres = listOf("Drama"),
            director = "Frank Darabont",
            writers = listOf("Stephen King", "Frank Darabont"),
            actors = listOf("Tim Robbins", "Morgan Freeman", "Bob Gunton"),
            plot = "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.",
            languages = listOf("English"),
            countries = listOf("USA"),
            awards = "Nominated for 7 Oscars. 21 wins & 43 nominations total",
            poster = "https://m.media-amazon.com/images/M/MV5BMDFkYTc0MGEtZmNhMC00ZDIzLWFmNTEtODM1ZmRlYWMwMWFmXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_SX300.jpg",
            ratings = listOf(
                Rating("Internet Movie Database", "9.3/10"),
                Rating("Rotten Tomatoes", "91%"),
                Rating("Metacritic", "80/100")
            ),
            metascore = 80,
            imdbRating = 9.3f,
            imdbVotes = "2,405,546",
            type = "movie",
            dvd = "21 Dec 1999",
            boxOffice = "$28,699,976",
            production = "Columbia Pictures, Castle Rock Entertainment",
            website = "N/A"
        ),
        Movie(
            imdbID = "tt0068646",
            title = "The Godfather",
            year = 1972,
            rated = "R",
            released = "24 Mar 1972",
            runtime = "175 min",
            genres = listOf("Crime", "Drama"),
            director = "Francis Ford Coppola",
            writers = listOf("Mario Puzo", "Francis Ford Coppola"),
            actors = listOf("Marlon Brando", "Al Pacino", "James Caan"),
            plot = "The aging patriarch of an organized crime dynasty in postwar New York City transfers control of his clandestine empire to his reluctant youngest son.",
            languages = listOf("English", "Italian", "Latin"),
            countries = listOf("USA"),
            awards = "Won 3 Oscars. 31 wins & 30 nominations total",
            poster = "https://m.media-amazon.com/images/M/MV5BM2MyNjYxNmUtYTAwNi00MTYxLWJmNWYtYzZlODY3ZTk3OTFlXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_SX300.jpg",
            ratings = listOf(
                Rating("Internet Movie Database", "9.2/10"),
                Rating("Rotten Tomatoes", "97%"),
                Rating("Metacritic", "100/100")
            ),
            metascore = 100,
            imdbRating = 9.2f,
            imdbVotes = "1,733,648",
            type = "movie",
            dvd = "11 May 2004",
            boxOffice = "$136,381,073",
            production = "Paramount Pictures",
            website = "N/A"
        ),
        Movie(
            imdbID = "tt0468569",
            title = "The Dark Knight",
            year = 2008,
            rated = "PG-13",
            released = "18 Jul 2008",
            runtime = "152 min",
            genres = listOf("Action", "Crime", "Drama"),
            director = "Christopher Nolan",
            writers = listOf("Jonathan Nolan", "Christopher Nolan"),
            actors = listOf("Christian Bale", "Heath Ledger", "Aaron Eckhart"),
            plot = "When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, Batman must accept one of the greatest psychological and physical tests of his ability to fight injustice.",
            languages = listOf("English", "Mandarin"),
            countries = listOf("USA", "UK"),
            awards = "Won 2 Oscars. 159 wins & 163 nominations total",
            poster = "https://m.media-amazon.com/images/M/MV5BMTMxNTMwODM0NF5BMl5BanBnXkFtZTcwODAyMTk2Mw@@._V1_SX300.jpg",
            ratings = listOf(
                Rating("Internet Movie Database", "9.0/10"),
                Rating("Rotten Tomatoes", "94%"),
                Rating("Metacritic", "84/100")
            ),
            metascore = 84,
            imdbRating = 9.0f,
            imdbVotes = "2,445,066",
            type = "movie",
            dvd = "09 Dec 2008",
            boxOffice = "$534,858,444",
            production = "Warner Bros. Pictures",
            website = "N/A"
        ),
        Movie(
            imdbID = "tt0133093",
            title = "The Matrix",
            year = 1999,
            rated = "R",
            released = "31 Mar 1999",
            runtime = "136 min",
            genres = listOf("Action", "Sci-Fi"),
            director = "Lana Wachowski, Lilly Wachowski",
            writers = listOf("Lana Wachowski", "Lilly Wachowski"),
            actors = listOf("Keanu Reeves", "Laurence Fishburne", "Carrie-Anne Moss"),
            plot = "When a beautiful stranger leads computer hacker Neo to a forbidding underworld, he discovers the shocking truth--the life he knows is the elaborate deception of an evil cyber-intelligence.",
            languages = listOf("English"),
            countries = listOf("USA", "Australia"),
            awards = "Won 4 Oscars. 42 wins & 51 nominations total",
            poster = "https://m.media-amazon.com/images/M/MV5BNzQzOTk3OTAtNDQ0Zi00ZTVkLWI0MTEtMDllZjNkYzNjNTc4L2ltYWdlXkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1_SX300.jpg",
            ratings = listOf(
                Rating("Internet Movie Database", "8.7/10"),
                Rating("Rotten Tomatoes", "88%"),
                Rating("Metacritic", "73/100")
            ),
            metascore = 73,
            imdbRating = 8.7f,
            imdbVotes = "1,758,266",
            type = "movie",
            dvd = "21 Sep 1999",
            boxOffice = "$171,479,930",
            production = "Warner Bros., Village Roadshow Pictures",
            website = "N/A"
        ),
        Movie(
            imdbID = "tt0109830",
            title = "Forrest Gump",
            year = 1994,
            rated = "PG-13",
            released = "06 Jul 1994",
            runtime = "142 min",
            genres = listOf("Drama", "Romance"),
            director = "Robert Zemeckis",
            writers = listOf("Winston Groom", "Eric Roth"),
            actors = listOf("Tom Hanks", "Robin Wright", "Gary Sinise"),
            plot = "The presidencies of Kennedy and Johnson, the Vietnam War, the Watergate scandal and other historical events unfold from the perspective of an Alabama man with an IQ of 75, whose only desire is to be reunited with his childhood sweetheart.",
            languages = listOf("English"),
            countries = listOf("USA"),
            awards = "Won 6 Oscars. 45 wins & 75 nominations total",
            poster = "https://m.media-amazon.com/images/M/MV5BNWIwODRlZTUtY2U3ZS00Yzg1LWJhNzYtMmZiYmEyNmU1NjMzXkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_SX300.jpg",
            ratings = listOf(
                Rating("Internet Movie Database", "8.8/10"),
                Rating("Rotten Tomatoes", "71%"),
                Rating("Metacritic", "82/100")
            ),
            metascore = 82,
            imdbRating = 8.8f,
            imdbVotes = "1,893,980",
            type = "movie",
            dvd = "28 Aug 2001",
            boxOffice = "$330,252,182",
            production = "Paramount Pictures",
            website = "N/A"
        )
    )
}