import ApiService from '../api/ApiService';
import DatabaseService from '../database/DatabaseService';
import { Movie, MovieFilter, MovieResponse, SearchResponse, SearchItem } from '../models';

class MovieRepository {
    // API methods
    async getMovieByFilter(filter: MovieFilter): Promise<MovieResponse> {
        return ApiService.getMovieByFilter(filter);
    }

    async searchMoviesByFilter(filter: MovieFilter): Promise<SearchResponse> {
        return ApiService.searchMoviesByFilter(filter);
    }

    // Database methods
    async insertMovie(movie: Movie): Promise<void> {
        return DatabaseService.insertMovie(movie);
    }

    async insertMovies(movies: Movie[]): Promise<void> {
        return DatabaseService.insertMovies(movies);
    }

    async searchMoviesByActor(actorName: string): Promise<Movie[]> {
        return DatabaseService.searchMoviesByActor(actorName);
    }

    async getAllMovies(): Promise<Movie[]> {
        return DatabaseService.getAllMovies();
    }

    // Utility method to convert API response to Movie entity
    movieResponseToEntity(response: MovieResponse): Movie {
        return {
            imdbID: response.imdbID,
            title: response.title,
            year: response.year,
            rated: response.rated,
            released: response.released,
            runtime: response.runtime,
            genres: response.Genre.split(', ').filter(g => g.length > 0),
            director: response.director,
            writers: response.Writer.split(', ').filter(w => w.length > 0),
            actors: response.Actors.split(', ').filter(a => a.length > 0),
            plot: response.plot,
            languages: response.Language.split(', ').filter(l => l.length > 0),
            countries: response.Country.split(', ').filter(c => c.length > 0),
            awards: response.awards,
            poster: response.poster,
            ratings: response.Ratings,
            metascore: response.metascore,
            imdbRating: response.imdbRating,
            imdbVotes: response.imdbVotes,
            type: response.type,
            dvd: response.dvd,
            boxOffice: response.boxOffice,
            production: response.production,
            website: response.website,
            response: response.response
        };
    }
}

export default new MovieRepository();