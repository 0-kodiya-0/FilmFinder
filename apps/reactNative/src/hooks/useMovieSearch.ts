import { useState, useCallback } from 'react';
import MovieRepository from '../data/repository/MovieRepository';
import { MovieFilter, MovieResponse } from '../data/models';

export const useMovieSearch = () => {
    const [movieResponse, setMovieResponse] = useState<MovieResponse | null>(null);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [movieSaved, setMovieSaved] = useState(false);

    const searchMovie = useCallback(async (filter: MovieFilter) => {
        if (!filter.title && !filter.imdbID) {
            setError('Please enter a movie title or IMDb ID');
            return;
        }

        setIsLoading(true);
        setError(null);
        setMovieResponse(null);

        try {
            const response = await MovieRepository.getMovieByFilter(filter);

            if (response.response === 'False') {
                setError('Movie not found');
                setMovieResponse(null);
            } else {
                setMovieResponse(response);
            }
        } catch (err) {
            console.error('Error searching for movie:', err);
            setError('Error searching for movie. Please try again.');
        } finally {
            setIsLoading(false);
        }
    }, []);

    const saveMovie = useCallback(async () => {
        if (!movieResponse || movieResponse.response !== 'True') {
            return;
        }

        try {
            const movie = MovieRepository.movieResponseToEntity(movieResponse);
            await MovieRepository.insertMovie(movie);
            setMovieSaved(true);
        } catch (err) {
            console.error('Error saving movie:', err);
            setError('Error saving movie to database');
        }
    }, [movieResponse]);

    const resetMovieSaved = useCallback(() => {
        setMovieSaved(false);
    }, []);

    const clearError = useCallback(() => {
        setError(null);
    }, []);

    return {
        movieResponse,
        isLoading,
        error,
        movieSaved,
        searchMovie,
        saveMovie,
        resetMovieSaved,
        clearError,
    };
};