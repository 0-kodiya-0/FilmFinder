import * as SQLite from 'expo-sqlite';
import { Movie } from '../models/Movie';

class DatabaseService {
    private db: SQLite.SQLiteDatabase | null = null;

    async initializeDatabase(): Promise<void> {
        try {
            this.db = await SQLite.openDatabaseAsync('movies.db');
            await this.createTables();
            console.log('Database initialized successfully');
        } catch (error) {
            console.error('Error initializing database:', error);
            throw error;
        }
    }

    private async createTables(): Promise<void> {
        if (!this.db) throw new Error('Database not initialized');

        const createMoviesTable = `
      CREATE TABLE IF NOT EXISTS movies (
        imdbID TEXT PRIMARY KEY,
        title TEXT NOT NULL,
        year INTEGER,
        rated TEXT,
        released TEXT,
        runtime TEXT,
        genres TEXT,
        director TEXT,
        writers TEXT,
        actors TEXT,
        plot TEXT,
        languages TEXT,
        countries TEXT,
        awards TEXT,
        poster TEXT,
        ratings TEXT,
        metascore INTEGER,
        imdbRating REAL,
        imdbVotes TEXT,
        type TEXT,
        dvd TEXT,
        boxOffice TEXT,
        production TEXT,
        website TEXT,
        response TEXT
      );
    `;

        await this.db.execAsync(createMoviesTable);
    }

    private serializeMovie(movie: Movie): any {
        return {
            imdbID: movie.imdbID,
            title: movie.title,
            year: movie.year,
            rated: movie.rated,
            released: movie.released,
            runtime: movie.runtime,
            genres: JSON.stringify(movie.genres),
            director: movie.director,
            writers: JSON.stringify(movie.writers),
            actors: JSON.stringify(movie.actors),
            plot: movie.plot,
            languages: JSON.stringify(movie.languages),
            countries: JSON.stringify(movie.countries),
            awards: movie.awards,
            poster: movie.poster,
            ratings: JSON.stringify(movie.ratings),
            metascore: movie.metascore,
            imdbRating: movie.imdbRating,
            imdbVotes: movie.imdbVotes,
            type: movie.type,
            dvd: movie.dvd,
            boxOffice: movie.boxOffice,
            production: movie.production,
            website: movie.website,
            response: movie.response
        };
    }

    private deserializeMovie(row: any): Movie {
        return {
            imdbID: row.imdbID,
            title: row.title,
            year: row.year,
            rated: row.rated,
            released: row.released,
            runtime: row.runtime,
            genres: JSON.parse(row.genres || '[]'),
            director: row.director,
            writers: JSON.parse(row.writers || '[]'),
            actors: JSON.parse(row.actors || '[]'),
            plot: row.plot,
            languages: JSON.parse(row.languages || '[]'),
            countries: JSON.parse(row.countries || '[]'),
            awards: row.awards,
            poster: row.poster,
            ratings: JSON.parse(row.ratings || '[]'),
            metascore: row.metascore,
            imdbRating: row.imdbRating,
            imdbVotes: row.imdbVotes,
            type: row.type,
            dvd: row.dvd,
            boxOffice: row.boxOffice,
            production: row.production,
            website: row.website,
            response: row.response
        };
    }

    async insertMovie(movie: Movie): Promise<void> {
        if (!this.db) throw new Error('Database not initialized');

        try {
            const serialized = this.serializeMovie(movie);

            await this.db.runAsync(
                `INSERT OR REPLACE INTO movies (
          imdbID, title, year, rated, released, runtime, genres, director,
          writers, actors, plot, languages, countries, awards, poster,
          ratings, metascore, imdbRating, imdbVotes, type, dvd, boxOffice,
          production, website, response
        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)`,
                [
                    serialized.imdbID, serialized.title, serialized.year, serialized.rated,
                    serialized.released, serialized.runtime, serialized.genres, serialized.director,
                    serialized.writers, serialized.actors, serialized.plot, serialized.languages,
                    serialized.countries, serialized.awards, serialized.poster, serialized.ratings,
                    serialized.metascore, serialized.imdbRating, serialized.imdbVotes, serialized.type,
                    serialized.dvd, serialized.boxOffice, serialized.production, serialized.website,
                    serialized.response
                ]
            );
        } catch (error) {
            console.error('Error inserting movie:', error);
            throw error;
        }
    }

    async insertMovies(movies: Movie[]): Promise<void> {
        for (const movie of movies) {
            await this.insertMovie(movie);
        }
    }

    async searchMoviesByActor(actorName: string): Promise<Movie[]> {
        if (!this.db) throw new Error('Database not initialized');

        try {
            const result = await this.db.getAllAsync(
                `SELECT * FROM movies WHERE actors LIKE ? COLLATE NOCASE`,
                [`%${actorName}%`]
            );

            return result.map(row => this.deserializeMovie(row));
        } catch (error) {
            console.error('Error searching movies by actor:', error);
            throw error;
        }
    }

    async getAllMovies(): Promise<Movie[]> {
        if (!this.db) throw new Error('Database not initialized');

        try {
            const result = await this.db.getAllAsync('SELECT * FROM movies');
            return result.map(row => this.deserializeMovie(row));
        } catch (error) {
            console.error('Error getting all movies:', error);
            throw error;
        }
    }
}

export default new DatabaseService();