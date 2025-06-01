import React, { useState } from 'react';
import {
    View,
    Text,
    TextInput,
    TouchableOpacity,
    StyleSheet,
    Switch,
} from 'react-native';
import { ActorSearchFilter } from '../data/models';

interface ActorSearchFilterPanelProps {
    filter: ActorSearchFilter;
    onFilterChange: (filter: ActorSearchFilter) => void;
    onApplyFilter: () => void;
}

const ActorSearchFilterPanel: React.FC<ActorSearchFilterPanelProps> = ({
    filter,
    onFilterChange,
    onApplyFilter,
}) => {
    const [fromYearText, setFromYearText] = useState(filter.fromYear?.toString() || '');
    const [toYearText, setToYearText] = useState(filter.toYear?.toString() || '');

    const handleGenreChange = (text: string) => {
        onFilterChange({ ...filter, genre: text });
    };

    const handleFromYearChange = (text: string) => {
        setFromYearText(text);
        const year = text ? parseInt(text) : undefined;
        onFilterChange({ ...filter, fromYear: year });
    };

    const handleToYearChange = (text: string) => {
        setToYearText(text);
        const year = text ? parseInt(text) : undefined;
        onFilterChange({ ...filter, toYear: year });
    };

    const handleHighRatedToggle = (value: boolean) => {
        onFilterChange({ ...filter, highRatedOnly: value });
    };

    return (
        <View style={styles.container}>
            <Text style={styles.title}>Filter Actor Results</Text>

            {/* Genre Filter */}
            <View style={styles.filterSection}>
                <Text style={styles.filterLabel}>Genre</Text>
                <TextInput
                    style={styles.textInput}
                    value={filter.genre || ''}
                    onChangeText={handleGenreChange}
                    placeholder="Any genre"
                />
            </View>

            {/* Year Range Filter */}
            <View style={styles.filterSection}>
                <Text style={styles.filterLabel}>Year Range</Text>
                <View style={styles.yearRangeContainer}>
                    <View style={styles.yearInputContainer}>
                        <Text style={styles.yearLabel}>From Year</Text>
                        <TextInput
                            style={styles.yearInput}
                            value={fromYearText}
                            onChangeText={handleFromYearChange}
                            placeholder="From"
                            keyboardType="numeric"
                            maxLength={4}
                        />
                    </View>
                    <View style={styles.yearInputContainer}>
                        <Text style={styles.yearLabel}>To Year</Text>
                        <TextInput
                            style={styles.yearInput}
                            value={toYearText}
                            onChangeText={handleToYearChange}
                            placeholder="To"
                            keyboardType="numeric"
                            maxLength={4}
                        />
                    </View>
                </View>
            </View>

            {/* High Rated Filter */}
            <View style={styles.switchSection}>
                <Text style={styles.switchLabel}>Show High Rated Only (IMDb ≥ 7.0)</Text>
                <Switch
                    value={filter.highRatedOnly || false}
                    onValueChange={handleHighRatedToggle}
                    trackColor={{ false: '#E0E0E0', true: '#90CAF9' }}
                    thumbColor={filter.highRatedOnly ? '#1565C0' : '#fff'}
                />
            </View>

            {/* Apply Button */}
            <TouchableOpacity style={styles.applyButton} onPress={onApplyFilter}>
                <Text style={styles.applyButtonText}>Apply Filters</Text>
            </TouchableOpacity>
        </View>
    );
};

const styles = StyleSheet.create({
    container: {
        backgroundColor: '#fff',
        marginHorizontal: 16,
        marginVertical: 8,
        borderRadius: 12,
        padding: 16,
        elevation: 4,
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.1,
        shadowRadius: 4,
    },
    title: {
        fontSize: 16,
        fontWeight: '600',
        color: '#333',
        marginBottom: 16,
    },
    filterSection: {
        marginBottom: 16,
    },
    filterLabel: {
        fontSize: 14,
        fontWeight: '600',
        color: '#333',
        marginBottom: 4,
    },
    textInput: {
        borderWidth: 1,
        borderColor: '#E0E0E0',
        borderRadius: 8,
        paddingHorizontal: 12,
        paddingVertical: 10,
        fontSize: 16,
        color: '#333',
    },
    yearRangeContainer: {
        flexDirection: 'row',
        justifyContent: 'space-between',
    },
    yearInputContainer: {
        flex: 1,
        marginHorizontal: 4,
    },
    yearLabel: {
        fontSize: 12,
        color: '#666',
        marginBottom: 4,
    },
    yearInput: {
        borderWidth: 1,
        borderColor: '#E0E0E0',
        borderRadius: 8,
        paddingHorizontal: 12,
        paddingVertical: 10,
        fontSize: 16,
        color: '#333',
    },
    switchSection: {
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'space-between',
        marginBottom: 16,
    },
    switchLabel: {
        fontSize: 14,
        color: '#333',
        flex: 1,
        marginRight: 8,
    },
    applyButton: {
        backgroundColor: '#1565C0',
        paddingVertical: 12,
        borderRadius: 8,
        alignItems: 'center',
    },
    applyButtonText: {
        color: '#fff',
        fontSize: 16,
        fontWeight: '600',
    },
});

export default ActorSearchFilterPanel;