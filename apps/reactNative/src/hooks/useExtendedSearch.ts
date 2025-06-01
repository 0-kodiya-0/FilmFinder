import { useState, useCallback } from 'react';
import MovieRepository from '../data/repository/MovieRepository';
import { MovieFilter, SearchItem, SearchResponse } from '../data/models';

export const useExtendedSearch = () => {
    const [searchResults, setSearchResults] = useState<SearchItem[]>([]);
    const [isLoading, setIsLoading] = useState(false);
    const [isLoadingMore, setIsLoadingMore] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [hasSearched, setHasSearched] = useState(false);
    const [canLoadMore, setCanLoadMore] = useState(false);
    const [totalResults, setTotalResults] = useState(0);
    const [currentFilter, setCurrentFilter] = useState<MovieFilter>({ page: 1 });
    const [movieSavedMessage, setMovieSavedMessage] = useState<string | null>(null);

    const searchMovies = useCallback(async (filter: MovieFilter) => {
        if (!filter.searchTerm || filter.searchTerm.length < 3) {
            setError('Please enter at least 3 characters');
            return;
        }

        // Reset pagination for new search
        const searchFilter = { ...filter, page: 1 };
        setCurrentFilter(searchFilter);
        setIsLoading(true);
        setError(null);
        setHasSearched(true);
        setSearchResults([]);

        try {
            const response = await MovieRepository.searchMoviesByFilter(searchFilter);

            if (response.Response === 'True') {
                setSearchResults(response.Search);
                const total = parseInt(response.totalResults) || 0;
                setTotalResults(total);
                setCanLoadMore(response.Search.length < total);
            } else {
                setError('No movies found matching your search');
                setSearchResults([]);
                setTotalResults(0);
                setCanLoadMore(false);
            }
        } catch (err) {
            console.error('Error searching movies:', err);
            setError('Error searching for movies. Please try again.');
            setSearchResults([]);
        } finally {
            setIsLoading(false);
        }
    }, []);

    const loadMore = useCallback(async () => {
        if (!canLoadMore || isLoadingMore || isLoading) {
            return;
        }

        setIsLoadingMore(true);

        try {
            const nextFilter = { ...currentFilter, page: (currentFilter.page || 1) + 1 };
            const response = await MovieRepository.searchMoviesByFilter(nextFilter);

            if (response.Response === 'True') {
                setSearchResults(prev => [...prev, ...response.Search]);
                setCurrentFilter(nextFilter);

                const total = parseInt(response.totalResults) || 0;
                setTotalResults(total);
                setCanLoadMore(searchResults.length + response.Search.length < total);
            } else {
                setCanLoadMore(false);
            }
        } catch (err) {
            console.error('Error loading more results:', err);
            setCanLoadMore(false);
        } finally {
            setIsLoadingMore(false);
        }
    }, [canLoadMore, isLoadingMore, isLoading, currentFilter, searchResults.length]);

    const addMovieToDatabase = useCallback(async (searchItem: SearchItem) => {
        try {
            const filter: MovieFilter = { imdbID: searchItem.imdbID };
            const movieResponse = await MovieRepository.getMovieByFilter(filter);

            if (movieResponse.response === 'True') {
                const movie = MovieRepository.movieResponseToEntity(movieResponse);
                await MovieRepository.insertMovie(movie);
                setMovieSavedMessage(`"${searchItem.title}" added to database`);
            } else {
                setMovieSavedMessage('Error: Movie details not found');
            }
        } catch (err) {
            console.error('Error adding movie to database:', err);
            setMovieSavedMessage('Error adding movie to database');
        }
    }, []);

    const clearMovieSavedMessage = useCallback(() => {
        setMovieSavedMessage(null);
    }, []);

    return {
        searchResults,
        isLoading,
        isLoadingMore,
        error,
        hasSearched,
        canLoadMore,
        totalResults,
        currentFilter,
        movieSavedMessage,
        searchMovies,
        loadMore,
        addMovieToDatabase,
        clearMovieSavedMessage,
        setCurrentFilter,
    };
};