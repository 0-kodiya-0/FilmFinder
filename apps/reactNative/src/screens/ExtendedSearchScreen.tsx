import React, { useState, useEffect } from 'react';
import {
    View,
    Text,
    TouchableOpacity,
    StyleSheet,
    FlatList,
    ScrollView,
    ActivityIndicator,
} from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { useNavigation } from '@react-navigation/native';
import { Ionicons } from '@expo/vector-icons';
import AppBar from '../components/AppBar';
import SearchBar from '../components/SearchBar';
import FilterPanel from '../components/FilterPanel';
import SearchResultCard from '../components/SearchResultCard';
import LoadingIndicator from '../components/LoadingIndicator';
import ErrorMessage from '../components/ErrorMessage';
import FloatingSnackbar from '../components/FloatingSnackbar';
import NetworkErrorScreen from '../components/NetworkErrorScreen';
import { useExtendedSearch } from '../hooks/useExtendedSearch';
import { useNetwork } from '../hooks/useNetwork';
import { useOrientation } from '../hooks/useOrientation';
import { MovieFilter, SearchItem } from '../data/models';

const ExtendedSearchScreen: React.FC = () => {
    const navigation = useNavigation();
    const isNetworkAvailable = useNetwork();
    const { isLandscape } = useOrientation(); // Add orientation detection

    const {
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
    } = useExtendedSearch();

    const [searchQuery, setSearchQuery] = useState('');
    const [panelFilter, setPanelFilter] = useState<MovieFilter>({ page: 1 });
    const [isFilterExpanded, setIsFilterExpanded] = useState(false);
    const [showSnackbar, setShowSnackbar] = useState(false);
    const [snackbarMessage, setSnackbarMessage] = useState('');

    // Debug log
    console.log('ExtendedSearchScreen orientation:', { isLandscape });

    // Handle movie saved message
    useEffect(() => {
        if (movieSavedMessage) {
            setSnackbarMessage(movieSavedMessage);
            setShowSnackbar(true);
            clearMovieSavedMessage();
        }
    }, [movieSavedMessage, clearMovieSavedMessage]);

    // Update search query from filter
    useEffect(() => {
        if (currentFilter.searchTerm && searchQuery !== currentFilter.searchTerm) {
            setSearchQuery(currentFilter.searchTerm);
        }
    }, [currentFilter.searchTerm]);

    const handleSearch = () => {
        if (searchQuery.length < 3) {
            setSnackbarMessage('Please enter at least 3 characters');
            setShowSnackbar(true);
            return;
        }

        const searchFilter: MovieFilter = {
            ...panelFilter,
            searchTerm: searchQuery.trim(),
            page: 1,
        };

        setCurrentFilter(searchFilter);
        searchMovies(searchFilter);
    };

    const handleApplyFilter = () => {
        if (searchQuery.length < 3) {
            setSnackbarMessage('Please enter at least 3 characters');
            setShowSnackbar(true);
            return;
        }

        const searchFilter: MovieFilter = {
            ...panelFilter,
            searchTerm: searchQuery.trim(),
            page: 1,
        };

        setCurrentFilter(searchFilter);
        searchMovies(searchFilter);
    };

    const handleLoadMore = () => {
        if (canLoadMore && !isLoadingMore && !isLoading) {
            loadMore();
        }
    };

    const handleRetry = () => {
        if (searchQuery.length >= 3) {
            handleSearch();
        }
    };

    if (!isNetworkAvailable) {
        return (
            <NetworkErrorScreen
                onRetry={() => { }}
                onNavigateBack={() => navigation.goBack()}
            />
        );
    }

    const renderSearchResult = ({ item }: { item: SearchItem }) => (
        <SearchResultCard
            movie={item}
            onAddToDatabase={addMovieToDatabase}
        />
    );

    const renderFooter = () => {
        if (!isLoadingMore) return null;

        return (
            <View style={styles.loadingFooter}>
                <ActivityIndicator size="small" color="#1565C0" />
            </View>
        );
    };

    const renderEndOfList = () => {
        if (!canLoadMore && !isLoadingMore && searchResults.length > 0) {
            return (
                <View style={styles.endOfList}>
                    <Text style={styles.endOfListText}>End of results</Text>
                </View>
            );
        }
        return null;
    };

    const renderContent = () => {
        if (isLoading && !isLoadingMore) {
            return <LoadingIndicator />;
        }

        if (error && hasSearched) {
            return (
                <View style={styles.errorContainer}>
                    <ErrorMessage message={error} />
                    <TouchableOpacity style={styles.retryButton} onPress={handleRetry}>
                        <Text style={styles.retryButtonText}>Try Again</Text>
                    </TouchableOpacity>
                    <TouchableOpacity style={styles.reportButton}>
                        <Text style={styles.reportButtonText}>Report Issue</Text>
                    </TouchableOpacity>
                </View>
            );
        }

        if (hasSearched && searchResults.length > 0) {
            return (
                <View style={styles.resultsContainer}>
                    <Text style={styles.resultsText}>
                        Found {totalResults} movies (showing {searchResults.length})
                    </Text>
                    <FlatList
                        data={searchResults}
                        renderItem={renderSearchResult}
                        keyExtractor={(item) => item.imdbID}
                        onEndReached={handleLoadMore}
                        onEndReachedThreshold={0.1}
                        ListFooterComponent={renderFooter}
                        ListFooterComponentStyle={styles.footerContainer}
                        showsVerticalScrollIndicator={false}
                        contentContainerStyle={styles.listContent}
                    />
                    {renderEndOfList()}
                </View>
            );
        }

        if (hasSearched) {
            return <ErrorMessage message="No movies found matching your search" />;
        }

        return (
            <View style={styles.emptyState}>
                <Text style={styles.emptyStateTitle}>Extended Movie Search</Text>
                <Text style={styles.emptyStateSubtitle}>
                    Search for multiple movies by title from the OMDb database
                </Text>
                <Text style={styles.emptyStateHint}>
                    Use filters to refine your search by year, type, and more
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
                    setCurrentFilter({ ...currentFilter, searchTerm: text });
                }}
                onSearch={handleSearch}
                placeholder="Enter movie title (min. 3 characters)"
            />

            {isFilterExpanded && (
                <FilterPanel
                    filter={panelFilter}
                    onFilterChange={setPanelFilter}
                    onApplyFilter={handleApplyFilter}
                />
            )}
        </View>
    );

    return (
        <SafeAreaView style={styles.container}>
            <AppBar
                title="Extended Movie Search"
                onBackPress={() => navigation.goBack()}
                rightActions={
                    <TouchableOpacity onPress={() => setIsFilterExpanded(!isFilterExpanded)}>
                        <Ionicons
                            name="options"
                            size={24}
                            color={isFilterExpanded ? '#1565C0' : '#333'}
                        />
                    </TouchableOpacity>
                }
            />

            <View key={`extended-search-${isLandscape}`} style={styles.content}>
                {isLandscape ? (
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
    resultsContainer: {
        flex: 1,
    },
    resultsText: {
        fontSize: 14,
        color: '#666',
        marginHorizontal: 16,
        marginVertical: 8,
    },
    listContent: {
        paddingBottom: 16,
    },
    loadingFooter: {
        paddingVertical: 16,
        alignItems: 'center',
    },
    footerContainer: {
        paddingBottom: 20,
    },
    endOfList: {
        paddingVertical: 16,
        alignItems: 'center',
    },
    endOfListText: {
        fontSize: 14,
        color: '#666',
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
    reportButton: {
        borderWidth: 1,
        borderColor: '#1565C0',
        paddingHorizontal: 24,
        paddingVertical: 12,
        borderRadius: 8,
        marginTop: 8,
    },
    reportButtonText: {
        color: '#1565C0',
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
        marginBottom: 16,
    },
    emptyStateHint: {
        fontSize: 14,
        color: '#999',
        textAlign: 'center',
    },
});

export default ExtendedSearchScreen;