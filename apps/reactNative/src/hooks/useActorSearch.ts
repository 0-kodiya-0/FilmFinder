import { useState, useCallback } from 'react';
import MovieRepository from '../data/repository/MovieRepository';
import { Movie, ActorSearchFilter } from '../data/models';

export const useActorSearch = () => {
    const [movies, setMovies] = useState<Movie[]>([]);
    const [isLoading, setIsLoading] = useState(false);
    const [hasSearched, setHasSearched] = useState(false);
    const [filter, setFilter] = useState<ActorSearchFilter>({});

    const searchByActor = useCallback(async (actorName: string, resetFilter = true) => {
        if (!actorName.trim()) {
            setHasSearched(false);
            setMovies([]);
            return;
        }

        if (resetFilter) {
            setFilter({});
        }

        setIsLoading(true);
        setHasSearched(true);
        setMovies([]);

        try {
            const results = await MovieRepository.searchMoviesByActor(actorName);

            // Apply filters
            const filteredResults = results.filter(movie => {
                let passes = true;

                // Genre filter
                if (filter.genre) {
                    passes = passes && movie.genres.some(genre =>
                        genre.toLowerCase().includes(filter.genre!.toLowerCase())
                    );
                }

                // Year range filter
                if (filter.fromYear) {
                    passes = passes && movie.year >= filter.fromYear;
                }

                if (filter.toYear) {
                    passes = passes && movie.year <= filter.toYear;
                }

                // Rating filter
                if (filter.highRatedOnly) {
                    passes = passes && movie.imdbRating >= 7.0;
                }

                return passes;
            });

            setMovies(filteredResults);
        } catch (err) {
            console.error('Error searching by actor:', err);
            setMovies([]);
        } finally {
            setIsLoading(false);
        }
    }, [filter]);

    const updateFilter = useCallback((newFilter: ActorSearchFilter) => {
        setFilter(newFilter);
    }, []);

    return {
        movies,
        isLoading,
        hasSearched,
        filter,
        searchByActor,
        updateFilter,
    };
};