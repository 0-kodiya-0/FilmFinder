import React, { useState } from 'react';
import {
    View,
    Text,
    Image,
    TouchableOpacity,
    StyleSheet,
    ScrollView,
} from 'react-native';
import { Movie, MovieResponse } from '../data/models';

interface MovieDisplayProps {
    movie?: Movie;
    movieResponse?: MovieResponse;
    expandable?: boolean;
    initiallyExpanded?: boolean;
    showImage?: boolean;
    highlightField?: string;
    onPress?: () => void;
}

const MovieDisplay: React.FC<MovieDisplayProps> = ({
    movie,
    movieResponse,
    expandable = true,
    initiallyExpanded = false,
    showImage = true,
    highlightField,
    onPress,
}) => {
    const [expanded, setExpanded] = useState(initiallyExpanded);

    // Extract data from either source
    const data = movie || (movieResponse ? {
        title: movieResponse.title,
        year: movieResponse.year,
        rated: movieResponse.rated,
        released: movieResponse.released,
        runtime: movieResponse.runtime,
        genres: movieResponse.Genre.split(', ').filter(g => g.length > 0),
        director: movieResponse.director,
        writers: movieResponse.Writer.split(', ').filter(w => w.length > 0),
        actors: movieResponse.Actors.split(', ').filter(a => a.length > 0),
        plot: movieResponse.plot,
        poster: movieResponse.poster,
        ratings: movieResponse.Ratings,
        imdbRating: movieResponse.imdbRating,
        awards: movieResponse.awards,
    } : null);

    if (!data) return null;

    const handlePress = () => {
        if (expandable) {
            setExpanded(!expanded);
        }
        onPress?.();
    };

    const renderInfoField = (label: string, value: string | string[]) => {
        if (!value || (Array.isArray(value) && value.length === 0)) return null;

        const displayValue = Array.isArray(value) ? value.join(', ') : value;
        if (!displayValue || displayValue === 'N/A') return null;

        return (
            <View style={styles.infoRow}>
                <Text style={styles.infoLabel}>{label}: </Text>
                <Text style={styles.infoValue}>{displayValue}</Text>
            </View>
        );
    };

    const renderHighlightField = (label: string, value: string | string[]) => {
        if (!value || (Array.isArray(value) && value.length === 0)) return null;

        const displayValue = Array.isArray(value) ? value.join(', ') : value;
        if (!displayValue || displayValue === 'N/A') return null;

        return (
            <View style={styles.highlightField}>
                <Text style={styles.highlightLabel}>{label}</Text>
                <Text style={styles.highlightValue}>{displayValue}</Text>
            </View>
        );
    };

    return (
        <TouchableOpacity
            style={styles.container}
            onPress={handlePress}
            activeOpacity={expandable ? 0.7 : 1}
        >
            <View style={styles.content}>
                {/* Title */}
                <Text style={styles.title}>{data.title}</Text>

                {/* Basic info row */}
                <View style={styles.basicInfoRow}>
                    {data.year && data.year > 0 && (
                        <Text style={styles.year}>{data.year}</Text>
                    )}
                    {data.genres && data.genres.length > 0 && (
                        <View style={styles.genreTag}>
                            <Text style={styles.genreText}>{data.genres[0]}</Text>
                        </View>
                    )}
                </View>

                {/* Image */}
                {showImage && data.poster && data.poster !== 'N/A' && (
                    <View style={styles.imageContainer}>
                        <Image
                            source={{ uri: data.poster }}
                            style={styles.poster}
                            resizeMode="contain"
                        />
                    </View>
                )}

                {/* Highlight field */}
                {highlightField && (
                    <>
                        {highlightField === 'actors' && renderHighlightField('Actors', data.actors)}
                        {highlightField === 'director' && renderHighlightField('Director', data.director)}
                    </>
                )}

                {/* Runtime */}
                {renderInfoField('Runtime', data.runtime)}

                {/* Expanded content */}
                {expanded && (
                    <View style={styles.expandedContent}>
                        <View style={styles.divider} />

                        {renderInfoField('Rated', data.rated)}
                        {renderInfoField('Released', data.released)}
                        {renderInfoField('Director', data.director)}
                        {renderInfoField('Writers', data.writers)}

                        {highlightField !== 'actors' && renderInfoField('Actors', data.actors)}

                        {/* Plot */}
                        {data.plot && data.plot !== 'N/A' && (
                            <View style={styles.plotSection}>
                                <Text style={styles.plotLabel}>Plot</Text>
                                <Text style={styles.plotText}>{data.plot}</Text>
                            </View>
                        )}

                        {/* Ratings */}
                        {data.ratings && data.ratings.length > 0 && (
                            <View style={styles.ratingsSection}>
                                <Text style={styles.ratingsLabel}>Ratings</Text>
                                {data.ratings.map((rating, index) => (
                                    <View key={index} style={styles.ratingRow}>
                                        <Text style={styles.ratingSource}>{rating.source}: </Text>
                                        <Text style={styles.ratingValue}>{rating.value}</Text>
                                    </View>
                                ))}
                            </View>
                        )}
                    </View>
                )}

                {/* Tap hint for expandable cards */}
                {expandable && !expanded && (
                    <Text style={styles.tapHint}>Tap for details</Text>
                )}
            </View>
        </TouchableOpacity>
    );
};

