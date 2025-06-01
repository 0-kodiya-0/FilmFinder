import { API_CONFIG } from '../../utils/Constants';
import { MovieFilter, MediaType, MovieResponse, SearchResponse } from '../models';

class ApiService {
    private buildUrl(params: Record<string, string>): string {
        const url = new URL(API_CONFIG.BASE_URL);

        // Add API key
        url.searchParams.append('apikey', API_CONFIG.API_KEY);

        // Add other parameters
        Object.entries(params).forEach(([key, value]) => {
            if (value) {
                url.searchParams.append(key, value);
            }
        });

        return url.toString();
    }

    private filterToQueryParams(filter: MovieFilter): Record<string, string> {
        const params: Record<string, string> = {};

        if (filter.imdbID) {
            params.i = filter.imdbID;
        } else if (filter.title) {
            params.t = filter.title;
        } else if (filter.searchTerm) {
            params.s = filter.searchTerm;
        }

        params.type = MediaType.MOVIE;

        if (filter.year) {
            params.y = filter.year.toString();
        }

        if (filter.includeRatings) {
            params.tomatoes = 'true';
        }

        if (filter.searchTerm && filter.page && filter.page > 1) {
            params.page = filter.page.toString();
        }

        return params;
    }

    private parseMovieResponse(json: any): MovieResponse {
        const ratings = json.Ratings?.map((rating: any) => ({
            source: rating.Source || '',
            value: rating.Value || ''
        })) || [];

        return {
            title: json.Title || '',
            year: parseInt(json.Year) || 0,
            rated: json.Rated || '',
            released: json.Released || '',
            runtime: json.Runtime || '',
            Genre: json.Genre || '',
            director: json.Director || '',
            Writer: json.Writer || '',
            Actors: json.Actors || '',
            plot: json.Plot || '',
            Language: json.Language || '',
            Country: json.Country || '',
            awards: json.Awards || '',
            poster: json.Poster || '',
            Ratings: ratings,
            metascore: json.Metascore === 'N/A' ? 0 : parseInt(json.Metascore) || 0,
            imdbRating: json.imdbRating === 'N/A' ? 0 : parseFloat(json.imdbRating) || 0,
            imdbVotes: json.imdbVotes || '',
            imdbID: json.imdbID || '',
            type: json.Type || '',
            dvd: json.DVD || 'N/A',
            boxOffice: json.BoxOffice || 'N/A',
            production: json.Production || 'N/A',
            website: json.Website || 'N/A',
            response: json.Response || ''
        };
    }

    async getMovieByFilter(filter: MovieFilter): Promise<MovieResponse> {
        try {
            const params = this.filterToQueryParams(filter);
            const url = this.buildUrl(params);

            console.log('API Request URL:', url);

            const controller = new AbortController();
            const timeoutId = setTimeout(() => controller.abort(), API_CONFIG.TIMEOUT);

            const response = await fetch(url, {
                method: 'GET',
                signal: controller.signal,
            });

            clearTimeout(timeoutId);

            if (!response.ok) {
                throw new Error(`HTTP Error: ${response.status}`);
            }

            const json = await response.json();
            console.log('API Response:', json);

            return this.parseMovieResponse(json);
        } catch (error) {
            console.error('Error fetching movie:', error);
            throw error;
        }
    }

    async searchMoviesByFilter(filter: MovieFilter): Promise<SearchResponse> {
        try {
            const params = this.filterToQueryParams(filter);
            const url = this.buildUrl(params);

            console.log('Search API Request URL:', url);

            const controller = new AbortController();
            const timeoutId = setTimeout(() => controller.abort(), API_CONFIG.TIMEOUT);

            const response = await fetch(url, {
                method: 'GET',
                signal: controller.signal,
            });

            clearTimeout(timeoutId);

            if (!response.ok) {
                throw new Error(`HTTP Error: ${response.status}`);
            }

            const json = await response.json();
            console.log('Search API Response:', json);

            if (json.Response === 'False') {
                return {
                    Search: [],
                    totalResults: '0',
                    Response: 'False'
                };
            }

            const searchItems = json.Search?.map((item: any) => ({
                title: item.Title || '',
                year: parseInt(item.Year) || 0,
                imdbID: item.imdbID || '',
                type: item.Type || '',
                poster: item.Poster || ''
            })) || [];

            return {
                Search: searchItems,
                totalResults: json.totalResults || '0',
                Response: json.Response || 'False'
            };
        } catch (error) {
            console.error('Error searching movies:', error);
            throw error;
        }
    }
}

export default new ApiService();