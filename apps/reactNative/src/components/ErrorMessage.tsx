import React from 'react';
import { View, Text, StyleSheet } from 'react-native';

interface ErrorMessageProps {
    message: string;
}

const ErrorMessage: React.FC<ErrorMessageProps> = ({ message }) => {
    return (
        <View style={styles.container}>
            <View style={styles.errorBox}>
                <Text style={styles.errorText}>{message}</Text>
            </View>
        </View>
    );
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        padding: 16,
    },
    errorBox: {
        backgroundColor: '#ffebee',
        paddingHorizontal: 16,
        paddingVertical: 12,
        borderRadius: 8,
        borderLeftWidth: 4,
        borderLeftColor: '#f44336',
    },
    errorText: {
        color: '#c62828',
        fontSize: 16,
        textAlign: 'center',
    },
});

export default ErrorMessage;