const styles = StyleSheet.create({
    container: {
        backgroundColor: '#fff',
        marginHorizontal: 16,
        marginVertical: 8,
        borderRadius: 12,
        elevation: 4,
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.1,
        shadowRadius: 4,
    },
    content: {
        padding: 16,
    },
    title: {
        fontSize: 18,
        fontWeight: '600',
        color: '#1565C0',
        marginBottom: 4,
    },
    basicInfoRow: {
        flexDirection: 'row',
        alignItems: 'center',
        marginBottom: 8,
    },
    year: {
        fontSize: 14,
        color: '#666',
        marginRight: 8,
    },
    genreTag: {
        backgroundColor: '#E3F2FD',
        paddingHorizontal: 6,
        paddingVertical: 2,
        borderRadius: 4,
    },
    genreText: {
        fontSize: 12,
        color: '#1565C0',
    },
    imageContainer: {
        alignItems: 'center',
        marginVertical: 8,
    },
    poster: {
        width: '100%',
        height: 240,
    },
    highlightField: {
        marginVertical: 4,
    },
    highlightLabel: {
        fontSize: 14,
        fontWeight: '600',
        color: '#1565C0',
    },
    highlightValue: {
        fontSize: 14,
        color: '#333',
        marginTop: 2,
    },
    infoRow: {
        flexDirection: 'row',
        marginVertical: 2,
    },
    infoLabel: {
        fontSize: 14,
        fontWeight: '600',
        color: '#333',
    },
    infoValue: {
        fontSize: 14,
        color: '#666',
        flex: 1,
    },
    expandedContent: {
        marginTop: 8,
    },
    divider: {
        height: 1,
        backgroundColor: '#E0E0E0',
        marginVertical: 8,
    },
    plotSection: {
        marginTop: 8,
    },
    plotLabel: {
        fontSize: 14,
        fontWeight: '600',
        color: '#333',
        marginBottom: 4,
    },
    plotText: {
        fontSize: 14,
        color: '#666',
        fontStyle: 'italic',
        lineHeight: 20,
    },
    ratingsSection: {
        marginTop: 8,
    },
    ratingsLabel: {
        fontSize: 14,
        fontWeight: '600',
        color: '#333',
        marginBottom: 4,
    },
    ratingRow: {
        flexDirection: 'row',
        marginVertical: 2,
    },
    ratingSource: {
        fontSize: 14,
        fontWeight: '600',
        color: '#333',
    },
    ratingValue: {
        fontSize: 14,
        color: '#666',
    },
    tapHint: {
        fontSize: 12,
        color: '#999',
        textAlign: 'right',
        marginTop: 8,
    },
});

export default MovieDisplay;