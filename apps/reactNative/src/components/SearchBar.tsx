import React, { useRef, useEffect } from 'react';
import { View, TextInput, TouchableOpacity, StyleSheet } from 'react-native';
import { Ionicons } from '@expo/vector-icons';

interface SearchBarProps {
    value: string;
    onChangeText: (text: string) => void;
    onSearch: () => void;
    placeholder?: string;
    autoFocus?: boolean;
}

const SearchBar: React.FC<SearchBarProps> = ({
    value,
    onChangeText,
    onSearch,
    placeholder = 'Search...',
    autoFocus = true,
}) => {
    const inputRef = useRef<TextInput>(null);

    useEffect(() => {
        if (autoFocus) {
            setTimeout(() => inputRef.current?.focus(), 100);
        }
    }, [autoFocus]);

    return (
        <View style={styles.container}>
            <TextInput
                ref={inputRef}
                style={styles.input}
                value={value}
                onChangeText={onChangeText}
                placeholder={placeholder}
                placeholderTextColor="#999"
                returnKeyType="search"
                onSubmitEditing={onSearch}
            />
            <TouchableOpacity onPress={onSearch} style={styles.searchButton}>
                <Ionicons name="search" size={24} color="#1565C0" />
            </TouchableOpacity>
        </View>
    );
};

const styles = StyleSheet.create({
    container: {
        flexDirection: 'row',
        alignItems: 'center',
        backgroundColor: '#fff',
        borderRadius: 12,
        marginHorizontal: 16,
        marginVertical: 8,
        paddingHorizontal: 4,
        elevation: 4,
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.1,
        shadowRadius: 4,
    },
    input: {
        flex: 1,
        paddingHorizontal: 16,
        paddingVertical: 12,
        fontSize: 16,
        color: '#333',
    },
    searchButton: {
        padding: 12,
    },
});

export default SearchBar;