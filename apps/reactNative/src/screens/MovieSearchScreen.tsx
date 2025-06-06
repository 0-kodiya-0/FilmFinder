import React, { useEffect, useState } from 'react';
import {
    View,
    Text,
    TouchableOpacity,
    StyleSheet,
    ScrollView,
} from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { useNavigation } from '@react-navigation/native';
import { Ionicons } from '@expo/vector-icons';
import AppBar from '../components/AppBar';
import SearchBar from '../components/SearchBar';
import FilterPanel from '../components/FilterPanel';
import MovieDisplay from '../components/MovieDisplay';
import LoadingIndicator from '../components/LoadingIndicator';
import ErrorMessage from '../components/ErrorMessage';
import FloatingSnackbar from '../components/FloatingSnackbar';
import NetworkErrorScreen from '../components/NetworkErrorScreen';
import { useMovieSearch } from '../hooks/useMovieSearch';
import { useNetwork } from '../hooks/useNetwork';
import { useOrientation } from '../hooks/useOrientation';
import { MovieFilter } from '../data/models';

const MovieSearchScreen: React.FC = () => {
    const navigation = useNavigation();
    const isNetworkAvailable = useNetwork();
    const { isLandscape } = useOrientation(); // Add orientation detection

    const {
        movieResponse,
        isLoading,
        error,
        movieSaved,
        searchMovie,
        saveMovie,
        resetMovieSaved,
        clearError,
    } = useMovieSearch();

    const [searchQuery, setSearchQuery] = useState('');
    const [filter, setFilter] = useState<MovieFilter>({});
    const [isFilterExpanded, setIsFilterExpanded] = useState(false);
    const [showSnackbar, setShowSnackbar] = useState(false);
    const [snackbarMessage, setSnackbarMessage] = useState('');

    // Debug log
    console.log('MovieSearchScreen orientation:', { isLandscape });

    useEffect(() => {
        if (movieSaved) {
            setSnackbarMessage('Movie saved to database successfully');
            setShowSnackbar(true);
            resetMovieSaved();
        }
    }, [movieSaved]);

    const handleSearch = () => {
        if (!searchQuery.trim()) {
            setSnackbarMessage('Please enter a movie title');
            setShowSnackbar(true);
            return;
        }

        const searchFilter: MovieFilter = {
            ...filter,
            title: searchQuery.trim(),
        };

        searchMovie(searchFilter);
    };

    const handleApplyFilter = () => {
        if (!searchQuery.trim()) {
            setSnackbarMessage('Please enter a movie title');
            setShowSnackbar(true);
            return;
        }

        const searchFilter: MovieFilter = {
            ...filter,
            title: searchQuery.trim(),
        };

        searchMovie(searchFilter);
    };

    const handleRetry = () => {
        clearError();
        handleSearch();
    };

    if (!isNetworkAvailable) {
        return (
            <NetworkErrorScreen
                onRetry={() => { }}
                onNavigateBack={() => navigation.goBack()}
            />
        );
    }

    const renderContent = () => {
        if (isLoading) {
            return <LoadingIndicator />;
        }

        if (error) {
            return (
                <View style={styles.errorContainer}>
                    <ErrorMessage message={error} />
                    <TouchableOpacity style={styles.retryButton} onPress={handleRetry}>
                        <Text style={styles.retryButtonText}>Try Again</Text>
                    </TouchableOpacity>
                </View>
            );
        }

        if (movieResponse?.response === 'True') {
            return (
                <ScrollView style={styles.contentScroll} showsVerticalScrollIndicator={false}>
                    <MovieDisplay
                        movieResponse={movieResponse}
                        expandable={false}
                        initiallyExpanded={true}
                        showImage={true}
                    />
                </ScrollView>
            );
        }

        return (
            <View style={styles.emptyState}>
                <Text style={styles.emptyStateTitle}>Search for a Movie</Text>
                <Text style={styles.emptyStateSubtitle}>
                    Enter a movie title in the search bar
                </Text>
            </View>
        );
    };

    const renderSearchControls = () => (
        <View style={isLandscape ? styles.landscapeControls : styles.portraitControls}>
            <SearchBar
                value={searchQuery}
                onChangeText={(text) => {
                    setSearchQuery(text);
                    setFilter({ ...filter, title: text });
                }}
                onSearch={handleSearch}
                placeholder="Enter movie title"
            />

            {isFilterExpanded && (
                <FilterPanel
                    filter={filter}
                    onFilterChange={setFilter}
                    onApplyFilter={handleApplyFilter}
                />
            )}
        </View>
    );

    return (
        <SafeAreaView style={styles.container}>
            <AppBar
                title="Movie Search"
                onBackPress={() => navigation.goBack()}
                rightActions={
                    <View style={styles.rightActions}>
                        <TouchableOpacity onPress={() => setIsFilterExpanded(!isFilterExpanded)}>
                            <Ionicons
                                name="options"
                                size={24}
                                color={isFilterExpanded ? '#1565C0' : '#333'}
                            />
                        </TouchableOpacity>
                        {movieResponse?.response === 'True' && (
                            <TouchableOpacity onPress={saveMovie} style={styles.saveButton}>
                                <Ionicons name="add" size={24} color="#1565C0" />
                            </TouchableOpacity>
                        )}
                    </View>
                }
            />

            {/* Force re-render on orientation change */}
            <View key={`movie-search-${isLandscape}`} style={styles.content}>
                {isLandscape ? (
                    // Landscape Layout
                    <View style={styles.landscapeLayout}>
                        <View style={styles.landscapeLeft}>
                            <ScrollView showsVerticalScrollIndicator={false}>
                                {renderSearchControls()}
                            </ScrollView>
                        </View>
                        <View style={styles.landscapeRight}>
                            {renderContent()}
                        </View>
                    </View>
                ) : (
                    // Portrait Layout
                    <View style={styles.portraitLayout}>
                        {renderSearchControls()}
                        <View style={styles.portraitContent}>
                            {renderContent()}
                        </View>
                    </View>
                )}
            </View>

            <FloatingSnackbar
                message={snackbarMessage}
                visible={showSnackbar}
                onDismiss={() => setShowSnackbar(false)}
            />
        </SafeAreaView>
    );
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#FAFAFA',
    },
    content: {
        flex: 1,
    },
    portraitLayout: {
        flex: 1,
    },
    portraitControls: {
        // Portrait specific styles
    },
    portraitContent: {
        flex: 1,
    },
    landscapeLayout: {
        flex: 1,
        flexDirection: 'row',
    },
    landscapeLeft: {
        flex: 1,
        paddingRight: 8,
        maxWidth: '40%', // Limit controls width in landscape
    },
    landscapeRight: {
        flex: 2,
        paddingLeft: 8,
    },
    landscapeControls: {
        // Landscape specific styles
    },
    rightActions: {
        flexDirection: 'row',
        alignItems: 'center',
    },
    saveButton: {
        marginLeft: 8,
    },
    contentScroll: {
        flex: 1,
    },
    errorContainer: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        padding: 16,
    },
    retryButton: {
        backgroundColor: '#1565C0',
        paddingHorizontal: 24,
        paddingVertical: 12,
        borderRadius: 8,
        marginTop: 16,
    },
    retryButtonText: {
        color: '#fff',
        fontSize: 16,
        fontWeight: '600',
    },
    emptyState: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        padding: 32,
    },
    emptyStateTitle: {
        fontSize: 20,
        fontWeight: '600',
        color: '#1565C0',
        marginBottom: 8,
    },
    emptyStateSubtitle: {
        fontSize: 16,
        color: '#666',
        textAlign: 'center',
    },
});

export default MovieSearchScreen;