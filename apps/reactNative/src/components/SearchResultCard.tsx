import React from 'react';
import {
    View,
    Text,
    Image,
    TouchableOpacity,
    StyleSheet,
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { SearchItem } from '../data/models';

interface SearchResultCardProps {
    movie: SearchItem;
    onAddToDatabase: (movie: SearchItem) => void;
}

const SearchResultCard: React.FC<SearchResultCardProps> = ({
    movie,
    onAddToDatabase,
}) => {
    const handleAddToDatabase = () => {
        onAddToDatabase(movie);
    };

    const renderImage = () => {
        if (movie.poster && movie.poster !== 'N/A') {
            return (
                <Image
                    source={{ uri: movie.poster }}
                    style={styles.poster}
                    resizeMode="cover"
                />
            );
        } else {
            return (
                <View style={styles.placeholderImage}>
                    <Text style={styles.placeholderText}>
                        {movie.title.charAt(0).toUpperCase()}
                    </Text>
                </View>
            );
        }
    };

    return (
        <View style={styles.container}>
            <View style={styles.imageContainer}>
                {renderImage()}
            </View>

            <View style={styles.contentContainer}>
                <Text style={styles.title} numberOfLines={2}>
                    {movie.title}
                </Text>

                <View style={styles.badgeContainer}>
                    <View style={styles.yearBadge}>
                        <Text style={styles.yearText}>{movie.year}</Text>
                    </View>
                    <View style={styles.typeBadge}>
                        <Text style={styles.typeText}>
                            {movie.type.charAt(0).toUpperCase() + movie.type.slice(1)}
                        </Text>
                    </View>
                </View>

                <Text style={styles.imdbId}>IMDb: {movie.imdbID}</Text>
            </View>

            <TouchableOpacity
                style={styles.addButton}
                onPress={handleAddToDatabase}
            >
                <Ionicons name="add" size={16} color="#fff" />
                <Text style={styles.addButtonText}>Add to DB</Text>
            </TouchableOpacity>
        </View>
    );
};

const styles = StyleSheet.create({
    container: {
        flexDirection: 'row',
        backgroundColor: '#fff',
        marginHorizontal: 16,
        marginVertical: 8,
        borderRadius: 12,
        padding: 16,
        alignItems: 'center',
        elevation: 4,
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.1,
        shadowRadius: 4,
    },
    imageContainer: {
        width: 80,
        height: 80,
        borderRadius: 8,
        overflow: 'hidden',
        marginRight: 16,
        elevation: 2,
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 1 },
        shadowOpacity: 0.1,
        shadowRadius: 2,
    },
    poster: {
        width: '100%',
        height: '100%',
    },
    placeholderImage: {
        width: '100%',
        height: '100%',
        backgroundColor: '#E3F2FD',
        justifyContent: 'center',
        alignItems: 'center',
    },
    placeholderText: {
        fontSize: 24,
        fontWeight: 'bold',
        color: '#1565C0',
    },
    contentContainer: {
        flex: 1,
        marginRight: 8,
    },
    title: {
        fontSize: 16,
        fontWeight: '600',
        color: '#333',
        marginBottom: 4,
    },
    badgeContainer: {
        flexDirection: 'row',
        marginBottom: 4,
    },
    yearBadge: {
        backgroundColor: '#E3F2FD',
        paddingHorizontal: 8,
        paddingVertical: 4,
        borderRadius: 4,
        marginRight: 8,
    },
    yearText: {
        fontSize: 12,
        color: '#1565C0',
        fontWeight: '600',
    },
    typeBadge: {
        backgroundColor: '#FFF3E0',
        paddingHorizontal: 8,
        paddingVertical: 4,
        borderRadius: 4,
    },
    typeText: {
        fontSize: 12,
        color: '#FF9800',
        fontWeight: '600',
    },
    imdbId: {
        fontSize: 12,
        color: '#999',
    },
    addButton: {
        flexDirection: 'row',
        backgroundColor: '#1565C0',
        paddingHorizontal: 12,
        paddingVertical: 8,
        borderRadius: 8,
        alignItems: 'center',
    },
    addButtonText: {
        color: '#fff',
        fontSize: 14,
        fontWeight: '600',
        marginLeft: 4,
    },
});

export default SearchResultCard;