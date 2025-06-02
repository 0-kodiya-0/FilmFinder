import React, { useState } from 'react';
import {
    View,
    Text,
    TextInput,
    TouchableOpacity,
    StyleSheet,
    Switch,
} from 'react-native';
import { MovieFilter } from '../data/models';

interface FilterPanelProps {
    filter: MovieFilter;
    onFilterChange: (filter: MovieFilter) => void;
    onApplyFilter: () => void;
    expanded?: boolean;
}

const FilterPanel: React.FC<FilterPanelProps> = ({
    filter,
    onFilterChange,
    onApplyFilter,
    expanded = true
}) => {
    const [yearText, setYearText] = useState(filter.year?.toString() || '');

    const handleYearChange = (text: string) => {
        setYearText(text);
        const year = text ? parseInt(text) : undefined;
        if (!text || (year && year >= 1800 && year <= 2030)) {
            onFilterChange({ ...filter, year });
        }
    };

    const handleIncludeRatingsChange = (value: boolean) => {
        onFilterChange({ ...filter, includeRatings: value });
    };

    return (
        expanded ?
            <View style={styles.container}>
                {/* Year Filter */}
                <View style={styles.filterSection}>
                    <Text style={styles.filterLabel}>Year</Text>
                    <TextInput
                        style={styles.textInput}
                        value={yearText}
                        onChangeText={handleYearChange}
                        placeholder="Enter year (optional)"
                        keyboardType="numeric"
                        maxLength={4}
                    />
                </View>

                {/* Include Ratings Filter */}
                <View style={styles.switchSection}>
                    <Text style={styles.switchLabel}>Include Ratings</Text>
                    <Switch
                        value={filter.includeRatings || false}
                        onValueChange={handleIncludeRatingsChange}
                        trackColor={{ false: '#E0E0E0', true: '#90CAF9' }}
                        thumbColor={filter.includeRatings ? '#1565C0' : '#fff'}
                    />
                </View>

                {/* Apply Button */}
                <TouchableOpacity style={styles.applyButton} onPress={onApplyFilter}>
                    <Text style={styles.applyButtonText}>Apply Filters</Text>
                </TouchableOpacity>
            </View>
            :
            <></>
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
    switchSection: {
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'space-between',
        marginBottom: 16,
    },
    switchLabel: {
        fontSize: 14,
        color: '#333',
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

export default FilterPanel;