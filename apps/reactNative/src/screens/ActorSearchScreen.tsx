import React, { useState } from 'react';
import {
    View,
    Text,
    TouchableOpacity,
    StyleSheet,
    FlatList,
    ScrollView,
} from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { useNavigation } from '@react-navigation/native';
import { Ionicons } from '@expo/vector-icons';
import AppBar from '../components/AppBar';
import SearchBar from '../components/SearchBar';
import ActorSearchFilterPanel from '../components/ActorSearchFilterPanel';
import MovieDisplay from '../components/MovieDisplay';
import LoadingIndicator from '../components/LoadingIndicator';
import ErrorMessage from '../components/ErrorMessage';
import FloatingSnackbar from '../components/FloatingSnackbar';
import { useActorSearch } from '../hooks/useActorSearch';
import { useOrientation } from '../hooks/useOrientation';
import { Movie } from '../data/models';

const ActorSearchScreen: React.FC = () => {
    const navigation = useNavigation();
    const { isLandscape } = useOrientation(); // Add orientation detection

    const {
        movies,
        isLoading,
        hasSearched,
        filter,
        searchByActor,
        updateFilter,
    } = useActorSearch();

    const [searchQuery, setSearchQuery] = useState('');
    const [isFilterExpanded, setIsFilterExpanded] = useState(false);
    const [showSnackbar, setShowSnackbar] = useState(false);
    const [snackbarMessage, setSnackbarMessage] = useState('');

    // Debug log
    console.log('ActorSearchScreen orientation:', { isLandscape });

    const handleSearch = () => {
        if (!searchQuery.trim()) {
            setSnackbarMessage('Please enter an actor name');
            setShowSnackbar(true);
            return;
        }

        searchByActor(searchQuery.trim());
    };

    const handleApplyFilter = () => {
        if (!searchQuery.trim()) {
            setSnackbarMessage('Please enter an actor name');
            setShowSnackbar(true);
            return;
        }

        searchByActor(searchQuery.trim(), false);
    };

    const renderMovie = ({ item }: { item: Movie }) => (
        <MovieDisplay
            movie={item}
            expandable={true}
            initiallyExpanded={false}
            showImage={true}
            highlightField="actors"
        />
    );

    const renderContent = () => {
        if (isLoading) {
            return <LoadingIndicator />;
        }

        if (hasSearched && movies.length === 0) {
            return <ErrorMessage message="No movies found with this actor" />;
        }

        if (hasSearched && movies.length > 0) {
            return (
                <View style={styles.resultsContainer}>
                    <Text style={styles.resultsText}>
                        Found {movies.length} movie(s) with "{searchQuery}"
                    </Text>
                    <FlatList
                        data={movies}
                        renderItem={renderMovie}
                        keyExtractor={(item) => item.imdbID}
                        showsVerticalScrollIndicator={false}
                        contentContainerStyle={styles.listContent}
                    />
                </View>
            );
        }

        return (
            <View style={styles.emptyState}>
                <Text style={styles.emptyStateTitle}>Search for an Actor</Text>
                <Text style={styles.emptyStateSubtitle}>
                    Enter an actor's name to find movies they've appeared in
                </Text>
                <Text style={styles.emptyStateHint}>
                    Use filters to refine your search by genre, year, and rating
                </Text>
            </View>
        );
    };

    const renderSearchControls = () => (
        <View style={isLandscape ? styles.landscapeControls : styles.portraitControls}>
            <SearchBar
                value={searchQuery}
                onChangeText={setSearchQuery}
                onSearch={handleSearch}
                placeholder="Enter actor name"
            />

            {isFilterExpanded && (
                <ActorSearchFilterPanel
                    filter={filter}
                    onFilterChange={updateFilter}
                    onApplyFilter={handleApplyFilter}
                />
            )}
        </View>
    );

    return (
        <SafeAreaView style={styles.container}>
            <AppBar
                title="Actor Search"
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

            <View key={`actor-search-${isLandscape}`} style={styles.content}>
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
        marginBottom: 8,
    },
    emptyStateHint: {
        fontSize: 14,
        color: '#999',
        textAlign: 'center',
    },
});

export default ActorSearchScreen;